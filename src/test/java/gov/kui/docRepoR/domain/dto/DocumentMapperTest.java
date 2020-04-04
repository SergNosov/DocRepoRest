package gov.kui.docRepoR.domain.dto;

import com.google.common.collect.Lists;
import gov.kui.docRepoR.domain.Doctype;
import gov.kui.docRepoR.domain.Document;
import gov.kui.docRepoR.domain.Sender;
import gov.kui.docRepoR.dto.DoctypeDto;
import gov.kui.docRepoR.dto.DocumentDto;
import gov.kui.docRepoR.dto.SenderDto;
import gov.kui.docRepoR.dto.mappers.*;
import org.mockito.Mock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class DocumentMapperTest {

   // private final DocumentMapper documentMapper = DocumentMapper.INSTANCE;

    @Mock
    private FileEntityMapper fileEntityMapper;

    @Mock
    private SenderMapper senderMapper;

    @Mock
    private DoctypeMapper doctypeMapper;

    @InjectMocks
    private DocumentMapperImpl documentMapper;

    @Test
    void documentToDocumentDto(){
        Doctype doctype = new Doctype();
        doctype.setId(1);
        doctype.setTitle("title of Doctype");
        DoctypeDto doctypeDto = DoctypeDto.builder().id(doctype.getId()).title(doctype.getTitle()).build();

        Sender sender1 = new Sender();
        sender1.setId(11);
        sender1.setTitle("Sender 1");
        SenderDto senderDto1 = SenderDto.builder().id(sender1.getId()).title(sender1.getTitle()).build();

        Sender sender2 = new Sender();
        sender2.setId(2);
        sender2.setTitle("Sender 2");
        SenderDto senderDto2 = SenderDto.builder().id(sender2.getId()).title(sender2.getTitle()).build();
        List<SenderDto> senderDtos = Lists.newArrayList(senderDto1,senderDto2);

        Document document = new Document();
        document.setId(21);
        document.setNumber("123-p");
        document.setDocDate(LocalDate.now());
        document.setTitle("DocumentTitle");
        document.setContent("Sic transit gloria mundi...");

        document.setDoctype(doctype);
        document.addSender(sender1);
        document.addSender(sender2);

        given(doctypeMapper.doctypeToDoctypeDto(any())).willReturn(doctypeDto);
        given(senderMapper.sendersToSenderDtos(any())).willReturn(senderDtos);
//        given(fileEntityMapper.fileEntitysToFileEntityDtos(any())).willReturn(new ArrayList<>());

        DocumentDto documentDto = documentMapper.documentToDocumentDto(document);

        System.out.println(documentDto);
    }
}
