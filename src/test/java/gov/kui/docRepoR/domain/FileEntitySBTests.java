package gov.kui.docRepoR.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class FileEntitySBTests {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    @Autowired
    private EntityManagerFactoryBuilder entityManagerFactoryBuilder;

    @Test
    void testReadFileEntityFromDB() {

        EntityManager em = entityManagerFactory.createEntityManager();

        Query q = em.createNativeQuery("SELECT * from files  where id=37",FileEntity.class);

        em.getTransaction().begin();
        //FileEntity fileEntity = em.find(FileEntity.class,37);
        FileEntity fileEntity = (FileEntity) q.getSingleResult();

        //System.out.println("----: "+fileEntitys.get(0));

        em.getTransaction().commit();
        em.close();
        System.out.println("--- the END ---");
    }
}
