package com.modzo.jsonxlsconverter.persistance;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalNotification;
import com.modzo.jsonxlsconverter.InternalServerError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.MINUTES;

@Service
@ConditionalOnProperty(name = "converter.persistence.type", havingValue = "memory")
class MemoryBasedPersistence implements DataPersistenceService {

    private static final Logger LOG = LoggerFactory.getLogger(MemoryBasedPersistence.class);

    private final Cache<Long, File> cache;

    MemoryBasedPersistence(PersistenceConfiguration configuration) {
        this.cache = CacheBuilder
                .newBuilder()
                .expireAfterWrite(configuration.getTimeInMinutes(), MINUTES)
                .removalListener(this::onRemoval)
                .build();
    }

    @Override
    public void save(long id, File file) {
        LOG.info(format("Caching file with id = `%s`", id));
        cache.put(id, file);
    }

    @Override
    public File retrieve(long id) {
        LOG.info(format("Retrieving cached file with id = `%s`", id));
        File file = cache.getIfPresent(id);
        if (file == null) {
            throw new InternalServerError(format("File with id = `%s` was not found", id));
        }
        return file;
    }

    private void onRemoval(RemovalNotification<Long, File> item) {
        LOG.info(format("Removing file from cache with id = `%s`", item.getKey()));
    }
}
