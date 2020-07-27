package gov.kui.docRepoR.dto;

import gov.kui.docRepoR.service.DoctypeService;
import gov.kui.docRepoR.validation.UniqueValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctypeDto implements Serializable {
    private int id;

    @UniqueValue(message = "Значение должно быть уникальным", service = DoctypeService.class, fieldName = "title")
    @NotBlank(message = "Необходимо указать тип документа")
    private String title;

    public void setTitle(String title) {
        if (title != null)
            this.title = title.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof DoctypeDto)) return false;
        DoctypeDto that = (DoctypeDto) o;
        return this.getTitle().equals(that.getTitle());
    }

    @Override
    public int hashCode() {
        return getTitle().hashCode();
    }
}
