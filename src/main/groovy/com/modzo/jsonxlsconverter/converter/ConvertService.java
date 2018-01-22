package com.modzo.jsonxlsconverter.converter;

import com.modzo.jsonxlsconverter.InternalServerError;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
class ConvertService {

    private static final Logger LOG = LoggerFactory.getLogger(ConvertService.class);

    byte[] convert(ConvertRequest request) {
        Workbook workBook = new XSSFWorkbook();
        request.getSheets().forEach(sheet -> convertSheet(workBook, sheet));
        return asBytes(workBook);
    }

    private void convertSheet(Workbook workbook, ConvertRequest.Sheet sheet) {
        Sheet xlsSheet = workbook.createSheet(sheet.getName());
        sheet.getRows().forEach(row -> convertRow(xlsSheet, row));
    }

    private void convertRow(Sheet sheet, ConvertRequest.Sheet.Row row) {
        Row xlsRow = sheet.createRow(sheet.getLastRowNum());
        row.getColumns().forEach(column -> convertColumn(xlsRow, column));
    }

    private void convertColumn(Row xlsRow, ConvertRequest.Sheet.Row.Column column) {
        Cell cell = xlsRow.createCell(xlsRow.getLastCellNum() + 1, CellType.STRING);
        cell.setCellValue(column.getData());
    }

    private byte[] asBytes (Workbook workbook){
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            workbook.write(output);
            return output.toByteArray();
        } catch (IOException exception) {
            LOG.error("Workbook conversion to byte array failed", exception);
            throw new InternalServerError(exception.getMessage());
        }
    }
}
