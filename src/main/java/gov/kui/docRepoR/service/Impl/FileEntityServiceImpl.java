package gov.kui.docRepoR.service.Impl;

import gov.kui.docRepoR.dao.DocumentRepository;
import gov.kui.docRepoR.dao.FileEntityBlobRepository;
import gov.kui.docRepoR.dao.dtoRepository.FileEntityRepository;
import gov.kui.docRepoR.domain.FileEntityBlob;
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
    private final FileEntityBlobRepository fileEntityBlobRepository;
    private final FileEntityRepository fileEntityRepository;
    private final DocumentRepository documentRepository;

    @Autowired
    public FileEntityServiceImpl(FileEntityBlobRepository fileEntityBlobRepository,
                                 FileEntityRepository fileEntityRepository,
                                 DocumentRepository documentRepository) {
        this.fileEntityBlobRepository = fileEntityBlobRepository;
        this.fileEntityRepository = fileEntityRepository;
        this.documentRepository = documentRepository;
    }

    @Override
    public List<FileEntityBlob> findAll() {
        return fileEntityBlobRepository.findAll();
    }

    @Override
    public FileEntityBlob findById(int id) {
        return fileEntityBlobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Не найден файл (fileEntity) с id - " + id));
    }

    @Override
    public FileEntityDto findDtoById(int id) {
        return fileEntityRepository.findDtoById(id)
                .orElseThrow(() -> new IllegalArgumentException("Не найден файл (fileEntityDto) с id - " + id));
    }

    @Override
    public byte[] getFileByte(int id) {
        FileEntityBlob fileEntityBlob = fileEntityBlobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Не найден файл (fileEntity) с id - " + id));

        try {
            byte[] fileByte = fileEntityBlob.getFileByte().getBytes(1, (int) fileEntityBlob.getFileByte().length());
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
    public FileEntityBlob save(final FileEntityBlob fileEntityBlob) {
        checkFileEntity(fileEntityBlob);

        if (!documentRepository.existsById(fileEntityBlob.getDocumentId())) {
            new RuntimeException("Не найден документ с id - " + fileEntityBlob.getDocumentId());
        }

        return fileEntityBlobRepository.save(fileEntityBlob);
    }

    private void checkFileEntity(final FileEntityBlob fileEntityBlob) {
        Assert.notNull(fileEntityBlob, "fileEntity is null");
        Assert.hasText(fileEntityBlob.getFilename(), "Не верно указаны реквизиты файла filename: " +
                fileEntityBlob.getFilename());

        try {
            final long byteLength = fileEntityBlob.getFileByte().length();

            if (fileEntityBlob.getFileByte() == null || byteLength == 0) {
                throw new IllegalArgumentException("Не добавлен файл:" +
                        fileEntityBlob.getFilename());
            }

            if (fileEntityBlob.getFileSize() != byteLength) {
                fileEntityBlob.setFileSize(byteLength);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обращении к fileEntity.getFileByte(): " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public int deleteById(int id) {
        if (!fileEntityRepository.existsById(id)) {
            new RuntimeException("Не найден файл с id - " + id);
        }
        fileEntityRepository.deleteById(id);
        return id;
    }
}
