package gov.kui.docRepoR.dto;

import gov.kui.docRepoR.Entity.Doctype;
import org.springframework.stereotype.Component;

@Component
public class DoctypeDto {
    private int id;
    private String type;

    public DoctypeDto getDoctypeDto(Doctype doctype){
        DoctypeDto doctypeDto = new DoctypeDto();
        doctypeDto.setId(doctype.getId());
        doctypeDto.setType(doctype.getTitle());
        return  doctypeDto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
