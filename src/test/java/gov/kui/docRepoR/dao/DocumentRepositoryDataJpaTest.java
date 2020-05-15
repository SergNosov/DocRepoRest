package gov.kui.docRepoR.dao;

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
import java.util.List;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DocumentRepositoryDataJpaTest {

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
                "select new gov.kui.docRepoR.dto.DoctypeDto(d.id,d.title) from Doctype d" +
                        " join Document doc on d.id = doc.doctype.id where doc.id = :id",
                DoctypeDto.class)
                .setParameter("id", documentId);
        DoctypeDto doctypeDto = doctypeDtoTypedQuery.getSingleResult();

        TypedQuery<SenderDto> senderDtoTypedQuery = entityManager.createQuery(
                "select new gov.kui.docRepoR.dto.SenderDto(s.id,s.title) from Document d" +
                        " join d.senders s  where d.id = :idDoc",
                SenderDto.class).setParameter("idDoc", documentId);

        List<SenderDto> senderDtos = senderDtoTypedQuery.getResultList();

        TypedQuery<FileEntityDto> fileEntityDtoTypedQuery = entityManager.createQuery(
                "select new gov.kui.docRepoR.dto.FileEntityDto(f.id, f.filename, f.contentType, f.fileSize)" +
                        " from FileEntity f" +
                        " where f.documentId = :idDoc",
                FileEntityDto.class).setParameter("idDoc", documentId);

        List<FileEntityDto> fileEntityDtos = fileEntityDtoTypedQuery.getResultList();

        documentDto.setDoctype(doctypeDto);
        documentDto.setSenders(senderDtos);
        documentDto.setFileEntities(fileEntityDtos);

        log.info("------ " + documentDto.toString());
    }
}
