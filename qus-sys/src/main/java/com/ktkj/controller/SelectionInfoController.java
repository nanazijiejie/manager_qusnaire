/*
 * 类名称:SelectionInfoController.java
 * 包名称:com.ktkj.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-02-21 22:35:28        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ktkj.annotation.SysLog;
import com.ktkj.cache.J2CacheUtils;
import com.ktkj.entity.*;
import com.ktkj.service.StaffInfoService;
import com.ktkj.service.SysConfigService;
import com.ktkj.util.CommonUtil;
import com.ktkj.utils.PageUtils;
import com.ktkj.utils.R;
import com.ktkj.controller.AbstractController;
import com.ktkj.service.SelectionInfoService;
import com.ktkj.utils.Station;
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

import com.ktkj.utils.Constant;

/**
 * Controller
 *
 * @author lipengjun
 * @date 2020-02-21 22:35:28
 */
@RestController
@RequestMapping("/tower/selectioninfo")
public class SelectionInfoController extends AbstractController {
    @Autowired
    private SelectionInfoService selectionInfoService;
    @Autowired
    private StaffInfoService staffInfoService;
    @Autowired
    private SysConfigService sysConfigService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return R
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("tower:selectioninfo:list")
    public R queryAll(@RequestParam Map<String, Object> params) {
        List<SelectionInfoEntity> list = selectionInfoService.queryAll(params);

        return R.ok().put("list", list);
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return R
     */
    @GetMapping("/list")
    @RequiresPermissions("tower:selectioninfo:list")
    public R list(@RequestParam Map<String, Object> params) {
        if(params.get("selCityId")!=null){
            String selCityId = java.net.URLDecoder.decode(""+params.get("selCityId"));
            params.put("selCityId",selCityId);
        }
        Page page = selectionInfoService.queryPage(params);
        PageUtils pageUtil = new PageUtils(page.getRecords(), (int)page.getTotal(), (int)page.getSize(), (int)page.getCurrent());
        return R.ok().put("page", pageUtil);
    }

    /**
     * 根据主键查询详情
     *
     * @param selId 主键
     * @return R
     */
    @RequestMapping("/info/{selId}")
    @RequiresPermissions("tower:selectioninfo:info")
    public R info(@PathVariable("selId") Integer selId) {
        SelectionInfoEntity selectionInfo = selectionInfoService.getById(selId);

        return R.ok().put("selectioninfo", selectionInfo);
    }

    /**
     * 新增
     *
     * @param
     * @return R

    @SysLog("新增")
    @RequestMapping("/save")
    @RequiresPermissions("tower:selectioninfo:save")
    public R save(@RequestBody SelectionInfoEntity selectionInfo) {

        selectionInfoService.add(selectionInfo);

        return R.ok();
    }*/

    /**
     * 下载投票结果
     * @param response
     * @param request
     * @throws Exception
    */
    @RequestMapping(value="downloadSel", method = RequestMethod.GET)//method = RequestMethod.GET将数据传递给前端
    public void downloadExcel(HttpServletResponse response, HttpServletRequest request)throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        try {
            List<FinalSelRetEntity> list = selectionInfoService.calSel();
            String tmpSelStationId = "";
            String tmpSelStation = "";
            List<FinalSelRetEntity> sheetList = new ArrayList<FinalSelRetEntity>();
            Set<String> colNameSet = new LinkedHashSet<String>();
            int sheetIndex = 0;
            Map<String, Object> params2 = new HashMap<String, Object>();
            params2.put("key", "REPORT_SEL_DOWNLOAD_PATH");
            List<SysConfigEntity> configList = sysConfigService.queryList(params2);
            String finalXlsxPath = configList.get(0).getValue();
            clearExel(finalXlsxPath);
            for (FinalSelRetEntity entity : list) {
                String selStationId = entity.getSelStationId();
                String selStation = entity.getSelStation();
                if (StringUtils.isNotEmpty(tmpSelStationId)&&!tmpSelStationId.equals(selStationId)) {//不相等另起一个exel
                    if (sheetList != null && sheetList.size() != 0) {
                        writeExcel(sheetList, finalXlsxPath, colNameSet,sheetIndex, tmpSelStation,tmpSelStationId);
                        colNameSet = new HashSet<String>();
                        sheetList = new ArrayList<FinalSelRetEntity>();
                        sheetIndex++;
                    }
                    colNameSet = new LinkedHashSet<String>();
                }
                Map<String,Integer> qusInfo= entity.getQusInfo();
                Set keySet = qusInfo.keySet();
                Iterator it = keySet.iterator();
                while(it.hasNext()){
                    colNameSet.add((String)it.next());
                }
                sheetList.add(entity);
                tmpSelStationId = selStationId;
                tmpSelStation = selStation;
            }
            writeExcel(sheetList, finalXlsxPath, colNameSet,sheetIndex, tmpSelStation,tmpSelStationId);
            //导出exel
            //获取输入流，原始模板位置
            InputStream bis = new BufferedInputStream(new FileInputStream(new File(finalXlsxPath)));
            //假如以中文名下载的话，设置下载文件名称
            String filename = "final_selection.xlsx";
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
    public void writeExcel(List<FinalSelRetEntity> dataList,
                                  String finalXlsxPath,Set<String>columnNameSet,Integer sheetIndex,String exelName,String selStationId){
        OutputStream out = null;
        try {
            Map selCountMap = new HashMap<String,Object>();
            selCountMap.put("selStationId",selStationId);
            selCountMap.put("selStaffId","99999999");
            Integer fpCount = selectionInfoService.selCountByStation(selCountMap);
            selCountMap.put("selStaffId","0");
            Integer kpCount = selectionInfoService.selCountByStation(selCountMap);
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
            third.setCellValue("被推荐人");
            Cell four = columnRow.createCell(1);
            four.setCellValue("单位");
            Cell first = columnRow.createCell(2);
            first.setCellValue("职务");
            Cell second = columnRow.createCell(3);
            second.setCellValue("总票数");
            Cell five = columnRow.createCell(4);
            five.setCellValue("总得票数");
            Cell six = columnRow.createCell(5);
            six.setCellValue("得票比例");

            int colunmIndex = 6;
            while (iterator.hasNext()) {
                String columnName = iterator.next().toString();
                Cell cell = columnRow.createCell(colunmIndex);
                cell.setCellValue(columnName);
                colunmIndex++;

            }
            /**
             * 往Excel中写新数据
             */
            String tmpStaffId  = "9999999999";
            Row row = sheet.createRow(1);
            Integer rowCount = 2;
            List<FinalSelRetEntity> rowData = new ArrayList<FinalSelRetEntity>();
            for (int j = 0; j < dataList.size(); j++) {
                // 得到要插入的每一条记录
                FinalSelRetEntity entity = dataList.get(j);
                if(j!=0&&!tmpStaffId.equals(entity.getStaffId())){
                    if(rowData!=null&&rowData.size()!=0){
                        colunmIndex = 6;
                        Cell cell1 = row.createCell(0);
                        cell1.setCellValue(rowData.get(0).getStaffName());
                        Cell cell2 = row.createCell(1);
                        cell2.setCellValue(rowData.get(0).getCity());
                        Cell cell3 = row.createCell(2);
                        cell3.setCellValue(rowData.get(0).getStation());
                        Cell cell4 = row.createCell(3);
                        cell4.setCellValue(rowData.get(0).getTotalCount());
                        Cell cell5 = row.createCell(4);
                        cell5.setCellValue(rowData.get(0).getTotalGet());
                        Cell cell6 = row.createCell(5);
                        cell6.setCellValue(rowData.get(0).getPercentGet());
                        Map<String,Integer> qusInfo = rowData.get(0).getQusInfo();
                        Iterator it = columnNameSet.iterator();
                        while(it.hasNext()){
                            Cell cell = row.createCell(colunmIndex++);
                            String key = (String)it.next();
                            Integer value = 0;
                            if(qusInfo.get(key)!=null){
                                value = qusInfo.get(key);
                            }
                            cell.setCellValue(value);
                        }
                        row = sheet.createRow(rowCount++);
                        rowData = new ArrayList<FinalSelRetEntity>();
                    }
                }
                rowData.add(entity);
                tmpStaffId = entity.getStaffId();
            }
            if(rowData!=null&&rowData.size()!=0){
                colunmIndex = 6;
                Cell cell1 = row.createCell(0);
                cell1.setCellValue(rowData.get(0).getStaffName());
                Cell cell2 = row.createCell(1);
                cell2.setCellValue(rowData.get(0).getCity());
                Cell cell3 = row.createCell(2);
                cell3.setCellValue(rowData.get(0).getStation());
                Cell cell4 = row.createCell(3);
                cell4.setCellValue(rowData.get(0).getTotalCount());
                Cell cell5 = row.createCell(4);
                cell5.setCellValue(rowData.get(0).getTotalGet());
                Cell cell6 = row.createCell(5);
                cell6.setCellValue(rowData.get(0).getPercentGet());
                Map<String,Integer> qusInfo = rowData.get(0).getQusInfo();
                Iterator it = columnNameSet.iterator();
                while(it.hasNext()){
                    Cell cell = row.createCell(colunmIndex++);
                    String key = (String)it.next();
                    Integer value = 0;
                    if(qusInfo.get(key)!=null){
                        value = qusInfo.get(key);
                    }
                    cell.setCellValue(value);
                }
            }
            row = sheet.createRow(rowCount++);
            Cell cellCount1 = row.createCell(0);
            cellCount1.setCellValue("空票："+kpCount);
            Cell cellCount2 = row.createCell(1);
            cellCount2.setCellValue("废票："+fpCount);
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

    @RequestMapping("/save")
    @ResponseBody
    //@RequiresPermissions("tower:examindexrel:save")
    public R save(HttpServletRequest request,
                  HttpServletResponse response) {
        List<SelectionInfoEntity> retList = new ArrayList<SelectionInfoEntity>();
        String scoreListStr = ""+request.getParameter("selListStr");
        String token = request.getParameter("token");
        StaffInfoEntity staffInfo = null;
        if(StringUtils.isEmpty(token)|| J2CacheUtils.get(token)!=null){
            Integer staffId = (Integer)J2CacheUtils.get(token);
            staffInfo = staffInfoService.getById(staffId);
        }else{
            return R.error("您未登录或已失效，请重新登录！");
        }
        try{
            List<SelectionInfoEntity> list = JSON.parseArray(scoreListStr,SelectionInfoEntity.class);
            for (SelectionInfoEntity selectionInfoEntity:list
            ) {
                selectionInfoEntity.setQusCity(staffInfo.getCity());
                selectionInfoEntity.setQusCityId(staffInfo.getCityId());
                selectionInfoEntity.setQusDept(staffInfo.getDept());
                selectionInfoEntity.setQusName(staffInfo.getStaffName());
                selectionInfoEntity.setQusStation(staffInfo.getStation());
                selectionInfoEntity.setQusStationId(staffInfo.getStationId());
                selectionInfoEntity.setCreateTime(new Date());
            }
            selectionInfoService.saveBatch(list);
            //将员工的填写状态更新
            staffInfo.setIsSelSubmit("1");//更新为提交状态
            staffInfoService.update(staffInfo);
            //examScoreInfoService.insertBatch(list,staffInfo);
        }catch (Exception e){
            e.printStackTrace();
            return R.error("投票失败，请联系管理员");
        }
        return R.ok();
    }

    /**
     * 修改
     *
     * @param selectionInfo selectionInfo
     * @return R
     */
    @SysLog("修改")
    @RequestMapping("/update")
    @RequiresPermissions("tower:selectioninfo:update")
    public R update(@RequestBody SelectionInfoEntity selectionInfo) {

        selectionInfoService.update(selectionInfo);

        return R.ok();
    }

    /**
     * 根据主键删除
     *
     * @param selIds selIds
     * @return R
     */
    @SysLog("删除")
    @RequestMapping("/delete")
    @RequiresPermissions("tower:selectioninfo:delete")
    public R delete(@RequestBody Integer[] selIds) {
        selectionInfoService.deleteBatch(selIds);

        return R.ok();
    }


}
