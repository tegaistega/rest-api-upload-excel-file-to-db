package com.fileupload.upload.utils;

import com.fileupload.upload.model.File;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelUtil {

    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERs = { "Id", "FileName", "FileType", "Data" };

    public static boolean hasExcelFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }

    public static List excelToFiles(InputStream inputStream){

        try {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            List fileList = new ArrayList();

            int rowNumber = 0;
            while (rows.hasNext()){
                Row currentRow = rows.next();
                if (rowNumber==0){
                    rowNumber++;
                    continue;
                }
                Iterator <Cell> cellInRow = currentRow.iterator();
                File file = new File();

                int cellIndex = 0;
                while (cellInRow.hasNext()){
                    Cell currentCell = cellInRow.next();
                    if (cellIndex == 0){
                        file.setId(currentCell.getStringCellValue());
                    }else if (cellIndex == 1){
                        file.setFileName(currentCell.getStringCellValue());
                    }else if (cellIndex == 2){
                        file.setFileType(currentCell.getStringCellValue());
                    }
                    cellIndex++;
                }
                fileList.add(file);
            }
            workbook.close();
            return fileList;
        }catch (IOException ioException){
            throw new RuntimeException("failed to parse excel file " + ioException.getMessage());
        }
    }

    public static ByteArrayInputStream filesToExcel(List<File> files) {

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.createSheet();

            // Header
            Row headerRow = sheet.createRow(0);

            for (int col = 0; col < HEADERs.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERs[col]);
            }

            int rowIdx = 1;
            for (File file : files) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(file.getId());
                row.createCell(1).setCellValue(file.getFileName());
                row.createCell(2).setCellValue(file.getFileType());
                row.createCell(3).setCellValue(file.getFileDescription());
                row.createCell(4).setCellValue(file.isPublished());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }

}
