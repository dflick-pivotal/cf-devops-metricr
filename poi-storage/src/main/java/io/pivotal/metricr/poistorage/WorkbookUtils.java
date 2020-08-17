package io.pivotal.metricr.poistorage;

import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class WorkbookUtils {
    static org.slf4j.Logger logger = LoggerFactory.getLogger(WorkbookUtils.class);
    private CellStyle customDateCellStyle;
    private XSSFWorkbook xssfWorkbook;
    private SXSSFWorkbook sxssfWorkbook;
    private Workbook workbook;

    private WorkbookUtils() {
        // make sure default constructor is not accessible
    }

    public WorkbookUtils(XSSFWorkbook wb) {
        this.xssfWorkbook = wb;
        this.workbook = wb;
        createDataFormat(this.xssfWorkbook);
    }

    public WorkbookUtils(SXSSFWorkbook wb) {
        this.sxssfWorkbook = wb;
        this.workbook = wb;
        createDataFormat(this.sxssfWorkbook);
    }

    private void createDataFormat(Workbook wb) {
        this.customDateCellStyle = wb.createCellStyle();
        CreationHelper createHelper = wb.getCreationHelper();
        short customDateFormat = createHelper.createDataFormat().getFormat("m/d/yy h:mm");
        this.customDateCellStyle.setDataFormat(customDateFormat);
    }

    private boolean isRowEmpty(Row row) {
        assert null != row;
        for (Cell cell : row) {
            if (cell.getCellType() == CellType._NONE ||
                    cell.getCellType() == CellType.BLANK) {
                continue;
            }
            if (cell.getCellType() == CellType.STRING &&
                    null != cell.getStringCellValue() &&
                    cell.getStringCellValue().trim().isEmpty()) {
                continue;
            }
            // return false if we find any non empty cell
            return false;
        }
        return true;
    }

    private void setCellValue(Cell cell, Object value) {
        if (value instanceof Instant) {
            cell.setCellValue(Date.from((Instant) value));
            cell.setCellStyle(customDateCellStyle);
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
            cell.setCellStyle(customDateCellStyle);
        } else if (value instanceof String) {
            String str = (String) value;
            // Excel can only accept a max of 32767 characters in a cell
            str = str.length() < 32767 ? str : str.substring(0, 32766);
            cell.setCellValue(str);
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        }

    }

    private int[] getTableColumnIndexes(XSSFTable table, String[] columnNames) {
        int[] result = new int[columnNames.length];
        for (int i = 0; i < columnNames.length; i++) {
            result[i] = table.findColumnIndex(columnNames[i]);
        }
        return result;
    }

    public XSSFTable findTable(String name) {
        assert null != name;

        Iterator i = xssfWorkbook.sheetIterator();
        while (i.hasNext()) {
            XSSFSheet s = (XSSFSheet) i.next();
            List<XSSFTable> table = s.getTables();
            if (null == table || table.isEmpty()) {
                continue;
            }
            for (Iterator<XSSFTable> iterator = table.iterator(); iterator.hasNext(); ) {
                XSSFTable t = iterator.next();
                if (name.equals(t.getName())) {
                    return t;
                }
            }
        }
        return null;
    }

    public void storeValuesInTable(String tableName, final String[] columnNames, final Object[][] cells) {
        XSSFTable table = findTable(tableName);
        // TODO: 10.05.2020 error handling for missing table
        assert table != null;

        XSSFSheet sheet = table.getXSSFSheet();
        int endRowIndex = table.getEndRowIndex();
        // overwrite last row or append a new row
        endRowIndex = isRowEmpty(sheet.getRow(endRowIndex)) ? endRowIndex : endRowIndex + 1;

        // find the header column indexes
        final int[] indexes = getTableColumnIndexes(table, columnNames);
        // TODO: 10.05.2020 error handling for missing table columns
        logger.trace("found table column mappings:" + Arrays.toString(indexes) + " for columns:" + columnNames);

        XSSFRow row;
        XSSFCell cell;
        for (int i = 0; i < cells.length; i++) {
            // Create row
            row = sheet.createRow(endRowIndex + i);
            for (int j = 0; j < indexes.length; j++) {
                // Create cell at index position of respective column
                if (-1 != indexes[j]) {
                    cell = row.createCell(indexes[j]);
                    setCellValue(cell, cells[i][j]);
                }
            }
        }

        // update table definition with newly created rows
        CellReference tableStart = table.getStartCellReference();
        CellReference tableEnd = table.getEndCellReference();
        SpreadsheetVersion version = xssfWorkbook.getSpreadsheetVersion();
        CellReference newTableEnd = new CellReference(
                endRowIndex + cells.length - 1,
                tableEnd.getCol());
        AreaReference newTableArea = new AreaReference(tableStart, newTableEnd, version);

        table.setCellReferences(newTableArea);
        table.updateReferences();
    }

    public void createSheetWithTableData(String sheetName, String[] columnNames, Object[][] cells) {
        Sheet sh = workbook.createSheet();
        workbook.setSheetName(workbook.getSheetIndex(sh), sheetName);

        Row row;
        row = sh.createRow(0);
        for(int cellnum = 0; cellnum < columnNames.length; cellnum++){
            Cell cell = row.createCell(cellnum);
            setCellValue(cell, columnNames[cellnum]);
        }

        for(int rownum = 0; rownum < cells.length; rownum++){
            row = sh.createRow(rownum + 1);
            for(int cellnum = 0; cellnum < cells[rownum].length; cellnum++){
                Cell cell = row.createCell(cellnum);
                setCellValue(cell, cells[rownum][cellnum]);
            }
        }
    }
}