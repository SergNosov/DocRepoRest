package gov.kui.docRepoR.service.Impl;

import gov.kui.docRepoR.model.Doctype;
import gov.kui.docRepoR.model.Document;
import gov.kui.docRepoR.model.FileEntity;
import gov.kui.docRepoR.model.Sender;
import gov.kui.docRepoR.dao.DocumentRepository;
import gov.kui.docRepoR.service.DoctypeService;
import gov.kui.docRepoR.service.DocumentService;
import gov.kui.docRepoR.service.FileEntityService;
import gov.kui.docRepoR.service.SenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class DocumentServiceImpl implements DocumentService {
    private DocumentRepository documentRepository;
    private DoctypeService doctypeService;
    private SenderService senderService;
    private FileEntityService fileEntityService;

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository,
                               DoctypeService doctypeService,
                               SenderService senderService,
                               FileEntityService fileEntityService) {
        this.documentRepository = documentRepository;
        this.doctypeService = doctypeService;
        this.senderService = senderService;
        this.fileEntityService = fileEntityService;
    }

    @Override
    public List<Document> findAll() {
        return documentRepository.findAll();
    }

    @Override
    public Document findById(int id) {
        Optional<Document> result = documentRepository.findById(id);
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new RuntimeException("Не найден документ с id - " + id);
        }
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
        Doctype doctypeFromBase = doctypeService.findById(document.getDoctype().getId());
        document.setDoctype(doctypeFromBase);
    }

    private void setupSenders(Document document) {
        List<Sender> senders = new ArrayList<>();
        document.getSenders().forEach(sender -> {
            senders.add(senderService.findById(sender.getId()));
        });
        document.setSenders(senders);
    }

    private void setupChildEntity(Document document) {
        setupDoctype(document);
        setupSenders(document);
    }
}
