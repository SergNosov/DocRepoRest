package gov.kui.docRepoR.service;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

public interface FileStorageService {
    /**
     * required for save received  file to file storage
     *
     * @param file - file for saving
     * @return document
     */
    public String storeFile(MultipartFile file);

    /**
     * getting resource descriptor of loaded file by filename
     *
     * @param fileName - the name of loaded file
     * @return Resource of loaded file
     */
    public Resource loadFileAsResource(String fileName);
}
