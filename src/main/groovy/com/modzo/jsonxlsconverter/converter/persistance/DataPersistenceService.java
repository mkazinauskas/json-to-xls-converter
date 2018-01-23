package com.modzo.jsonxlsconverter.converter.persistance;

public interface DataPersistenceService {
    void save(long id, File file);

    File retrieve(long id);

    class File {
        private final String fileName;

        private final byte[] content;

        public File(String fileName, byte[] content) {
            this.fileName = fileName;
            this.content = content;
        }

        public String getFileName() {
            return fileName;
        }

        public byte[] getContent() {
            return content;
        }
    }
}
