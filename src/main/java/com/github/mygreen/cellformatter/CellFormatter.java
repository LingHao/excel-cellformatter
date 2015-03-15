package com.github.mygreen.cellformatter;

import java.util.Locale;


/**
 * セルのフォーマッタの共通インタフェース
 * @author T.TSUCHIE
 *
 */
public abstract class CellFormatter {
    
    /**
     * セルの値をフォーマットする
     * @param cell
     * @return
     */
    public String format(final CommonCell cell) {
        return format(cell, Locale.getDefault());
    }
    
    /**
     * セルの値をロケールを指定してフォーマットする
     * @param cell
     * @param locale
     * @return
     */
    public abstract String format(final CommonCell cell, final Locale locale);
    
}
