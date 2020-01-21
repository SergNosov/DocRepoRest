package gov.kui.docRepoR.IT.SenderControllerOther;

import gov.kui.docRepoR.IT.BaseTests;
import gov.kui.docRepoR.DocRepoURL;
import gov.kui.docRepoR.domain.Sender;
import gov.kui.docRepoR.IT.JsonSenders;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.http.HttpStatus;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SenderControllerRestAssuredIntegrationTests extends BaseTests<Sender> {

    @BeforeEach
    public void init() {
        requestSpec = RestAssured.given().baseUri(DocRepoURL.SENDERS_LOCALHOST.toString()).contentType(ContentType.JSON);
    }

    @AfterEach
    public void destroy() {
        if (!idEntitySet.isEmpty()) {
            idEntitySet.stream().forEach(id -> deleteById(id));
            idEntitySet.clear();
        }
    }

    @ParameterizedTest(name = "{index} json = {0}")
    @EnumSource(JsonSenders.class)
    @DisplayName("Add Sender. Check HttpStatus of response")
    @Order(1)
    public void testAddSenderWithDifferentJsonSenderValue(JsonSenders jsonSendersEnum) throws IOException {
        Sender senderFromJson = mapper.readValue(jsonSendersEnum.toString(), Sender.class);
        Response response = addNewDocRepoEntity(senderFromJson, Sender.class);

        int httpStatus = setHttpStatus(jsonSendersEnum);
        checkStatusCodeAndJSON(response, httpStatus);
    }

    @ParameterizedTest(name = "{index} json = {0}")
    @EnumSource(value = JsonSenders.class, names = {"JSON_GOOD"})
    @DisplayName("Add Sender. Check Sender from response")
    @Order(2)
    public void testAddSenderOK(JsonSenders jsonSendersEnum) throws IOException {
        Sender senderFromJson = mapper.readValue(jsonSendersEnum.toString(), Sender.class);
        Response response = addNewDocRepoEntity(senderFromJson, Sender.class);
        Sender senderFromResponse = response.as(Sender.class);

        assertEquals(senderFromJson.getTitle(), senderFromResponse.getTitle());
    }

    @Test
    @DisplayName("Testing the receipt of all sender")
    @Order(3)
    public void testGetAllSenders() {
        Response response = getAll();
        List<Sender> senderList = response.as(ArrayList.class);

        checkStatusCodeAndJSON(response, HttpStatus.OK.value());
        assertNotNull(senderList);
    }

    @Test
    @DisplayName("Testing the receipt of sender by id. OK.")
    @Order(4)
    public void testGetSenderByIdOk() throws IOException {
        Sender senderFromJson = mapper.readValue(JsonSenders.JSON_GOOD.toString(), Sender.class);
        Sender senderExpected = addNewDocRepoEntity(senderFromJson, Sender.class).as(Sender.class);
        Response response = getById(senderExpected.getId());
        Sender senderActual = response.as(Sender.class);

        assertAll(
                () -> assertNotNull(senderActual),
                () -> assertEquals(senderExpected.getId(), senderActual.getId()),
                () -> assertEquals(senderExpected.getTitle(), senderActual.getTitle())
        );
    }

    @Test
    @DisplayName("Testing the receipt of sender by id. BAD.")
    @Order(5)
    public void testGetSenderByIdBAD() {
        int badId = Integer.MIN_VALUE;
        Response response = getById(badId);
        checkStatusCodeAndJSON(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Testing delete sender by id. OK.")
    @Order(6)
    public void testDeleteSenderByIdOK() throws IOException {
        Sender senderFromJson = mapper.readValue(JsonSenders.JSON_GOOD.toString(), Sender.class);
        Sender senderExpected = addNewDocRepoEntity(senderFromJson, Sender.class).as(Sender.class);

        Response response = deleteById(senderExpected.getId());
        checkStatusCodeAndJSON(response, HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Testing delete sender by id. BAD.")
    @Order(7)
    public void testDeleteDocumentByIdBAD() {
        int badId = Integer.MIN_VALUE;
        Response response = deleteById(badId);
        checkStatusCodeAndJSON(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Testing update sender. OK.")
    @Order(8)
    public void testUpdateSenderOK() throws IOException {
        Sender senderFromJson = mapper.readValue(JsonSenders.JSON_GOOD.toString(), Sender.class);
        Sender senderExpected = addNewDocRepoEntity(senderFromJson, Sender.class).as(Sender.class);
        senderExpected.setTitle("new123");

        Response response = update(senderExpected);
        Sender senderUpdated = response.as(Sender.class);

        assertAll(
                () -> assertNotNull(senderUpdated),
                () -> assertEquals(senderExpected.getId(), senderUpdated.getId()),
                () -> assertEquals(senderExpected.getTitle(), senderUpdated.getTitle())
        );
    }

    @Test
    @DisplayName("Testing update sender. Bad ID.")
    @Order(9)
    public void testUpdateSenderBadID() throws IOException {
        Sender senderFromJson = mapper.readValue(JsonSenders.JSON_GOOD.toString(), Sender.class);
        Sender senderExpected = addNewDocRepoEntity(senderFromJson, Sender.class).as(Sender.class);
        senderExpected.setId(0);

        Response response = update(senderExpected);
        checkStatusCodeAndJSON(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Testing update sender. Invalid Document.")
    @Order(10)
    public void testUpdateDocumentNotValidDocument() throws IOException {
        Sender senderFromJson = mapper.readValue(JsonSenders.JSON_GOOD.toString(), Sender.class);
        Sender senderExpected = addNewDocRepoEntity(senderFromJson, Sender.class).as(Sender.class);
        senderExpected.setTitle(" ");

        Response response = update(senderExpected);
        checkStatusCodeAndJSON(response, HttpStatus.BAD_REQUEST.value());
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
