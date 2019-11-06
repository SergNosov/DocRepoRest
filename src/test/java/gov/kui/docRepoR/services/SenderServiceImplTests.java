package gov.kui.docRepoR.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gov.kui.docRepoR.Entity.Sender;
import gov.kui.docRepoR.IT.JsonSenders;
import gov.kui.docRepoR.dao.SenderRepository;
import gov.kui.docRepoR.service.Impl.SenderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
public class SenderServiceImplTests {

    @Mock
    private SenderRepository senderRepository;

    @InjectMocks
    private SenderServiceImpl senderService;

    private Sender validSender;
    private Sender invalidSender;

    @BeforeEach
    void setUp() throws IOException {
        validSender = new ObjectMapper().registerModule(new JavaTimeModule())
                .readValue(JsonSenders.JSON_GOOD.toString(), Sender.class);
        invalidSender = new ObjectMapper().registerModule(new JavaTimeModule())
                .readValue(JsonSenders.JSON_NO_REQURED_FIELDS.toString(), Sender.class);
    }

    @Test
    @DisplayName("1. Testing the receipt of all senders.")
    @Order(1)
    void testGetAllSenders() {
        List<Sender> senderList = new ArrayList<>();
        senderList.add(validSender);

        given(senderRepository.findAll()).willReturn(senderList);
        List<Sender> returnedList = senderService.findAll();
        then(senderRepository).should().findAll();
        assertNotNull(returnedList);
        assertEquals(1, returnedList.size());
    }

    @Test
    @DisplayName("2. Testing the receipt of sender by id. OK.")
    @Order(2)
    void testGetSenderByIdOk() {
        given(senderRepository.findById(anyInt())).willReturn(Optional.of(validSender));
        Sender returnedsender = senderService.findById(validSender.getId());
        then(senderRepository).should().findById(validSender.getId());
        assertAll(
                () -> assertNotNull(returnedsender),
                () -> assertEquals(returnedsender.getId(), returnedsender.getId()),
                () -> assertEquals(returnedsender.getTitle(), returnedsender.getTitle())
        );
    }

    @Test
    @DisplayName("3. Testing the receipt of sender by id. BAD.")
    @Order(3)
    void testGetSenderByIdBad() {
        given(senderRepository.findById(anyInt())).willReturn(Optional.empty());
        RuntimeException rte = assertThrows(RuntimeException.class, () -> senderService.findById(validSender.getId()));
        assertEquals("Не найден отправитель с id - " + validSender.getId(), rte.getMessage());
    }

    @Test
    @DisplayName("4. Testing deleteById sender by id. OK.")
    @Order(4)
    void testDeleteSenderByIdOk() {
        given(senderRepository.findById(anyInt())).willReturn(Optional.of(validSender));
        int deletedId = senderService.deleteById(validSender.getId());
        assertEquals(validSender.getId(), deletedId);
    }

    @Test
    @DisplayName("5. Testing deleteById sender by id. BAD.")
    @Order(5)
    void testDeleteSenderByIdBad() {
        given(senderRepository.findById(anyInt())).willReturn(Optional.empty());
        RuntimeException rte = assertThrows(RuntimeException.class, () -> senderService.findById(validSender.getId()));
        assertEquals("Не найден отправитель с id - " + validSender.getId(), rte.getMessage());
    }

    @Test
    @DisplayName("6. Testing save sender (null). BAD.")
    @Order(6)
    void testSaveSenderNull() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> senderService.save(null));
        assertEquals("Не указан Sender (null), или заголовок (sender.title) пуст.", iae.getMessage());
    }

    @Test
    @DisplayName("7. Testing save sender. BAD (Title is null)")
    @Order(7)
    void testSaveSenderTitleNull() {
        System.out.println("title:" + invalidSender.getTitle().length());
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> senderService.save(invalidSender));
        assertEquals("Не указан Sender (null), или заголовок (sender.title) пуст.", iae.getMessage());
    }

    @Test
    @DisplayName("8. Testing save sender. BAD (Title is blank).")
    @Order(8)
    void testSaveSenderTitleBlank() {
        validSender.setTitle("   ");
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> senderService.save(validSender));
        assertEquals("Не указан Sender (null), или заголовок (sender.title) пуст.", iae.getMessage());
    }

    @Test
    @DisplayName("9. Testing save sender. OK.")
    @Order(9)
    void testSaveSenderOk() {
        given(senderRepository.findById(anyInt())).willReturn(Optional.of(validSender));
        given(senderRepository.save(any())).willReturn(validSender);

        Sender savedSender = senderService.save(validSender);

        assertAll(
                () -> assertNotNull(savedSender),
                () -> assertEquals(validSender.getId(), savedSender.getId()),
                () -> assertEquals(validSender.getTitle(), savedSender.getTitle())

        );
    }

    @Test
    @DisplayName("10. Testing of  title is exists(isExists = false).")
    @Order(10)
    void testIsExistsValueInField() {
        given(senderRepository.existsByTitle(anyString())).willReturn(false);
        boolean actualExists = senderService.isExistsValueInField("Title", "title");
        then(senderRepository).should().existsByTitle(anyString());
        assertFalse(actualExists);
    }

    @Test
    @DisplayName("11. Testing of  title is exists(isExists = true).")
    @Order(11)
    void testIsExistsValueInField_True() {
        given(senderRepository.existsByTitle(anyString())).willReturn(true);
        boolean actualExists = senderService.isExistsValueInField("Title", "title");
        then(senderRepository).should().existsByTitle(anyString());
        assertTrue(actualExists);
    }

    @Test
    @DisplayName("12. Testing of  title is exists(value - null).")
    @Order(12)
    void testIsExistsValueInField_Null() {
        boolean actualExists = senderService.isExistsValueInField(null, null);
        assertFalse(actualExists);
    }

    @Test
    @DisplayName("13. Testing of  title is exists(illegal field).")
    @Order(13)
    void testIsExistsValueInField_illegalField() {
        String fieldName = "otherField";
        UnsupportedOperationException uoe = assertThrows(UnsupportedOperationException.class,
                () -> senderService.isExistsValueInField("title", fieldName));
        assertEquals("Validation on field: '" + fieldName + "' not supported.", uoe.getMessage());
    }
}
