package com.modzo.jsonxlsconverter

import com.modzo.jsonxlsconverter.converter.ConvertRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification
import spock.lang.Unroll

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@ContextConfiguration
@SpringBootTest(webEnvironment = RANDOM_PORT)
class IncorrectRequestConverterSpec extends Specification {
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
}