package com.lge.hems.device;

import com.lge.hems.device.utilities.CollectionFactory;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by netsga on 2016. 6. 27..
 */
public class TempTest {

    @Test
    public void replaceTest() {
        String test = "a.b.c.d";
        System.out.println(StringUtils.substring(test, 0, test.lastIndexOf(".d")));
        System.out.println(StringUtils.substring(test, 0, test.lastIndexOf(".d")).concat(".e"));
    }

    @Test
    public void regexTest() {
        String test = "http_get.getDeviceData(localhems,UFNNVF8wMDEuU1QuTGFzdENvbW1UbS5zdFZhbA==)";
        List<String> result = CollectionFactory.newList();
        String exp = "([\\w]+)\\.([\\w]+)\\(([\\w\\W]*)\\)";
        Pattern pattern = Pattern.compile(exp);

        Matcher match = pattern.matcher(test);

        match.find();

        for(int i = 1; i <= match.groupCount(); i++) {
            System.out.println(match.group(i));
            System.out.println(match.group());
        }
    }

    @Test
    public void extractVariableMapFromExpr11() {
        String exp = "\\#\\{(\\w+)\\}";
        Pattern pattern = Pattern.compile(exp);

        String expr = "asdkljfhajksdf #{asdf} awfasdf#{eeeee}112341";
        Map<String, String> result = CollectionFactory.newMap();

        Matcher match = pattern.matcher(expr);

        while(match.find()) {
            result.put(match.group(1), match.group());
        }
        System.out.println(result.toString());
    }

    @Test
    public void extractVariableMapFromExpr22() {
        String exp = "\\$\\{(\\w+)\\}";
        Pattern pattern = Pattern.compile(exp);

        String expr = "asdkljfhajksdf ${asdf} awfasdf${eeeee}112341";
        Map<String, String> result = CollectionFactory.newMap();

        Matcher match = pattern.matcher(expr);

        while(match.find()) {
            result.put(match.group(1), match.group());
        }
        System.out.println(result.toString());
    }

    @Test
    public void stringTest() {
        String test = "test:testtttt";
        System.out.println(test.substring(0, test.indexOf(":")));
        System.out.println(test.substring(test.indexOf(":") + 1, test.length() ));

    }

//    public static List<String> extractVariableListFromExpr(String expr) {
//        List<String> result = CollectionFactory.newList();
//
//        Matcher match = pattern.matcher(expr);
//
//        while(match.find()) {
//            result.add(match.group(1));
//        }
//        return result;
//    }
//

}
