package gov.kui.docRepoR.domain.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gov.kui.docRepoR.JsonDocument;
import gov.kui.docRepoR.domain.DocumentRandomFactory;
import gov.kui.docRepoR.dto.DocumentDto;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

@ActiveProfiles("kebab")
public class DocumentDtoJsonTests extends BaseJsonTest {

    @Test
    void testDeserializationFromJson() throws IOException {
        //PropertyNamingStrategy
        ObjectMapper objectMapper1 = new ObjectMapper().registerModule(new JavaTimeModule());

        DocumentDto documentDto =  objectMapper1.readValue(JsonDocument.JSON_GOOD_2_SENDERS.toString(),DocumentDto.class);
        System.out.println("---documentDto: "+documentDto);
    }

    @Test
    void testSerializedToJson() throws JsonProcessingException {
        DocumentDto documentDto = DocumentRandomFactory.getDtoFromDocument(
                DocumentRandomFactory.getRandomDocument()
        );

        System.out.println("--- documentDto Json with kebab strategy: "+ objectMapper.writeValueAsString(documentDto));
    }
}
