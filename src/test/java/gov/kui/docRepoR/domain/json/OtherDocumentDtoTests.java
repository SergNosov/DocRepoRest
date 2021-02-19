package gov.kui.docRepoR.domain.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import gov.kui.docRepoR.JsonDocument;
import gov.kui.docRepoR.dto.DocumentDto;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

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
        OtherDocumentDto otherDocumentDto = OtherDocumentDto.builder()
                .id(123)
                .title("new Document Title")
                .docDate(LocalDate.now())
                .number("123-irr")
                .price(new BigDecimal("1.23"))
                .build();

        String stringJson = objectMapper.writeValueAsString(otherDocumentDto);
        System.out.println("---stringJson from otherDocumentDto: "+ stringJson);
    }
}
