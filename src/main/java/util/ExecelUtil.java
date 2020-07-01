package util;

import model.CmdAndResult;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileInputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class ExecelUtil {
    public static CmdAndResult[] readExcelInfo(String pathname) throws Exception {
        return readExcelInfo(pathname, 0);
    }

    public static CmdAndResult[] readExcelInfo(String pathname, int sheetIndex) throws Exception {
        CmdAndResult[] cmdAndResults;

        //用流的方式先读取到你想要的excel的文件
        FileInputStream fis = new FileInputStream(new File(pathname));
        //解析excel
        POIFSFileSystem pSystem = new POIFSFileSystem(fis);
        //获取整个excel
        HSSFWorkbook hb = new HSSFWorkbook(pSystem);
        System.out.println("getNumCellStyles: " + hb.getNumCellStyles());
        //获取第一个表单sheet
        HSSFSheet sheet = hb.getSheetAt(sheetIndex);
        //获取第一行
        int firstrow = sheet.getFirstRowNum();
        //获取最后一行
        int lastrow = sheet.getLastRowNum();

        int index = 0;
        cmdAndResults = new CmdAndResult[lastrow - firstrow];

        //循环行数依次获取列数
        for (int i = firstrow + 1; i <= lastrow; i++) {
            //获取哪一行i
            Row row = sheet.getRow(i);
            if (row != null) {
                //获取这一行的第一列
                int firstcell = row.getFirstCellNum();
                //获取这一行的最后一列
                int lastcell = row.getLastCellNum();
                //创建一个集合，用处将每一行的每一列数据都存入集合中
                List<String> list = new ArrayList<>();

                for (int j = firstcell; j < lastcell; j++) {
                    //获取第j列
                    Cell cell = row.getCell(j);

                    if (cell != null) {
                        System.out.print(cell + "\t");
                        list.add(EnumberFormat(cell));
                    }
                }
                System.out.println();
                CmdAndResult mCmdAndResult = new CmdAndResult();
                mCmdAndResult.setCmdIndex((int) Double.parseDouble(list.get(0)));//命令词序号
                mCmdAndResult.setCmd(list.get(1));//命令词名称
                mCmdAndResult.setSericalPortProtocolValue(list.get(2));//命令词指令值
                System.out.println("CmdIndex : " + mCmdAndResult.getCmdIndex() + " " +
                        "Cmd : " + mCmdAndResult.getCmd() + " " +
                        "SericalPortProtocolValue : " + mCmdAndResult.getSericalPortProtocolValue()
                );
                cmdAndResults[index] = mCmdAndResult;
                index++;
            }
        }
        fis.close();
        return cmdAndResults;
    }

    private static String EnumberFormat(Cell cell) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 不显示千位分割符，否则显示结果会变成类似1,234,567,890
        numberFormat.setGroupingUsed(false);
        if (cell == null) {
            return "null";
        }
        String value = cell.toString();
        if (CellType.STRING == cell.getCellType()) {
            return value;
        } else {
            value = numberFormat.format(cell.getNumericCellValue());
            return value;
        }
    }

    public static void main(String[] args) throws Exception {
        readExcelInfo("C:\\Users\\LWB\\Desktop\\cmd.xls");
    }
}
