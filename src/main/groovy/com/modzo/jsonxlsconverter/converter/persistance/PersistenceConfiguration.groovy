package com.modzo.jsonxlsconverter.converter.persistance

import org.hibernate.validator.constraints.NotBlank
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

import javax.validation.constraints.AssertTrue
import javax.validation.constraints.Min
import java.nio.file.Paths

@Component
@ConfigurationProperties('converter.persistence')
class PersistenceConfiguration {
    @Min(1L)
    long timeInMinutes = 20

    @NotBlank
    String filesLocation = '/tmp'

    @AssertTrue
    boolean isPath(){
        return Paths.get(filesLocation)
    }
}
