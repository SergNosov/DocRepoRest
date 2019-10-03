package gov.kui.docRepoR.Entity;

import gov.kui.docRepoR.service.DoctypeService;
import gov.kui.docRepoR.validation.UniqueValue;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="doctype")
public class Doctype {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="title")
    @UniqueValue(message = "Значение должно быть уникальным", service = DoctypeService.class, fieldName = "title")
    private String title;

    public Doctype(){
    }

    public Doctype(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Doctype{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
