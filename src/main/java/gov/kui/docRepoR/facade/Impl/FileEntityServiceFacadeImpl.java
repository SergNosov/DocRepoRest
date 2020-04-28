package gov.kui.docRepoR.facade.Impl;

import gov.kui.docRepoR.domain.FileEntity;
import gov.kui.docRepoR.dto.FileEntityDto;
import gov.kui.docRepoR.facade.FileEntityServiceFacade;
import gov.kui.docRepoR.service.FileEntityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class FileEntityServiceFacadeImpl implements FileEntityServiceFacade {

    private final FileEntityService fileEntityService;

    @Autowired
    public FileEntityServiceFacadeImpl(FileEntityService fileEntityService) {
        this.fileEntityService = fileEntityService;
    }

    @Override
    public FileEntityDto save(FileEntity fileEntity) {
        return null;
    }

    @Override
    public FileEntityDto findById(int id) {
        return fileEntityService.findDtoById(id);
    }

    @Override
    public List<FileEntityDto> findDtosByDocId(int id) {
        return null;
    }

    @Override
    public int deleteById(int id) {
        return 0;
    }
}
