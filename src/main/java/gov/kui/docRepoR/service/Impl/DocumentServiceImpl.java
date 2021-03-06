package gov.kui.docRepoR.service.Impl;

import gov.kui.docRepoR.dao.DoctypeRepository;
import gov.kui.docRepoR.dao.FileEntityBlobRepository;
import gov.kui.docRepoR.dao.FileEntityRepository;
import gov.kui.docRepoR.dao.SenderRepository;
import gov.kui.docRepoR.domain.Document;
import gov.kui.docRepoR.domain.FileEntity;
import gov.kui.docRepoR.domain.Sender;
import gov.kui.docRepoR.dao.DocumentRepository;
import gov.kui.docRepoR.dto.DocumentDto;
import gov.kui.docRepoR.service.DocumentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
//@CacheConfig(cacheNames = "documents")
@Transactional(readOnly = true)
@AllArgsConstructor
@Service
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;
    private final DoctypeRepository doctypeRepository;
    private final SenderRepository senderRepository;
    private final FileEntityRepository fileEntityRepository;
    private final FileEntityBlobRepository fileEntityBlobRepository;

    @Override
    //@Cacheable
    public List<Document> findAll() {
        return documentRepository.findAll();
    }

    @Override
    public List<DocumentDto> findAllDtos() {
        return documentRepository.findAllDtos();
    }

    @Override
    public List<DocumentDto> findAllDtosByPage(Pageable pagable) {
        Assert.notNull(pagable, "pagable is null.");
        return documentRepository.findAllDtosByPage(pagable);
    }

    @Override
    public Document findById(int id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Не найден документ с id - " + id));
    }

    @Override
    public DocumentDto findDtoById(int id) {
        return documentRepository.findDtoById(id)
                .orElseThrow(() -> new IllegalArgumentException("Не найден документ с id - " + id));
    }

    @Override
    //@CacheEvict(allEntries = true)
    @Transactional
    public int deleteById(int id) {
        Document document = findById(id);
        document.getFileEntities().forEach(f -> fileEntityBlobRepository.deleteFileEntityBlobById(f.getId()));
        documentRepository.delete(document);
        return id;
    }

    @Override
    //@CacheEvict(allEntries = true)
    @Transactional
    public Document save(final Document document) {
        this.checkDocument(document);
        this.setupChildEntity(document);
        return documentRepository.save(document);
    }

    private void checkDocument(final Document document) {
        Assert.notNull(document, "Document is null.");
        Assert.hasText(document.getTitle(), "Не указан заголовок документа. id = " + document.getId());
        Assert.notNull(document.getDocDate(),"Не указана дата документа. id = " + document.getId());
        Assert.notNull(document.getDoctype(), "Не установлен тип документа (Doctype of document id = " +
                document.getId() + " is null)");
        Assert.notEmpty(document.getSenders(), "Не указаны стороны подписания документа. id = " + document.getId());

        if (document.getId() != 0 && !documentRepository.existsById(document.getId())) {
            throw new IllegalArgumentException("Не найден документ с id - " + document.getId());
        }
    }

    private void setupChildEntity(final Document document) {
        setupDoctype(document);
        setupSenders(document);
        if (document.getId() != 0) {
            setupFileEntities(document);
        }
    }

    private void setupDoctype(final Document document) {
        final int idDoctypeInDoc = document.getDoctype().getId();
        document.setDoctype(
                doctypeRepository.findById(idDoctypeInDoc)
                        .orElseThrow(() -> new IllegalArgumentException("Не найден тип документа с id - " + idDoctypeInDoc))
        );
    }

    private void setupSenders(final Document document) {
        Set<Sender> senders = new HashSet<>();
        document.getSenders().forEach(sender ->
                senders.add(senderRepository.findById(sender.getId())
                        .orElseThrow(
                                () -> new IllegalArgumentException("Не найден отправитель с id - " + sender.getId())))
        );
        document.setSenders(senders);
    }

    private void setupFileEntities(final Document document) {
        Set<FileEntity> fileEntities = new HashSet<>();
        document.getFileEntities().forEach(fileEntity ->
                fileEntities.add(fileEntityRepository.findById(fileEntity.getId())
                        .orElseThrow(
                                () -> new IllegalArgumentException((" Не найден  файл с id - " + fileEntity.getId()))))
        );
        document.setFileEntities(fileEntities);
    }
}
