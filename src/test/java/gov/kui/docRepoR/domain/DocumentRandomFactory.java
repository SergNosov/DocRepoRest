package gov.kui.docRepoR.domain;

import gov.kui.docRepoR.dto.DoctypeDto;
import gov.kui.docRepoR.dto.DocumentDto;
import gov.kui.docRepoR.dto.FileEntityDto;
import gov.kui.docRepoR.dto.SenderDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDate;
import java.util.Random;
import java.util.Set;

@Slf4j
public class DocumentRandomFactory {
    private DocumentRandomFactory() {
    }

    public static Document getRandomDocument() {
        Document document = new Document();
        document.setId(new Random().nextInt(100));
        document.setNumber(String.valueOf(new Random().nextInt(100)));
        document.setDocDate(LocalDate.now());
        document.setTitle(RandomStringUtils.randomAlphabetic(12));
        document.setContent("Content:" + RandomStringUtils.randomAlphabetic(20));
        document.setDoctype(DoctypeRandomFactory.getRandomDoctype());
        document.addSender(SenderRandomFactory.getRandomSender());
        document.addSender(SenderRandomFactory.getRandomSender());
        document.addFileEntity(FileEntityRandomFactory.getRandomFileEntityBlob(document.getId()).getFileEntity());
        document.addFileEntity(FileEntityRandomFactory.getRandomFileEntityBlob(document.getId()).getFileEntity());

        log.info("--- document:" + document.info());
        log.info("--- document.getFileEntities:" + document.getFileEntities());

        return document;
    }

    public static DocumentDto getDtoFromDocument(Document document) {
        if (document == null) {
            return null;
        }
        DoctypeDto doctypeDto = DoctypeRandomFactory.getDtoFromDoctype(document.getDoctype());
        Set<SenderDto> senderDtos = SenderRandomFactory.getDtosFromSenders(document.getSenders());
        Set<FileEntityDto> fileEntityDtos = FileEntityRandomFactory.getDtosFromFileEntities(document.getFileEntities());

        DocumentDto documentDto = new DocumentDto(
                document.getId(),
                document.getNumber(),
                document.getDocDate(),
                document.getTitle(),
                document.getContent()
        );
        documentDto.setDoctype(doctypeDto);
        documentDto.setSenders(senderDtos);
        documentDto.setFileEntities(fileEntityDtos);

        log.info("--- documentDto" + documentDto);

        return documentDto;
    }
}
