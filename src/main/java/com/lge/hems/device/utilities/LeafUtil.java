package com.lge.hems.device.utilities;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by netsga on 2016. 6. 30..
 */
public class LeafUtil {
    private final static String LEAF_EXP = "([\\w\\W]+)\\.([\\w\\W]+)\\(([\\w\\W]*)\\)";
    private final static String REQ_PARAM_EXP = "\\#\\{(\\w+)\\}";
//    private final static String MODEL_PARAM_EXP = "\\$\\{(\\w+)\\}";
    private static Pattern leafPattern = Pattern.compile(LEAF_EXP);
    private static Pattern paramPattern = Pattern.compile(REQ_PARAM_EXP);
//    private static Pattern modelParamPattern = Pattern.compile(MODEL_PARAM_EXP);

    public final static String TYPE = "type";
    public final static String METHOD = "method";
    public final static String PARAMETERS = "parameters";

    public static Map<String, String> leafInfoExtractor(String str) {
        Map<String, String> result = CollectionFactory.newMap();
        if(str != null && !str.isEmpty()) {
	        Matcher match = leafPattern.matcher(str);
	
	        if(match.find()) {
	            result.put(TYPE, match.group(1));
	            result.put(METHOD, match.group(2));
	            result.put(PARAMETERS, match.group(3));
	        }
        }
        return result;
    }

//    public static Map<String, String> modelParameterExtractor(String str) {
//        Map<String, String> result = CollectionFactory.newMap();
//
//        Matcher match = modelParamPattern.matcher(str);
//
//        while(match.find()) {
//            result.put(match.group(1), match.group());
//        }
//        return result;
//    }

    public static Map<String, String> parameterExtractor(String str) {
        Map<String, String> result = CollectionFactory.newMap();

        Matcher match = paramPattern.matcher(str);

        while(match.find()) {
            result.put(match.group(1), match.group());
        }
        return result;
    }
}
