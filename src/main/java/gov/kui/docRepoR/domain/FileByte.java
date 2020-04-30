package gov.kui.docRepoR.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "file_bytes")
public class FileByte extends BaseEntity {

    @Lob
    @ToString.Exclude
    @Column(name = "bytes")
    private byte[] bytes;

    public FileByte(byte[] bytes) {
        this.bytes = bytes;
    }
}
