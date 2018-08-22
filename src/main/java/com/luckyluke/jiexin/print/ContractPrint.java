package com.luckyluke.jiexin.print;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.swing.*;

import javafx.scene.layout.BorderWidths;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.luckyluke.jiexin.vo.ContractProductVO;
import com.luckyluke.jiexin.vo.ContractVO;
import com.luckyluke.util.DownloadUtil;
import com.luckyluke.util.UtilFuns;
import com.luckyluke.util.file.PoiUtil;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * @Description: 手工POI写excel文件
 * @Author:	nutony
 * @Company:	http://java.itcast.cn
 * @CreateDate:	2014-4-2
 */
public class ContractPrint{
	
	
	/*步骤思路：
	* 一、 将每页需要打印的 字段 封装到 pageMap 中
	* 二、 循环设置每页 字段 的 值&样式
	* */
	public void print(ContractVO contract,String path, HttpServletResponse response) throws Exception{
		//相同厂家的信息一起打印
		List<ContractProductVO> oList = contract.getContractProducts();
		UtilFuns utilFuns = new UtilFuns();
		String tempXlsFile = path + "make/xlsprint/tCONTRACT.xls";		//获取模板文件，该模板只包含列的宽度设置
		
		//填写每页的内容，之后在循环每页读取打印
		Map<String,String> pageMap = null;              //pageMap 代表的是一页的打印信息
		List<Map> pageList = new ArrayList();			//打印页，pageList 代表的是一次打印的所有也的集合，即pageMap的集合
		
		ContractProductVO oProduct = null;
		String stars = "";
		for(int j=0;j<contract.getImportNum();j++){		//重要程度
			stars += "★";
		}
		
		String oldFactory = "";
		for(int i=0;i<oList.size();i++){
			oProduct = oList.get(i);	//获得货物
			pageMap = new HashMap();	//每页的内容
			
			pageMap.put("Offeror", "收 购 方：" + contract.getOfferor());
			pageMap.put("Factory", "生产工厂：" + oProduct.getFactory().getFactoryName());
			pageMap.put("ContractNo", "合 同 号：" + contract.getContractNo());
			pageMap.put("Contractor", "联 系 人：" + oProduct.getFactory().getContacts());
			pageMap.put("SigningDate", "签单日期："+UtilFuns.formatDateTimeCN(UtilFuns.dateTimeFormat(contract.getSigningDate())));
			pageMap.put("Phone", "电    话：" + oProduct.getFactory().getPhone());
			pageMap.put("InputBy", "制单：" + contract.getInputBy());
			pageMap.put("CheckBy", "审单："+ utilFuns.fixSpaceStr(contract.getCheckBy(),26)+"验货员："+utilFuns.convertNull(contract.getInspector()));
			pageMap.put("Remark", "  "+contract.getRemark());
			pageMap.put("Request", "  "+contract.getCrequest());
			
			pageMap.put("ProductImage", oProduct.getProductImage());
			pageMap.put("ProductDesc", oProduct.getProductDesc());
			pageMap.put("Cnumber", String.valueOf(oProduct.getCnumber().doubleValue()));
			//判断单位
			if(oProduct.getPackingUnit().equals("PCS")){
				pageMap.put("PackingUnit", "只");
			}else if(oProduct.getPackingUnit().equals("SETS")){
				pageMap.put("PackingUnit", "套");
			}
			pageMap.put("Price", String.valueOf(oProduct.getPrice().doubleValue()));
			pageMap.put("ProductNo", oProduct.getProductNo());
			
			oldFactory = oProduct.getFactory().getFactoryName();

			//判断一页是否是打印两条货物信息
			if(contract.getPrintStyle().equals("2")){
				i++;	//读取第二个货物信息
				if(i<oList.size()){
					oProduct = oList.get(i);
					
					if(oProduct.getFactory().getFactoryName().equals(oldFactory)){	//厂家不同另起新页打印，除去第一次的比较
						//以下是有别于第一个货物信息的属性，其余的同第一个
						pageMap.put("ProductImage2", oProduct.getProductImage());
						pageMap.put("ProductDesc2", oProduct.getProductDesc());
						pageMap.put("Cnumber2", String.valueOf(oProduct.getCnumber().doubleValue()));
						if(oProduct.getPackingUnit().equals("PCS")){
							pageMap.put("PackingUnit2", "只");
						}else if(oProduct.getPackingUnit().equals("SETS")){
							pageMap.put("PackingUnit2", "套");
						}						
						pageMap.put("Price2", String.valueOf(oProduct.getPrice().doubleValue()));
						//pageMap.put("Amount2", String.valueOf(oProduct.getAmount().doubleValue()));			//在excel中金额采用公式，所以无需准备数据
						pageMap.put("ProductNo2", oProduct.getProductNo());
					}else{
						i--;	//tip:list退回
					}
				}else{
					pageMap.put("ProductNo2", null);	//后面依据此判断是否有第二个货物
				}
			}
			
			pageMap.put("ContractDesc", stars+" 货物描述");			//重要程度 + 货物描述
			
			pageList.add(pageMap);
		}
		
		int cellHeight = 96;	//一个货物的高度			用户需求，一个货物按192高度打印，后来又嫌难看，打印高度和2款高度一样。
//		if(contract.getPrintStyle().equals("2")){
//			cellHeight = 96;	//两个货物的高度
//		}
		
		PoiUtil poiUtil = new PoiUtil();
		HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(tempXlsFile));	//打开excel文件
		HSSFFont defaultFont10 = poiUtil.defaultFont10(wb);		//设置字体
		HSSFFont defaultFont12 = poiUtil.defaultFont12(wb);		//设置字体
		HSSFFont blackFont = poiUtil.blackFont12(wb);			//设置字体
		Short rmb2Format = poiUtil.rmb2Format(wb);				//设置格式
		Short rmb4Format = poiUtil.rmb4Format(wb);				//设置格式
		

