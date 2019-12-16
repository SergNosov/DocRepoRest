package gov.kui.docRepoR.IT.SpringBootTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gov.kui.docRepoR.DocRepoURL;
import gov.kui.docRepoR.model.CommonMessage;
import gov.kui.docRepoR.model.Doctype;
import gov.kui.docRepoR.IT.BaseSBTests;
import gov.kui.docRepoR.IT.JsonDoctypes;
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
public class DoctypeControllerSpringBootTest extends BaseSBTests<Doctype> {

    @Autowired
    public DoctypeControllerSpringBootTest(TestRestTemplate testRestTemplate) {
        super(testRestTemplate, Doctype.class);
    }

    @BeforeEach
    public void setUp() {
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        entityUrl = DocRepoURL.DOCTYPES.toString();
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
    @EnumSource(JsonDoctypes.class)
    @DisplayName("1. Add Doctype. Check HttpStatus of response")
    @Order(1)
    public void testAddDoctypeWithDifferentJsonValue(JsonDoctypes jsonDoctypesEnum) throws IOException {
        int httpStatus = setHttpStatus(jsonDoctypesEnum);
        testAddEntityWithDifferentJsonValue(jsonDoctypesEnum.toString(), httpStatus);
    }

    @ParameterizedTest(name = "{index} json = {0}")
    @EnumSource(value = JsonDoctypes.class, names = {"JSON_GOOD"})
    @DisplayName("2. Add Doctype. Check doctype from response")
    @Order(2)
    public void testAddDoctypeOK(JsonDoctypes jsonDoctypesEnum) throws IOException {
        Doctype doctypeFromJson = mapper.readValue(jsonDoctypesEnum.toString(), Doctype.class);
        ResponseEntity<Doctype> response = addNewEntity(doctypeFromJson);
        Doctype doctypeFromResponse = response.getBody();

        System.out.println("Doctype from response:" + "\n" + doctypeFromResponse);

        assertAll(
                () -> assertNotNull(doctypeFromResponse),
                () -> assertEquals(doctypeFromJson.getTitle(), doctypeFromResponse.getTitle())
        );
    }

    @Test
    @DisplayName("3. Testing the receipt of all doctypes")
    @Order(3)
    public void testGetAllDoctypes() {
        getAll();
    }

    @Test
    @DisplayName("4. Testing the receipt of doctype by id. OK.")
    @Order(4)
    public void testGetSenderById() throws IOException {
        Doctype doctypeFromJson = mapper.readValue(JsonDoctypes.JSON_GOOD.toString(), Doctype.class);
        ResponseEntity<Doctype> response = addNewEntity(doctypeFromJson);
        Doctype doctypeExpected = response.getBody();

        ResponseEntity<Doctype> responseEntity = getById(doctypeExpected.getId());
        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCode().value());

        Doctype doctypeActual = responseEntity.getBody();
        assertAll(
                () -> assertNotNull(doctypeActual),
                () -> assertEquals(doctypeExpected.getTitle(), doctypeActual.getTitle())
        );
    }

    @Test
    @DisplayName("5. Testing the receipt of doctype by id. BAD.")
    @Order(5)
    public void testGetDoctypeByIdBAD() {
        testGetEntityByIdBad();
    }

    @Test
    @DisplayName("6. Testing delete doctype by id. OK.")
    @Order(6)
    public void testDeleteSenderByIdOK() throws IOException {
        Doctype doctypeFromJson = mapper.readValue(JsonDoctypes.JSON_GOOD.toString(), Doctype.class);
        Doctype doctypeExpected = addNewEntity(doctypeFromJson).getBody();

        ResponseEntity<CommonMessage> response = deleteById(doctypeExpected.getId());
        System.out.println("response: " + response.getBody().getMessage());

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatusCode().value()),
                () -> assertEquals("Удален тип документа id - " + doctypeExpected.getId(),
                        response.getBody().getMessage())
        );
    }

    @Test
    @DisplayName("7. Testing delete doctype by id. BAD.")
    @Order(7)
    public void testDeleteDoctypeByIdBAD() {
        testDeleteEntityByIdBad();
    }

    @Test
    @DisplayName("8. Testing update doctype. OK.")
    @Order(8)
    public void testUpdateSenderOK() throws IOException {
        Doctype doctypeFromJson = mapper.readValue(JsonDoctypes.JSON_GOOD.toString(), Doctype.class);
        Doctype doctypeExpected = addNewEntity(doctypeFromJson).getBody();
        doctypeExpected.setTitle("new123");

        ResponseEntity<Doctype> response = update(doctypeExpected);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        Doctype doctypeUpdated = response.getBody();
        assertAll(
                () -> assertNotNull(doctypeUpdated),
                () -> assertEquals(doctypeExpected.getId(), doctypeUpdated.getId()),
                () -> assertEquals(doctypeExpected.getTitle(), doctypeUpdated.getTitle())
        );
    }

    @Test
    @DisplayName("9. Testing update doctype. Bad ID.")
    @Order(9)
    public void testUpdateDoctypeBadID() throws IOException {
        testUpdateEntityBadId(JsonDoctypes.JSON_GOOD.toString());
    }

    @Test
    @DisplayName("10. Testing update doctype. Invalid Doctype.")
    @Order(10)
    public void testUpdateSenderNotValidSender() throws IOException {
        Doctype doctypeFromJson = mapper.readValue(JsonDoctypes.JSON_GOOD.toString(), Doctype.class);
        Doctype doctypeExpected = addNewEntity(doctypeFromJson).getBody();
        doctypeExpected.setTitle(" ");

        ResponseEntity<Doctype> response = update(doctypeExpected);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
    }

    private int setHttpStatus(JsonDoctypes jsonDoctypeEnum) {
        switch (jsonDoctypeEnum) {
            case JSON_GOOD: {
                return HttpStatus.OK.value();
            }
            default: {
                return HttpStatus.BAD_REQUEST.value();
            }
        }
    }
}
