package test;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * @author ctl
 * @date 2020/3/20
 */
public class ExcelTest {

    @Test
    public void testImport() throws IOException, BiffException {
        // jxl 只支持xls版本，03及以下
        File file = new File("C:\\Users\\ctl\\Desktop\\test.xls");
        System.out.println(file.exists());
        System.out.println(file.isFile());
        Workbook workbook = Workbook.getWorkbook(file);
        Sheet sheet = workbook.getSheet(0);
        int rows = sheet.getRows();
        System.out.println("rows: " + rows);
        for (int i = 0; i < 8; i++) {
            Cell cell = sheet.getCell(0, i);
            String userIdStr = cell.getContents();
            System.out.println(userIdStr);
            System.out.println(cell.getType());
            System.out.println(cell.getCellFormat());
        }
        workbook.close();
    }

}
