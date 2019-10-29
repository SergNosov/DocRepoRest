package gov.kui.docRepoR.service.Impl;

import gov.kui.docRepoR.Entity.Doctype;
import gov.kui.docRepoR.Entity.Document;
import gov.kui.docRepoR.Entity.Sender;
import gov.kui.docRepoR.dao.DocumentRepository;
import gov.kui.docRepoR.service.DoctypeService;
import gov.kui.docRepoR.service.DocumentService;
import gov.kui.docRepoR.service.SenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentServiceImpl implements DocumentService {
    private DocumentRepository documentRepository;
    private DoctypeService doctypeService;
    private SenderService senderService;

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository,
                               DoctypeService doctypeService,
                               SenderService senderService) {
        this.documentRepository = documentRepository;
        this.doctypeService = doctypeService;
        this.senderService = senderService;
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
        if (!(document == null) || document.getTitle() == null || document.getTitle().trim().isEmpty()) {
            if (document.getId() != 0) {
                this.findById(document.getId());
            }
            this.checkDoctypeAndSenders(document);
            return documentRepository.save(document);
        } else {
            throw new IllegalArgumentException("Document is null or title is empty");
        }
    }

    @Override
    public int deleteById(int id) {
        documentRepository.deleteById(this.findById(id).getId());
        return id;
    }

    private void checkDoctypeAndSenders(Document document) {//todo: разделить на отдельные методы. Вызывать методы внутри этого?
        if (document == null) {
            throw new IllegalArgumentException("checkDoctypeAndSenders: document is null");
        }
        if (document.getDoctype() == null) {
            throw new IllegalArgumentException("Не установлен тип документа (Doctype of document id=" +
                    document.getId() + " is null");
        }

        Doctype doctypeFromBase = doctypeService.findById(document.getDoctype().getId());
        document.setDoctype(doctypeFromBase);

        List<Sender> senders = new ArrayList<>();
        for (Sender sender : document.getSenders()) {
            senders.add(senderService.findById(sender.getId()));
        }
        document.setSenders(senders);
    }
}
