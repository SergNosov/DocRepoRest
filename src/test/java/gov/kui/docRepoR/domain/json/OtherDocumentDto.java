package gov.kui.docRepoR.domain.json;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class OtherDocumentDto {

    private int id;
    private String number;

    @NotNull(message = "Укажите дату документа")
    private LocalDate docDate;

    @NotBlank(message = "Необходимо указать заголовок документа")
    private String title;
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal startPrice;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonSerialize(using = DocRepoLDTSerializer.class)
    private LocalDateTime lastUpdatedDate;
}
