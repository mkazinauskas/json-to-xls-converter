package com.modzo.jsonxlsconverter

import com.fasterxml.jackson.databind.ObjectMapper
import com.modzo.jsonxlsconverter.converter.ConvertRequest
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
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

    def 'should create xls document with correct headers'() {
        when:
            def response = template.postForEntity('/convert', resource('/converter/complex.json'), byte[])
        then:
            response.statusCode == HttpStatus.OK
        and:
            response.headers.get('Content-disposition').first() == 'attachment; filename=Two sheets file.xls'
            response.headers.get('charset').first() == 'utf-8'
            response.headers.getContentType() == valueOf("application/vnd.ms-excel")
            response.headers.getContentLength() == response.body.length
    }

    def 'should create xls with correct document'() {
        when:
            def response = template.postForEntity('/convert', resource('/converter/complex.json'), byte[])
        then:
            response.statusCode == HttpStatus.OK
        and:
            Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(response.body))
            hasTwoSheets(workbook)
            firstSheetHasTwoRows(workbook)
            firstSheetHasTwoRowsWithColumnInside(workbook)
            secondSheetHasOneRow(workbook)
            firstSheetHasRowWithTwoColumnsInside(workbook)
    }

    private static void hasTwoSheets(XSSFWorkbook workbook) {
        assert workbook.numberOfSheets == 2

        assert workbook.getSheet('Two rows sheet') != null
        assert workbook.getSheet('Two columns sheet') != null
    }

    private static void firstSheetHasTwoRows(XSSFWorkbook workbook) {
        assert workbook.getSheetAt(0).physicalNumberOfRows == 2
    }

    private static void firstSheetHasTwoRowsWithColumnInside(XSSFWorkbook workbook) {
        assert workbook.getSheetAt(0).getRow(0).physicalNumberOfCells == 1
        assert workbook.getSheetAt(0).getRow(0).getCell(0).toString() == 'First row column data'
        assert workbook.getSheetAt(0).getRow(1).physicalNumberOfCells == 1
        assert workbook.getSheetAt(0).getRow(1).getCell(0).toString() == 'Second row column data'
    }

    private static void secondSheetHasOneRow(XSSFWorkbook workbook) {
        assert workbook.getSheetAt(1).physicalNumberOfRows == 1
    }

    private static void firstSheetHasRowWithTwoColumnsInside(XSSFWorkbook workbook) {
        assert workbook.getSheetAt(1).getRow(0).physicalNumberOfCells == 2
        assert workbook.getSheetAt(1).getRow(0).getCell(0).toString() == 'First column data'
        assert workbook.getSheetAt(1).getRow(0).getCell(1).toString() == 'Second column data'
    }

    private static ConvertRequest resource(String path) {
        return new ObjectMapper().readValue(getClass().getResource(path), ConvertRequest)
    }
}