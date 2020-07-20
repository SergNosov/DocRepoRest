package gov.kui.docRepoR.facade.Impl;

import com.google.common.collect.Lists;
import gov.kui.docRepoR.domain.Document;
import gov.kui.docRepoR.domain.DocumentRandomFactory;
import gov.kui.docRepoR.dto.DocumentDto;
import gov.kui.docRepoR.dto.mappers.DocumentMapper;
import gov.kui.docRepoR.service.DocumentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
public class DocumentServiceFacadeTests {

    @Mock
    private  DocumentService documentService;
    @Mock
    private  DocumentMapper documentMapper;

    @InjectMocks
    private  DocumentServiceFacadeImpl documentServiceFacade;

    private Document validDocument;
    private DocumentDto validDocumentDto;

    @BeforeEach
    void setUp(){
        validDocument = DocumentRandomFactory.getRandomDocument();
        validDocumentDto = DocumentRandomFactory.getDtoFromDocument(validDocument);
    }

    @Test
    @Order(1)
    @DisplayName("1. Testing the receipt of all documentDtos. Ok")
    void findAllTestOk(){
        List<DocumentDto> documentDtos  = Lists.newArrayList(validDocumentDto);
        given(documentService.findAllDtos()).willReturn(documentDtos);

        List<DocumentDto> documentDtosActual = documentServiceFacade.findAll();

        then(documentService).should(times(1)).findAllDtos();
        assertNotNull(documentDtosActual);
        assertFalse(documentDtos.isEmpty());
        assertEquals(documentDtos.size(),documentDtosActual.size());
    }

    @Test
    @Order(2)
    @DisplayName("2. Testing the receipt of documentDtos by pages. Ok")
    void findAllByPagesTestOk(){
        List<DocumentDto> documentDtos  = Lists.newArrayList(validDocumentDto);
        Pageable pageable = PageRequest.of(1, 1);
        given(documentService.findAllDtosByPage(pageable)).willReturn(documentDtos);

        List<DocumentDto> documentDtosActual = documentServiceFacade.findAllByPage(pageable);

        then(documentService).should(times(1)).findAllDtosByPage(pageable);
        assertNotNull(documentDtosActual);
        assertFalse(documentDtos.isEmpty());
        assertEquals(documentDtos.size(),documentDtosActual.size());
    }

    @Test
    @Order(3)
    @DisplayName("3. Testing the receipt of documentDtos by pages. Bad.")
    void findAllByPagesTestBad(){
        RuntimeException rte = assertThrows(RuntimeException.class,
                () -> documentServiceFacade.findAllByPage(null)
        );

        then(documentService).should(times(0)).findAllDtosByPage(any());
        assertEquals("pagable is null.", rte.getMessage());
    }

    @Test
    @Order(4)
    @DisplayName("4. Testing the receipt of documentDto by id. OK.")
    void findByIdTestOk(){
        given(documentService.findDtoById(anyInt())).willReturn(validDocumentDto);

        DocumentDto documentDtoActual = documentServiceFacade.findById(validDocumentDto.getId());

        then(documentService).should(times(1)).findDtoById(validDocumentDto.getId());
        assertNotNull(documentDtoActual);
        assertEquals(validDocumentDto.getId(),documentDtoActual.getId());
        assertEquals(validDocumentDto.getNumber(),documentDtoActual.getNumber());
        assertEquals(validDocumentDto.getDocDate(),documentDtoActual.getDocDate());
    }

    @Test
    @Order(5)
    @DisplayName("5. Testing the receipt of documentDto by id. Not found.")
    void findByIdNotFoundTest(){
        given(documentService.findDtoById(anyInt())).willThrow(
                new IllegalArgumentException("Не найден документ с id - " + validDocumentDto.getId())
        );

        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> documentServiceFacade.findById(validDocumentDto.getId())
        );

        then(documentService).should(times(1)).findDtoById(anyInt());
        assertEquals("Не найден документ с id - " + validDocumentDto.getId(), iae.getMessage());
    }
}
