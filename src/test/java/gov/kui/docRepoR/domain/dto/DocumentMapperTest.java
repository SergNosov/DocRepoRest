package gov.kui.docRepoR.domain.dto;

import com.google.common.collect.Sets;
import gov.kui.docRepoR.domain.*;
import gov.kui.docRepoR.dto.DoctypeDto;
import gov.kui.docRepoR.dto.DocumentDto;
import gov.kui.docRepoR.dto.FileEntityDto;
import gov.kui.docRepoR.dto.SenderDto;
import gov.kui.docRepoR.dto.mappers.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.mockito.Mock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class DocumentMapperTest {

    @Mock
    private FileEntityMapper fileEntityMapper;

    @Mock
    private SenderMapper senderMapper;

    @Mock
    private DoctypeMapper doctypeMapper;

    @InjectMocks
    private DocumentMapperImpl documentMapper;

    private final int docId = 21;

    @Test
    void documentToDocumentDto() {
        Document document = generateDocument();
        log.info("---info: " + document.info());
        log.info("---fileEntitys info:" + document.getFileEntities());

        given(doctypeMapper.doctypeToDoctypeDto(any())).willReturn(
                generateDoctypeDto(document.getDoctype())
        );
        given(senderMapper.sendersToSenderDtos(any())).willReturn(
                SenderRandomFactory.getDtosFromSenders(document.getSenders())
        );
        //Set<FileEntityDto> fileEntityDtos = FileEntityRandomFactory.getDtosFromFileEntities(document.getFileEntities());
        given(fileEntityMapper.fileEntitiesToFileEntityDtos(any())).willReturn(
                FileEntityRandomFactory.getDtosFromFileEntities(document.getFileEntities())
        );

        DocumentDto documentDtoActual = documentMapper.documentToDocumentDto(document);
        log.info("--- documentDtoActual: " + documentDtoActual);

        then(doctypeMapper).should(times(1)).doctypeToDoctypeDto(any(Doctype.class));
        then(senderMapper).should(times(1)).sendersToSenderDtos(anySet());
        then(fileEntityMapper).should(times(1)).fileEntitiesToFileEntityDtos(anySet());

        assertAll(
                () -> assertNotNull(documentDtoActual),
                () -> assertEquals(document.getId(), documentDtoActual.getId()),
                () -> assertEquals(document.getNumber(), documentDtoActual.getNumber()),
                () -> assertEquals(document.getDocDate(), documentDtoActual.getDocDate()),
                () -> assertEquals(document.getTitle(), documentDtoActual.getTitle()),
                () -> assertEquals(document.getContent(), documentDtoActual.getContent()),
                () -> assertEquals(document.getDoctype().getId(), documentDtoActual.getDoctype().getId()),
                () -> assertEquals(document.getDoctype().getTitle(), documentDtoActual.getDoctype().getTitle()),
                () -> assertEquals(document.getSenders().size(), documentDtoActual.getSenders().size()),
                () -> assertEquals(document.getFileEntities().size(), documentDtoActual.getFileEntities().size())
        );
    }

    @Test
    void documentDtoToDocument() {
        DocumentDto documentDto = generateDocumentDto();
        log.info("--- documentDto: " + documentDto);

        given(doctypeMapper.doctypeDtoToDoctype(any(DoctypeDto.class)))
                .willReturn(
                        DoctypeRandomFactory.getDoctypeFromDoctypeDto(
                                documentDto.getDoctype()
                        )
                );
        given(senderMapper.senderDtosToSenders(any()))
                .willReturn(
                        SenderRandomFactory.getSendersFromDtos(
                                documentDto.getSenders()
                        )
                );
        given(fileEntityMapper.fileEntityDtosToFileEntities(any()))
                .willReturn(
                        FileEntityRandomFactory.getFileEntitiesFromDtos(
                                documentDto.getFileEntities()
                        )
                );

        Document documentActual = documentMapper.documentDtoToDocument(documentDto);
        log.info("--- documentActual: " + documentActual.info());
        log.info("--- documentActual.fileEntities: " + documentActual.getFileEntities());

        then(doctypeMapper).should(times(1)).doctypeDtoToDoctype(any(DoctypeDto.class));
        then(senderMapper).should(times(1)).senderDtosToSenders(anySet());
        then(fileEntityMapper).should(times(1)).fileEntityDtosToFileEntities(anySet());

        assertAll(
                () -> assertNotNull(documentActual),
                () -> assertEquals(documentDto.getId(), documentActual.getId()),
                () -> assertEquals(documentDto.getNumber(), documentActual.getNumber()),
                () -> assertEquals(documentDto.getDocDate(), documentActual.getDocDate()),
                () -> assertEquals(documentDto.getTitle(), documentActual.getTitle()),
                () -> assertEquals(documentDto.getContent(), documentActual.getContent()),
                () -> assertEquals(documentDto.getDoctype().getId(), documentActual.getDoctype().getId()),
                () -> assertEquals(documentDto.getDoctype().getTitle(), documentActual.getDoctype().getTitle()),
                () -> assertEquals(documentDto.getSenders().size(), documentActual.getSenders().size()),
                () -> assertEquals(documentDto.getFileEntities().size(), documentActual.getFileEntities().size())
        );
    }

    private Document generateDocument() {
        Document document = new Document();
        document.setId(docId);
        document.setNumber("123-p");
        document.setDocDate(LocalDate.now());
        document.setTitle("DocumentTitle");
        document.setContent("Sic transit gloria mundi...");
        document.setDoctype(generateDoctype());
        document.addSender(generateSender());
        document.addSender(generateSender());
        document.setFileEntities(Sets.newHashSet(generateFileEntity(), generateFileEntity()));
        return document;
    }

    private Doctype generateDoctype() {
        return DoctypeRandomFactory.getRandomDoctype();
    }

    private Sender generateSender() {
        Sender sender = new Sender();
        sender.setId(randomInt());
        sender.setTitle(new StringBuilder("SenderTitle:").append(RandomStringUtils.randomAlphabetic(5)).toString());
        return sender;
    }

    private FileEntity generateFileEntity() {
        final String filename = RandomStringUtils.randomAlphabetic(12);
        MultipartFile multipartFile = new MockMultipartFile(
                filename,
                filename + ".pdf",
                "application/pdf",
                new byte[]{1, 2, 3}
        );

        FileEntity fileEntity = new FileEntity();
        fileEntity.setId(randomInt());
        fileEntity.setFilename(filename+".pdf");
        fileEntity.setFileSize(3);
        fileEntity.setDocumentId(docId);
        fileEntity.setContentType("application/pdf");

        return fileEntity;
    }

    private DocumentDto generateDocumentDto() {
        DocumentDto documentDto = new DocumentDto();
        documentDto.setId(docId);
        documentDto.setNumber("321-р");
        documentDto.setDocDate(LocalDate.now());
        documentDto.setTitle("DocumentTitle from Dto");
        documentDto.setContent("Aquila non captat muscas.");
        documentDto.setDoctype(
                DoctypeRandomFactory.getRandomDoctypeDto()
        );
        documentDto.setSenders(
                SenderRandomFactory.getDtosFromSenders(
                        Sets.newHashSet(generateSender(), generateSender())
                ));
        documentDto.setFileEntities(
                FileEntityRandomFactory.getDtosFromFileEntities(
                        Sets.newHashSet(generateFileEntity(), generateFileEntity())
                ));

        return documentDto;
    }

    private DoctypeDto generateDoctypeDto(Doctype doctype) {
        return DoctypeRandomFactory.getDtoFromDoctype(doctype);
    }

//    private Set<SenderDto> generateSenderDtos(Set<Sender> senders) {
//        if (senders == null) {
//            return null;
//        }
//        Set<SenderDto> dtoSet = new HashSet<>(senders.size());
//        for (Sender sender : senders) {
//            dtoSet.add(SenderDto.builder().id(sender.getId()).title(sender.getTitle()).build());
//        }
//        return dtoSet;
//    }
//
//    private Set<FileEntityDto> generateFileEntityDtos(Set<FileEntity> fileEntities) {
//        if (fileEntities == null) {
//            return null;
//        }
//        Set<FileEntityDto> dtos = new HashSet<>(fileEntities.size());
//        for (FileEntity fileEntity : fileEntities) {
//            dtos.add(FileEntityDto.builder()
//                    .id(fileEntity.getId())
//                    .filename(fileEntity.getFilename())
//                    .fileSize(fileEntity.getFileSize())
//                    .build()
//            );
//        }
//        return dtos;
//    }

    private int randomInt() {
        return new Random().nextInt(100);
    }
}
