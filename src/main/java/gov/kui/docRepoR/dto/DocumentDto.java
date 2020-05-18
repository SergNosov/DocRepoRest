package gov.kui.docRepoR.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class DocumentDto {
    private int id;
    private String number;
    private LocalDate docDate;
    private String title;
    private String content;
    private DoctypeDto doctype;
    private Set<SenderDto> senders = new HashSet<>();
    private List<FileEntityDto> fileEntities = new ArrayList<>();

    public DocumentDto(int id, String number, LocalDate docDate, String title, String content) {
        this.id = id;
        this.number = number;
        this.docDate = docDate;
        this.title = title;
        this.content = content;
    }
}
