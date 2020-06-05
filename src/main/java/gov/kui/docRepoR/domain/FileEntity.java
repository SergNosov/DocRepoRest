package gov.kui.docRepoR.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gov.kui.docRepoR.dto.FileEntityDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import java.io.IOException;
import java.sql.Blob;

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
        int result = this.getFilename().hashCode();
        result = 31 * result + (int) (this.getFileSize() ^ (this.getFileSize() >>> 32));
        result = 31 * result + this.getDocumentId();
        return result;
    }
}
