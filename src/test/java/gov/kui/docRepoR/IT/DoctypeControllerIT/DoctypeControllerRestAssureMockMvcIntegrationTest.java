package gov.kui.docRepoR.IT.DoctypeControllerIT;

import gov.kui.docRepoR.Entity.Doctype;
import gov.kui.docRepoR.controller.DoctypeRestController;
import gov.kui.docRepoR.controller.RestExceptionHandler;
import gov.kui.docRepoR.service.DoctypeService;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import java.util.ArrayList;
import java.util.List;

//@RunWith(MockitoJUnitRunner.class)
public class DoctypeControllerRestAssureMockMvcIntegrationTest {
    /*
    private final String ROOT = "http://localhost:8080/api/doctypes";
    private List<Doctype> doctypes = new ArrayList<>();

    @Mock
    private DoctypeService doctypeService;

    @InjectMocks
    private DoctypeRestController doctypeRestController;

    @InjectMocks
    private RestExceptionHandler restExceptionHandler;

    @Before
    public void init() {
        RestAssuredMockMvc.standaloneSetup(doctypeRestController, restExceptionHandler);
        this.initDoctypes();
    }

    private void initDoctypes() {
        Doctype d1 = this.createRandomDoctype();
        d1.setId(1);
        doctypes.add(d1);
    }

    @Test()
    public void checkGetAllDatatypes() {
        Mockito.when(doctypeService.findAll()).thenReturn(doctypes);
        MockMvcResponse mockMvcResponse = this.getAllDoctypes();

        this.checkStatusCodeAndJSON(mockMvcResponse,HttpStatus.OK.value());

        List<Doctype> returnedDoctypes = mockMvcResponse.then().extract().jsonPath()
                .getList("", Doctype.class);
        assertNotNull(returnedDoctypes);
        assertFalse(returnedDoctypes.isEmpty());
    }

    private MockMvcResponse getAllDoctypes() {
        return RestAssuredMockMvc.given().contentType("application/json").when().get(ROOT);
    }

    private Doctype createRandomDoctype() {
        String doctypeTitle = RandomStringUtils.randomAlphanumeric(5);
        return new Doctype(doctypeTitle);
    }

    private void checkStatusCodeAndJSON(MockMvcResponse response,int statusCode){
        response.then().log().ifValidationFails()
                .statusCode(statusCode)
                .contentType(ContentType.JSON);
    }

     */
}
