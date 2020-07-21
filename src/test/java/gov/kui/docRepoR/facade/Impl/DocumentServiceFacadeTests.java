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

    @Test
    @Order(6)
    @DisplayName("6. Testing the save new documentDto. Ok.")
    void saveDocumentDtoTestOk(){
        given(documentService.save(any(Document.class))).willReturn(validDocument);
        given(documentMapper.documentDtoToDocument(any(DocumentDto.class))).willReturn(validDocument);
        given(documentMapper.documentToDocumentDto(any(Document.class))).willReturn(validDocumentDto);

        DocumentDto documentDtoActual = documentServiceFacade.save(validDocumentDto);
        DocumentDto documentDtoZeroId = new DocumentDto();

        then(documentMapper).should(times(1)).documentDtoToDocument(documentDtoZeroId);
        then(documentService).should(times(1)).save(validDocument);
        then(documentMapper).should(times(1)).documentToDocumentDto(validDocument);
        assertNotNull(documentDtoActual);
        assertEquals(validDocumentDto.getNumber(),documentDtoActual.getNumber());
    }

    @Test
    @Order(7)
    @DisplayName("7. Testing the save null documentDto. Bad.")
    void saveDocumentDtoNull() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> documentServiceFacade.save(null)
        );

        then(documentMapper).should(times(0)).documentDtoToDocument(any());
        then(documentService).should(times(0)).save(any());
        then(documentMapper).should(times(0)).documentToDocumentDto(any());
        assertEquals("Не указан documentDto (null)", iae.getMessage());
    }

    @Test
    @Order(8)
    @DisplayName("8. Testing the update documentDto. Ok")
    void updateDocumentDtoTest() {
        given(documentMapper.documentDtoToDocument(any(DocumentDto.class))).willReturn(validDocument);
        given(documentService.save(any(Document.class))).willReturn(validDocument);
        given(documentMapper.documentToDocumentDto((any(Document.class)))).willReturn(validDocumentDto);

        DocumentDto documentDtoActual = documentServiceFacade.update(validDocumentDto);

        then(documentMapper).should(times(1)).documentDtoToDocument(validDocumentDto);
        then(documentService).should(times(1)).save(validDocument);
        then(documentMapper).should(times(1)).documentToDocumentDto(validDocument);

        assertNotNull(documentDtoActual);
        assertEquals(validDocumentDto.getNumber(), documentDtoActual.getNumber());
    }

    @Test
    @Order(9)
    @DisplayName("9. Testing the update null documentDto. Bad.")
    void updateDocumentDtoNullTest() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> documentServiceFacade.update(null)
        );

        then(documentMapper).should(times(0)).documentToDocumentDto(any());
        then(documentService).should(times(0)).save(any());
        then(documentMapper).should(times(0)).documentDtoToDocument(any());
        assertEquals("Не указан documentDto (null)", iae.getMessage());
    }

    @Test
    @Order(10)
    @DisplayName("10. Test delete of documentDto by id. Ok.")
    void deleteByIdTest() {
        given(documentService.deleteById(anyInt())).willReturn(validDocumentDto.getId());

        int deletedId = documentServiceFacade.deleteById(validDocumentDto.getId());

        then(documentService).should(times(1)).deleteById(validDocumentDto.getId());
        assertEquals(validDocumentDto.getId(), deletedId);
    }
}
