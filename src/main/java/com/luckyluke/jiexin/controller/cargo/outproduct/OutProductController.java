package com.luckyluke.jiexin.controller.cargo.outproduct;

import com.luckyluke.jiexin.controller.BaseController;
import com.luckyluke.jiexin.service.OutProductVOService;
import com.luckyluke.jiexin.util.DownloadUtil;
import com.luckyluke.jiexin.vo.OutProductVO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * Created by Luke Wang on 2018/8/16.
 */
@Controller
public class OutProductController extends BaseController {
    @Autowired
    OutProductVOService outProductVOService;

    //跳转到打印页面
    @RequestMapping("/cargo/outproduct/toedit.action")
    public String toprint() {
        return "/cargo/outproduct/jOutProduct.jsp";
    }

    //打印出货表
    @RequestMapping("/cargo/outproduct/print.action")
    public void print(String inputDate, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //Linux下jdk1.8 getRealPath方法获取时，不会拼接自己写的目录
        String path = request.getSession().getServletContext().getRealPath("/") + "/make/xlsprint/";
        InputStream is = new FileInputStream(new File(path + "tOUTPRODUCT.xlsx"));
        //打开一个模板文件，作为工作簿
        Workbook wb = new XSSFWorkbook(is);
        //获得第一个工作表
        Sheet sheet = wb.getSheetAt(0);

        Row nRow = null;
        Cell nCell = null;
        int rowNo = 0;  //行号
        int colNo = 1;  //列号

        //获取模板上的单元格样式
        nRow = sheet.getRow(2);

        //客户的样式
        nCell = nRow.getCell(1);
        CellStyle customStyle = nCell.getCellStyle();
        //订单号的样式
        nCell = nRow.getCell(2);
        CellStyle contractNoStyle = nCell.getCellStyle();
        //货号的样式
        nCell = nRow.getCell(3);
        CellStyle productNoStyle = nCell.getCellStyle();
        //数量的样式
        nCell = nRow.getCell(4);
        CellStyle numStyle = nCell.getCellStyle();
        //生产厂家的样式
        nCell = nRow.getCell(5);
        CellStyle factoryStyle = nCell.getCellStyle();
        //日期的样式
        nCell = nRow.getCell(6);
        CellStyle dateStyle = nCell.getCellStyle();
        //贸易条款的样式
        nCell = nRow.getCell(8);
        CellStyle tradeStyle = nCell.getCellStyle();

        int var22 = rowNo + 1;
        nRow = sheet.getRow(rowNo);
        nCell = nRow.getCell(colNo);
        //对日期格式化输出
        nCell.setCellValue(inputDate.replaceFirst("-0", "-").replaceFirst("-", "年") + "月份出货表");
        ++var22;
        List<OutProductVO> dataList = this.outProductVOService.find(inputDate);

        for (int j = 0; j < dataList.size(); ++j) {
            colNo = 1;
            OutProductVO op = dataList.get(j);
            nRow = sheet.createRow(var22++);
            nRow.setHeightInPoints(24.0F);
            int var23 = colNo + 1;
            nCell = nRow.createCell(colNo);
            nCell.setCellValue(op.getCustomName());
            nCell.setCellStyle(customStyle);
            nCell = nRow.createCell(var23++);
            nCell.setCellValue(op.getContractNo());
            nCell.setCellStyle(contractNoStyle);
            nCell = nRow.createCell(var23++);
            nCell.setCellValue(op.getProductNo());
            nCell.setCellStyle(productNoStyle);
            nCell = nRow.createCell(var23++);
            nCell.setCellValue(op.getCnumber());
            nCell.setCellStyle(numStyle);
            nCell = nRow.createCell(var23++);
            nCell.setCellValue(op.getFactoryName());
            nCell.setCellStyle(factoryStyle);
            nCell = nRow.createCell(var23++);
            nCell.setCellValue(op.getDeliveryPeriod());
            nCell.setCellStyle(dateStyle);
            nCell = nRow.createCell(var23++);
            nCell.setCellValue(op.getShipTime());
            nCell.setCellStyle(dateStyle);
            nCell = nRow.createCell(var23++);
            nCell.setCellValue(op.getTradeTerms());
            nCell.setCellStyle(tradeStyle);
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        wb.write(os);
        DownloadUtil downloadUtil = new DownloadUtil();
        downloadUtil.download(os, response, "出货表.xlsx");
        os.flush();
        os.close();
    }
}