package gov.kui.docRepoR.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.io.IOException;
import java.sql.Blob;


@Entity
@Table(name = "files")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class FileEntityBlob extends FileEntity {

    @Lob
    @JsonIgnore
    @ToString.Exclude
    @Column(name = "file")
    private Blob fileByte;

    public static FileEntityBlob getInstance(final MultipartFile file, final int idDoc) {

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
            FileEntityBlob fileEntityBlob = new FileEntityBlob();
            fileEntityBlob.setFilename(file.getOriginalFilename());
            fileEntityBlob.setContentType(file.getContentType());
            fileEntityBlob.setFileSize(file.getSize());
            fileEntityBlob.setDocumentId(idDoc);
            fileEntityBlob.setFileByte(
                    BlobProxy.generateProxy(file.getBytes())
            );

            return fileEntityBlob;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки файла. file: " + file.getName() + "; " + e.getMessage());
        }
    }
}
