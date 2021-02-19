package gov.kui.docRepoR.domain.json;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

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
    private BigDecimal price;
}
