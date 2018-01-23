package com.modzo.jsonxlsconverter

import org.hibernate.validator.constraints.NotBlank
import org.hibernate.validator.constraints.NotEmpty

import javax.validation.Valid

class ConvertRequest {
    @NotBlank
    String fileName

    @NotEmpty
    @Valid
    List<Sheet> sheets

    static class Sheet{
        @NotBlank
        String name

        @NotEmpty
        @Valid
        List<Row> rows

        static class Row {
            @NotEmpty
            @Valid
            List<Column> columns

            static class Column {
                @NotEmpty
                String data
            }
        }
    }
}
