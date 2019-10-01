package gov.kui.docRepoR.service.Impl;

import gov.kui.docRepoR.Entity.Document;
import gov.kui.docRepoR.dao.DocumentRepository;
import gov.kui.docRepoR.dto.DocumentDto;
import gov.kui.docRepoR.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentServiceImpl implements DocumentService {

    private DocumentRepository documentRepository;
    private DocumentDto documentDto;

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository, DocumentDto documentDto) {
        this.documentRepository = documentRepository;
        this.documentDto = documentDto;
    }

    @Override
    public List<DocumentDto> findAll() {
        List<Document> documents = documentRepository.findAll();
        List<DocumentDto> documentDtos = new ArrayList<>();

        for (Document document: documents){
            documentDtos.add(documentDto.getDocumentDto(document));
        }

        return documentDtos;
    }

    @Override
    public DocumentDto findById(int id) {

        Optional<Document> result = documentRepository.findById(id);

        if (result.isPresent()) {
            return documentDto.getDocumentDto(result.get());
        } else {
            throw new RuntimeException("Did not find document id - "+id);
        }
    }

    @Override
    public Document save(Document document) {
        return documentRepository.save(document);
    }

    @Override
    public void deleteById(int id) {
        documentRepository.deleteById(id);
    }
}
