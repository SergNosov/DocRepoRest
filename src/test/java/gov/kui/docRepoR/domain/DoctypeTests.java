package gov.kui.docRepoR.domain;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
        doctype3.setTitle("null111");

        Set<Doctype> doctypes = new HashSet<>(Arrays.asList(new Doctype[]{doctype1, doctype2, doctype3}));

        assertAll(
                () -> assertEquals(doctype1,doctype3),
                () -> assertNotEquals(doctype1,doctype2),
                () -> assertEquals(2, doctypes.size())
        );
    }

    @Test
    void testSetTitle(){
        String title = " new title ";
        Doctype doctype = new Doctype();
        doctype.setTitle(title);

        assertEquals(doctype.getTitle(),title.trim());
    }
}