		HSSFSheet sheet = wb.getSheetAt(0);				//选择第一个工作簿
		wb.setSheetName(0, "购销合同");					//设置工作簿的名称


		//sheet.setDefaultColumnWidth((short) 20); 		// 设置每列默认宽度
		
//		POI分页符有BUG，必须在模板文件中插入一个分页符，然后再此处删除预设的分页符；最后在下面重新设置分页符。
//		sheet.setAutobreaks(false);
//		int iRowBreaks[] = sheet.getRowBreaks();
//		sheet.removeRowBreak(3);
//		sheet.removeRowBreak(4);
//		sheet.removeRowBreak(5);
//		sheet.removeRowBreak(6);
		
		CellRangeAddress region = null;
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();		//add picture

		HSSFRow nRow = null;
		HSSFCell nCell   = null;
		int curRow = 0;
		
		//打印每页
		Map<String,String> printMap = null;
		for(int p=0;p<pageList.size();p++){
			printMap = pageList.get(p);
			
			if(p>0){
				sheet.setRowBreak(curRow++);	//在第startRow行设置分页符
			}
			
			
			//设置logo图片
			poiUtil.setPicture(wb, patriarch, path+"make/xlsprint/logo.jpg", curRow, 2, curRow+4, 2);
			
			//header设置：“SHAANXI”
			nRow = sheet.createRow(curRow++);//0，1
			nRow.setHeightInPoints(21);
			nCell   = nRow.createCell((3));
			nCell.setCellValue("SHAANXI");
			nCell.setCellStyle(headStyle(wb));  //调用方法设置其样式

			//header设置："     JK INTERNATIONAL "
			nRow = sheet.createRow(curRow++);//1，2
			nRow.setHeightInPoints(41);
			nCell   = nRow.createCell((3));
			nCell.setCellValue("     JK INTERNATIONAL ");
			nCell.setCellStyle(tipStyle(wb));   //调用方法设置其样式

            //为什么多加一行？
			curRow++;//2，3
			
			//header设置："                 西经济技术开发区西城一路27号无迪大厦19楼"
			nRow = sheet.createRow(curRow++);//3，4
			nRow.setHeightInPoints(20);
			nCell   = nRow.createCell((1));     //该字段实际写入的是第2列
			nCell.setCellValue("                 西经济技术开发区西城一路27号无迪大厦19楼");
			nCell.setCellStyle(addressStyle(wb));
			
			//header设置：" CO., LTD."
			nCell   = nRow.createCell((6));
			nCell.setCellValue(" CO., LTD.");
			nCell.setCellStyle(ltdStyle(wb));

			//header设置："                   TEL: 0086-29-86339371  FAX: 0086-29-86303310               E-MAIL: ijackix@glass.cn"
			nRow = sheet.createRow(curRow++);//4，5
			nRow.setHeightInPoints(15);
			nCell   = nRow.createCell((1));
			nCell.setCellValue("                   TEL: 0086-29-86339371  FAX: 0086-29-86303310               E-MAIL: ijackix@glass.cn");
			nCell.setCellStyle(telStyle(wb));
			
			//line：”购销合同“上面的一条线
			nRow = sheet.createRow(curRow++);//5，6
			nRow.setHeightInPoints(7);
			poiUtil.setLine(wb, patriarch, curRow, 2, curRow, 8);	//draw line

			//header设置："    购   销   合   同"
			nRow = sheet.createRow(curRow++);//6，7
			nRow.setHeightInPoints(30);
			nCell   = nRow.createCell((4));
			nCell.setCellValue("    购   销   合   同");
			nCell.setCellStyle(titleStyle(wb));
			
			//Offeror：收购方
			nRow = sheet.createRow(curRow++);//7，8
			nRow.setHeightInPoints(20);
			nCell   = nRow.createCell((1));
			nCell.setCellValue(printMap.get("Offeror"));
			nCell.setCellStyle(poiUtil.titlev12(wb, blackFont));

			//Facotry：生产工厂
			nCell   = nRow.createCell((5));
			nCell.setCellValue(printMap.get("Factory"));
			nCell.setCellStyle(poiUtil.titlev12(wb, blackFont));
			
			//ContractNo：合同号
			nRow = sheet.createRow(curRow++);//8.9
			nRow.setHeightInPoints(20);
			nCell   = nRow.createCell(1);
			nCell.setCellValue(printMap.get("ContractNo"));
			nCell.setCellStyle(poiUtil.titlev12(wb, blackFont));
			
			//Contractor：联系人
			nCell  = nRow.createCell(5);
			nCell.setCellValue(printMap.get("Contractor"));
			nCell.setCellStyle(poiUtil.titlev12(wb, blackFont));
			
			//SigningDate：签单日期
			nRow = sheet.createRow(curRow++);//9，10
			nRow.setHeightInPoints(20);
			nCell = nRow.createCell(1);
			nCell.setCellValue(printMap.get("SigningDate"));
			nCell.setCellStyle(poiUtil.titlev12(wb, blackFont));
			
			//Phone：电话
			nCell = nRow.createCell(5);
			nCell.setCellValue(printMap.get("Phone"));
			nCell.setCellStyle(poiUtil.titlev12(wb, blackFont));
			
			//“产品”字段单元格合并
			nRow = sheet.createRow(curRow++);//10，11
			nRow.setHeightInPoints(24);
			region = new CellRangeAddress(curRow-1, curRow-1, 1, 3);	//横向合并单元格
			sheet.addMergedRegion(region);
			//表格静态字段：”产品“
			nCell = nRow.createCell(1);
			nCell.setCellValue("产品");
			nCell.setCellStyle(thStyle(wb));		
			//“产品”字段 第2个单元格样式
			nCell = nRow.createCell(2);
			nCell.setCellStyle(poiUtil.notehv10_BorderThin(wb, defaultFont10));
            //“产品”字段 第3个单元格样式
            nCell = nRow.createCell(3);
			nCell.setCellStyle(poiUtil.notehv10_BorderThin(wb, defaultFont10));

			//ContractDesc：重要程度+“货物描述”
			nCell = nRow.createCell(4);
			nCell.setCellValue(printMap.get("ContractDesc"));
			nCell.setCellStyle(thStyle(wb));	

			//“数量”字段单元格合并
			region = new CellRangeAddress(curRow-1, curRow-1, 5, 6);	//横向合并单元格
			sheet.addMergedRegion(region);
			//表格静态字段：“数量”
			nCell = nRow.createCell(5);
			nCell.setCellValue("数量");
			nCell.setCellStyle(thStyle(wb));
			nCell = nRow.createCell(6);
			nCell.setCellStyle(poiUtil.notehv10_BorderThin(wb, defaultFont10));			

			//表格静态字段：“单价”
			nCell = nRow.createCell(7);
			nCell.setCellValue("单价");
			nCell.setCellStyle(thStyle(wb));						

			//表格静态字段：“总金额”
			nCell = nRow.createCell(8);
			nCell.setCellValue("总金额");
			nCell.setCellStyle(thStyle(wb));						

            //产品图片 单元格合并
			nRow = sheet.createRow(curRow++);//11，12
			nRow.setHeightInPoints(96);
			region = new CellRangeAddress(curRow-1, curRow-1, 1, 3);	//横向合并单元格
			sheet.addMergedRegion(region);
			//插入产品图片
			if(UtilFuns.isNotEmpty(printMap.get("ProductImage"))){
				System.out.println(printMap.get("ProductImage"));   //控制台显示，验证产品图片路径
				poiUtil.setPicture(wb, patriarch, path+"ufiles/jquery/"+printMap.get("ProductImage"), curRow-1, 1, curRow, 3);
			}
			//设置 产品图片 第2个单元格样式
			nCell = nRow.createCell(2);
			nCell.setCellStyle(poiUtil.notehv10_BorderThin(wb, defaultFont10));
			//设置 产品图片 第3个单元格样式
			nCell = nRow.createCell(3);
			nCell.setCellStyle(poiUtil.notehv10_BorderThin(wb, defaultFont10));
			
			//ProductDesc：货物描述
            //货物描述 单元格合并
			region = new CellRangeAddress(curRow-1, curRow, 4, 4);	//纵向合并单元格 
			sheet.addMergedRegion(region);
			//设置 货物描述 字段值
			nCell = nRow.createCell(4);
			nCell.setCellValue(printMap.get("ProductDesc"));
			nCell.setCellStyle(poiUtil.notehv10_BorderThin(wb, defaultFont10));		
			
			//Cnumber：货物数量
            //货物数量 单元格合并
			region = new CellRangeAddress(curRow-1, curRow, 5, 5);	//纵向合并单元格 
			sheet.addMergedRegion(region);
			//设置 货物数量 字段值
			nCell = nRow.createCell(5);
			nCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			nCell.setCellValue(Double.parseDouble(printMap.get("Cnumber")));
			nCell.setCellStyle(poiUtil.numberrv10_BorderThin(wb, defaultFont10));	
			
			//Unit：货物单位
            //货物单位 单元格合并
			region = new CellRangeAddress(curRow-1, curRow, 6, 6);	//纵向合并单元格 
			sheet.addMergedRegion(region);
			//设置 货物单位 字段值
			nCell = nRow.createCell(6);
			nCell.setCellValue(printMap.get("PackingUnit"));
			nCell.setCellStyle(poiUtil.moneyrv10_BorderThin(wb, defaultFont10, rmb4Format));	
			
			//Price：单价
            //单价 单元格合并
			region = new CellRangeAddress(curRow-1, curRow, 7, 7);	//纵向合并单元格 
			sheet.addMergedRegion(region);
			//设置 单价 字段值&样式
			nCell = nRow.createCell(7);
			nCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			nCell.setCellValue(Double.parseDouble(printMap.get("Price")));
			nCell.setCellStyle(poiUtil.moneyrv10_BorderThin(wb, defaultFont10, rmb4Format));
			
			
			//Amount：总金额
            //总金额 单元格合并
			region = new CellRangeAddress(curRow-1, curRow, 8, 8);	//纵向合并单元格 
			sheet.addMergedRegion(region);
			//设置 总金额 计算公式
			nCell = nRow.createCell(8);
			if(UtilFuns.isNotEmpty(printMap.get("Cnumber")) && UtilFuns.isNotEmpty(printMap.get("Price"))){
				nCell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
				nCell.setCellFormula("F"+String.valueOf(curRow)+"*H"+String.valueOf(curRow));
			}
			nCell.setCellStyle(poiUtil.moneyrv10_BorderThin(wb, defaultFont10, rmb4Format));			

			curRow++;//12，13

            //ProductNo：产品编号
            //产品编号 单元格合并
			region = new CellRangeAddress(curRow-1, curRow-1, 1, 3);	//横向合并单元格
			sheet.addMergedRegion(region);
			//设置 产品编号 字段值&样式
			nRow = sheet.createRow(curRow-1);
			nRow.setHeightInPoints(24);
			nCell = nRow.createCell(1);
			nCell.setCellValue(printMap.get("ProductNo"));
			nCell.setCellStyle(poiUtil.notecv10_BorderThin(wb, defaultFont10));

			//设置 产品编号 后面几列被合并的单元格样式
			for(int j=2;j<9;j++){
				nCell = nRow.createCell(j);
				nCell.setCellStyle(poiUtil.notehv10_BorderThin(wb, defaultFont10));
			}
			
			
			//设置 第二个产品
			if(contract.getPrintStyle().equals("2") && UtilFuns.isNotEmpty(printMap.get("ProductNo2"))){
				nRow = sheet.createRow(curRow++);//13，14
				nRow.setHeightInPoints(96);
				
				region = new CellRangeAddress(curRow-1, curRow-1, 1, 3);	//纵向合并单元格 
				sheet.addMergedRegion(region);
				
				//插入产品图片
				if(UtilFuns.isNotEmpty(printMap.get("ProductImage2"))){
					System.out.println(printMap.get("ProductImage2"));
					poiUtil.setPicture(wb, patriarch, path+"ufiles/jquery/"+printMap.get("ProductImage2"), curRow-1, 1, curRow, 3);
				}
				
				//ProductDesc
				region = new CellRangeAddress(curRow-1, curRow, 4, 4);	//纵向合并单元格 
				sheet.addMergedRegion(region);
				
				nCell = nRow.createCell(4);
				nCell.setCellValue(printMap.get("ProductDesc2"));
				nCell.setCellStyle(poiUtil.notehv10_BorderThin(wb, defaultFont10));		
				
				//Cnumber
				region = new CellRangeAddress(curRow-1, curRow, 5, 5);	//纵向合并单元格 
				sheet.addMergedRegion(region);
				
				nCell = nRow.createCell(5);
				nCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
				nCell.setCellValue(Double.parseDouble(printMap.get("Cnumber2")));
				nCell.setCellStyle(poiUtil.numberrv10_BorderThin(wb, defaultFont10));	
				
				//Unit
				region = new CellRangeAddress(curRow-1, curRow, 6, 6);	//纵向合并单元格 
				sheet.addMergedRegion(region);
				
				nCell = nRow.createCell(6);
				nCell.setCellValue(printMap.get("PackingUnit2"));
				nCell.setCellStyle(poiUtil.moneyrv10_BorderThin(wb, defaultFont10, rmb4Format));	
				
				//Price
				region = new CellRangeAddress(curRow-1, curRow, 7, 7);	//纵向合并单元格 
				sheet.addMergedRegion(region);
				
				nCell = nRow.createCell(7);
				nCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
				nCell.setCellValue(Double.parseDouble(printMap.get("Price2")));
				nCell.setCellStyle(poiUtil.moneyrv10_BorderThin(wb, defaultFont10, rmb4Format));
				
				
				//Amount
				region = new CellRangeAddress(curRow-1, curRow, 8, 8);	//纵向合并单元格 
				sheet.addMergedRegion(region);
				
				nCell = nRow.createCell(8);
				if(UtilFuns.isNotEmpty(printMap.get("Cnumber2")) && UtilFuns.isNotEmpty(printMap.get("Price2"))){
					nCell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
					nCell.setCellFormula("F"+String.valueOf(curRow)+"*H"+String.valueOf(curRow));
				}
				nCell.setCellStyle(poiUtil.moneyrv10_BorderThin(wb, defaultFont10, rmb4Format));		
				
				curRow++;//14，15
				
				region = new CellRangeAddress(curRow-1, curRow-1, 1, 3);	//纵向合并单元格
				sheet.addMergedRegion(region);
				
				nRow = sheet.createRow(curRow-1);
				nRow.setHeightInPoints(24);
				
				nCell = nRow.createCell(1);
				nCell.setCellValue(printMap.get("ProductNo2"));
				nCell.setCellStyle(poiUtil.notecv10_BorderThin(wb, defaultFont10));			
				
				//合并单元格画线
				for(int j=2;j<9;j++){
					nCell = nRow.createCell(j);
					nCell.setCellStyle(poiUtil.notehv10_BorderThin(wb, defaultFont10));
				}				
			}
			
			
			//InputBy：制单
			nRow = sheet.createRow(curRow++);//15，16
			nRow.setHeightInPoints(24);
			nCell = nRow.createCell(1);
			nCell.setCellValue(printMap.get("InputBy"));
			nCell.setCellStyle(poiUtil.bnormalv12(wb,defaultFont12));
			
			//CheckBy+inspector：审单+验货员
			nCell = nRow.createCell(4);
			nCell.setCellValue(printMap.get("CheckBy"));
			nCell.setCellStyle(poiUtil.bnormalv12(wb,defaultFont12));
			
			//if(contract.getPrintStyle().equals("2") && UtilFuns.isNotEmpty(printMap.get("ProductNo2"))){
				//静态字段：总总金额
				nCell = nRow.createCell(7);
				nCell.setCellValue("总金额：");
				nCell.setCellStyle(bcv12(wb));
				
				//TotalAmount：总总金额 计算公式
				nRow = sheet.createRow(curRow-1);
				nRow.setHeightInPoints(24);
				if(UtilFuns.isNotEmpty(printMap.get("Cnumber"))&&UtilFuns.isNotEmpty(printMap.get("Price"))){
					nCell  = nRow.createCell(8);
					nCell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
					nCell.setCellFormula("SUM(I"+String.valueOf(curRow-4)+":I"+String.valueOf(curRow-1)+")");
					nCell.setCellStyle(poiUtil.moneyrv12_BorderThin(wb,defaultFont12,rmb2Format));		
				}
			//}
			
			
			//note：备注
            //设置 备注 字段值&样式
			nRow = sheet.createRow(curRow++);//16，17
			nRow.setHeightInPoints(21);
			nCell = nRow.createCell(2);
			nCell.setCellValue(printMap.get("Remark"));
			nCell.setCellStyle(noteStyle(wb));

			//要求 单元格合并
			region = new CellRangeAddress(curRow, curRow, 1, 8);	//横向合并单元格，指定合并区域
			sheet.addMergedRegion(region);
			//要求 单元格高度
			nRow = sheet.createRow(curRow++);//17，18
			float height = poiUtil.getCellAutoHeight(printMap.get("Request"), 12f);		//自动高度
			nRow.setHeightInPoints(height);
			//设置 要求 字段值&样式
			nCell = nRow.createCell(1);
			nCell.setCellValue(printMap.get("Request"));
			nCell.setCellStyle(requestStyle(wb));
			
			//space line：表格下面的空行
			nRow = sheet.createRow(curRow++);//18，19
			nRow.setHeightInPoints(20);
			
			//duty："未按以上要求出货而导致客人索赔，由供方承担。"
			nRow = sheet.createRow(curRow++);//19，20
			nRow.setHeightInPoints(32);
			nCell = nRow.createCell(1);
			nCell.setCellValue("未按以上要求出货而导致客人索赔，由供方承担。");
			nCell.setCellStyle(dutyStyle(wb));	
			
			//space line：责任声明 下面的空行
			nRow = sheet.createRow(curRow++);//20，21
			nRow.setHeightInPoints(32);
			
			//buyer："    收购方负责人："
			nRow = sheet.createRow(curRow++);//21，22
			nRow.setHeightInPoints(25);
			nCell = nRow.createCell(1);
			nCell.setCellValue("    收购方负责人：");
			nCell.setCellStyle(dutyStyle(wb));				
			
			//seller："供方负责人："
			nCell = nRow.createCell(5);
			nCell.setCellValue("供方负责人：");
			nCell.setCellStyle(dutyStyle(wb));	

			//又加一行？？？
			curRow++;//22，23

		}

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();			//生成流对象
		wb.write(byteArrayOutputStream);													//将excel写入流

