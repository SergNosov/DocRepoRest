package gov.kui.docRepoR.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import java.io.IOException;

@Entity
@Table(name = "files")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
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
    @ToString.Exclude
    private byte[] bytes;

    public static FileEntity getInstance(final MultipartFile file, final int idDoc){

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FileEntity)) return false;
        FileEntity that = (FileEntity) o;
        return getId() == that.getId();
    }
}
