package gov.kui.docRepoR.facade.Impl;

import gov.kui.docRepoR.domain.FileEntity;
import gov.kui.docRepoR.dto.FileEntityDto;
import gov.kui.docRepoR.dto.mappers.FileEntityMapper;
import gov.kui.docRepoR.facade.FileEntityServiceFacade;
import gov.kui.docRepoR.service.FileEntityService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

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
        Assert.notNull(fileEntity,"Не указан fileEntity (null)");
        return fileEntityMapper.fileEntityToFileEntityDto(
                fileEntityService.save(fileEntity)
        );
    }

    @Override
    public FileEntityDto findDtoById(int id) {
        return fileEntityService.findDtoById(id);
    }

    @Override
    public FileEntity findById(int id){
        return  fileEntityService.findById(id);
    }

    @Override
    public List<FileEntityDto> findDtosByDocId(int id) {
        return fileEntityService.findDtosByDocId(id);
    }

    @Override
    public int deleteById(int id) {
        return fileEntityService.deleteById(id);
    }
}
