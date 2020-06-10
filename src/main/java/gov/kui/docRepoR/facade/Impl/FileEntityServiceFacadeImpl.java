package gov.kui.docRepoR.facade.Impl;

import gov.kui.docRepoR.domain.FileEntityBlob;
import gov.kui.docRepoR.dto.FileEntityDto;
import gov.kui.docRepoR.dto.mappers.FileEntityMapper;
import gov.kui.docRepoR.facade.FileEntityServiceFacade;
import gov.kui.docRepoR.service.FileEntityService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.sql.SQLException;
import java.util.List;

@Slf4j
@Service
public class FileEntityServiceFacadeImpl implements FileEntityServiceFacade {

    private final FileEntityService fileEntityService;
    private final FileEntityMapper fileEntityMapper;

    @Autowired
    public FileEntityServiceFacadeImpl(FileEntityService fileEntityService, FileEntityMapper fileEntityMapper) {
        this.fileEntityService = fileEntityService;
        this.fileEntityMapper = fileEntityMapper;
    }

    @Override
    public FileEntityDto save(FileEntityBlob fileEntityBlob) {
        Assert.notNull(fileEntityBlob, "Не указан fileEntity (null)");
        return fileEntityMapper.fileEntityToFileEntityDto(
                fileEntityService.save(fileEntityBlob)
        );
    }

    @Override
    public FileEntityDto findDtoById(int id) {
        return fileEntityService.findDtoById(id);
    }

    @Override
    public List<FileEntityDto> findDtosByDocId(int id) {
        return fileEntityService.findDtosByDocId(id);
    }

    @Override
    public int deleteById(int id) {
        return fileEntityService.deleteById(id);
    }

    @Override
    public ResponseEntity<Resource> getResourseById(int id) {
        FileEntityBlob fileEntityBlob = fileEntityService.findById(id);
        return generateResponseEntity(fileEntityBlob);
    }

    private ResponseEntity<Resource> generateResponseEntity(FileEntityBlob fileEntityBlob) {
        Assert.notNull(fileEntityBlob, "Не указан fileEntityBlob (null)");
        Assert.notNull(fileEntityBlob.getFileEntity(), "Не указан fileEntity (null)");
        Assert.notNull(fileEntityBlob.getFileByte(),"Содержимое файла null");

        try {
            if (fileEntityBlob.getFileByte().length() == 0) {
                return ResponseEntity.noContent().build();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось загрузить файл из базы данных. fileEntityBlob:"+fileEntityBlob);
        }

        Resource resource = getResourse(fileEntityBlob);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileEntityBlob.getFileEntity().getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                fileEntityBlob.getFileEntity().getFilename() + "\"")
                .body(resource);
    }

    private Resource getResourse(FileEntityBlob fileEntityBlob) {
        try {
            Resource resource = new ByteArrayResource(fileEntityBlob.getFileByte()
                    .getBytes(1, (int) fileEntityBlob.getFileByte().length()));
            return resource;
        } catch (SQLException ex) {
            throw new RuntimeException("Не удалось загрузить файл из базы данных. fileEntityBlob: " + fileEntityBlob);
        }
    }
}
