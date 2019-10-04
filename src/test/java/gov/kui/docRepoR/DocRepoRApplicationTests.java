package gov.kui.docRepoR;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.kui.docRepoR.dao.DoctypeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;



@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DocRepoRApplicationTests {
	private static final String BASE_URL_DOCTYPE = "http://localhost:8080/api/doctypes";

	@Autowired
	private MockMvc mvc;

	@MockBean
	private DoctypeRepository mockDoctypeRepository;

	private ObjectMapper mapper = new ObjectMapper();

	@Test
	public void contextLoads() {
		assertThat(mvc).isNotNull();
	}

	private ResultActions invokeAllDatatypes() throws Exception {
		return mvc.perform(get(BASE_URL_DOCTYPE).accept(MediaType.APPLICATION_JSON));
	}

	@Test
	public void getAllDoctypes() throws Exception{

		MvcResult result = mvc.perform(get(BASE_URL_DOCTYPE))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andReturn();

		MockHttpServletResponse response =  result.getResponse();
	}

	@Test
	public void getDoctypeById_OK(){

	}
}
