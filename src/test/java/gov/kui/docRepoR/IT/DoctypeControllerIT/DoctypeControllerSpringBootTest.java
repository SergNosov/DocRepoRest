package gov.kui.docRepoR.IT.DoctypeControllerIT;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gov.kui.docRepoR.DocRepoURL;
import gov.kui.docRepoR.Entity.Doctype;
import gov.kui.docRepoR.IT.BaseSBTests;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.HashSet;

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
    public void testAddSenderWithDifferentJsonSenderValue(JsonDoctypes jsonDoctypesEnum) throws IOException {
        int httpStatus = setHttpStatus(jsonDoctypesEnum);
        testAddEntityWithDifferentJsonValue(jsonDoctypesEnum.toString(), httpStatus);
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
