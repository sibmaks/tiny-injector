package xyz.tiny.injector.utils;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
public class StringUtils {
    private StringUtils() {

    }

    public static String toLowerCase1st(String str) {
        if(str == null) {
            return null;
        }
        if("".equals(str)) {
            return "";
        }
        char[] simpleName = str.toCharArray();
        simpleName[0] += 32;
        return new String(simpleName);
    }
}
