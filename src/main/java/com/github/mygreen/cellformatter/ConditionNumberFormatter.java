package com.github.mygreen.cellformatter;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

import com.github.mygreen.cellformatter.callback.Callback;
import com.github.mygreen.cellformatter.number.FormattedNumber;
import com.github.mygreen.cellformatter.number.NumberFactory;
import com.github.mygreen.cellformatter.term.NumberTerm;
import com.github.mygreen.cellformatter.term.Term;
import com.github.mygreen.cellformatter.tokenizer.Token;


/**
 * 数値のフォーマッタ
 * @author T.TSUCHIE
 *
 */
public class ConditionNumberFormatter extends ConditionFormatter {
    
    /**
     * 各書式の項
     */
    private List<Term<FormattedNumber>> terms = new CopyOnWriteArrayList<>();
    
    /**
     * フォーマット対象の数値を作成する
     */
    private NumberFactory numberFactory;
    
    public ConditionNumberFormatter(final String pattern) {
        super(pattern);
    }
    
    @Override
    public FormatterType getType() {
        return FormatterType.Number;
    }
    
    @Override
    public boolean isMatch(final CommonCell cell) {
        final double value = cell.getNumberCellValue();
        return getOperator().isMatch(value);
    }
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public String format(final CommonCell cell, final Locale runtimeLocale) {
        
        final double number = cell.getNumberCellValue();
        final FormattedNumber numObj = numberFactory.create(number);
        
        final StringBuilder sb = new StringBuilder();
        if(getOperator().equals(ConditionOperator.ALL) && numObj.isNegative()) {
            //条件がALLの時に符号を付ける。
            sb.append("-");
        }
        
        for(Term<FormattedNumber> term : terms) {
            sb.append(term.format(numObj, getLocale(), runtimeLocale));
        }
        
        //TODO: 項目ごとに特殊条件の処理を行う。
        
        // 特殊条件の処理を行う。
        String value = sb.toString();
        for(Callback callback : getCallbacks()) {
            value = callback.call(number, value);
        }
        
        return value;
    }
    
    /**
     * フォーマットの項を追加する。
     * @param term
     */
    public void addTerm(final Term<FormattedNumber> term) {
        this.terms.add(term);
    }
    
    /**
     * フォーマットの項を全て取得する。
     * @return
     */
    public List<Term<FormattedNumber>> getTerms() {
        return terms;
    }
    
    /**
     * 記号用の項の中で、指定した記号を含むかどうか。
     * @param symbol
     * @return
     */
    public boolean containsSymbolTerm(Token.Symbol symbol) {
        
        for(Term<FormattedNumber> term : terms) {
            if(!(term instanceof NumberTerm.SymbolTerm)) {
                continue;
            }
            
            final NumberTerm.SymbolTerm symbolTerm = (NumberTerm.SymbolTerm) term;
            if(symbolTerm.getToken().equals(symbol)) {
                return true;
            }
        }
        
        return false;
        
    }
    
    public NumberFactory getNumberFactory() {
        return numberFactory;
    }
    
    public void setNumberFactory(NumberFactory numberFactory) {
        this.numberFactory = numberFactory;
    }
}
