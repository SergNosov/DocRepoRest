package gov.kui.docRepoR.service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gov.kui.docRepoR.JsonDocument;
import gov.kui.docRepoR.dao.DoctypeRepository;
import gov.kui.docRepoR.dao.SenderRepository;
import gov.kui.docRepoR.domain.Document;
import gov.kui.docRepoR.dao.DocumentRepository;
import gov.kui.docRepoR.domain.DocumentRandomFactory;
import gov.kui.docRepoR.domain.FileEntityRandomFactory;
import gov.kui.docRepoR.domain.Sender;
import gov.kui.docRepoR.dto.DocumentDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class DocumentServiceImplTests {
    @Mock
    DocumentRepository documentRepository;
    @Mock
    DoctypeRepository doctypeRepository;
    @Mock
    SenderRepository senderRepository;
    @InjectMocks
    DocumentServiceImpl documentService;

    Document validDocument;
    Document invalidDocument;

    @BeforeEach
    void setUp() throws IOException {
        validDocument = new ObjectMapper().registerModule(new JavaTimeModule())
                .readValue(JsonDocument.JSON_GOOD.toString(), Document.class);
        invalidDocument = new ObjectMapper().registerModule(new JavaTimeModule())
                .readValue(JsonDocument.JSON_NO_REQURED_FIELDS.toString(), Document.class);
    }

    @Test
    @DisplayName("Testing the receipt of all documents.")
    void testGetAllDocuments() {
        List<Document> documentList = new ArrayList<>();
        documentList.add(validDocument);

        given(documentRepository.findAll()).willReturn(documentList);
        List<Document> returnedList = documentService.findAll();
        then(documentRepository).should().findAll();
        assertNotNull(returnedList);
        assertEquals(1, returnedList.size());
    }

    @Test
    @DisplayName("Testing the receipt of document by id. OK.")
    void testGetDocumentByIdOk() {
        given(documentRepository.findById(anyInt())).willReturn(Optional.of(validDocument));
        Document returnedDocument = documentService.findById(validDocument.getId());
        then(documentRepository).should().findById(validDocument.getId());
        assertAll(
                () -> assertNotNull(returnedDocument),
                () -> assertEquals(validDocument.getId(), returnedDocument.getId()),
                () -> assertEquals(validDocument.getNumber(), returnedDocument.getNumber()),
                () -> assertEquals(validDocument.getDocDate(), returnedDocument.getDocDate()),
                () -> assertEquals(validDocument.getTitle(), returnedDocument.getTitle()),
                () -> assertEquals(validDocument.getContent(), returnedDocument.getContent()),
                () -> assertEquals(validDocument.getDoctype(), returnedDocument.getDoctype()),
                () -> assertEquals(validDocument.getSenders().size(), returnedDocument.getSenders().size()),
                () -> assertArrayEquals(validDocument.getSenders().toArray(), returnedDocument.getSenders().toArray())
        );
    }

    @Test
    @DisplayName("Testing the receipt of document by id. BAD.")
    void testGetDocumentByIdBad() {
        given(documentRepository.findById(anyInt())).willReturn(Optional.empty());
        RuntimeException rte = assertThrows(RuntimeException.class, () -> documentService.findById(validDocument.getId()));
        assertEquals("Не найден документ с id - " + validDocument.getId(), rte.getMessage());
    }

    @Test
    @DisplayName("Testing the receipt of documentDto by id. BAD.")
    void testGetDocumentDtoByIdBad(){
        given(documentRepository.findDtoById(anyInt())).willReturn(Optional.empty());
        RuntimeException rte = assertThrows(RuntimeException.class, () -> documentService.findDtoById(validDocument.getId()));
        assertEquals("Не найден документ с id - " + validDocument.getId(), rte.getMessage());
    }

    @Test
    @DisplayName("Testing the receipt of documentDto by id. OK.")
    void testGetDocumentDtoByIdOk() {
        validDocument.addFileEntity(FileEntityRandomFactory.getRandomFileEntityBlob(validDocument.getId()).getFileEntity());
        validDocument.addFileEntity(FileEntityRandomFactory.getRandomFileEntityBlob(validDocument.getId()).getFileEntity());

        DocumentDto documentDto = DocumentRandomFactory.getDtoFromDocument(validDocument);

        given(documentRepository.findDtoById(anyInt())).willReturn(Optional.of(documentDto));
        DocumentDto documentDtoActual = documentService.findDtoById(validDocument.getId());
        then(documentRepository).should(times(1)).findDtoById(validDocument.getId());

        assertAll(
                () -> assertNotNull(documentDtoActual),
                () -> assertEquals(documentDto.getId(), documentDtoActual.getId()),
                () -> assertEquals(documentDto.getNumber(), documentDtoActual.getNumber()),
                () -> assertEquals(documentDto.getDocDate(), documentDtoActual.getDocDate()),
                () -> assertEquals(documentDto.getTitle(), documentDtoActual.getTitle()),
                () -> assertEquals(documentDto.getContent(), documentDtoActual.getContent()),
                () -> assertEquals(documentDto.getDoctype().getTitle(), documentDtoActual.getDoctype().getTitle()),
                () -> assertEquals(documentDto.getSenders().size(), documentDtoActual.getSenders().size()),
                () -> assertEquals(documentDto.getFileEntities().size(), documentDtoActual.getFileEntities().size())
        );
    }

    @Test
    @DisplayName("Testing deleteById document by id. OK.")
    void testDeleteDocumentByIdOk() {
        given(documentRepository.findById(anyInt())).willReturn(Optional.of(validDocument));
        int deletedId = documentService.deleteById(validDocument.getId());
        assertEquals(validDocument.getId(), deletedId);
    }

    @Test
    @DisplayName("Testing deleteById document by id. BAD.")
    void testDeleteDocumentByIdBad() {
        given(documentRepository.findById(anyInt())).willReturn(Optional.empty());
        RuntimeException rte = assertThrows(RuntimeException.class, () -> documentService.findById(validDocument.getId()));
        assertEquals("Не найден документ с id - " + validDocument.getId(), rte.getMessage());
    }

    @Test
    @DisplayName("Testing save document (null). BAD.")
    void testSaveDocumentNull() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> documentService.save(null));
        assertEquals("Document is null.", iae.getMessage());
    }

    @Test
    @DisplayName("Testing save document.BAD (Title is null)")
    void testSaveDocumentTitleNull() {
        System.out.println("title:" + invalidDocument.getTitle().length());
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> documentService.save(invalidDocument));
        assertEquals("Не указан заголовок документа. id = "+invalidDocument.getId(), iae.getMessage());
    }

    @Test
    @DisplayName("Testing save document. BAD (Title is blank).")
    void testSaveDocumentTitleBlank() {
        invalidDocument.setTitle("   ");
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> documentService.save(invalidDocument));
        assertEquals("Не указан заголовок документа. id = "+invalidDocument.getId(), iae.getMessage());
    }

    @Test
    @DisplayName("Testing save document. BAD (Doctype is null).")
    void testSaveDocumentDoctypeNull() {
        validDocument.setDoctype(null);
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> documentService.save(validDocument));
        assertEquals("Не установлен тип документа (Doctype of document id = " +
                validDocument.getId() + " is null)", iae.getMessage());
    }

    @Test
    @DisplayName("Testing save document. OK.")
    void testSaveDocumentOk() {
        given(documentRepository.existsById(anyInt())).willReturn(true);
        given(documentRepository.save(any())).willReturn(validDocument);
        given(doctypeRepository.findById(anyInt())).willReturn(Optional.of(validDocument.getDoctype()));
        given(senderRepository.findById(anyInt())).willReturn(Optional.of(
                validDocument.getSenders().toArray(new Sender[validDocument.getSenders().size()])[0])
        );

        Document savedDocument = documentService.save(validDocument);

        assertAll(
                () -> assertNotNull(savedDocument),
                () -> assertEquals(validDocument.getId(), savedDocument.getId()),
                () -> assertEquals(validDocument.getNumber(), savedDocument.getNumber()),
                () -> assertEquals(validDocument.getDocDate(), savedDocument.getDocDate()),
                () -> assertEquals(validDocument.getTitle(), savedDocument.getTitle()),
                () -> assertEquals(validDocument.getContent(), savedDocument.getContent()),
                () -> assertEquals(validDocument.getDoctype(), savedDocument.getDoctype()),
                () -> assertEquals(validDocument.getSenders().size(), savedDocument.getSenders().size()),
                () -> assertArrayEquals(validDocument.getSenders().toArray(), savedDocument.getSenders().toArray())
        );
    }
}
