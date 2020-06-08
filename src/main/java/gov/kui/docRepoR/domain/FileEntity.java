package gov.kui.docRepoR.domain;

import gov.kui.docRepoR.dto.FileEntityDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "FileEntityDtoById",
                query = "SELECT  f.id AS id, f.filename AS filename, f.type as contenttype, f.size as filesize FROM files f WHERE f.id = :fileId",
                resultSetMapping = "FileEntityDto"
        ),
        @NamedNativeQuery(
                name = "FileEntityDtosByDocId",
                query = "SELECT f.id AS id, f.filename AS filename, f.type as contenttype, f.size as filesize FROM files f WHERE f.document_id = :docId",
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
                        @ColumnResult(name = "contenttype"),
                        @ColumnResult(name = "filesize", type = Long.class)
                }
        )
)
@Entity
@Table(name = "files")
public class FileEntity extends BaseEntity {

    @Column(name = "filename")
    private String filename;

    @Column(name = "type")
    private String contentType;

    @Column(name = "size")
    private long fileSize;

    @Column(name = "document_id")
    private int documentId;

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

        FileEntity fileEntity = new FileEntity();
        fileEntity.setFilename(file.getOriginalFilename());
        fileEntity.setContentType(file.getContentType());
        fileEntity.setFileSize(file.getSize());
        fileEntity.setDocumentId(idDoc);

        return fileEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof FileEntity)) return false;

        FileEntity that = (FileEntity) o;

        if (this.getFileSize() != that.getFileSize()) return false;
        if (this.getDocumentId() != that.getDocumentId()) return false;
        return this.getFilename().equals(that.getFilename());
    }

    @Override
    public int hashCode() {
        int result = filename.hashCode();
        result = 31 * result + (int) (fileSize ^ (fileSize >>> 32));
        result = 31 * result + documentId;
        return result;
    }
}
