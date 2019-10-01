package gov.kui.docRepoR.dto;

import gov.kui.docRepoR.Entity.Document;
import gov.kui.docRepoR.Entity.Sender;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class SenderDto {
    private int id;
    private String title;
  //  private List<DocumentDto> documents = new ArrayList<>();

    public SenderDto getSenderDto(Sender sender){
        SenderDto senderDto = new SenderDto();
        senderDto.setId(sender.getId());
        senderDto.setTitle(sender.getTitle());
    //    senderDto.setDocuments(getDocumentDtofromSender(sender));
        return senderDto;
    }
/*
    private List<DocumentDto> getDocumentDtofromSender(Sender sender){
        List<DocumentDto> documentDtos = new ArrayList<>();
        for ( Document doc : sender.getDocuments()){
            DocumentDto documentDto = new DocumentDto();
            documentDto.setId(doc.getId());
            documentDto.setNumber(doc.getNumber());
            documentDto.setDocDate(doc.getDocDate());
            documentDto.setContent(doc.getContent());
            documentDto.setDoctype(doc.getDoctype());
            documentDto.setSenders(Arrays.asList());
            documentDtos.add(documentDto);
        }
        return documentDtos;
    }
*/
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
