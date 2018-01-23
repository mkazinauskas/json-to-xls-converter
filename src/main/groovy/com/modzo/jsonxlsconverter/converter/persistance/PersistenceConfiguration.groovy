package com.modzo.jsonxlsconverter.converter.persistance

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

import javax.validation.constraints.Min

@Component
@ConfigurationProperties('converter.persistence')
class PersistenceConfiguration {
    @Min(1L)
    long timeInMinutes = 20
}
