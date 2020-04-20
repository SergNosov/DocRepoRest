package gov.kui.docRepoR.domain;

import gov.kui.docRepoR.dto.DoctypeDto;
import gov.kui.docRepoR.service.DoctypeService;
import gov.kui.docRepoR.validation.UniqueValue;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "DoctypeDtoById",
                query = "SELECT  d.id AS id, d.title AS title FROM doctype d WHERE d.id = :doctypeId",
                resultSetMapping = "DoctypeDto"
        ),
        @NamedNativeQuery(
                name = "DoctypeDtoAll",
                query = "SELECT  d.id AS id, d.title AS title FROM doctype d",
                resultSetMapping = "DoctypeDto"
        )
})
@SqlResultSetMapping(
        name = "DoctypeDto",
        classes = @ConstructorResult(
                targetClass = DoctypeDto.class,
                columns = {
                        @ColumnResult(name = "id"),
                        @ColumnResult(name = "title")
                }
        )
)
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
