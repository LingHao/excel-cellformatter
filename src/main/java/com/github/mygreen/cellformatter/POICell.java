package com.github.mygreen.cellformatter;

import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormat;

import com.github.mygreen.cellformatter.lang.ArgUtils;


/**
 * POIのセル
 * @author T.TSUCHIE
 *
 */
public class POICell implements CommonCell {
    
    private final Cell cell;
    
    public POICell(final Cell cell) {
        ArgUtils.notNull(cell, "cell");
        this.cell = cell;
    }
    
    public Cell getCell() {
        return cell;
    }
    
    @Override
    public short getFormatIndex() {
        final short formatIndex = getCell().getCellStyle().getDataFormat();
        return formatIndex;
    }
    
    @Override
    public String getFormatPattern() {
        final DataFormat dataFormat = cell.getSheet().getWorkbook().createDataFormat();
        final short formatIndex = getFormatIndex();
        
        String formatPattern = null;
        try {
            formatPattern = dataFormat.getFormat(formatIndex);
        } catch(Exception e) {
            formatPattern = "";
        }
        
        if(formatPattern == null) {
            formatPattern = "";
        }
        
        return formatPattern;
        
    }
    
    @Override
    public boolean isText() {
        return cell.getCellType() == Cell.CELL_TYPE_STRING || cell.getCellType() == Cell.CELL_TYPE_BOOLEAN;
    }
    
    @Override
    public String getTextCellValue() {
        if(cell.getCellType() == Cell.CELL_TYPE_STRING) {
            return cell.getStringCellValue();
            
        } else if(cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
            return Boolean.toString(cell.getBooleanCellValue()).toUpperCase();
        }
        return "";
    }
    
    @Override
    public double getNumberCellValue() {
        return cell.getNumericCellValue();
    }
    
    @Override
    public Date getDateCellValue() {
        final Date date = cell.getDateCellValue();
        return date;
    }
    
}
