package gov.kui.docRepoR.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "files")
public class FileEntity extends BaseEntity {

    @Column(name = "filename")
    @NotBlank(message = "Не указано имя файла")
    private String filename;

    @Column(name = "size")
    @Digits(integer = 50, fraction = 0)
    private long fileSize;

    @Column(name = "document_id")
    @Digits(integer = 50, fraction = 0)
    private int documentId;

    @JsonIgnore
    @Column(name = "file")
    private byte[] data;

    public FileEntity() {
    }

    public FileEntity(@NotBlank(message = "Не указано имя файла") String filename,
                      @Digits(integer = 50, fraction = 0) long fileSize,
                      @Digits(integer = 50, fraction = 0) int documentId) {
        this.filename = filename;
        this.fileSize = fileSize;
        this.documentId = documentId;
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

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "FileEntity{" +
                "id=" + this.getId() +
                ", filename='" + filename + '\'' +
                ", fileSize=" + fileSize +
                ", document_id=" + documentId +
                '}';
    }
}
