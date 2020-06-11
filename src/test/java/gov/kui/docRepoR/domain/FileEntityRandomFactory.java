package gov.kui.docRepoR.domain;

import gov.kui.docRepoR.dto.FileEntityDto;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class FileEntityRandomFactory {
    private FileEntityRandomFactory() {
    }

    public static FileEntityBlob getRandomFileEntityBlob(int docId) {
        final String filename = RandomStringUtils.randomAlphabetic(12);
        final int idFileEntity = new Random().nextInt(100);

        MultipartFile multipartFile = new MockMultipartFile(
                filename,
                filename + ".pdf",
                "application/pdf",
                new byte[]{1, 2, 3}
        );
        FileEntityBlob fileEntityBlob = FileEntityBlob.getInstance(multipartFile,docId);
        fileEntityBlob.getFileEntity().setId(idFileEntity);
        fileEntityBlob.setId(idFileEntity);

        return fileEntityBlob;
    }

    public static FileEntityDto getFileEntityDtoFromFileEntity(FileEntity fileEntity) {
        if (fileEntity == null) {
            return null;
        }

        FileEntityDto fileEntityDto = FileEntityDto.builder().id(fileEntity.getId())
                .filename(fileEntity.getFilename())
                .contentType(fileEntity.getContentType())
                .fileSize(fileEntity.getFileSize())
                .build();

        return fileEntityDto;
    }

    public static  FileEntity getFileEntityFromDto(FileEntityDto fileEntityDto){
        if (fileEntityDto == null) {
            return null;
        }
        FileEntity fileEntity = new FileEntity();
        fileEntity.setId(fileEntityDto.getId());
        fileEntity.setFilename(fileEntityDto.getFilename());
        fileEntity.setContentType(fileEntityDto.getContentType());
        fileEntity.setFileSize(fileEntityDto.getFileSize());

        return fileEntity;
    }

    public static Set<FileEntityDto> getDtosFromFileEntities(Set<FileEntity> fileEntities) {
        if (fileEntities == null) {
            return null;
        }
        Set<FileEntityDto> fileEntityDtos = new HashSet<>(fileEntities.size());
        for (FileEntity fileEntity : fileEntities) {
            fileEntityDtos.add(FileEntityRandomFactory.getFileEntityDtoFromFileEntity(fileEntity));
        }
        return fileEntityDtos;
    }

    public  static  Set<FileEntity> getFileEntitiesFromDtos(Set<FileEntityDto> fileEntityDtos){
        if (fileEntityDtos == null) {
            return null;
        }
        Set<FileEntity> fileEntities = new HashSet<>(fileEntityDtos.size());
        for (FileEntityDto fileEntityDto : fileEntityDtos) {
            fileEntities.add(FileEntityRandomFactory.getFileEntityFromDto(fileEntityDto));
        }
        return fileEntities;
    }

    public static ResponseEntity<Resource> getTestResourceFromFileEntityBlob(FileEntityBlob fileEntityBlob){
        try {
            if (fileEntityBlob.getFileByte().length() == 0) {
                return ResponseEntity.noContent().build();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось загрузить файл из базы данных. fileEntityBlob:"+fileEntityBlob);
        }

        try {
            Resource resource = new ByteArrayResource(fileEntityBlob.getFileByte()
                    .getBytes(1, (int) fileEntityBlob.getFileByte().length()));
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(fileEntityBlob.getFileEntity().getContentType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" +
                                    fileEntityBlob.getFileEntity().getFilename() + "\"")
                    .body(resource);
        } catch (SQLException ex) {
            throw new RuntimeException("Не удалось загрузить файл из базы данных. fileEntityBlob: " + fileEntityBlob);
        }
    }
}
