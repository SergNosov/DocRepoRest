package gov.kui.docRepoR.domain;

import gov.kui.docRepoR.service.SenderService;
import gov.kui.docRepoR.validation.UniqueValue;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "sender")
public class Sender extends BaseEntity {

    @Column(name = "title")
    @UniqueValue(message = "Значение должно быть уникальным", service = SenderService.class, fieldName = "title")
    @NotBlank(message = "Необходимо указать наименование отправителя")
    private String title;

    public void setTitle(String title) {
        if (title != null)
            this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sender sender = (Sender) o;
        return this.getId() == sender.getId();
    }
}
