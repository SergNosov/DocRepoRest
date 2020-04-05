package gov.kui.docRepoR.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileEntityDto {
    private int id;
    private String filename;
    private long fileSize;
}
