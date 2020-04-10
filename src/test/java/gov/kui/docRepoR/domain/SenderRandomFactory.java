package gov.kui.docRepoR.domain;

import gov.kui.docRepoR.dto.SenderDto;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

public class SenderRandomFactory {
    private SenderRandomFactory() {
    }

    public static Sender getRandomSender(){
        Sender sender = new Sender();
        sender.setId(new Random().nextInt(100));
        sender.setTitle(new StringBuilder("Sender ").append(RandomStringUtils.randomAlphabetic(12)).toString());

        return sender;
    }

    public static SenderDto getRandomSenderDto(){
        return SenderDto.builder()
                .id(new Random().nextInt(100))
                .title(new StringBuilder("SenderDto ").append(RandomStringUtils.randomAlphabetic(12)).toString())
                .build();
    }

    public static SenderDto getDtoFromSender(Sender sender){
        if ( sender == null ) {
            return null;
        }

        SenderDto senderDto = new SenderDto();

        senderDto.setId( sender.getId() );
        senderDto.setTitle( sender.getTitle() );

        return senderDto;
    }
}
