package gov.kui.docRepoR.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.IOException;
import java.sql.Blob;

@Getter
@Setter
@ToString(callSuper = true)
@Entity
@Table(name = "files_blob")
public class FileEntityBlob {
    @Id
    private int id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id")
    @MapsId
    private FileEntity fileEntity;

    @Lob
    @JsonIgnore
    @ToString.Exclude
    @Column(name = "file")
    private Blob fileByte;

    protected FileEntityBlob(){
    }

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
            FileEntity fileEntity = new FileEntity();
            fileEntity.setFilename(file.getOriginalFilename());
            fileEntity.setContentType(file.getContentType());
            fileEntity.setFileSize(file.getSize());
            fileEntity.setDocumentId(idDoc);

            FileEntityBlob fileEntityBlob = new FileEntityBlob();
            fileEntityBlob.setFileEntity(fileEntity);
            fileEntityBlob.setFileByte(
                    BlobProxy.generateProxy(file.getBytes())
            );

            return fileEntityBlob;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки файла. file: " + file.getName() + "; " + e.getMessage());
        }
    }
}
