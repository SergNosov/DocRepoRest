package gov.kui.docRepoR.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SenderTests {

    @Test
    void testEquals() {
        Sender sender1 = new Sender();
        sender1.setId(1);
        sender1.setTitle("new Title");

        Sender sender2 = new Sender();
        sender2.setId(2);
        sender2.setTitle("new Title2");

        Sender sender3 = new Sender();
        sender3.setId(1);
        sender3.setTitle("new Title11");

        System.out.println("--- sender1: "+sender1);

        assertAll(
                () -> assertEquals(sender1, sender3),
                () -> assertNotEquals(sender1, sender2)
        );
    }
}
