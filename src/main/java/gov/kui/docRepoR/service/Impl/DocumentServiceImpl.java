package gov.kui.docRepoR.service.Impl;

import gov.kui.docRepoR.Entity.Document;
import gov.kui.docRepoR.dao.DocumentRepository;
import gov.kui.docRepoR.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentServiceImpl implements DocumentService {
    private DocumentRepository documentRepository;

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
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
