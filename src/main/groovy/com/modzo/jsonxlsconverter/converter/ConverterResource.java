package com.modzo.jsonxlsconverter.converter;

import com.modzo.jsonxlsconverter.converter.persistance.DataPersistenceService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Random;

@RestController
class ConverterResource {

    private final DataPersistenceService dataPersistence;

    private final ConvertService service;

    ConverterResource(DataPersistenceService dataPersistence, ConvertService service) {
        this.dataPersistence = dataPersistence;
        this.service = service;
    }

    @PostMapping("/convert")
    ResponseEntity convert(@Valid @RequestBody ConvertRequest request) {
        byte[] workbook = service.convert(request);
        long fileId = new Random().nextLong();
        dataPersistence.save(fileId, new DataPersistenceService.File(request.getFileName(), workbook));

        return ResponseEntity.created(URI.create("/download?id=" + fileId)).build();
    }

    @GetMapping("/download")
    ResponseEntity<byte[]> download(@RequestParam("id") long id) {
        DataPersistenceService.File file = dataPersistence.retrieve(id);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("charset", "utf-8");
        responseHeaders.setContentType(MediaType.valueOf("application/vnd.ms-excel"));
        responseHeaders.setContentLength(file.getContent().length);
        responseHeaders.set("Content-disposition", "attachment; filename=" + file.getFileName() + ".xlsx");

        return new ResponseEntity<>(file.getContent(), responseHeaders, HttpStatus.OK);
    }
}
