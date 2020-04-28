package gov.kui.docRepoR.service.Impl;

import gov.kui.docRepoR.dao.DocumentRepository;
import gov.kui.docRepoR.dao.FileEntityRepository;
import gov.kui.docRepoR.domain.FileEntity;
import gov.kui.docRepoR.dto.FileEntityDto;
import gov.kui.docRepoR.service.FileEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class FileEntityServiceImpl implements FileEntityService {
    private final FileEntityRepository fileEntityRepository;
    private final DocumentRepository documentRepository;

    @Autowired
    public FileEntityServiceImpl(FileEntityRepository fileEntityRepository,
                                 DocumentRepository documentRepository) {
        this.fileEntityRepository = fileEntityRepository;
        this.documentRepository = documentRepository;
    }

    @Override
    public List<FileEntity> findAll() {
        return fileEntityRepository.findAll();
    }

    @Override
    public FileEntity findById(int id) {
        return fileEntityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Не найден файл (fileEntity) с id - " + id));
    }

    @Override
    public FileEntityDto findDtoById(int id) {
        return fileEntityRepository.findDtoById(id)
                .orElseThrow(() -> new IllegalArgumentException("Не найден файл (fileEntityDto) с id - " + id));
    }

    @Override
    public List<FileEntityDto> findDtosByDocId(int id) {
        return fileEntityRepository.findFileEntityDtosByDocId(id);
    }

    @Override
    @Transactional
    public FileEntity save(final FileEntity fileEntity) {
        checkFileEntity(fileEntity);

        documentRepository.findById(fileEntity.getDocumentId()) // todo замена на получение DocumentDto?  или отдельный метод для проверки наличия document
                .orElseThrow(() -> new RuntimeException("Не найден документ с id - " + fileEntity.getDocumentId()));

        return fileEntityRepository.save(fileEntity);
    }

    private void checkFileEntity(final FileEntity fileEntity) {
        Assert.notNull(fileEntity, "fileEntity is null");
        Assert.hasText(fileEntity.getFilename(), "Не верно указаны реквизиты файла filename: " +
                fileEntity.getFilename());

        if (fileEntity.getFileByte() == null || fileEntity.getFileByte().getBytes().length == 0) {
            throw new IllegalArgumentException("Не добавлен файл:" +
                    fileEntity.getFilename());
        }

        if (fileEntity.getFileSize() != fileEntity.getFileByte().getBytes().length) {
            fileEntity.setFileSize(fileEntity.getFileByte().getBytes().length);
        }
    }

    @Override
    @Transactional
    public int deleteById(int id) {
        fileEntityRepository.deleteById(this.findById(id).getId());
        return id;
    }
}
