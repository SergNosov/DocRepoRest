package gov.kui.docRepoR.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.Arrays;

@Entity
@Table(name = "files")
public class FileEntity extends BaseEntity {

    @Column(name = "filename")
    @NotBlank(message = "Не указано имя файла")
    private String filename;

    @Column(name = "type")
    @NotBlank(message = "Не указан тип файла")
    private String contentType;

    @Column(name = "size")
    @Digits(integer = 50, fraction = 0)
    private long fileSize;

    @Column(name = "document_id")
    @Digits(integer = 50, fraction = 0)
    private int documentId;

    @JsonIgnore //todo нужен переход на DTO
    @Lob
    @Column(name = "file")
    private byte[] bytes;

    public FileEntity(){
    }

    public FileEntity(@NotBlank(message = "Не указано имя файла") String filename,
                      @NotBlank(message = "Не указан тип файла") String contentType,
                      @Digits(integer = 50, fraction = 0) long fileSize,
                      @Digits(integer = 50, fraction = 0) int documentId) {

        if (filename != null) {
            this.filename = filename;
        }
        if (contentType != null) {
            this.contentType = contentType;
        }
        this.fileSize = fileSize;
        this.documentId = documentId;
    }

    public static FileEntity getInstance(MultipartFile file, final int idDoc){

        if (file == null) {
            throw new IllegalArgumentException("Ошибка загрузки файла. File is null.");
        }

        if (file.isEmpty()) {
            throw new IllegalArgumentException("Ошибка загрузки файла. File is empty.");
        }

        if (idDoc == 0) {
            throw new IllegalArgumentException("Ошибка загрузки файла. Document.Id не может быть равен 0.");
        }

        try {
            FileEntity fileEntity = new FileEntity();
            fileEntity.setFilename(file.getOriginalFilename());
            fileEntity.setContentType(file.getContentType());
            fileEntity.setFileSize(file.getSize());
            fileEntity.setDocumentId(idDoc);
            fileEntity.setBytes(file.getBytes());

            return fileEntity;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки файла. file: " + file.getName() + "; " + e.getMessage());
        }
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        if (filename != null)
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

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String toString() {
        return "FileEntity{" +
                "id=" + this.getId() +
                ", filename='" + filename + '\'' +
                ", contentType=" + contentType +
                ", fileSize=" + fileSize +
                ", document_id=" + documentId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FileEntity)) return false;
        FileEntity that = (FileEntity) o;
        return getId() == that.getId() &&
                getFileSize() == that.getFileSize() &&
                getDocumentId() == that.getDocumentId() &&
                getFilename().equals(that.getFilename()) &&
                getContentType().equals(that.getContentType()) &&
                Arrays.equals(getBytes(), that.getBytes());
    }
}
