package com.sharpower.utils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

public class ExportExlUtils{
	/**
	 * 设置sheet表头信息
	 * @author David
	 * @param headersInfo
	 * @param sheet
	 */
	public static void outputHeaders(String[] headersInfo,HSSFSheet sheet ){
		HSSFRow row = sheet.createRow(0);
		for (int i = 0; i < headersInfo.length; i++) {
			sheet.setColumnWidth(i, 4000);
			row.createCell(i).setCellValue(headersInfo[i]);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void outputColumnsforMap(String[] fieldIfo, List<Map<String, Object>> list, HSSFSheet sheet){
		HSSFRow row ;
		
		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow(1+i);
			
			for (int j = 0; j < fieldIfo.length; j++) {
				String[] fieldNames = fieldIfo[j].split("\\.");
				
				Object result =list.get(i);
				
				for (String string : fieldNames) {
					result = ((Map<String, Object>)result).get(string);
				}
				
				if (result!=null) {
					row.createCell(j).setCellValue(result.toString());
				}
			}
		}
		
	}
	/**
	*输出内容
	*/
	public static void outputColumns(String[] headersInfo,
			List columnsInfo,HSSFSheet sheet,int rowIndex ){
		
		HSSFRow row ;
		int headerSize = headersInfo.length;
		int columnSize = columnsInfo.size();
		
		//循环插入多少行
		for (int i = 0; i < columnsInfo.size(); i++) {
			row = sheet.createRow(rowIndex+i);
			Object obj = columnsInfo.get(i);
			//循环每行多少列
			for (int j = 0; j < headersInfo.length; j++) {
				String[] fieldNames = headersInfo[j].split("\\.");
				
					Object result;
					if (fieldNames.length==1) {
						result = getFieldValueByName(fieldNames[0], obj);
					}else if(fieldNames.length==2){
						result = getFieldValueByName(fieldNames[0], obj);
						result = getFieldValueByName(fieldNames[1], result);
					}else {
						result = "null";
					}
					
					row.createCell(j).setCellValue(result.toString());
				}
					
			}
		}
		
	/**
	 * 根据对象的属性获取值
	 * @author David
	 * @param string
	 * @param obj
	 * @return
	 */
	private static Object getFieldValueByName(String fieldName, Object obj) {

			String firstLetter = fieldName.substring(0,1).toUpperCase();
			String getter = "get" +firstLetter + fieldName.substring(1);
			try {
				Method method = obj.getClass().getMethod(getter, new Class[]{});
				Object value = method.invoke(obj, new Object[]{});
				return value;
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("属性不存在！");
				return null;
			} 

		}
}
