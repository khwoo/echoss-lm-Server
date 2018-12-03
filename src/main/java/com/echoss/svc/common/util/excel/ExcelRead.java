package com.echoss.svc.common.util.excel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.echoss.svc.common.exception.ComBizException;
import com.echoss.svc.common.util.TAData;

public class ExcelRead {
	private static final Logger logger = LoggerFactory.getLogger(ExcelRead.class);
	
	private Workbook wb;
	private String filePath;
	
	public ExcelRead(String filePath){
		this.filePath = filePath;
		//엑셀파일을 읽어 들인다.
		wb = ExcelFileType.getWorkbook(filePath);
	}
	
	public List<TAData> read(ExcelReadOption excelReadOption) {
		/**
		 * 엑셀 파일에서 첫번째 시트를 가지고 온다.
		 */
		Sheet sheet = wb.getSheetAt(0);

		logger.debug("Sheet 이름: "+ wb.getSheetName(0)); 
		logger.debug("데이터가 있는 Sheet의 수 :" + wb.getNumberOfSheets());
		
		/**
		 * sheet에서 유효한(데이터가 있는) 행의 개수를 가져온다.
		 */
		int numOfRows = sheet.getPhysicalNumberOfRows();
		int numOfCells = 0;

		Row row = null;
		Cell cell = null;

		String cellName = "";
		/**
		 * 각 row마다의 값을 저장할 맵 객체
		 * 저장되는 형식은 다음과 같다.
		 * set("A", "이름");
		 * set("B", "게임명");
		 */
		TAData map = null;
		/*
		 * 각 Row를 리스트에 담는다.
		 * 하나의 Row를 하나의 Map으로 표현되며
		 * List에는 모든 Row가 포함될 것이다.
		 */
		List<TAData> result = new ArrayList<TAData>(); 
		/**
		 * 각 Row만큼 반복을 한다.
		 */
		for(int rowIndex = excelReadOption.getStartRow() - 1; rowIndex < numOfRows; rowIndex++) {
			/*
			 * 워크북에서 가져온 시트에서 rowIndex에 해당하는 Row를 가져온다.
			 * 하나의 Row는 여러개의 Cell을 가진다.
			 */
			row = sheet.getRow(rowIndex);

			if(row != null) {
				/*
				 * 가져온 Row의 Cell의 개수를 구한다.
				 */
				numOfCells = row.getPhysicalNumberOfCells();
				/*
				 * 데이터를 담을 맵 객체 초기화
				 */
				map = new TAData();
				map.set("rowIndex", rowIndex);
				
				/*
				 * cell의 수 만큼 반복한다.
				 */
				for(int cellIndex = 0; cellIndex < numOfCells; cellIndex++) {
					/*
					 * Row에서 CellIndex에 해당하는 Cell을 가져온다.
					 */
					cell = row.getCell(cellIndex);
					/*
					 * 현재 Cell의 이름을 가져온다
					 * 이름의 예 : A,B,C,D,......
					 */
					cellName = ExcelCellRef.getName(cell, cellIndex);
					
					/*
					 * 추출 대상 컬럼인지 확인한다
					 * 추출 대상 컬럼이 아니라면, 
					 * for로 다시 올라간다
					 */
//					if( !excelReadOption.getOutputColumns().contains(cellName) ) {
//						continue;
//					}
					/*
					 * map객체의 Cell의 이름을 키(Key)로 데이터를 담는다.
					 */
					map.set(excelReadOption.getOutputColumns().get(cellIndex), ExcelCellRef.getValue(cell));
				}
				
				/*
				 * 만들어진 Map객체를 List로 넣는다.
				 */
				result.add(map);
			}
		}
		
		return result;
	}
	
	/**
	 * 결과 Cell Title 설정
	 * @param sheetIdx
	 * @param rowIdx
	 * @param cellIdx
	 * @param contents
	 */
	public void setCellTitle(int sheetIdx, int rowIdx, int cellIdx, String contents) {
		CellStyle styleHd = wb.createCellStyle();
		Font fontHd = wb.createFont();
		fontHd.setFontHeightInPoints((short) 11);
		fontHd.setBoldweight((short) 700);
		fontHd.setColor(HSSFColor.WHITE.index);
		
		styleHd.setFont(fontHd);
		styleHd.setAlignment(CellStyle.ALIGN_CENTER);
		styleHd.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		styleHd.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
		styleHd.setFillPattern(CellStyle.SOLID_FOREGROUND);
		
		Sheet sheet = wb.getSheetAt(sheetIdx);
		sheet.setColumnWidth(cellIdx, 10000);
		
		Row row = sheet.getRow(rowIdx);
		Cell cell = row.createCell(cellIdx);
		cell.setCellStyle(styleHd);
		
		cell.setCellValue(contents);
	}
	
	public void setCell(int sheetIdx, int rowIdx, int cellIdx, String contents) {
		Sheet sheet = wb.getSheetAt(sheetIdx);
		Row row = sheet.getRow(rowIdx);
		Cell cell = row.createCell(cellIdx);
		
		cell.setCellValue(contents);
	}
	
	public void write() {
		FileOutputStream fo = null;
		try{
			fo = new FileOutputStream(filePath);
			wb.write(fo);
		}catch(Exception e){
			logger.error("엑셀파일 작성 중 오류가 발생하였습니다.", e);
			throw new ComBizException("SM902");
		}finally{
			try {
				fo.close();
			} catch (IOException e) {
			}
		}
	}
}
