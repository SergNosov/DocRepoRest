package gov.kui.docRepoR.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class DoctypeTests {

    @Test
    void testEquals(){
        Doctype doctype1 = new Doctype();
        doctype1.setId(1);
        doctype1.setTitle("null111");

        Doctype doctype2 = new Doctype();
        doctype2.setId(2);
        doctype2.setTitle("new Title2");

        Doctype doctype3 = new Doctype();
        doctype3.setId(1);
        doctype3.setTitle("new Title45");

        assertAll(
                () -> assertEquals(doctype1,doctype3),
                () -> assertNotEquals(doctype1,doctype2)
        );
    }
}
