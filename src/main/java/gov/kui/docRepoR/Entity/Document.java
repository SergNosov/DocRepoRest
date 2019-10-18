package gov.kui.docRepoR.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="document")
public class Document implements DocRepoEntity{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="number")
    private String number;

    @Column(name="doc_date")
    @NotNull(message = "Укажите дату документа")
    private LocalDate docDate;

    @Column(name="title")
    @NotBlank(message = "Необходимо указать заголовок документа")
    private String title;

    @Column(name="content")
    private String content;

    @ManyToOne(optional=false)
    @JoinColumn(name="doctype_id")
    @NotNull(message = "Укажите тип документа")
    private Doctype doctype;

    @ManyToMany
    @JoinTable(
            name = "document_sender",
            joinColumns = @JoinColumn(name = "document_id"),
            inverseJoinColumns = @JoinColumn(name = "sender_id"))
    private List<Sender> senders = new ArrayList<>();

    public Document() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content.trim();
    }

    public Doctype getDoctype() {
        return doctype;
    }

    public void setDoctype(Doctype doctype) {
        this.doctype = doctype;
    }

    public LocalDate getDocDate() {
        return docDate;
    }

    public void setDocDate(LocalDate docDate) {
        this.docDate = docDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title.trim();
    }

    public List<Sender> getSenders() {
        return senders;
    }

    public void setSenders(List<Sender> senders) {
        this.senders = senders;
    }

    public void addSender(Sender sender){
        if (sender != null) {
            this.senders.add(sender);
        }
    }

    public void removeSender(Sender sender){
        if (sender != null) {
            senders.remove(sender);
        }
    }

    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ",\n number='" + number + '\'' +
                ",\n docDate='"+docDate + '\''+
                ",\n title='"+title + '\''+
                ",\n content='" + content + '\'' +
                ",\n doctype=" + doctype +
                ",\n senders=" + senders +
                '}';
    }
}

