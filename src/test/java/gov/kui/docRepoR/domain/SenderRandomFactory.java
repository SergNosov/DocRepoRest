package gov.kui.docRepoR.domain;

import gov.kui.docRepoR.dto.SenderDto;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class SenderRandomFactory {
    private SenderRandomFactory() {
    }

    public static Sender getRandomSender() {
        Sender sender = new Sender();
        sender.setId(new Random().nextInt(100));
        sender.setTitle(new StringBuilder("Sender ").append(RandomStringUtils.randomAlphabetic(12)).toString());

        return sender;
    }

    public static SenderDto getRandomSenderDto() {
        return SenderDto.builder()
                .id(new Random().nextInt(100))
                .title(new StringBuilder("SenderDto ").append(RandomStringUtils.randomAlphabetic(12)).toString())
                .build();
    }

    public static SenderDto getDtoFromSender(Sender sender) {
        if (sender == null) {
            return null;
        }
        SenderDto senderDto = new SenderDto();
        senderDto.setId(sender.getId());
        senderDto.setTitle(sender.getTitle());
        return senderDto;
    }

    public static Sender getSenderFromDto(SenderDto senderDto) {
        if (senderDto == null) {
            return null;
        }
        Sender sender = new Sender();
        sender.setId(senderDto.getId());
        sender.setTitle(senderDto.getTitle());
        return sender;

    }

    public static Set<SenderDto> getDtosFromSenders(Set<Sender> senders) {
        if (senders == null) {
            return null;
        }

        Set<SenderDto> senderDtos = new HashSet<>(senders.size());
        for (Sender sender : senders) {
            senderDtos.add(getDtoFromSender(sender));
        }
        return senderDtos;
    }

    public static Set<Sender> getSendersFromDtos(Set<SenderDto> senderDtos) {
        if (senderDtos == null) {
            return null;
        }
        Set<Sender> senders = new HashSet<>(senderDtos.size());
        for (SenderDto senderDto : senderDtos) {
            senders.add(getSenderFromDto(senderDto));
        }
        return senders;
    }
}
