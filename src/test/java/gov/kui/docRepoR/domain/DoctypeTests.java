package gov.kui.docRepoR.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class DoctypeTests {

    @Test
    void testEquals(){
        Doctype doctype0 = new Doctype(null);

        //doctype0.setTitle(null);

        Doctype doctype1 = Doctype.builder().id(1).title("null111").build();
        Doctype doctype2 = Doctype.builder().id(2).title("new Title2").build();
        Doctype doctype3 = Doctype.builder().id(1).title("new Title4").build();

        System.out.println("--- dictype1: "+doctype0);

        assertAll(
                () -> assertEquals(doctype1,doctype3),
                () -> assertNotEquals(doctype1,doctype2)
        );
    }
}
