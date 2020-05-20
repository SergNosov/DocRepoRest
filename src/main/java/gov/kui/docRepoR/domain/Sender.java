package gov.kui.docRepoR.domain;

import gov.kui.docRepoR.dto.SenderDto;
import gov.kui.docRepoR.service.SenderService;
import gov.kui.docRepoR.validation.UniqueValue;
import lombok.Getter;
import lombok.ToString;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@ToString(callSuper = true)
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "SenderDtoById",
                query = "SELECT  s.id AS id, s.title AS title FROM sender s WHERE s.id = :senderId",
                resultSetMapping = "SenderDto"
        ),
        @NamedNativeQuery(
                name = "SenderDtoAll",
                query = "SELECT  s.id AS id, s.title AS title FROM sender s",
                resultSetMapping = "SenderDto"
        )
})
@SqlResultSetMapping(
        name = "SenderDto",
        classes = @ConstructorResult(
                targetClass = SenderDto.class,
                columns = {
                        @ColumnResult(name = "id"),
                        @ColumnResult(name = "title")
                }
        )
)
@Entity
@Table(name = "sender")
public class Sender extends BaseEntity {

    @Column(name = "title")
    @UniqueValue(message = "Значение должно быть уникальным", service = SenderService.class, fieldName = "title")
    @NotBlank(message = "Необходимо указать наименование отправителя")
    private String title;

    public void setTitle(String title) {
        if (title != null)
            this.title = title.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Sender)) return false;
        Sender that = (Sender) o;
        return this.getTitle().equals(that.getTitle());
    }

    @Override
    public int hashCode() {
        return getTitle().hashCode();
    }
}
