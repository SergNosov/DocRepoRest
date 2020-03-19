package gov.kui.docRepoR.IT.SpringBootTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gov.kui.docRepoR.DocRepoURL;
import gov.kui.docRepoR.domain.CommonMessage;
import gov.kui.docRepoR.domain.Sender;
import gov.kui.docRepoR.JsonSender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SenderControllerSpringBootTest extends BaseSBTests<Sender> {

    @Autowired
    public SenderControllerSpringBootTest(TestRestTemplate testRestTemplate) {
        super(testRestTemplate, Sender.class);
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
    @EnumSource(JsonSender.class)
    @DisplayName("1. Add Sender. Check HttpStatus of response")
    @Order(1)
    public void testAddSenderWithDifferentJsonSenderValue(JsonSender jsonSenderEnum) throws IOException {
        int httpStatus = setHttpStatus(jsonSenderEnum);
        testAddEntityWithDifferentJsonValue(jsonSenderEnum.toString(), httpStatus);
    }

    @ParameterizedTest(name = "{index} json = {0}")
    @EnumSource(value = JsonSender.class, names = {"JSON_GOOD"})
    @DisplayName("2. Add Sender. Check sender from response")
    @Order(2)
    public void testAddSenderOK(JsonSender jsonSenderEnum) throws IOException {
        Sender senderFromJson = mapper.readValue(jsonSenderEnum.toString(), Sender.class);
        addSenderOk(senderFromJson);
    }

    @ParameterizedTest(name = "{index} json = {0}")
    @EnumSource(value = JsonSender.class, names = {"JSON_GOOD"})
    @DisplayName("3. Add Sender. Check unique sender title")
    @Order(3)
    public void testAddSenderWithNotUniqueTitleBad(JsonSender jsonSenderEnum) throws IOException {

        Sender senderFromJson = mapper.readValue(jsonSenderEnum.toString(), Sender.class);
        addSenderOk(senderFromJson);
        ResponseEntity<Sender> responseEntityNotUniqueTitle = addNewEntity(senderFromJson);

        assertEquals(HttpStatus.BAD_REQUEST.value(), responseEntityNotUniqueTitle.getStatusCode().value());
    }

    private void addSenderOk(Sender sender) {
        ResponseEntity<Sender> response = addNewEntity(sender);
        Sender senderFromResponse = response.getBody();

        assertAll(
                () -> assertNotNull(senderFromResponse),
                () -> assertEquals(sender.getTitle(), senderFromResponse.getTitle())
        );
    }

    @Test
    @DisplayName("4. Testing the receipt of all senders")
    @Order(4)
    public void testGetAllSenders() {
        getAll();
    }

    @Test
    @DisplayName("5. Testing the receipt of sender by id. OK.")
    @Order(5)
    public void testGetSenderById() throws IOException {
        Sender senderFromJson = mapper.readValue(JsonSender.JSON_GOOD.toString(), Sender.class);
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
    @DisplayName("6. Testing the receipt of sender by id. BAD.")
    @Order(6)
    public void testGetSenderByIdBAD() {
        testGetEntityByIdBad();
    }

    @Test
    @DisplayName("7. Testing delete sender by id. OK.")
    @Order(7)
    public void testDeleteSenderByIdOK() throws IOException {
        Sender senderFromJson = mapper.readValue(JsonSender.JSON_GOOD.toString(), Sender.class);
        Sender senderExpected = addNewEntity(senderFromJson).getBody();

        ResponseEntity<CommonMessage> response = deleteById(senderExpected.getId());

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatusCode().value()),
                () -> assertEquals("Удален отправитель id - " + senderExpected.getId(),
                        response.getBody().getMessage())
        );
    }

    @Test
    @DisplayName("8. Testing delete sender by id. BAD.")
    @Order(8)
    public void testDeleteSenderByIdBAD() {
        testDeleteEntityByIdBad();
    }

    @Test
    @DisplayName("9. Testing update sender. OK.")
    @Order(9)
    public void testUpdateSenderOK() throws IOException {
        Sender senderFromJson = mapper.readValue(JsonSender.JSON_GOOD.toString(), Sender.class);
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
    @DisplayName("10. Testing update sender. Bad ID.")
    @Order(10)
    public void testUpdateSenderBadID() throws IOException {
        testUpdateEntityBadId(JsonSender.JSON_GOOD.toString());
    }

    @Test
    @DisplayName("11. Testing update sender. Invalid Sender.")
    @Order(11)
    public void testUpdateSenderNotValidSender() throws IOException {
        Sender senderFromJson = mapper.readValue(JsonSender.JSON_GOOD.toString(), Sender.class);
        Sender senderExpected = addNewEntity(senderFromJson).getBody();
        senderExpected.setTitle(" ");

        ResponseEntity<Sender> response = update(senderExpected);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
    }

    @Test
    @DisplayName("12. Testing update&GetAll.")
    @Order(12)
    public void testUpdateAndGetAllSenders() throws IOException {
        Sender senderFromJson = mapper.readValue(JsonSender.JSON_GOOD.toString(), Sender.class);
        Sender senderAdd = addNewEntity(senderFromJson).getBody();
        List<Sender> allSenders = getAll().getBody();

        Sender senderExpected = getSenderFromList(allSenders,senderAdd.getId());
        assertAll(
                () -> assertNotNull(senderExpected),
                () -> assertEquals(senderAdd.getId(), senderExpected.getId()),
                () -> assertEquals(senderAdd.getTitle(), senderExpected.getTitle())
        );
        senderExpected.setTitle("new123");

        ResponseEntity<Sender> response = update(senderExpected);
        List<Sender> newAllSenders = getAll().getBody();
        Sender senderActual = getSenderFromList(newAllSenders,senderExpected.getId());

        assertAll(
                () -> assertNotNull(senderActual),
                () -> assertEquals(senderExpected.getId(), senderActual.getId()),
                () -> assertEquals(senderExpected.getTitle(), senderActual.getTitle())
        );
    }

    private Sender getSenderFromList(List<Sender> allSenders, int id){
        Sender tempSender = null;
        for (Sender sender:allSenders){
            if (sender.getId() == id){
                tempSender = sender;
            }
        }
        return tempSender;
    }

    protected ResponseEntity<List<Sender>> getAll() {
        ResponseEntity<List<Sender>> response = restTemplate.exchange(
                entityUrl,
                HttpMethod.GET,
                new HttpEntity<>(this.httpHeaders),
                new ParameterizedTypeReference<List<Sender>>() {});
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        return response;
    }

    private int setHttpStatus(JsonSender jsonSenderEnum) {
        switch (jsonSenderEnum) {
            case JSON_GOOD:
            case JSON_ZERO_ID:{
                return HttpStatus.OK.value();
            }
            default: {
                return HttpStatus.BAD_REQUEST.value();
            }
        }
    }
}
