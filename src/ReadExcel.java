
// parameters - excel file path, sheetname
// return array of excel sheet data
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import jxl.*;

public class ReadExcel {
	
	Hashtable<String, String> dict= new Hashtable<String, String>();
	private String inputFile,workSheet;
	public static String [][] arrData;	 
	//private ReadExcel classAInstance;
	
	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}
	
	public void setWorkSheet(String sheetName){
		this.workSheet = sheetName;
	}
	
	public void read() throws IOException  {
		File inputWorkbook = new File(inputFile);
		Workbook w;
		
		try {
			w = Workbook.getWorkbook(inputWorkbook);
			// Get the first sheet
			Sheet sheet = w.getSheet(workSheet);
			// Loop for all rows in excel
		
			arrData = new String[sheet.getRows()][sheet.getColumns()];

			for (int i = 0; i < sheet.getRows(); i++)
			{
				for (int j = 0; j < sheet.getColumns(); j++) 
				{
					Cell cell = sheet.getCell(j, i);
					arrData[i][j]= cell.getContents();
					//System.out.println(cell.getContents());
				}
			}
			
		} catch (Exception e) {
			TestSuitRunner.logger.error("ReadExcel|read|Unable to read file.",e);
		}

	}

	public static String[][] main(String filepath, String sheetname) {
		
		try{
		ReadExcel fetchExcelData = new ReadExcel();
		
		fetchExcelData.setInputFile(filepath);
		
		fetchExcelData.setWorkSheet(sheetname);
		
		fetchExcelData.read();
				
		
		}
		catch (Exception e){
			TestSuitRunner.logger.error("ReadExcel|read|Unable to read file.",e);
		}
		return arrData;
	}

}
