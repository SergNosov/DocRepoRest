package gov.kui.docRepoR.domain;

import gov.kui.docRepoR.service.SenderService;
import gov.kui.docRepoR.validation.UniqueValue;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Entity
@Table(name = "sender")
public class Sender extends BaseEntity {

    @Column(name = "title")
    @UniqueValue(message = "Значение должно быть уникальным", service = SenderService.class, fieldName = "title")
    @NotBlank(message = "Необходимо указать наименование отправителя")
    private String title;

    public Sender() {
    }

    public Sender(String title) {
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
        return "Sender{" +
                "id=" + this.getId() +
                ", title='" + title + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sender sender = (Sender) o;
        return this.getId() == sender.getId();
    }
}
