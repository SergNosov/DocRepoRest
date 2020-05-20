package gov.kui.docRepoR.domain;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
        sender3.setId(11);
        sender3.setTitle("new Title");

        Sender senderZ = new Sender();
        senderZ.setTitle("senderZero1");

        Sender senderZ1 = new Sender();
        senderZ1.setTitle("senderZero1");

        Set<Sender> senders = new HashSet<>(Arrays.asList(new Sender[]{sender1, sender2, sender3, senderZ, senderZ1}));

        assertAll(
                () -> assertEquals(sender1, sender3),
                () -> assertNotEquals(sender1, sender2),
                () -> assertEquals(3, senders.size())
        );
    }

    @Test
    void testSetTitle() {
        String title = " new title ";
        Sender sender = new Sender();
        sender.setTitle(title);

        assertEquals(sender.getTitle(), title.trim());
    }
}
