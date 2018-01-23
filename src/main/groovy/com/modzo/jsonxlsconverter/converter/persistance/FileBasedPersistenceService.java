package com.modzo.jsonxlsconverter.converter.persistance;

import com.modzo.jsonxlsconverter.InternalServerError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@ConditionalOnProperty(name = "converter.persistence.type", havingValue = "file")
class FileBasedPersistenceService implements DataPersistenceService {

    private String filePath;

    FileBasedPersistenceService(@Value("${file.save.location:/tmp}") String filePath) {
        this.filePath = filePath;
    }

    private static final Logger LOG = LoggerFactory.getLogger(FileBasedPersistenceService.class);

    @Override
    public void save(long id, File file) {
//        try {
//            Files.write(getFilePath(id), content);
//        } catch (IOException e) {
//            LOG.error("Failed to write file {}", fileId, e);
//            throw new InternalServerError("Failed to create temp file: " + fileId);
//        }
    }

    @Override
    public File get(long id) {
        try {
            return new File("File name", Files.readAllBytes(getFilePath(id)));
        } catch (IOException e) {
            LOG.error("Failed to read file {}", id, e);
            throw new InternalServerError("Failed to read file: " + id);
        }
    }

    private Path getFilePath(long fileId) {
        return Paths.get(filePath + "/" + fileId + ".xls");
    }
}
