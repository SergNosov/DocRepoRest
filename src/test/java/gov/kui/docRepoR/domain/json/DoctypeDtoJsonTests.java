package gov.kui.docRepoR.domain.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.kui.docRepoR.JsonDoctype;
import gov.kui.docRepoR.JsonDocument;
import gov.kui.docRepoR.dto.DoctypeDto;
import gov.kui.docRepoR.dto.DocumentDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import java.io.IOException;

@Slf4j
@JsonTest
public class DoctypeDtoJsonTests {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSerializedToJson() throws JsonProcessingException {
        DoctypeDto doctypeDto = DoctypeDto.builder().id(1).title("Title of DoctypeDto").build();

        String stringJson = objectMapper.writeValueAsString(doctypeDto);
        log.info("--- stringJson: "+stringJson );
    }

    @Test
    void testDeserializationFromJson() throws IOException {
        DoctypeDto doctypeDto = objectMapper.readValue(JsonDoctype.JSON_GOOD.toString(),DoctypeDto.class);
        log.info("--- doctypeDto: "+doctypeDto );
    }
}
