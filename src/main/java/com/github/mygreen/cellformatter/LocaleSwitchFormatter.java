package com.github.mygreen.cellformatter;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.github.mygreen.cellformatter.lang.ArgUtils;


/**
 * ロケールによって、フォーマッタを切り替えるフォーマッタ。
 * <p>該当するロケールがない場合、標準のフォーマッタを返す。
 *
 * @version 0.10
 * @author T.TSUCHIE
 *
 */
public class LocaleSwitchFormatter extends CellFormatter {

    private final CellFormatter defaultFormatter;

    private final Map<Locale, CellFormatter> formatterMap = new ConcurrentHashMap<>();

    /**
     * 標準のフォーマッタを指定するコンストラクタ
     * @param defaultFormatter 標準のフォーマッタ。
     * @throws IllegalArgumentException {@literal defaultFormatter == null.}
     */
    public LocaleSwitchFormatter(final CellFormatter defaultFormatter) {
        ArgUtils.notNull(defaultFormatter, "defaultFormatter");
        this.defaultFormatter = defaultFormatter;
    }

    @Override
    public CellFormatResult format(final CommonCell cell, final Locale locale) {

        ArgUtils.notNull(cell, "cell");

        return getCellFormatter(locale).format(cell, locale);
    }

    /**
     * ロケールに対応するセルフォーマットを取得する
     * @param locale ロケール
     * @return ロケールに対応するセルフォーマッタ。ロケールがnullの場合は、デフォルトのフォーマッタを返す。
     */
    private CellFormatter getCellFormatter(final Locale locale) {
        if(locale == null) {
            return defaultFormatter;

        } else if(formatterMap.containsKey(locale)) {
            return formatterMap.get(locale);
        }

        return defaultFormatter;
    }

    /**
     * ローケールとフォーマッタを登録する。
     * @param cellFormatter 登録対象のフォーマッタ。
     * @param locales 登録対象のロケール。複数指定可能。
     * @return 現在の自身のインスタンス。
     * @throws IllegalArgumentException {@literal cellFormatter == null.}
     * @throws IllegalArgumentException {@literal locales == null || locales.length == 0.}
     */
    public LocaleSwitchFormatter register(final CellFormatter cellFormatter, final Locale... locales) {
        ArgUtils.notNull(cellFormatter, "cellFormatter");
        ArgUtils.notEmpty(locales, "locales");

        for(Locale locale : locales) {
            formatterMap.put(locale, cellFormatter);
        }

        return this;
    }

    @Override
    public String getPattern(final Locale locale) {
        return getCellFormatter(locale).getPattern();
    }

}
