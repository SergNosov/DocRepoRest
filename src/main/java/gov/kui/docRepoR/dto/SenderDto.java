package gov.kui.docRepoR.dto;

import gov.kui.docRepoR.service.SenderService;
import gov.kui.docRepoR.validation.UniqueValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SenderDto {
    private int id;

    @UniqueValue(message = "Значение должно быть уникальным", service = SenderService.class, fieldName = "title")
    @NotBlank(message = "Необходимо указать наименование отправителя")
    private String title;
}
