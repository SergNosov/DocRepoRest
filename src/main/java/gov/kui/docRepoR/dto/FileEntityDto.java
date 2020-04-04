package gov.kui.docRepoR.dto;

import lombok.Data;

@Data
public class FileEntityDto {
    private int id;
    private String filename;
    private long fileSize;
}
