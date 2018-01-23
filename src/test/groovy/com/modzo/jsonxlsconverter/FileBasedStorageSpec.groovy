package com.modzo.jsonxlsconverter

import org.springframework.test.context.TestPropertySource

@TestPropertySource(properties="converter.persistence.type=file")
class FileBasedStorageSpec extends AbstractConverterSpec {
}