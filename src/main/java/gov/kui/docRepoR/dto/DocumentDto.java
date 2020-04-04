package gov.kui.docRepoR.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class DocumentDto {
    private int id;
    private String number;
    private LocalDate docDate;
    private String title;
    private String content;
    private DoctypeDto doctype;
    private List<SenderDto> senders = new ArrayList<>();
    private List<FileEntityDto> fileEntities = new ArrayList<>();
}
