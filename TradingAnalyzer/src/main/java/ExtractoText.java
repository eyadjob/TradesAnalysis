import groovy.json.JsonSlurper;

import java.util.HashMap;
import java.util.Map;

public class ExtractoText {

    public static void main(String[] args) {
        System.out.println(isNumeric("test"));
        System.out.println(isNumeric("222"));
        System.out.println(isNumeric("3.97889798234"));
        System.out.println(round(144.0666666667));
        System.out.println(round(144.06666666666666666666666667));
        System.out.println(getFieldLabelString("leasing-quotation-basic-info.fieldEyadLabel.fields.labels.byxpath"));
    }


    public static String getFieldLabelString(String value) {
       return value.split("\\.")[1];
    }

    public static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
    public static boolean isNumeric(String str) {
        if (str == null || str.trim().isEmpty()) {
            return false;
        }

        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
