package gov.kui.docRepoR.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class DoctypeTests {

    @Test
    void testEquals(){
        Doctype doctype1 = Doctype.builder().id(1).title("new Title").build();
        Doctype doctype2 = Doctype.builder().id(2).title("new Title2").build();
        Doctype doctype3 = Doctype.builder().id(1).title("new Title4").build();

        assertAll(
                () -> assertEquals(doctype1,doctype3),
                () -> assertNotEquals(doctype1,doctype2)
        );
    }
}
