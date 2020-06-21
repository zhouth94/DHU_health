package com.dhu.test;

import com.dhu.utils.POIUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.SocketTimeoutException;
import java.util.List;

/**
 * @author zhou
 * @create 2020/5/7
 */
public class POITest {
    //使用POI读取excel文件中的数据
    @Test
    public void test1() throws Exception {
        //加载文件，获取excel对象
        XSSFWorkbook excel = new XSSFWorkbook(new FileInputStream("D:/poi.xlsx"));
        XSSFSheet sheet = excel.getSheetAt(0); //工作表
        for (Row row : sheet) {
            for (Cell cell : row) { //单元格
                System.out.println(cell.getStringCellValue());
            }
        }
        excel.close();
    }

    //使用POI读取excel文件中的数据
    @Test
    public void test2() throws Exception {
        //加载文件，获取excel对象
        XSSFWorkbook excel = new XSSFWorkbook(new FileInputStream("D:/poi.xlsx"));
        XSSFSheet sheet = excel.getSheetAt(0); //工作表
        int lastRowNum = sheet.getLastRowNum(); //获取最后一个行号，注意行号从0开始
        for (int i = 0; i <= lastRowNum; i++) {
            XSSFRow row = sheet.getRow(i);
            short lastCellNum = row.getLastCellNum(); //获取最后一列
            for (int j = 0; j < lastCellNum; j++) {
                XSSFCell cell = row.getCell(j);
                System.out.println(cell.getStringCellValue());
            }
        }
        excel.close();
    }

    //向excel文件写入数据
    @Test
    public void test3() throws Exception {
        //在内存中创建一个excel
        XSSFWorkbook excel = new XSSFWorkbook();
        XSSFSheet sheet = excel.createSheet("东华"); //创建工作表
        XSSFRow title = sheet.createRow(0); //创建行对象
        //在行上创建单元格
        title.createCell(0).setCellValue("姓名");
        title.createCell(1).setCellValue("地址");

        XSSFRow dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue("小米");
        dataRow.createCell(1).setCellValue("北京");

        FileOutputStream fos = new FileOutputStream(new File("D:/hello.xlsx"));
        excel.write(fos);
        fos.flush();

        fos.close();
        excel.close();
    }

 }
