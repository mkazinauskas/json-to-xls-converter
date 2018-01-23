package com.modzo.jsonxlsconverter

import org.springframework.test.context.TestPropertySource

@TestPropertySource(properties="converter.persistence.type=memory")
class MemoryBasedStorageSpec extends AbstractConverterSpec {
}