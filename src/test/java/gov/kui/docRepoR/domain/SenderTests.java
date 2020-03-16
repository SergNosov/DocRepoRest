package gov.kui.docRepoR.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SenderTests {

    @Test
    void testEquals() {
        Sender sender1 = Sender.builder().id(1).title("new Title").build();
        Sender sender2 = Sender.builder().id(2).title("new Title2").build();
        Sender sender3 = Sender.builder().id(1).title("new Title3").build();

        System.out.println("--- sender1: "+sender1);

        assertAll(
                () -> assertEquals(sender1, sender3),
                () -> assertNotEquals(sender1, sender2)
        );
    }
}
