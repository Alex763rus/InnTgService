package com.example.inntgservice.utils;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.val;

import java.text.DecimalFormat;

import static org.example.tgcommons.constant.Constant.TextConstants.SHIELD;
import static org.example.tgcommons.constant.Constant.TextConstants.EMPTY;

@Getter
@SuperBuilder(setterPrefix = "set", builderMethodName = "init", toBuilder = true)
public class StringUtils {
    private static final String[] shieldingSimbols = {"_", "*", "[", "]", /*"(", ")",*/ "~", "`", ">", "#", /*"+",*/ " -", "=", "|", "{", "}", /*".",*/ "!"};

    public static final DecimalFormat decimalFormat1 = new DecimalFormat("#.##");

    public static String prepareLong(Long source) {
        return source == null ? EMPTY : String.valueOf(source);
    }

    public static String prepareShield(String source) {
        if (source == null || source.equals(EMPTY)) {
            return EMPTY;
        }
        for (String shieldingSimbol : shieldingSimbols) {
            source = source.replace(shieldingSimbol, SHIELD + shieldingSimbol);
        }
        return source;
    }

    private static final Double ZERO = 0.0;
    private static final Double ONE_MILLIARD = 1000000000.0;
    private static final Double ONE_MILLION = 1000000.0;
    private static final Double ONE_THOUSAND = 1000.0;

    public static String convertDoubleFormat(Double value) {
        Double absValue = value.compareTo(ZERO) == 1 ? value : -value;
        val result = new StringBuilder();
        if (absValue.compareTo(ONE_MILLIARD) == 1) {
            return result.append(decimalFormat1.format(value / ONE_MILLIARD)).append(" млрд руб ").toString();
        }
        if (absValue.compareTo(ONE_MILLION) == 1) {
            return result.append(decimalFormat1.format(value / ONE_MILLION)).append(" млн руб ").toString();
        }
        if (absValue.compareTo(ONE_THOUSAND) == 1) {
            return result.append(decimalFormat1.format(value / ONE_THOUSAND)).append(" тыс руб ").toString();
        }
        result.append(decimalFormat1.format(value)).append(" руб ");
        return result.toString();
    }

    public static String prepareTaskId(Long taskId) {
        return String.format("%05d", taskId);
    }

}
