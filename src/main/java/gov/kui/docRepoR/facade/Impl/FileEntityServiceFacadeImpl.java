package gov.kui.docRepoR.facade.Impl;

import gov.kui.docRepoR.domain.FileEntity;
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
    public FileEntityDto save(FileEntity fileEntity) {
        Assert.notNull(fileEntity, "Не указан fileEntity (null)");
        return fileEntityMapper.fileEntityToFileEntityDto(
                fileEntityService.save(fileEntity)
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
        FileEntity fileEntity = fileEntityService.findById(id);
        return generateResponseEntity(fileEntity);
    }

    private ResponseEntity<Resource> generateResponseEntity(FileEntity fileEntity) {
        Assert.notNull(fileEntity, "Не указан fileEntity (null)");

//        if (fileEntity.getFileByte() != null) {
//            Resource resource = getResourse(fileEntity);
//            return ResponseEntity.ok()
//                    .contentType(MediaType.parseMediaType(fileEntity.getContentType()))
//                    .header(HttpHeaders.CONTENT_DISPOSITION,
//                            "attachment; filename=\"" +
//                                    fileEntity.getFilename() + "\"")
//                    .body(resource);
//        }
        return ResponseEntity.noContent().build();
    }

    private Resource getResourse(FileEntity fileEntity) {
//        try {
//            Resource resource = new ByteArrayResource(fileEntity.getFileByte()
//                    .getBytes(1, (int) fileEntity.getFileByte().length()));
//            return resource;
//        } catch (SQLException ex) {
//            throw new RuntimeException("Не удалось загрузить файл из базы данных. fileEntity: " + fileEntity);
//        }
        return null;
    }
}
