package gov.kui.docRepoR.domain.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonTest
public class BaseJsonTest {

    protected  OtherDocumentDto otherDocumentDto = OtherDocumentDto.builder()
            .id(123)
            .title("new Document Title")
            .docDate(LocalDate.now())
            .number("123-irr")
            .startPrice(new BigDecimal("1.23"))
            .lastUpdatedDate(LocalDateTime.now())
            .build();

    @Autowired
    protected ObjectMapper objectMapper;
}
