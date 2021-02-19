package gov.kui.docRepoR.domain.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

@JsonTest
public class BaseJsonTest {

    @Autowired
    protected ObjectMapper objectMapper;
}
