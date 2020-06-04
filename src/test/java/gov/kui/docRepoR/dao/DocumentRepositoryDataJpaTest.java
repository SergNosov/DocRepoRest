package gov.kui.docRepoR.dao;

import gov.kui.docRepoR.domain.Doctype;
import gov.kui.docRepoR.domain.Document;
import gov.kui.docRepoR.domain.FileEntity;
import gov.kui.docRepoR.domain.Sender;
import gov.kui.docRepoR.dto.DocumentDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DocumentRepositoryDataJpaTest {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DoctypeRepository doctypeRepository;

    @Autowired
    private EntityManager entityManager;

    private Document document;

   // @BeforeEach
    void setUp() {
        document = new Document();
        document.setNumber("123-p");
        document.setDocDate(LocalDate.now());
        document.setContent("contentDocument");
        document.setTitle("document Title");

        Sender sender1 = new Sender();
        sender1.setTitle("Алькор");

        Sender sender2 = new Sender();
        sender2.setId(0);
        sender2.setTitle("Мицар");

        Set<Sender> senders = new HashSet<>();
        senders.add(sender1);
        senders.add(sender2);

        Doctype doctype = new Doctype();
      //  doctype.setId(0);
        doctype.setTitle("Анонимка111");

        document.setDoctype(doctype);
        document.setSenders(senders);

        entityManager.persist(sender1);
        entityManager.persist(sender2);
        entityManager.persist(doctype);
        entityManager.persist(document);

        MultipartFile multipartFile = new MockMultipartFile(
                "testFile.pdf",
                "testFile.pdf",
                "application/pdf",
                new byte[]{1, 2, 3}
        );

        FileEntity fileEntity = FileEntity.getInstance(multipartFile, document.getId());
        entityManager.persist(fileEntity);

        log.info("--- document: " + document.info());
    }

    @Test
    void findDocumentDtoByIdOK() {
        DocumentDto documentDto = documentRepository.findDtoById(document.getId()).get();
        log.info("--- documentDto: " + documentDto);

        assertNotNull(documentDto);
        assertEquals(document.getId(), documentDto.getId());
        assertEquals(document.getNumber(), documentDto.getNumber());
        assertEquals(document.getDocDate(), documentDto.getDocDate());
        assertEquals(document.getTitle(), documentDto.getTitle());
        assertEquals(document.getContent(), documentDto.getContent());
        assertNotNull(documentDto.getDoctype());
        assertEquals(document.getDoctype().getId(), documentDto.getDoctype().getId());
        assertEquals(document.getDoctype().getTitle(), documentDto.getDoctype().getTitle());
        assertEquals(document.getSenders().size(), documentDto.getSenders().size());
        assertEquals(1, documentDto.getFileEntities().size());
    }

    @Test
    void findDocumentDtoByIdBad() {
        Optional<DocumentDto> documentDtoOptional = documentRepository.findDtoById(Integer.MIN_VALUE);
        assertNotNull(documentDtoOptional);
        assertFalse(documentDtoOptional.isPresent());
    }

    @Test
    void getAllByPage(){
        Query query = entityManager.createQuery("select count(d.id) from Document d");
        final Long countId = (Long) query.getSingleResult();
        log.info("--- count documentDto: "+countId);

        Pageable pageable = PageRequest.of(countId.intValue(), 1);
        List<DocumentDto> documentDtos = documentRepository.findAllDtosByPage(pageable);

        log.info("--- documentDtos: "+documentDtos);

        assertNotNull(documentDtos);
        assertEquals(1,documentDtos.size());
        assertEquals(document.getId(),documentDtos.get(0).getId());
    }

    void testPersist(){

        Doctype d = doctypeRepository.getOne(1);
//        Doctype d = new Doctype();
//        d.setId(6);
//        d.setTitle("johndoe");

        document.setDoctype(d);

        entityManager.persist(document);

        System.out.println("--- document: "+ document.info());
    }

    @Test
    void testGetTwentyFirstDoc() throws SQLException {

        Document document=null;

        if (!documentRepository.existsById(211111)) {
            //document = documentRepository.findById(21).get();
            throw new RuntimeException("document not found!!!!");
        }

        System.out.println("--- document: "+document.info());

    }
}
