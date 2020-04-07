package gov.kui.docRepoR.domain.dto;

import com.google.common.collect.Lists;
import gov.kui.docRepoR.domain.Doctype;
import gov.kui.docRepoR.domain.Document;
import gov.kui.docRepoR.domain.FileEntity;
import gov.kui.docRepoR.domain.Sender;
import gov.kui.docRepoR.dto.DoctypeDto;
import gov.kui.docRepoR.dto.DocumentDto;
import gov.kui.docRepoR.dto.FileEntityDto;
import gov.kui.docRepoR.dto.SenderDto;
import gov.kui.docRepoR.dto.mappers.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.mockito.Mock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

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

    @Test
    void documentToDocumentDto() {
        Document document = generateDocument();

        given(doctypeMapper.doctypeToDoctypeDto(any())).willReturn(
                generateDoctypeDto(document.getDoctype())
        );
        given(senderMapper.sendersToSenderDtos(any())).willReturn(
                generateSenderDtos(document.getSenders())
        );
        given(fileEntityMapper.fileEntitiesToFileEntityDtos(any())).willReturn(
                generateFileEntityDtos(document.getFileEntities())
        );

        DocumentDto documentDtoActual = documentMapper.documentToDocumentDto(document);

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

    private Document generateDocument() {
        Document document = new Document();
        document.setId(21);
        document.setNumber("123-p");
        document.setDocDate(LocalDate.now());
        document.setTitle("DocumentTitle");
        document.setContent("Sic transit gloria mundi...");
        document.setDoctype(generateDoctype());
        document.addSender(generateSender());
        document.addSender(generateSender());
        document.setFileEntities(Lists.newArrayList(generateFileEntity(), generateFileEntity()));
        return document;
    }

    private Doctype generateDoctype() {
        Doctype doctype = new Doctype();
        doctype.setId(randomInt());
        doctype.setTitle(RandomStringUtils.randomAlphabetic(5));
        return doctype;
    }

    private Sender generateSender() {
        Sender sender = new Sender();
        sender.setId(randomInt());
        sender.setTitle(new StringBuilder("SenderTitle:").append(RandomStringUtils.randomAlphabetic(5)).toString());
        return sender;
    }

    private FileEntity generateFileEntity() {
        MultipartFile multipartFile = new MockMultipartFile(
                RandomStringUtils.randomAlphabetic(12),
                "testFile.pdf",
                "application/pdf",
                new byte[]{1, 2, 3}
        );
        return FileEntity.getInstance(multipartFile, randomInt());
    }

    private DoctypeDto generateDoctypeDto(Doctype doctype) {
        return DoctypeDto.builder().id(doctype.getId())
                .title(doctype.getTitle())
                .build();
    }

    private List<SenderDto> generateSenderDtos(List<Sender> senders) {
        if (senders == null) {
            return null;
        }
        List<SenderDto> list = new ArrayList<>(senders.size());
        for (Sender sender : senders) {
            list.add(SenderDto.builder().id(sender.getId()).title(sender.getTitle()).build());
        }
        return list;
    }

    private List<FileEntityDto> generateFileEntityDtos(List<FileEntity> fileEntities) {
        if (fileEntities == null) {
            return null;
        }
        List<FileEntityDto> list = new ArrayList<>(fileEntities.size());
        for (FileEntity fileEntity : fileEntities) {
            list.add(FileEntityDto.builder()
                    .id(fileEntity.getId())
                    .filename(fileEntity.getFilename())
                    .fileSize(fileEntity.getFileSize())
                    .build()
            );
        }
        return list;
    }

    private int randomInt() {
        return new Random().nextInt(100);
    }
}
