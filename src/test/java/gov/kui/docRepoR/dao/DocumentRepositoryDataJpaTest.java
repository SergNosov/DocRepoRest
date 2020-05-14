package gov.kui.docRepoR.dao;

import gov.kui.docRepoR.domain.Document;
import gov.kui.docRepoR.dto.DoctypeDto;
import gov.kui.docRepoR.dto.DocumentDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DocumentRepositoryDataJpaTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    void testEm() {
        TypedQuery<DocumentDto> query = entityManager.createQuery(
                "select new gov.kui.docRepoR.dto.DocumentDto(d.id,d.number,d.docDate,d.title,d.content)" +
                        " from Document d where d.id = :id",
                DocumentDto.class)
                .setParameter("id", 21);

        DocumentDto documentDto = query.getSingleResult();

        TypedQuery<DoctypeDto> doctypeDtoTypedQuery = entityManager.createQuery(
                "select new gov.kui.docRepoR.dto.DoctypeDto(d.id,d.title) from Doctype d" +
                        " inner join Document doc on d.id = doc.doctype.id ",DoctypeDto.class
       //                 " inner join Document doc on d.id = doc.doctype.id ",DoctypeDto.class
        );

        DoctypeDto doctypeDto = doctypeDtoTypedQuery.getSingleResult();

        documentDto.setDoctype(doctypeDto);

        log.info("------ " + documentDto.toString());
    }
}
