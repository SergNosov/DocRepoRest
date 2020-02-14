package gov.kui.docRepoR.ControllersTestMockMVC;

import gov.kui.docRepoR.DocRepoURL;
import gov.kui.docRepoR.controller.DoctypeController;
import gov.kui.docRepoR.service.DoctypeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ContextConfiguration
@WebMvcTest
public class DoctypeControllerTestWebMVC {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllDoctypes() throws Exception {

        MvcResult result = mockMvc.perform(post(DocRepoURL.DOCTYPES.toString())
                .with(user("john"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();
    }
}
