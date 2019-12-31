package gov.kui.docRepoR.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name="files")
public class FileEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="filename")
    @NotBlank(message = "Не указано имя файла")
    private String filename;

    @Column(name="size")
    @Digits(integer = 50, fraction =0 )
    private long fileSize;

    @Column(name="document_id")
    @Digits(integer = 50, fraction =0 )
    private int documentId;

    public FileEntity() {
    }

    public FileEntity(@NotBlank(message = "Не указано имя файла") String filename,
                      @Digits(integer = 50, fraction = 0) long fileSize,
                      @Digits(integer = 50, fraction = 0) int documentId) {
        this.filename = filename;
        this.fileSize = fileSize;
        this.documentId = documentId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename.trim();
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int document_id) {
        this.documentId = document_id;
    }

    @Override
    public String toString() {
        return "FileEntity{" +
                "id=" + id +
                ", filename='" + filename + '\'' +
                ", fileSize=" + fileSize +
                ", document_id=" + documentId +
                '}';
    }
}
