package gov.kui.docRepoR.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "document")
public class Document extends BaseEntity {

    @Column(name = "number")
    private String number;

    @Column(name = "doc_date")
    @NotNull(message = "Укажите дату документа")
    private LocalDate docDate;

    @Column(name = "title")
    @NotBlank(message = "Необходимо указать заголовок документа")
    private String title;

    @Column(name = "content")
    private String content; //todo когда приходит null из mysql тесты не проходят

    @ManyToOne(optional = false)
    @JoinColumn(name = "doctype_id")
    @NotNull(message = "Укажите тип документа")
    private Doctype doctype;

    @ManyToMany
    @JoinTable(
            name = "document_sender",
            joinColumns = @JoinColumn(name = "document_id"),
            inverseJoinColumns = @JoinColumn(name = "sender_id"))
    private Set<Sender> senders = new HashSet<>();

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "document_id")
    private Set<FileEntity> fileEntities = new HashSet<>();

    public void setNumber(String number) {
        if (number != null)
            this.number = number.trim();
    }

    public void setContent(String content) {
        if (content != null)
            this.content = content.trim();
    }

    public void setTitle(String title) {
        if (title != null) {
            this.title = title.trim();
        }
    }

    public void addSender(Sender sender) {
        if (sender != null) {
            this.senders.add(sender);
        }
    }

    public void removeSender(Sender sender) {
        if (sender != null) {
            senders.remove(sender);
        }
    }
}