		//工具类，封装弹出下载框：		
		String outFile = "购销合同.xls";
		DownloadUtil down = new DownloadUtil();
		down.download(byteArrayOutputStream, response, outFile);

	}
	
	private HSSFCellStyle leftStyle(HSSFWorkbook wb){
		HSSFCellStyle curStyle = wb.createCellStyle();
		curStyle.setWrapText(true);  						//换行   
		HSSFFont curFont = wb.createFont();					//设置字体
		curFont.setCharSet(HSSFFont.DEFAULT_CHARSET);		//设置中文字体，那必须还要再对单元格进行编码设置
		//fTitle.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);	//加粗
		curFont.setFontHeightInPoints((short)10);
		curStyle.setFont(curFont);
		curStyle.setVerticalAlignment(VerticalAlignment.CENTER);		//单元格垂直居中

		curStyle.setBorderLeft(BorderStyle.THIN);				//实线右边框,HSSFCellStyle.BORDER_THIN
		curStyle.setBorderBottom(BorderStyle.THIN);				//实线右边框,HSSFCellStyle.BORDER_THIN
		
		return curStyle;
	}  

	//设置“SHAANXI”的样式
	private HSSFCellStyle headStyle(HSSFWorkbook wb){
		HSSFCellStyle curStyle = wb.createCellStyle();
		HSSFFont curFont = wb.createFont();					//设置字体
		curFont.setFontName("Comic Sans MS");
		curFont.setCharSet(HSSFFont.DEFAULT_CHARSET);		//设置中文字体，那必须还要再对单元格进行编码设置
		
		curFont.setItalic(true);
		curFont.setFontHeightInPoints((short)16);
		curStyle.setFont(curFont);
		curStyle.setVerticalAlignment(VerticalAlignment.CENTER);		//单元格垂直居中,HSSFCellStyle.VERTICAL_CENTER
		
		return curStyle;
	}  
	
	private HSSFCellStyle tipStyle(HSSFWorkbook wb){
		HSSFCellStyle curStyle = wb.createCellStyle();
		HSSFFont curFont = wb.createFont();					//设置字体
		curFont.setFontName("Georgia");
		curFont.setCharSet(HSSFFont.DEFAULT_CHARSET);		//设置中文字体，那必须还要再对单元格进行编码设置
		
		curFont.setBold(true);	//加粗,HSSFFont.BOLDWEIGHT_BOLD
		curFont.setFontHeightInPoints((short)28);
		curStyle.setFont(curFont);
		curStyle.setVerticalAlignment(VerticalAlignment.CENTER);		//单元格垂直居中,HSSFCellStyle.VERTICAL_CENTER
		
		return curStyle;
	}  
	
	private HSSFCellStyle addressStyle(HSSFWorkbook wb){
		HSSFCellStyle curStyle = wb.createCellStyle();
		HSSFFont curFont = wb.createFont();					//设置字体
		curFont.setFontName("宋体");
		curFont.setCharSet(HSSFFont.DEFAULT_CHARSET);		//设置中文字体，那必须还要再对单元格进行编码设置
		
		//fTitle.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);	//加粗
		curFont.setFontHeightInPoints((short)10);
		curStyle.setFont(curFont);
		curStyle.setVerticalAlignment(VerticalAlignment.CENTER);		//单元格垂直居中
		
		return curStyle;
	}  
	
	private HSSFCellStyle ltdStyle(HSSFWorkbook wb){
		HSSFCellStyle curStyle = wb.createCellStyle();
		HSSFFont curFont = wb.createFont();					//设置字体
		curFont.setFontName("Times New Roman");
		curFont.setCharSet(HSSFFont.DEFAULT_CHARSET);		//设置中文字体，那必须还要再对单元格进行编码设置
		
		curFont.setBold(true);	//加粗
		curFont.setItalic(true);
		curFont.setFontHeightInPoints((short)16);
		curStyle.setFont(curFont);
		curStyle.setVerticalAlignment(VerticalAlignment.CENTER);		//单元格垂直居中
		
		return curStyle;
	} 	
	
	private HSSFCellStyle telStyle(HSSFWorkbook wb){
		HSSFCellStyle curStyle = wb.createCellStyle();
		HSSFFont curFont = wb.createFont();					//设置字体
		curFont.setFontName("宋体");
		curFont.setCharSet(HSSFFont.DEFAULT_CHARSET);		//设置中文字体，那必须还要再对单元格进行编码设置
		
		//fTitle.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);	//加粗
		curFont.setFontHeightInPoints((short)9);
		curStyle.setFont(curFont);
		curStyle.setVerticalAlignment(VerticalAlignment.CENTER);		//单元格垂直居中
		
		return curStyle;
	} 	
	
	private HSSFCellStyle titleStyle(HSSFWorkbook wb){
		HSSFCellStyle curStyle = wb.createCellStyle();
		HSSFFont curFont = wb.createFont();					//设置字体
		curFont.setFontName("黑体");
		curFont.setCharSet(HSSFFont.DEFAULT_CHARSET);		//设置中文字体，那必须还要再对单元格进行编码设置
		
		curFont.setBold(true);	//加粗
		curFont.setFontHeightInPoints((short)18);
		curStyle.setFont(curFont);
		curStyle.setVerticalAlignment(VerticalAlignment.CENTER);		//单元格垂直居中
		
		return curStyle;
	} 	
	
	private HSSFCellStyle requestStyle(HSSFWorkbook wb){
		HSSFCellStyle curStyle = wb.createCellStyle();
		curStyle.setWrapText(true);  						//换行   
		HSSFFont curFont = wb.createFont();					//设置字体
		curFont.setFontName("宋体");
		curFont.setCharSet(HSSFFont.DEFAULT_CHARSET);		//设置中文字体，那必须还要再对单元格进行编码设置
		
		curFont.setFontHeightInPoints((short)10);
		curStyle.setFont(curFont);
		curStyle.setVerticalAlignment(VerticalAlignment.CENTER);		//单元格垂直居中
		
		return curStyle;
	} 	
	
	private HSSFCellStyle dutyStyle(HSSFWorkbook wb){
		HSSFCellStyle curStyle = wb.createCellStyle();
		HSSFFont curFont = wb.createFont();					//设置字体
		curFont.setFontName("黑体");
		curFont.setCharSet(HSSFFont.DEFAULT_CHARSET);		//设置中文字体，那必须还要再对单元格进行编码设置

        curFont.setBold(true);	//加粗
		curFont.setFontHeightInPoints((short)16);
		curStyle.setFont(curFont);
		curStyle.setVerticalAlignment(VerticalAlignment.CENTER);		//单元格垂直居中
		
		return curStyle;
	} 	
	
	private HSSFCellStyle noteStyle(HSSFWorkbook wb){
		HSSFCellStyle curStyle = wb.createCellStyle();
		HSSFFont curFont = wb.createFont();					//设置字体
		curFont.setFontName("宋体");
		curFont.setCharSet(HSSFFont.DEFAULT_CHARSET);		//设置中文字体，那必须还要再对单元格进行编码设置

        curFont.setBold(true);	//加粗
		curFont.setFontHeightInPoints((short)12);
		curStyle.setFont(curFont);
		curStyle.setVerticalAlignment(VerticalAlignment.CENTER);		//单元格垂直居中
		
		return curStyle;
	} 
	
	public HSSFCellStyle thStyle(HSSFWorkbook wb){
		HSSFCellStyle curStyle = wb.createCellStyle();
		HSSFFont curFont = wb.createFont();					//设置字体
		curFont.setFontName("宋体");
        curFont.setBold(true);	//加粗
		curFont.setFontHeightInPoints((short)12);
		curFont.setCharSet(HSSFFont.DEFAULT_CHARSET);		//设置中文字体，那必须还要再对单元格进行编码设置
		
		curStyle.setBorderTop(BorderStyle.THIN);				//实线右边框
		curStyle.setBorderRight(BorderStyle.THIN);				//实线右边框
		curStyle.setBorderBottom(BorderStyle.THIN);			//实线右边框
		curStyle.setBorderLeft(BorderStyle.THIN);				//实线右边框
		
		curStyle.setAlignment(HorizontalAlignment.CENTER);
		curStyle.setVerticalAlignment(VerticalAlignment.CENTER);		//单元格垂直居中
		
		return curStyle;
	}  
	
	public HSSFCellStyle bcv12(HSSFWorkbook wb){
		HSSFCellStyle curStyle = wb.createCellStyle();
		HSSFFont curFont = wb.createFont();						//设置字体
		curFont.setFontName("Times New Roman");
		curFont.setCharSet(HSSFFont.DEFAULT_CHARSET);			//设置中文字体，那必须还要再对单元格进行编码设置

        curFont.setBold(true);		//加粗
		curFont.setFontHeightInPoints((short)12);
		curStyle.setFont(curFont);
		
		curStyle.setBorderTop(BorderStyle.THIN);				//实线
		curStyle.setBorderRight(BorderStyle.THIN);			//粗实线
		curStyle.setBorderBottom(BorderStyle.THIN);			//实线
		curStyle.setBorderLeft(BorderStyle.THIN);				//实线
		
		curStyle.setAlignment(HorizontalAlignment.RIGHT);
		curStyle.setVerticalAlignment(VerticalAlignment.CENTER);		//单元格垂直居中
		
		return curStyle;
	}		
	
}
