package gov.kui.docRepoR.dto;

import gov.kui.docRepoR.Entity.Doctype;
import gov.kui.docRepoR.Entity.Document;
import gov.kui.docRepoR.Entity.Sender;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class DocumentDto {

    private int id;
    private String number;
    private LocalDate docDate;
    private String content;
    private Doctype doctype;
    private List<SenderDto> senders = new ArrayList<>();

    public DocumentDto getDocumentDto(Document document){
        DocumentDto documentDto = new DocumentDto();
        documentDto.setId(document.getId());
        documentDto.setNumber(document.getNumber());
        documentDto.setDocDate(document.getDocDate());
        documentDto.setContent(document.getContent());
        documentDto.setDoctype(document.getDoctype());
        documentDto.setSenders(getSenderDtoFromDocument(document));

        return documentDto;
    }

    private List<SenderDto> getSenderDtoFromDocument(Document document){
        List<SenderDto> senderDtos = new ArrayList<>();
        for (Sender sender: document.getSenders()){
            SenderDto senderDto = new SenderDto();
            senderDto.setId(sender.getId());
            senderDto.setTitle(sender.getTitle());
            senderDtos.add(senderDto);
        }
        return senderDtos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDate getDocDate() {
        return docDate;
    }

    public void setDocDate(LocalDate docDate) {
        this.docDate = docDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<SenderDto> getSenders() {
        return senders;
    }

    public void setSenders(List<SenderDto> senders) {
        this.senders = senders;
    }

    public Doctype getDoctype() {
        return doctype;
    }

    public void setDoctype(Doctype doctype) {
        this.doctype = doctype;
    }
}
