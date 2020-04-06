package gov.kui.docRepoR.dto.mappers;

import gov.kui.docRepoR.domain.FileEntity;
import gov.kui.docRepoR.dto.FileEntityDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface FileEntityMapper {
    public static FileEntityMapper INSTANCE = Mappers.getMapper(FileEntityMapper.class);
    public FileEntityDto fileEntityToFileEntityDto(FileEntity fileEntity);
    public FileEntity fileEntityDtoToFileEntity(FileEntityDto fileEntityDto);
    public List<FileEntityDto> fileEntitiesToFileEntityDtos(List<FileEntity> fileEntitys);
    public List<FileEntity> fileEntityDtosToFileEntities(List<FileEntityDto> fileEntityDtos);
}
