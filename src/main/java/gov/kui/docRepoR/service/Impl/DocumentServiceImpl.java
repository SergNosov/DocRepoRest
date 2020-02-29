package gov.kui.docRepoR.service.Impl;

import gov.kui.docRepoR.dao.DoctypeRepository;
import gov.kui.docRepoR.dao.SenderRepository;
import gov.kui.docRepoR.domain.Doctype;
import gov.kui.docRepoR.domain.Document;
import gov.kui.docRepoR.domain.Sender;
import gov.kui.docRepoR.dao.DocumentRepository;
import gov.kui.docRepoR.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;
    private final DoctypeRepository doctypeRepository;
    private final SenderRepository senderRepository;

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository,
                               DoctypeRepository doctypeRepository,
                               SenderRepository senderRepository) {
        this.documentRepository = documentRepository;
        this.doctypeRepository = doctypeRepository;
        this.senderRepository = senderRepository;
    }

    @Override
    public List<Document> findAll() {
        return documentRepository.findAll();
    }

    @Override
    public Document findById(int id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Не найден документ с id - " + id));
    }

    @Override
    public Document save(Document document) {
        if (document == null) {
            throw new IllegalArgumentException("Document is null.");
        }
        if (document.getTitle() == null || document.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Не указан заголовок документа.");
        }
        if (document.getDoctype() == null) {
            throw new IllegalArgumentException("Не установлен тип документа (Doctype of document id=" +
                    document.getId() + " is null)");
        }
        if (document.getId() != 0) {
            this.findById(document.getId());
        }

        this.setupChildEntity(document);
        return documentRepository.save(document);
    }

    @Override
    public int deleteById(int id) {
        documentRepository.deleteById(this.findById(id).getId());
        return id;
    }

    private void setupDoctype(Document document) {
        final int idDoc = document.getDoctype().getId();
        Doctype doctypeFromBase = doctypeRepository.findById(idDoc)
                .orElseThrow(() -> new IllegalArgumentException("Не найден тип документа с id - " + idDoc));
        document.setDoctype(doctypeFromBase);
    }

    private void setupSenders(Document document) {
        List<Sender> senders = new ArrayList<>();
        document.getSenders().forEach(sender -> {
            senders.add(senderRepository.findById(sender.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Не найден отправитель с id - "+sender.getId()))
            );
        });
        document.setSenders(senders);
    }

    private void setupChildEntity(Document document) {
        setupDoctype(document);
        setupSenders(document);
    }
}
