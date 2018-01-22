package com.modzo.jsonxlsconverter.converter;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
class ConverterResource {

    private final ConvertService service;

    public ConverterResource(ConvertService service) {
        this.service = service;
    }

    @PostMapping("/convert")
    ResponseEntity<byte[]> convert(@Valid @RequestBody ConvertRequest request) {
        byte[] workbook = service.convert(request);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("charset", "utf-8");
        responseHeaders.setContentType(MediaType.valueOf("application/vnd.ms-excel"));
        responseHeaders.setContentLength(workbook.length);
        responseHeaders.set("Content-disposition", "attachment; filename=" + request.getFileName() + ".xls");

        return new ResponseEntity<>(workbook, responseHeaders, HttpStatus.OK);
    }
}
