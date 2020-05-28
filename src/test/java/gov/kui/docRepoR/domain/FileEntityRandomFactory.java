package gov.kui.docRepoR.domain;

import gov.kui.docRepoR.dto.FileEntityDto;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class FileEntityRandomFactory {
    private FileEntityRandomFactory() {
    }

    public static FileEntity getRandomFileEntity(int docId) {
        final String filename = RandomStringUtils.randomAlphabetic(12);

        MultipartFile multipartFile = new MockMultipartFile(
                filename,
                filename + ".pdf",
                "application/pdf",
                new byte[]{1, 2, 3}
        );
        FileEntity fileEntity = FileEntity.getInstance(multipartFile, docId);
        fileEntity.setId(new Random().nextInt(100));
        return fileEntity;
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
}
