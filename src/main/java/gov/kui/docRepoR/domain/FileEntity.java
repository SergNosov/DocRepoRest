package gov.kui.docRepoR.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gov.kui.docRepoR.dto.FileEntityDto;
import gov.kui.docRepoR.dto.SenderDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToOne;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import java.io.IOException;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "FileEntityDtoById",
                query = "SELECT  f.id AS id, f.filename AS filename, f.size as filesize FROM files f WHERE f.id = :fileId",
                resultSetMapping = "FileEntityDto"
        ),
        @NamedNativeQuery(
                name = "FileEntityDtosByDocId",
                query = "f.id AS id, f.filename AS filename, f.size as filesize FROM files f WHERE f.document_id = :documentId",
                resultSetMapping = "FileEntityDto"
        )
})
@SqlResultSetMapping(
        name = "FileEntityDto",
        classes = @ConstructorResult(
                targetClass = FileEntityDto.class,
                columns = {
                        @ColumnResult(name = "id"),
                        @ColumnResult(name = "filename"),
                        @ColumnResult(name = "filesize")
                }
        )
)
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
    @ToString.Exclude
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "file_bytes_id")
    private FileByte fileByte;

    public static FileEntity getInstance(final MultipartFile file, final int idDoc) {

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
            fileEntity.setFileByte(new FileByte(file.getBytes()));

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
