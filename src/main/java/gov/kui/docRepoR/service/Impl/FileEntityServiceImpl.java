package gov.kui.docRepoR.service.Impl;

import gov.kui.docRepoR.dao.DocumentRepository;
import gov.kui.docRepoR.dao.FileEntityBlobRepository;
import gov.kui.docRepoR.dao.FileEntityRepository;
import gov.kui.docRepoR.domain.FileEntity;
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
    private final FileEntityRepository fileEntityRepository;
    private final FileEntityBlobRepository fileEntityBlobRepository;
    private final DocumentRepository documentRepository;

    @Autowired
    public FileEntityServiceImpl(FileEntityRepository fileEntityRepository,
                                 FileEntityBlobRepository fileEntityBlobRepository,
                                 DocumentRepository documentRepository) {
        this.fileEntityRepository = fileEntityRepository;
        this.fileEntityBlobRepository = fileEntityBlobRepository;
        this.documentRepository = documentRepository;
    }

    @Override
    @Transactional
    public int deleteById(int id) {
        FileEntity fileEntity = fileEntityRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Не найден файл (fileEntity) с id - " + id));
        FileEntityBlob fileEntityBlob = this.findById(id);

        fileEntityBlobRepository.delete(fileEntityBlob);
        fileEntityRepository.delete(fileEntity);
        return id;
    }

    @Override
    public FileEntityBlob findById(int id) {
        return fileEntityBlobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Не найден файл (fileEntityBlob) с id - " + id));
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
    public FileEntity save(final FileEntityBlob fileEntityBlob) {
        Assert.notNull(fileEntityBlob, "fileEntityBlob is null");
        Assert.notNull(fileEntityBlob.getFileByte(), "fileEntityBlob.getFileByte() is null");
        Assert.notNull(fileEntityBlob.getFileEntity(), "fileEntity is null");
        Assert.hasText(fileEntityBlob.getFileEntity().getFilename(), "Не верно указаны реквизиты файла filename: " +
                fileEntityBlob.getFileEntity().getFilename());

        if (!documentRepository.existsById(fileEntityBlob.getFileEntity().getDocumentId())) {
            throw new IllegalArgumentException("Не найден документ с id - " + fileEntityBlob.getFileEntity().getDocumentId());
        }

        checkBlobField(fileEntityBlob);
        return fileEntityBlobRepository.save(fileEntityBlob).getFileEntity();
    }

    private void checkBlobField(final FileEntityBlob fileEntityBlob) {
        try {
            if (fileEntityBlob.getFileByte().length() == 0) {
                throw new IllegalArgumentException("Ошибка при сохранении в базу данных. Размер файла равен 0. " +
                        fileEntityBlob.getFileEntity().getFilename());
            }

            if (fileEntityBlob.getFileEntity().getFileSize() != fileEntityBlob.getFileByte().length()) {
                fileEntityBlob.getFileEntity().setFileSize(fileEntityBlob.getFileByte().length());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обращении к fileEntityBlob.getFileByte(): " + e.getMessage());
        }
    }
}
