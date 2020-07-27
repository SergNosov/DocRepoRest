package gov.kui.docRepoR.dto;

import gov.kui.docRepoR.service.SenderService;
import gov.kui.docRepoR.validation.UniqueValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SenderDto implements Serializable {
    private int id;

    @UniqueValue(message = "Значение должно быть уникальным", service = SenderService.class, fieldName = "title")
    @NotBlank(message = "Необходимо указать наименование отправителя")
    private String title;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof SenderDto)) return false;
        SenderDto that = (SenderDto) o;
        return this.getTitle().equals(that.getTitle());
    }

    @Override
    public int hashCode() {
        return getTitle().hashCode();
    }
}
