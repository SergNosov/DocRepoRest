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

import java.sql.SQLException;
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
    public byte[] getFileByte(int id) {
        FileEntity fileEntity = fileEntityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Не найден файл (fileEntity) с id - " + id));

        try {
            byte[] fileByte = fileEntity.getFileByte().getBytes(1, (int) fileEntity.getFileByte().length());
            return fileByte;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Не удалось загрузить файл из базы данных: " + e.getMessage());
        }
    }

    @Override
    public List<FileEntityDto> findDtosByDocId(int id) {
        return fileEntityRepository.findFileEntityDtosByDocId(id);
    }

    @Override
    @Transactional
    public FileEntity save(final FileEntity fileEntity) {
        checkFileEntity(fileEntity);

        if (!documentRepository.existsById(fileEntity.getDocumentId())) {
            throw new IllegalArgumentException("Не найден документ с id - " + fileEntity.getDocumentId());
        }

        return fileEntityRepository.save(fileEntity);
    }

    private void checkFileEntity(final FileEntity fileEntity) {
        Assert.notNull(fileEntity, "fileEntity is null");
        Assert.hasText(fileEntity.getFilename(), "Не верно указаны реквизиты файла filename: " +
                fileEntity.getFilename());

        try {
            final long byteLength = fileEntity.getFileByte().length();

            if (fileEntity.getFileByte() == null || byteLength == 0) {
                throw new IllegalArgumentException("Не добавлен файл:" +
                        fileEntity.getFilename());
            }

            if (fileEntity.getFileSize() != byteLength) {
                fileEntity.setFileSize(byteLength);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обращении к fileEntity.getFileByte(): " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public int deleteById(int id) {
        fileEntityRepository.deleteById(this.findById(id).getId());
        return id;
    }
}
