package gov.kui.docRepoR.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SenderDto {
    private int id;
    private String title;
}
