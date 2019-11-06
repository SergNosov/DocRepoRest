package gov.kui.docRepoR.IT.SpringBootTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gov.kui.docRepoR.DocRepoURL;
import gov.kui.docRepoR.Entity.CommonMessage;
import gov.kui.docRepoR.Entity.Sender;
import gov.kui.docRepoR.IT.BaseSBTests;
import gov.kui.docRepoR.IT.JsonSenders;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.HashSet;


import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SenderControllerSpringBootTest extends BaseSBTests<Sender> {

    @Autowired
    public SenderControllerSpringBootTest(TestRestTemplate testRestTemplate) {
        super(testRestTemplate,Sender.class);
    }

    @BeforeEach
    public void setUp() {
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        entityUrl = DocRepoURL.SENDERS.toString();
        idEntitySet = new HashSet<>();
    }

    @AfterEach
    public void tearDown() {
        if (!idEntitySet.isEmpty()) {
            idEntitySet.stream().forEach(id -> deleteById(id));
            idEntitySet.clear();
        }
    }

    @ParameterizedTest(name = "{index} json = {0}")
    @EnumSource(JsonSenders.class)
    @DisplayName("1. Add Sender. Check HttpStatus of response")
    @Order(1)
    public void testAddSenderWithDifferentJsonSenderValue(JsonSenders jsonSendersEnum) throws IOException {
        int httpStatus = setHttpStatus(jsonSendersEnum);
        testAddEntityWithDifferentJsonValue(jsonSendersEnum.toString(), httpStatus);
    }

    @ParameterizedTest(name = "{index} json = {0}")
    @EnumSource(value = JsonSenders.class, names = {"JSON_GOOD"})
    @DisplayName("2. Add Sender. Check sender from response")
    @Order(2)
    public void testAddSenderOK(JsonSenders jsonSendersEnum) throws IOException {
        Sender senderFromJson = mapper.readValue(jsonSendersEnum.toString(), Sender.class);
        ResponseEntity<Sender> response = addNewEntity(senderFromJson);
        Sender senderFromResponse = response.getBody();

        System.out.println("Sender from response:" + "\n" + senderFromResponse);

        assertAll(
                () -> assertNotNull(senderFromResponse),
                () -> assertEquals(senderFromJson.getTitle(), senderFromResponse.getTitle())
        );
    }

    @Test
    @DisplayName("3. Testing the receipt of all senders")
    @Order(3)
    public void testGetAllSenders() {
        getAll();
    }

    @Test
    @DisplayName("4. Testing the receipt of sender by id. OK.")
    @Order(4)
    public void testGetSenderById() throws IOException {
        Sender senderFromJson = mapper.readValue(JsonSenders.JSON_GOOD.toString(), Sender.class);
        ResponseEntity<Sender> response = addNewEntity(senderFromJson);
        Sender senderExpected = response.getBody();

        ResponseEntity<Sender> responseEntity = getById(senderExpected.getId());
        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCode().value());

        Sender senderActual = responseEntity.getBody();
        assertAll(
                () -> assertNotNull(senderActual),
                () -> assertEquals(senderExpected.getTitle(), senderActual.getTitle())
        );
    }

    @Test
    @DisplayName("5. Testing the receipt of sender by id. BAD.")
    @Order(5)
    public void testGetSenderByIdBAD() {
        testGetEntityByIdBad();
    }

    @Test
    @DisplayName("6. Testing delete sender by id. OK.")
    @Order(6)
    public void testDeleteSenderByIdOK() throws IOException {
        Sender senderFromJson = mapper.readValue(JsonSenders.JSON_GOOD.toString(), Sender.class);
        Sender senderExpected = addNewEntity(senderFromJson).getBody();

        ResponseEntity<CommonMessage> response = deleteById(senderExpected.getId());
        System.out.println("response: " + response.getBody().getMessage());

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatusCode().value()),
                () -> assertEquals("Удален отправитель id - " + senderExpected.getId(),
                        response.getBody().getMessage())
        );
    }

    @Test
    @DisplayName("7. Testing delete sender by id. BAD.")
    @Order(7)
    public void testDeleteSenderByIdBAD() {
        testDeleteEntityByIdBad();
    }

    @Test
    @DisplayName("8. Testing update sender. OK.")
    @Order(8)
    public void testUpdateSenderOK() throws IOException {
        Sender senderFromJson = mapper.readValue(JsonSenders.JSON_GOOD.toString(), Sender.class);
        Sender senderExpected = addNewEntity(senderFromJson).getBody();
        senderExpected.setTitle("new123");

        ResponseEntity<Sender> response = update(senderExpected);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        Sender senderUpdated = response.getBody();
        assertAll(
                () -> assertNotNull(senderUpdated),
                () -> assertEquals(senderExpected.getId(), senderUpdated.getId()),
                () -> assertEquals(senderExpected.getTitle(), senderUpdated.getTitle())
        );
    }

    @Test
    @DisplayName("9. Testing update sender. Bad ID.")
    @Order(9)
    public void testUpdateSenderBadID() throws IOException {
        testUpdateEntityBadId(JsonSenders.JSON_GOOD.toString());
    }

    @Test
    @DisplayName("10. Testing update sender. Invalid Sender.")
    @Order(10)
    public void testUpdateSenderNotValidSender() throws IOException {
        Sender senderFromJson = mapper.readValue(JsonSenders.JSON_GOOD.toString(), Sender.class);
        Sender senderExpected = addNewEntity(senderFromJson).getBody();
        senderExpected.setTitle(" ");

        ResponseEntity<Sender> response = update(senderExpected);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
    }

    private int setHttpStatus(JsonSenders jsonSendersEnum) {
        switch (jsonSendersEnum) {
            case JSON_GOOD: {
                return HttpStatus.OK.value();
            }
            default: {
                return HttpStatus.BAD_REQUEST.value();
            }
        }
    }
}
