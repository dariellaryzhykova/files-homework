package utils;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

public class Files {
    public static String readTextFromFile(File file) throws IOException {
        return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
    }

    public static String readTextFromPath(String path) throws IOException {
        return FileUtils.readFileToString(getFile(path), StandardCharsets.UTF_8);
    }

    public static String readDocFile(String path) throws IOException {
        FileInputStream fis = new FileInputStream(path);
        HWPFDocument doc = new HWPFDocument(fis);
        WordExtractor we = new WordExtractor(doc);
        String docFileText = we.getText();
        fis.close();
        return docFileText;
    }

    public static String readDocxFile(String path) throws IOException {
        String docFileText = null;
        FileInputStream fis = new FileInputStream(path);
        XWPFDocument doc = new XWPFDocument(fis);
        List<XWPFParagraph> paragraphs = doc.getParagraphs();
        for (XWPFParagraph para : paragraphs) {
            docFileText = docFileText + " " + para.getText();
        }
        fis.close();
        return docFileText;
    }

    public static File getFile(String path) {
        return new File(path);
    }

    public static PDF getPDF(String path) throws IOException {
        return new PDF(getFile(path));
    }

    public static XLS getXLS(String path) throws IOException {
        return new XLS(getFile(path));
    }

    public static String readXLSXFile(String path) {
        {
            String result = "";
            XSSFWorkbook myExcelBook = null;

            try {
                myExcelBook = new XSSFWorkbook(new FileInputStream(path));
            } catch (IOException e) {
                e.printStackTrace();
            }

            XSSFSheet myExcelSheet = myExcelBook.getSheetAt(0);
            Iterator<Row> rows = myExcelSheet.iterator();

            while (rows.hasNext()) {
                Row row = rows.next();
                Iterator<Cell> cells = row.iterator();
                while (cells.hasNext()) {
                    Cell cell = cells.next();
                    CellType cellType = cell.getCellType();
                    //перебираем возможные типы ячеек
                    switch (cellType) {
//                    case Cell.CELL_TYPE_STRING:
//                        result += cell.getStringCellValue() + "=";
//                        break;
//                    case Cell.CELL_TYPE_NUMERIC:
//                        result += "[" + cell.getNumericCellValue() + "]";
//                        break;
//
//                    case Cell.CELL_TYPE_FORMULA:
//                        result += "[" + cell.getNumericCellValue() + "]";
//                        break;
                        default:
                            result += cell.toString();
                            break;
                    }
                }
            }

            try {
                myExcelBook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }
    }
}
