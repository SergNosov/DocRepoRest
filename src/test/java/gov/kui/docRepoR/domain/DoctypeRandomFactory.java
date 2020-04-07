package gov.kui.docRepoR.domain;

import gov.kui.docRepoR.dto.DoctypeDto;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DoctypeRandomFactory {
    private DoctypeRandomFactory() {
    }

    public static Doctype getRandomDoctype() {
        Doctype doctype = new Doctype();
        doctype.setId(new Random().nextInt(100));
        doctype.setTitle(RandomStringUtils.randomAlphabetic(12));

        return doctype;
    }

    public static DoctypeDto getRandomDoctypeDto(){
        return DoctypeDto.builder()
                .id(new Random().nextInt(100))
                .title(RandomStringUtils.randomAlphabetic(12)).build();
    }

    public static DoctypeDto getDtoFromDoctype(Doctype doctype){
        if (doctype == null) {
            return null;
        }

        return DoctypeDto.builder().id(doctype.getId())
                .title(doctype.getTitle())
                .build();
    }

    public static List<DoctypeDto> getDtosFromDoctypes(List<Doctype> doctypes){
        if ( doctypes == null ) {
            return null;
        }

        List<DoctypeDto> list = new ArrayList<>( doctypes.size() );
        for ( Doctype doctype : doctypes ) {
            list.add( getDtoFromDoctype( doctype ) );
        }

        return list;
    }
}
