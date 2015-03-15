package com.github.mygreen.cellformatter.number;


/**
 * 数値の部分を表現するクラス。
 * @version 0.4
 * @since 0.4
 * @author T.TSUCHIE
 *
 */
public enum PartType {
    
    /** 整数部分 */
    Integer,
    
    /** 小数部分 */
    Decimal,
    
    /** 指数部分 */
    Exponent,
    
    /** 分数の分母部分 */
    Denominator,
    
    /** 分数の分子部分 */
    Numerator,
    
    /** 帯分数の整数部分 */
    WholeNumber,
    
    ;
    
}
