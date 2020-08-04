package com.ktkj.utils.excel;

import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFName;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.*;

public class ExelUtils {

    public static void main(String[] args) throws Exception {
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("导入模板");

        // 第一行
        Row row = sheet.createRow(0);
        CellStyle style = CatalogExcelUtil.getHeadStyle(wb);

        CatalogExcelUtil.initCell(row.createCell(0), style, "第1列列头");
        CatalogExcelUtil.initCell(row.createCell(1), style, "第2列列头");
        CatalogExcelUtil.initCell(row.createCell(2), style, "部门");
        CatalogExcelUtil.initCell(row.createCell(3), style, "层级");
        CatalogExcelUtil.initCell(row.createCell(4), style, "第5列列头");
        CatalogExcelUtil.initCell(row.createCell(5), style, "第6列列头");

        // 设置部门
        String[] departSelectList = new String[] { "刘德华", "张学友", "黎明", "郭富城", "金城武", "梁朝伟" };
        // 第3列的第1行到第21行单元格部门下拉 ，可替换为从数据库的部门表数据，
        // hidden_depart 为隐藏的sheet的别名，1为这个sheet的索引 ，考虑到有多个列绑定下拉列表
        wb = dropDownList2003(wb, sheet, departSelectList, 1, 1000, 2, 2, "hidden_depart", 1);

        // 设置层级
        String[] levelSelectList = new String[] { "科比", "詹姆斯", "库里", "麦迪", "艾弗森" };
        for (int i = 0; i < levelSelectList.length; i++) {
        }
        wb = dropDownList2003(wb, sheet, levelSelectList, 1, 1000, 3, 3, "hidden_level", 2);
        FileOutputStream stream = new FileOutputStream("/Users/luonana/IdeaProjects/qusnaire/qus-sys/src/main/webapp/template/success9.xls");
        wb.write(stream);
        stream.close();
    }

    /**
     * @param wb HSSFWorkbook对象
     * @param realSheet 需要操作的sheet对象
     * @param datas 下拉的列表数据
     * @param startRow 开始行
     * @param endRow 结束行
     * @param startCol 开始列
     * @param endCol 结束列
     * @param hiddenSheetName 隐藏的sheet名
     * @param hiddenSheetIndex 隐藏的sheet索引
     * @return
     * @throws Exception
     */
    public static HSSFWorkbook dropDownList2003(Workbook wb, Sheet realSheet, String[] datas, int startRow, int endRow,
                                                int startCol, int endCol, String hiddenSheetName, int hiddenSheetIndex)
            throws Exception {

        HSSFWorkbook workbook = (HSSFWorkbook) wb;
        // 创建一个数据源sheet
        HSSFSheet hidden = workbook.createSheet(hiddenSheetName);
        // 数据源sheet页不显示
        workbook.setSheetHidden(hiddenSheetIndex, true);
        // 将下拉列表的数据放在数据源sheet上
        HSSFRow row = null;
        HSSFCell cell = null;
        for (int i = 0, length = datas.length; i < length; i++) {
            row = hidden.createRow(i);
            cell = row.createCell(0);
            cell.setCellValue(datas[i]);
        }
        //2016-12-15更新，遇到问题：生成的excel下拉框还是可以手动编辑，不满足
        //HSSFName namedCell = workbook.createName();
        //namedCell.setNameName(hiddenSheetName);
        // A1 到 Adatas.length 表示第一列的第一行到datas.length行，需要与前一步生成的隐藏的数据源sheet数据位置对应
        //namedCell.setRefersToFormula(hiddenSheetName + "!$A$1:$A" + datas.length);
        // 指定下拉数据时，给定目标数据范围 hiddenSheetName!$A$1:$A5   隐藏sheet的A1到A5格的数据
        DVConstraint constraint = DVConstraint.createFormulaListConstraint(hiddenSheetName + "!$A$1:$A" + datas.length);
        CellRangeAddressList addressList = null;
        HSSFDataValidation validation = null;
        row = null;
        cell = null;
        // 单元格样式
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        // 循环指定单元格下拉数据
        for (int i = startRow; i <= endRow; i++) {
            row = (HSSFRow) realSheet.createRow(i);
            cell = row.createCell(startCol);
            cell.setCellStyle(style);
            addressList = new CellRangeAddressList(i, i, startCol, endCol);
            validation = new HSSFDataValidation(addressList, constraint);
            realSheet.addValidationData(validation);
        }

        return workbook;
    }
    public static void dropDownList42007(String filePath)
            throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("下拉列表测试");
        String[] datas = new String[] {"维持","恢复","调整"};
        XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
        XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint) dvHelper
                .createExplicitListConstraint(datas);
        CellRangeAddressList addressList = null;
        XSSFDataValidation validation = null;

        addressList = new CellRangeAddressList(1, 100, 0, 0);
        validation = (XSSFDataValidation) dvHelper.createValidation(
                dvConstraint, addressList);
        // 07默认setSuppressDropDownArrow(true);
        // validation.setSuppressDropDownArrow(true);
        // validation.setShowErrorBox(true);
        sheet.addValidationData(validation);

        FileOutputStream stream = new FileOutputStream(filePath);
        workbook.write(stream);
        stream.close();
        addressList = null;
        validation = null;
    }

}

