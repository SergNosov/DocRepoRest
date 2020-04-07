package gov.kui.docRepoR.service.Impl;

import gov.kui.docRepoR.dao.DoctypeRepository;
import gov.kui.docRepoR.dao.SenderRepository;
import gov.kui.docRepoR.domain.Doctype;
import gov.kui.docRepoR.domain.Document;
import gov.kui.docRepoR.domain.Sender;
import gov.kui.docRepoR.dao.DocumentRepository;
import gov.kui.docRepoR.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Service
@CacheConfig(cacheNames = "documents")
@Transactional(readOnly = true)
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
    @Cacheable //todo перенести на уровень фасада? для DTO?
    public List<Document> findAll() {
        System.err.println("--- in findAll()");
        return documentRepository.findAll();
    }

    @Override
    public Document findById(int id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Не найден документ с id - " + id));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional
    public Document save(final Document document) {
        Assert.notNull(document,"Document is null.");
        Assert.hasText(document.getTitle(),"Не указан заголовок документа.");
        Assert.notNull(document.getDoctype(),"Не установлен тип документа (Doctype of document id=" +
                document.getId() + " is null)");

        if (document.getId() != 0) {
            this.findById(document.getId());
        }

        this.setupChildEntity(document);
        return documentRepository.save(document);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional
    public int deleteById(int id) {
        documentRepository.deleteById(this.findById(id).getId());
        return id;
    }

    private void setupDoctype(final Document document) {
        final int idDoc = document.getDoctype().getId();
        final Doctype doctypeFromBase = doctypeRepository.findById(idDoc)
                .orElseThrow(() -> new IllegalArgumentException("Не найден тип документа с id - " + idDoc));
        document.setDoctype(doctypeFromBase);
    }

    private void setupSenders(final Document document) {
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
