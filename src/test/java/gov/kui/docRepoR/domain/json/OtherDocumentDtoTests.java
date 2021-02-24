package gov.kui.docRepoR.domain.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import gov.kui.docRepoR.JsonDocument;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@ActiveProfiles("kebab")
public class OtherDocumentDtoTests extends BaseJsonTest {

    @Test
    void testDeserializationFromJson() throws IOException {
        OtherDocumentDto otherDocumentDto = objectMapper.readValue(
                JsonDocument.JSON_GOOD_2_SENDERS.toString(),OtherDocumentDto.class
        );

        System.out.println("--- otherDocumentDto:"+otherDocumentDto);
    }

    @Test
    void testSerializedToJson() throws JsonProcessingException {

        String stringJson = objectMapper.writeValueAsString(otherDocumentDto);

        System.out.println("---stringJson from otherDocumentDto: "+ stringJson);
    }
}
