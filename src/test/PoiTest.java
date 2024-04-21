package test;

import org.apache.poi.ss.usermodel.*;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;

/**
 * @author ctl
 * @createTime 2023/03/09 17:24
 * @description
 */
public class PoiTest {


    @Test
    public void testPoi() throws Exception {
        InputStream in = new FileInputStream("/Users/chengtianlong/Desktop/表3-已提现公共包代理充值分成明细-202303 - 需提现审核导入处理.xlsx");
        Workbook workbook = WorkbookFactory.create(in);
        Sheet sheet = workbook.getSheetAt(0);
//        System.out.println(sheet.getFirstRowNum());
//        System.out.println(sheet.getLastRowNum());
        Iterator<Row> rowIterator = sheet.rowIterator();
        // 跳过表头
        if (rowIterator.hasNext()) {
            Row row = rowIterator.next();
//            System.out.println(row.getLastCellNum());
        }
//        rowIterator.next();
        if (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (row != null) {
                System.out.println(row.getRowNum());
//                System.out.println(row.getLastCellNum());
                Cell cell = row.getCell(7);
                cell.setCellType(CellType.STRING);
                System.out.println(cell.getStringCellValue());
                cell = row.getCell(5);
                cell.setCellType(CellType.STRING);
                System.out.println(cell.getStringCellValue());
                cell = row.getCell(8);
                cell.setCellType(CellType.STRING);
                System.out.println(cell.getStringCellValue());
                cell = row.getCell(10);
                cell.setCellType(CellType.STRING);
                System.out.println(cell.getStringCellValue());
                cell = row.getCell(11);
                cell.setCellType(CellType.STRING);
                System.out.println(cell.getStringCellValue());
            }
        }

    }

}
