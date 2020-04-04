package gov.kui.docRepoR.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DoctypeDto {
    private int id;
    private String title;
}
