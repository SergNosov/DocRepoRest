package gov.kui.docRepoR.service.Impl;

import gov.kui.docRepoR.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Deprecated
@Service
@PropertySource("classpath:application.properties")
public class FileStorageServiceImpl implements FileStorageService {
    private final Path fileStorageLocation;

    @Autowired
    public FileStorageServiceImpl(@Value("${image.upload.dir}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Невозможно создать каталог для хранения загружаемых файлов.", ex);
        }
    }

    @Override
    public String storeFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Невозможно записать файл: " + file +
                    ". Попробуйте еще раз!");
        }

        String fileName = System.currentTimeMillis() +
                StringUtils.cleanPath(file.getOriginalFilename());

        if (fileName.contains("..")) {
            throw new RuntimeException("Невозможно сохранить файл по указанному пути: " + fileName);
        }

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream,
                    this.fileStorageLocation.resolve(fileName),
                    StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Невозможно записать файл: " + fileName + ". Попробуйте еще раз!", e);
        }
    }

    public void deleteFile(String fileName){
        //Files.deleteIfExists()
    }

    @Override
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("Файл не найден. " + fileName);
            }
        } catch (MalformedURLException | FileNotFoundException ex) {
            throw new RuntimeException("Файл не найден. " + fileName, ex);
        }
    }
}
