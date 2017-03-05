package Utils;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.InputStream;

public class ExcelRead {

    private static int count =0;
    private static int totalRows=0;
    private static int totalCols =0;
    private static int startRow=1;
    private static int startCol=0;
    private static int ci, cj;

    private static String pathWorkBook;
    private static String workSheetName;

    private static String[][] tabArray;

    private static InputStream inputStream;
    private static XSSFWorkbook wb;
    private static XSSFCell Cell;


    /**
     *
     * @return Returns an array of objects contained within the specified workbook <br>
     *         from the specified worksheet.
     */
    public static Object[][] getTableArray(){
        try {
            //get file
            inputStream = new FileInputStream(pathWorkBook);
            wb = new XSSFWorkbook(inputStream);

            //count sheets
            count = wb.getNumberOfSheets();

            //test number of sheets, access correct sheet, read data, and store to array
            if(count<2 && count >0){
                totalRows = wb.getSheetAt(0).getLastRowNum();
                totalCols = wb.getSheetAt(0).getRow(0).getLastCellNum();

                tabArray = new String[totalRows][totalCols];

                dataLoop();
            } else if (count > 1) {

                totalRows = wb.getSheetAt(wb.getSheetIndex(workSheetName)).getLastRowNum();
                totalCols = wb.getSheetAt(wb.getSheetIndex(workSheetName)).getRow(0).getLastCellNum();

                tabArray = new String[totalRows][totalCols];

                dataLoop();
            } else {
                System.out.println("This workbook is Empty.  Please add your data");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tabArray;
    }

    //internal loop to cycle through the data in each row / column
    private static void dataLoop() throws Exception {
        for(int i=startRow; i<= totalRows; i++, ci++){
            cj=0;
            for(int j=startCol; j<=totalCols-1; j++, cj++){
                tabArray[ci][cj]=getCellData(i, j, 0);
                System.out.println(tabArray[ci][cj]);
            }
        }
    }

    /**
     *
     * @param rowNum the row index for the cell to be accessed
     * @param colNum the column index for the cell to be accessed
     * @param index the sheet index where the data resides
     * @return Returns the contents of the cell.
     * @throws Exception
     */
    public static String getCellData(int rowNum, int colNum, int index) throws Exception{
        try{
            Cell=wb.getSheetAt(index).getRow(rowNum).getCell(colNum);
            int dataType = Cell.getCellType();

            if(dataType==2){
                return "";
            } else if (dataType == 1){
                String CellData = Cell.getStringCellValue();
                return CellData;
            } else if (dataType == 0) {
                double CellData = Cell.getNumericCellValue();
                return String.valueOf(CellData);
            } else {
                return "";
            }
        } catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    /**
     *
     * @return Returns the path for the workbook as a String
     */
    public static String getPathWorkBook() {
        return pathWorkBook;
    }

    /**
     *
     * @param pathWB Sets the path for the workbook
     */
    public static void setPathWorkBook(String pathWB) {
       pathWorkBook = pathWB;
    }

    /**
     *
     * @return gets the worksheet name
     */
    public static String getWorkSheetName() {
        return workSheetName;
    }

    /**
     *
     * @param workSheet Returns the worksheet name
     */
    public static void setWorkSheetName(String workSheet) {
        workSheetName = workSheet;
    }

}