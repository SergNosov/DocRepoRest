package gov.kui.docRepoR.service.Impl;

import gov.kui.docRepoR.dao.FileEntityRepository;
import gov.kui.docRepoR.domain.FileEntity;
import gov.kui.docRepoR.service.FileEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class FileEntityServiceImpl implements FileEntityService {
    private final FileEntityRepository fileEntityRepository;

    @Autowired
    public FileEntityServiceImpl(FileEntityRepository fileEntityRepository) {
        this.fileEntityRepository = fileEntityRepository;
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
    public Set<FileEntity> findByDocId(int id) {
        Set<FileEntity> fileEntities = fileEntityRepository.findAllByDocumentId(id);
        return fileEntities;
    }

    @Override
    @Transactional
    public FileEntity save(FileEntity fileEntity) {
        Assert.notNull(fileEntity, "fileEntity is null");
        Assert.hasText(fileEntity.getFilename(), "Не верно указаны реквизиты файла filename: " +
                fileEntity.getFilename());

        if (fileEntity.getBytes() == null || fileEntity.getBytes().length == 0) {
            throw new IllegalArgumentException("Не добавлен файл:" +
                    fileEntity.getFilename());
        }

        if (fileEntity.getFileSize() != fileEntity.getBytes().length) {
            fileEntity.setFileSize(fileEntity.getBytes().length);
        }

        return fileEntityRepository.save(fileEntity);
    }

    @Override
    @Transactional
    public int deleteById(int id) {
        fileEntityRepository.deleteById(this.findById(id).getId());
        return id;
    }
}
