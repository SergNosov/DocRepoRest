package gov.kui.docRepoR.domain;

import gov.kui.docRepoR.service.DoctypeService;
import gov.kui.docRepoR.validation.UniqueValue;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Entity
@Table(name = "doctype")
public class Doctype extends BaseEntity {

    @Column(name = "title")
    @UniqueValue(message = "Значение должно быть уникальным", service = DoctypeService.class, fieldName = "title")
    @NotBlank(message = "Необходимо указать тип документа")
    private String title;

    public Doctype() {
    }

    public Doctype(String title) {
        if (title != null)
            this.title = title.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title != null)
            this.title = title.trim();
    }

    @Override
    public String toString() {
        return "Doctype{" +
                "id=" + this.getId() +
                ", title='" + title + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Doctype doctype = (Doctype) o;
        return this.getId() == doctype.getId();
    }
}
