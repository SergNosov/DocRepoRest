package gov.kui.docRepoR.service.Impl;

import gov.kui.docRepoR.dao.FileEntityRepository;
import gov.kui.docRepoR.model.FileEntity;
import gov.kui.docRepoR.service.FileEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
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
        Optional<FileEntity> result = fileEntityRepository.findById(id);
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new RuntimeException("Не найден файл (fileEntity) с id - " + id);
        }
    }

    @Override
    public Set<FileEntity> findByDocId(int id) {
        Set<FileEntity> fileEntities = fileEntityRepository.findAllByDocumentId(id);
        return fileEntities;
    }

    @Override
    public FileEntity save(FileEntity fileEntity) {
        if (fileEntity.getFilename() == null || fileEntity.getFileSize() <= 0) {
            throw new IllegalArgumentException("Не верно указаны реквизиты файла:" +
                    "filename: " +
                    fileEntity.getFilename() +
                    "; fileSize: " +
                    fileEntity.getFileSize()
            );
        }

        if (fileEntity.getData() == null){
            throw new IllegalArgumentException("Не добавлен файл:" +
                    fileEntity.getFilename());
        }
        return fileEntityRepository.save(fileEntity);
    }

    @Override
    public int deleteById(int id) {
        fileEntityRepository.deleteById(this.findById(id).getId());
        return id;
    }
}
