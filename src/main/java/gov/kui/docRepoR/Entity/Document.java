package gov.kui.docRepoR.Entity;

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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="document")
public class Document {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="number")
    private String number;

    @Column(name="doc_date")
    private LocalDate docDate;

    @Column(name="content")
    private String content;

    @ManyToOne(cascade= {CascadeType.DETACH, CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name="doctype_id")
    private Doctype doctype;

    @ManyToMany(cascade= {CascadeType.DETACH, CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
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
                ", number='" + number + '\'' +
                ", docDate='"+docDate + '\''+
                ", content='" + content + '\'' +
                ", doctype=" + doctype +
                ", senders=" + senders +
                '}';
    }
}

