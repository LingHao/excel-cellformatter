package com.github.mygreen.cellformatter;

import java.util.Locale;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.github.mygreen.cellformatter.lang.ArgUtils;
import com.github.mygreen.cellformatter.lang.Utils;


/**
 * POIのセルの値を文字列として取得する。
 * 参考URL
 * <ul>
 *   <li><a href="http://www.ne.jp/asahi/hishidama/home/tech/apache/poi/cell.html"></a></li>
 *   <li><a href="http://shin-kawara.seesaa.net/article/159663314.html">POIでセルの値をとるのは大変　日付編</a></li>
 *   
 * @author T.TSUCHIE
 *
 */
public class POICellFormatter {
    
    private FormatterResolver formatterResolver = new FormatterResolver();
    
    /**
     * セルの値を文字列として取得する
     * @param cell 取得対象のセル
     * @return
     */
    public String format(final Cell cell) {
        return format(cell, Locale.getDefault());
    }
    
    /**
     * ロケールを指定してセルの値を文字列として取得する
     * @param cell 取得対象のセル
     * @param locale ロケール
     * @return フォーマットした文字列
     * @throws IllegalArgumentException cell is null.
     */
    public String format(final Cell cell, final Locale locale) {
        ArgUtils.notNull(cell, "cell");
        
        switch(cell.getCellType()) {
            case Cell.CELL_TYPE_BLANK:
                // 結合しているセルの場合、左上のセル以外に値が設定されている場合がある。
                return getMergedCellValue(cell, locale);
                
            case Cell.CELL_TYPE_BOOLEAN:
                return getOtherCellValue(cell, locale);
                
            case Cell.CELL_TYPE_STRING:
                return getOtherCellValue(cell, locale);
                
            case Cell.CELL_TYPE_NUMERIC:
                return getNumericCellValue(cell, locale);
                
            case Cell.CELL_TYPE_FORMULA:
                return getFormulaCellValue(cell, locale);
                
            case Cell.CELL_TYPE_ERROR:
                return "";
                
            default:
                return "";
        }
    }
    
    /**
     * 式が設定されているセルの値を評価する。
     * @param cell
     * @param locale
     * @return
     */
    private String getFormulaCellValue(final Cell cell, final Locale locale) {
        
        final int cellType = cell.getCellType();
        if(cellType != Cell.CELL_TYPE_FORMULA) {
            throw new IllegalArgumentException(String.format("cell type should be FORMULA, but %d.", cellType));
        }
        
        final Workbook workbook = cell.getSheet().getWorkbook();
        final CreationHelper helper = workbook.getCreationHelper();
        final FormulaEvaluator evaluator = helper.createFormulaEvaluator();
        
        // 再帰的に処理する
        final Cell evalCell = evaluator.evaluateInCell(cell);
        return format(evalCell, locale);
        
    }
    
    /**
     * 結合されているセルの値の取得。
     * <p>通常は左上のセルに値が設定されているが、結合されているときは左上以外のセルの値を取得する。
     * <p>左上以外のセルに値が設定されている場合は、CellTypeがCELL_TYPE_BLANKになるため注意が必要。
     * @param cell
     * @return
     */
    private String getMergedCellValue(final Cell cell, final Locale locale) {
        
        final int rowIndex = cell.getRowIndex();
        final int columnIndex = cell.getColumnIndex();
        
        final Sheet sheet = cell.getSheet();
        final int size = sheet.getNumMergedRegions();
        
        for(int i=0; i < size; i++) {
            final CellRangeAddress range = sheet.getMergedRegion(i);
            if(range.isInRange(rowIndex, columnIndex)) {
                final Cell firstCell = getCell(sheet, range.getFirstColumn(), range.getFirstRow());
                return format(firstCell, locale);
            }
        }
        
        return "";
    }
    
    /**
     * シートから任意のセルを取得する。
     * 
     * @see jxl.Sheet.getCell(int column, int row)
     * @param sheet
     * @param column
     * @param row
     * @return
     */
    private static Cell getCell(final Sheet sheet, final int column, final int row) {
        ArgUtils.notNull(sheet, "sheet");
        
        Row rows = sheet.getRow(row);
        if(rows == null) {
            rows = sheet.createRow(row);
        }
        
        Cell cell = rows.getCell(column);
        if(cell == null) {
            cell = rows.createCell(column, Cell.CELL_TYPE_BLANK);
        }
        
        return cell;
    }
    
    /**
     * 数値以外ののフォーマット
     * @return
     */
    private String getOtherCellValue(final Cell cell, final Locale locale) {
        
        final int cellType = cell.getCellType();
        if(!(cellType != Cell.CELL_TYPE_STRING || cellType != Cell.CELL_TYPE_BOOLEAN)) {
            throw new IllegalArgumentException(String.format("cell type should be String or Boolean, but %d.", cellType));
        }
        
        // セルの書式の取得。
        final POICell poiCell = new POICell(cell);
        final short formatIndex = poiCell.getFormatIndex();
        final String formatPattern = poiCell.getFormatPattern();
        
        if(formatterResolver.canResolve(formatIndex)) {
            final CellFormatter cellFormatter = formatterResolver.getFormatter(formatIndex);
            return cellFormatter.format(poiCell, locale);
            
        } else if(formatterResolver.canResolve(formatPattern)) {
            final CellFormatter cellFormatter = formatterResolver.getFormatter(formatPattern);
            return cellFormatter.format(poiCell, locale);
            
        } else if(Utils.isNotEmpty(formatPattern)) {
            final CellFormatter cellFormatter = formatterResolver.createFormatter(formatPattern) ;
            formatterResolver.registerFormatter(formatPattern, cellFormatter);
            return cellFormatter.format(poiCell, locale);
            
        } else {
            // 書式を持たない場合は、そのまま返す。
            return poiCell.getTextCellValue();
        }
        
    }
    
    /**
     * 数値型のセルの値を取得する。
     * <p>書式付きの数字か日付のどちらかの場合がある。
     * @param cell
     * @param locale
     * @return
     */
    private String getNumericCellValue(final Cell cell, final Locale locale) {
        
        final int cellType = cell.getCellType();
        if(cellType != Cell.CELL_TYPE_NUMERIC) {
            throw new IllegalArgumentException(String.format("cell type should be NUMERIC, but %d.", cellType));
        }
        
        // セルの書式の取得。
        final POICell poiCell = new POICell(cell);
        final short formatIndex = poiCell.getFormatIndex();
        final String formatPattern = poiCell.getFormatPattern();
        
        if(formatterResolver.canResolve(formatIndex)) {
            final CellFormatter cellFormatter = formatterResolver.getFormatter(formatIndex);
            return cellFormatter.format(poiCell, locale);
            
        } else if(formatterResolver.canResolve(formatPattern)) {
            final CellFormatter cellFormatter = formatterResolver.getFormatter(formatPattern);
            return cellFormatter.format(poiCell, locale);
            
        } else {
            // キャッシュに登録する。
            final CellFormatter cellFormatter = formatterResolver.createFormatter(formatPattern) ;
            formatterResolver.registerFormatter(formatPattern, cellFormatter);
            return cellFormatter.format(poiCell, locale);
            
        }
    }
    
    public FormatterResolver getFormatterResolver() {
        return formatterResolver;
    }
    
    public void setFormatterResolver(FormatterResolver formatterResolver) {
        this.formatterResolver = formatterResolver;
    }
}
