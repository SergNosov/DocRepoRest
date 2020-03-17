package gov.kui.docRepoR.domain;

import gov.kui.docRepoR.service.DoctypeService;
import gov.kui.docRepoR.validation.UniqueValue;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Table(name = "doctype")
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class Doctype extends BaseEntity {

    @Column(name = "title")
    @UniqueValue(message = "Значение должно быть уникальным", service = DoctypeService.class, fieldName = "title")
    @NotBlank(message = "Необходимо указать тип документа")
    private String title;

    public Doctype() {
    }

    public Doctype(String title) {
            this.title = title;
    }

    @Builder
    public Doctype(int id,String title){
        super(id);
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Doctype doctype = (Doctype) o;
        return this.getId() == doctype.getId();
    }
}
