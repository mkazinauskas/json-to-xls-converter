package com.modzo.jsonxlsconverter.persistance;

import com.modzo.jsonxlsconverter.InternalServerError;
import com.modzo.jsonxlsconverter.persistance.PersistenceConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.String.format;
import static java.nio.file.Files.readAllBytes;

@Service
@ConditionalOnProperty(name = "converter.persistence.type", havingValue = "file")
class FileBasedPersistenceService implements DataPersistenceService {

    private static final Logger LOG = LoggerFactory.getLogger(FileBasedPersistenceService.class);

    private final String filePath;

    FileBasedPersistenceService(PersistenceConfiguration configuration) {
        this.filePath = configuration.getFilesLocation();
    }

    @Override
    public void save(long id, File file) {
        LOG.info(format("Saving file with id = `%s`", id));
        try {
            Files.write(getHeaderPath(id), file.getFileName().getBytes());
            Files.write(getContentPath(id), file.getContent());
        } catch (IOException e) {
            LOG.error("Failed to write file {}", id, e);
            throw new InternalServerError(format("Failed to create temp file: %s", id));
        }
    }

    @Override
    public File retrieve(long id) {
        LOG.info(format("Retrieving file with id = `%s`", id));
        try {
            return new File(new String(readAllBytes(getHeaderPath(id))), readAllBytes(getContentPath(id)));
        } catch (IOException e) {
            LOG.error("Failed to read file {}", id, e);
            throw new InternalServerError(format("Failed to read file: %s", id));
        }
    }

    private Path getContentPath(long fileId) {
        return Paths.get(filePath + "/" + fileId + ".xls_content");
    }

    private Path getHeaderPath(long fileId) {
        return Paths.get(filePath + "/" + fileId + ".xls_header");
    }
}
