package gov.kui.docRepoR.domain;

import gov.kui.docRepoR.dto.DoctypeDto;
import gov.kui.docRepoR.service.DoctypeService;
import gov.kui.docRepoR.validation.UniqueValue;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor
@NamedQueries(
        @NamedQuery(
                name="DoctypeDtoByDocId",
                query = "select new gov.kui.docRepoR.dto.DoctypeDto(d.id, d.title)" +
                        " from Document doc join doc.doctype d where doc.id = :docId"
        )
)
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
        if (o == null || !(o instanceof Doctype)) return false;
        Doctype that = (Doctype) o;
        return this.getTitle().equals(that.getTitle());
    }

    @Override
    public int hashCode() {
        return getTitle().hashCode();
    }
}
