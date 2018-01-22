package com.modzo.jsonxlsconverter

import com.fasterxml.jackson.databind.ObjectMapper
import com.modzo.jsonxlsconverter.converter.ConvertRequest
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpStatus
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import spock.lang.Unroll

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import static org.springframework.http.MediaType.valueOf

@ContextConfiguration
@SpringBootTest(webEnvironment = RANDOM_PORT)
class ConverterSpec extends Specification {
    @Autowired
    TestRestTemplate template

    @Unroll
    def 'should fail to convert empty request'() {
        when:
            def response = template.postForEntity('/convert', request, String)
        then:
            response.statusCode == HttpStatus.BAD_REQUEST
            response.body != ''
        where:
            request << [
                    new ConvertRequest(),
                    new ConvertRequest(sheets: []),
                    new ConvertRequest(sheets: [new ConvertRequest.Sheet()]),
                    new ConvertRequest(sheets: [new ConvertRequest.Sheet(rows: [])]),
                    new ConvertRequest(sheets: [new ConvertRequest.Sheet(rows: [new ConvertRequest.Sheet.Row()])]),
                    new ConvertRequest(sheets: [new ConvertRequest.Sheet(rows: [
                            new ConvertRequest.Sheet.Row(columns: [new ConvertRequest.Sheet.Row.Column()])]
                    )]),
                    new ConvertRequest(sheets: [new ConvertRequest.Sheet(
                            rows: [new ConvertRequest.Sheet.Row(
                                    columns: [new ConvertRequest.Sheet.Row.Column(data: '')]
                            )]
                    )]),
                    new ConvertRequest(
                            fileName: 'FileName',
                            sheets: [new ConvertRequest.Sheet(
                                    rows: [new ConvertRequest.Sheet.Row(
                                            columns: [new ConvertRequest.Sheet.Row.Column(data: '')]
                                    )]
                            )]),
                    new ConvertRequest(sheets: [new ConvertRequest.Sheet(
                            name: 'SheetName',
                            rows: [new ConvertRequest.Sheet.Row(
                                    columns: [new ConvertRequest.Sheet.Row.Column(data: '')]
                            )]
                    )])
            ]
    }

    def 'should create minimal xls document with correct headers'() {
        when:
            def response = template.postForEntity('/convert', resource('/converter/minimal.json'), byte[])
        then:
            response.statusCode == HttpStatus.OK
        and:
            response.headers.get('Content-disposition').first() == 'attachment; filename=FileName.xls'
            response.headers.get('charset').first() == 'utf-8'
            response.headers.getContentType() == valueOf("application/vnd.ms-excel")
            response.headers.getContentLength() == response.body.length
    }

    def 'should create minimal xls document with correct data'() {
        when:
            def response = template.postForEntity('/convert', resource('/converter/minimal.json'), byte[])
        then:
            response.statusCode == HttpStatus.OK
        and:
            Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(response.body))
            workbook.numberOfSheets == 1

            def sheet = workbook.getSheet('FirstSheet')
            sheet.physicalNumberOfRows == 1

            def row = sheet.getRow(0)
            row.physicalNumberOfCells == 1

            row.getCell(0).toString() == 'sample'
    }

    private static ConvertRequest resource(String path) {
        return new ObjectMapper().readValue(getClass().getResource(path), ConvertRequest)
    }
}
