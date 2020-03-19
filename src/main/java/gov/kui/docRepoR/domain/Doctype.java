package gov.kui.docRepoR.domain;

import gov.kui.docRepoR.service.DoctypeService;
import gov.kui.docRepoR.validation.UniqueValue;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "doctype")
public class Doctype extends BaseEntity {

    @Column(name = "title")
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
        if (o == null || getClass() != o.getClass()) return false;
        Doctype doctype = (Doctype) o;
        return this.getId() == doctype.getId();
    }
}
