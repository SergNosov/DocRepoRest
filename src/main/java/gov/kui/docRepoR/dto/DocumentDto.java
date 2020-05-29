package gov.kui.docRepoR.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class DocumentDto {
    private int id;
    private String number;

    @NotNull(message = "Укажите дату документа")
    private LocalDate docDate;

    @NotBlank(message = "Необходимо указать заголовок документа")
    private String title;
    private String content;

    @NotNull(message = "Укажите тип документа")
    private DoctypeDto doctype;

    @NotEmpty(message = "Укажите стророну(ы) подписания документа")
    private Set<SenderDto> senders = new HashSet<>();
    private Set<FileEntityDto> fileEntities = new HashSet<>();

    public DocumentDto(int id, String number, LocalDate docDate, String title, String content) {
        this.id = id;
        this.number = number;
        this.docDate = docDate;
        this.title = title;
        this.content = content;
    }
}
