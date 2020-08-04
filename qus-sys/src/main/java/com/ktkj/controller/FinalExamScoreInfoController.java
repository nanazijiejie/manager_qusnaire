/*
 * 类名称:FinalExamScoreInfoController.java
 * 包名称:com.ktkj.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-10-24 11:58:43        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ktkj.annotation.SysLog;
import com.ktkj.entity.SysConfigEntity;
import com.ktkj.service.SysConfigService;
import com.ktkj.util.CommonUtil;
import com.ktkj.utils.Constant;
import com.ktkj.utils.PageUtils;
import com.ktkj.utils.Query;
import com.ktkj.utils.R;
import com.ktkj.controller.AbstractController;
import com.ktkj.entity.FinalExamScoreInfoEntity;
import com.ktkj.service.FinalExamScoreInfoService;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;


/**
 * Controller
 *
 * @author lipengjun
 * @date 2019-10-24 11:58:43
 */
@RestController
@RequestMapping("/tower/finalexamscoreinfo")
public class FinalExamScoreInfoController extends AbstractController {
    @Autowired
    private FinalExamScoreInfoService finalExamScoreInfoService;
    @Autowired
    private SysConfigService sysConfigService;

    //String finalXlsxPath = "/Users/luonana/IdeaProjects/qusnaire/qus-sys/src/main/webapp/template/final_score.xlsx";

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return R
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("tower:finalexamscoreinfo:list")
    public R queryAll(@RequestParam Map<String, Object> params) {
        List<FinalExamScoreInfoEntity> list = finalExamScoreInfoService.queryAll(params);

        return R.ok().put("list", list);
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return R
     */
    @GetMapping("/list")
    @RequiresPermissions("tower:finalexamscoreinfo:list")
    public R list(@RequestParam Map<String, Object> params) {
        if(params.get("examName")!=null){
            String examName = java.net.URLDecoder.decode(""+params.get("examName"));
            params.put("examName",examName);
        }
        Page page = finalExamScoreInfoService.queryPage(params);

        PageUtils pageUtil = new PageUtils(page.getRecords(), (int)page.getTotal(), (int)page.getSize(), (int)page.getCurrent());

        return R.ok().put("page", pageUtil);
    }


    /**
     * 根据主键查询详情
     *
     * @param examResultId 主键
     * @return R
     */
    @RequestMapping("/info/{examResultId}")
    @RequiresPermissions("tower:finalexamscoreinfo:info")
    public R info(@PathVariable("examResultId") Integer examResultId) {
        FinalExamScoreInfoEntity finalExamScoreInfo = finalExamScoreInfoService.getById(examResultId);

        return R.ok().put("finalexamscoreinfo", finalExamScoreInfo);
    }

    /**
     * 新增
     *
     * @param finalExamScoreInfo finalExamScoreInfo
     * @return R
     */
    @SysLog("新增")
    @RequestMapping("/save")
    @RequiresPermissions("tower:finalexamscoreinfo:save")
    public R save(@RequestBody FinalExamScoreInfoEntity finalExamScoreInfo) {

        finalExamScoreInfoService.add(finalExamScoreInfo);

        return R.ok();
    }

    /**
     * 修改
     *
     * @param finalExamScoreInfo finalExamScoreInfo
     * @return R
     */
    @SysLog("修改")
    @RequestMapping("/update")
    @RequiresPermissions("tower:finalexamscoreinfo:update")
    public R update(@RequestBody FinalExamScoreInfoEntity finalExamScoreInfo) {

        finalExamScoreInfoService.update(finalExamScoreInfo);

        return R.ok();
    }

    /**
     * 根据主键删除
     *
     * @param examResultIds examResultIds
     * @return R
     */
    @SysLog("删除")
    @RequestMapping("/delete")
    @RequiresPermissions("tower:finalexamscoreinfo:delete")
    public R delete(@RequestBody Integer[] examResultIds) {
        finalExamScoreInfoService.deleteBatch(examResultIds);

        return R.ok();
    }

    /**
     * 下载考核结果
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping(value="downloadScore", method = RequestMethod.GET)//method = RequestMethod.GET将数据传递给前端
    public void downloadExcel(HttpServletResponse response, HttpServletRequest request)throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        try {
            String cityId = getUser().getCityId();
            //查询数据库中的考核结果
            if(StringUtils.isNotEmpty(cityId)&&!"Z".equals(cityId)) {
                params.put("cityId", cityId);
            }
            List<FinalExamScoreInfoEntity> list = finalExamScoreInfoService.queryAll(params);
            String tmpStation = "";
            List<FinalExamScoreInfoEntity> sheetList = new ArrayList<FinalExamScoreInfoEntity>();
            Set<String> colNameSet = new LinkedHashSet<String>();
            int sheetIndex = 0;
            String qusStationName = "";
            String tmpExamStationName = "";
            Map<String, Object> params2 = new HashMap<String, Object>();
            params2.put("key", Constant.REPORT_DOWNLOAD_PATH);
            List<SysConfigEntity> configList = sysConfigService.queryList(params2);
            String finalXlsxPath = configList.get(0).getValue();
            clearExel(finalXlsxPath);
            for (FinalExamScoreInfoEntity entity : list) {
                String examStation = entity.getExamStation();
                String examStationName = entity.getExamStationName();//exel名称
                qusStationName = entity.getQusNaireStationName();
                Integer staffIfd = entity.getExamStaffId();//员工ID
                if (StringUtils.isNotEmpty(tmpStation)&&!tmpStation.equals(examStation)) {//不相等另起一个exel
                    if (sheetList != null && sheetList.size() != 0) {
                        writeExcel(sheetList, finalXlsxPath, colNameSet, sheetIndex, cityId, tmpExamStationName);
                        colNameSet = new HashSet<String>();
                        sheetList = new ArrayList<FinalExamScoreInfoEntity>();
                        sheetIndex++;
                    }
                    colNameSet = new LinkedHashSet<String>();
                }
                colNameSet.add(qusStationName);
                sheetList.add(entity);
                tmpStation = examStation;
                tmpExamStationName = examStationName;
            }
            writeExcel(sheetList, finalXlsxPath, colNameSet, sheetIndex, cityId, tmpExamStationName);
            //导出exel
            //获取输入流，原始模板位置
            InputStream bis = new BufferedInputStream(new FileInputStream(new File(finalXlsxPath)));
            //假如以中文名下载的话，设置下载文件名称
            String filename = "final_score.xlsx";
            //转码，免得文件名中文乱码
            filename = URLEncoder.encode(filename, "UTF-8");
            //设置文件下载头
            response.addHeader("Content-Disposition", "attachment;filename=" + filename);
            //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
            response.setContentType("multipart/form-data");
            BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
            int len = 0;
            while ((len = bis.read()) != -1) {
                out.write(len);
                out.flush();
            }
            out.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public static void clearExel(String finalXlsxPath){
        OutputStream out = null;
        try {
            File finalXlsxFile = new File(finalXlsxPath);
            Workbook workBook = getWorkbok(finalXlsxFile);
            int sheetNum = workBook.getNumberOfSheets();
            for (int i=0;i<sheetNum;i++){
                // sheet 对应一个工作页
                Sheet sheet = workBook.getSheetAt(i);
                workBook.setSheetName(i, CommonUtil.generateOrderNumber());
                /**
                 * 删除原有数据
                 */
                int rowNumber = sheet.getLastRowNum();    // 第一行从0开始算
                System.out.println("原始数据总行数，除属性列：" + rowNumber);
                for (int j = rowNumber; j >=0; j--) {
                    Row row = sheet.getRow(j);
                    if(row!=null){
                        sheet.removeRow(row);
                    }
                }
            }
            out =  new FileOutputStream(finalXlsxPath);
            workBook.write(out);
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            try {
                if(out != null){
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public static void writeExcel(List<FinalExamScoreInfoEntity> dataList,
                                  String finalXlsxPath,Set<String>columnNameSet,Integer sheetIndex,String cityId,String exelName){
        OutputStream out = null;
        try {
            // 获取总列数
            int columnNumCount = columnNameSet.size();
            // 读取Excel文档
            File finalXlsxFile = new File(finalXlsxPath);
            Workbook workBook = getWorkbok(finalXlsxFile);
            // sheet 对应一个工作页
            Sheet sheet = workBook.getSheetAt(sheetIndex);
            workBook.setSheetName(sheetIndex, exelName.replace("/","、"));
            // 创建文件输出流，输出电子表格：这个必须有，否则你在sheet上做的任何操作都不会有效
            out =  new FileOutputStream(finalXlsxPath);
            workBook.write(out);
            /**
             * 往Excel中写列名
             */
            Row columnRow = sheet.createRow(0);
            Iterator iterator = columnNameSet.iterator();
            Cell third = columnRow.createCell(0);
            third.setCellValue("归属单位");
            Cell four = columnRow.createCell(1);
            four.setCellValue("归属部门");
            Cell first = columnRow.createCell(2);
            first.setCellValue("职务");
            Cell second = columnRow.createCell(3);
            second.setCellValue("姓名");

            int colunmIndex = 4;
            if(StringUtils.isEmpty(cityId)) {
                while (iterator.hasNext()) {
                    String columnName = iterator.next().toString();
                    Cell cell = columnRow.createCell(colunmIndex);
                    cell.setCellValue(columnName);
                    colunmIndex++;

                }
            }
            Cell last = columnRow.createCell(colunmIndex);
            last.setCellValue("胜任力考核得分");
            /**
             * 往Excel中写新数据
             */
            Integer tmpStaffId  = 99999999;
            Row row = sheet.createRow(1);
            Integer rowCount = 2;
            List<FinalExamScoreInfoEntity> rowData = new ArrayList<FinalExamScoreInfoEntity>();
            for (int j = 0; j < dataList.size(); j++) {
                // 得到要插入的每一条记录
                FinalExamScoreInfoEntity entity = dataList.get(j);
                if(j!=0&&!tmpStaffId.equals(entity.getExamStaffId())){
                    if(rowData!=null&&rowData.size()!=0){
                        colunmIndex = 4;
                        Double totalFinalScore = 0d;
                        Cell cell1 = row.createCell(0);
                        cell1.setCellValue(rowData.get(0).getExamCity());
                        Cell cell2 = row.createCell(1);
                        cell2.setCellValue(rowData.get(0).getExamDept());
                        Cell cell3 = row.createCell(2);
                        cell3.setCellValue(rowData.get(0).getExamStationName());
                        Cell cell4 = row.createCell(3);
                        cell4.setCellValue(rowData.get(0).getExamName());

                        for (int k = 0; k < rowData.size(); k++) {
                            if (StringUtils.isEmpty(cityId)) {
                                Cell cell = row.createCell(colunmIndex++);
                                cell.setCellValue(""+rowData.get(k).getFinalScore());
                            }
                            if (StringUtils.isNotEmpty(""+rowData.get(k).getFinalScore())) {
                                totalFinalScore += Double.valueOf(""+rowData.get(k).getFinalScore());
                            }
                        }
                        //合计
                        Cell finalScoreCell = row.createCell(colunmIndex);
                        finalScoreCell.setCellValue(totalFinalScore);
                        row = sheet.createRow(rowCount++);
                        rowData = new ArrayList<FinalExamScoreInfoEntity>();
                    }
                }
                rowData.add(entity);
                tmpStaffId = entity.getExamStaffId();
            }
            if(rowData!=null&&rowData.size()!=0){
                colunmIndex = 4;
                Double totalFinalScore = 0d;
                Cell cell1 = row.createCell(0);
                cell1.setCellValue(rowData.get(0).getExamCity());
                Cell cell2 = row.createCell(1);
                cell2.setCellValue(rowData.get(0).getExamDept());
                Cell cell3 = row.createCell(2);
                cell3.setCellValue(rowData.get(0).getExamStationName());
                Cell cell4 = row.createCell(3);
                cell4.setCellValue(rowData.get(0).getExamName());

                for (int k = 0; k < rowData.size(); k++) {
                    if (StringUtils.isEmpty(cityId)) {
                        Cell cell = row.createCell(colunmIndex++);
                        cell.setCellValue(""+rowData.get(k).getFinalScore());
                    }
                    if (StringUtils.isNotEmpty(""+rowData.get(k).getFinalScore())) {
                        totalFinalScore += Double.valueOf(""+rowData.get(k).getFinalScore());
                    }
                }
                //合计
                Cell finalScoreCell = row.createCell(colunmIndex);
                finalScoreCell.setCellValue(totalFinalScore);
            }
            for(int i=0;i<colunmIndex;i++){
                sheet.autoSizeColumn(colunmIndex);
            }
            // 创建文件输出流，准备输出电子表格：这个必须有，否则你在sheet上做的任何操作都不会有效
            out =  new FileOutputStream(finalXlsxPath);
            workBook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            try {
                if(out != null){
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("数据导出成功");
    }

    /**
     * 判断格式来获取sheet
     * @param file
     * @return
     * @throws IOException
     */
    public static Workbook getWorkbok(File file) throws IOException {
        Workbook wb = null;
        FileInputStream in = new FileInputStream(file);
        if(file.getName().endsWith("xls")){     //Excel&nbsp;2003
            wb = new HSSFWorkbook(in);
        }else if(file.getName().endsWith("xlsx")){    // Excel 2007/2010
            wb = new XSSFWorkbook(in);
        }
        return wb;
    }

}
