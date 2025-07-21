package org.lukah.Physics.utils;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.lukah.Physics.math.Constants;

public class MathParser {

    public static float parseFloatOrExpr(String string) {

        if (isNumeric(string)) {

            return Float.parseFloat(string);

        } else {

            Expression expr = new ExpressionBuilder(string)
                    .variable("G")
                    .build()
                    .setVariable("G", (float) Constants.G);
            return (float) expr.evaluate();
        }
    }

    public static boolean isNumeric(String string) {

        return string.matches("^[-+]?\\d*\\.?\\d+$");
    }
}
