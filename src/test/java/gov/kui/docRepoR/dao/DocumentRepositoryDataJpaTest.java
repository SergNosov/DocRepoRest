package gov.kui.docRepoR.dao;

import gov.kui.docRepoR.domain.Doctype;
import gov.kui.docRepoR.domain.Document;
import gov.kui.docRepoR.dto.DoctypeDto;
import gov.kui.docRepoR.dto.DocumentDto;
import gov.kui.docRepoR.dto.FileEntityDto;
import gov.kui.docRepoR.dto.SenderDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DocumentRepositoryDataJpaTest {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void testEm() {
        final int documentId = 21;

        TypedQuery<DocumentDto> query = entityManager.createQuery(
                "select new gov.kui.docRepoR.dto.DocumentDto(d.id,d.number,d.docDate,d.title,d.content)" +
                        " from Document d where d.id = :id",
                DocumentDto.class)
                .setParameter("id", documentId);
        DocumentDto documentDto = query.getSingleResult();

        TypedQuery<DoctypeDto> doctypeDtoTypedQuery = entityManager.createQuery(
                "select new gov.kui.docRepoR.dto.DoctypeDto(d.id,d.title)" +
                        " from Document doc join doc.doctype d where doc.id = :id",
                DoctypeDto.class)
                .setParameter("id", documentId);
        DoctypeDto doctypeDto = doctypeDtoTypedQuery.getSingleResult();

        TypedQuery<SenderDto> senderDtoTypedQuery = entityManager.createQuery(
                "select new gov.kui.docRepoR.dto.SenderDto(s.id,s.title)" +
                        " from Document doc join doc.senders s  where doc.id = :idDoc",
                SenderDto.class)
                .setParameter("idDoc", documentId);
        Set<SenderDto> senderDtos = new HashSet<>(senderDtoTypedQuery.getResultList());

        TypedQuery<FileEntityDto> fileEntityDtoTypedQuery = entityManager.createQuery(
                "select new gov.kui.docRepoR.dto.FileEntityDto(f.id, f.filename, f.contentType, f.fileSize)" +
                        " from FileEntity f  where f.documentId = :idDoc",
                FileEntityDto.class)
                .setParameter("idDoc", documentId);
        Set<FileEntityDto> fileEntityDtos = new HashSet<>(fileEntityDtoTypedQuery.getResultList());

        documentDto.setDoctype(doctypeDto);
        documentDto.setSenders(senderDtos);
        documentDto.setFileEntities(fileEntityDtos);

        log.info("------ " + documentDto.toString());
    }

    @Test
    void findDocumentDtoByIdOK() {

        Doctype doctype = new Doctype();
        doctype.setTitle("New Doctype Title");
        log.info("--- doctype:" + doctype);

        Document document = new Document();
        document.setNumber("123-p");
        document.setDocDate(LocalDate.now());
        document.setContent("contentDocument");
        document.setTitle("document Tiltle");
        document.setDoctype(doctype);

        entityManager.persist(doctype);
        entityManager.persist(document);

        DocumentDto documentDto =  documentRepository.findDtoById(document.getId()).get();

        log.info("--- document: "+document.info());
        log.info("--- documentDto: "+documentDto);

        assertNotNull(documentDto);
        assertEquals(document.getId(),documentDto.getId());
        assertEquals(document.getNumber(), documentDto.getNumber());
        assertEquals(document.getDocDate(), documentDto.getDocDate());
        assertEquals(document.getTitle(), documentDto.getTitle());
        assertEquals(document.getContent(), documentDto.getContent());
        assertNotNull(documentDto.getDoctype());
        assertEquals(document.getDoctype().getId(), documentDto.getDoctype().getId());
        assertEquals(document.getDoctype().getTitle(), documentDto.getDoctype().getTitle());
    }
}
