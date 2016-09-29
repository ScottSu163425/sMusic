package com.su.scott.slibrary.util;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * StringUtil
 */
public class StringUtil {

    private StringUtil() {
    }

    /**
     * 判断字符串是否由纯英文字母组成
     *
     * @param str
     * @return
     */
    public static boolean isPureLetters(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (!Character.isLetter(c)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 判断字符串是否由纯数字组成
     *
     * @param str
     * @return
     */
    public static boolean isPureDigits(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (!Character.isDigit(c)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 判断字符串是否由英文字母和数字组成
     *
     * @param str
     * @return
     */
    public static boolean isLettersAndDigits(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }

        int letterCount = 0, digitCount = 0;

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isDigit(c)) {
                digitCount++;
            }
            if (Character.isLetter(c)) {
                letterCount++;
            }
        }

        if (letterCount > 0 && digitCount > 0 && letterCount + digitCount == str.length()) {
            return true;
        }

        return false;
    }

    /**
     * 判断是否包含关键字（忽略字母大小写）
     * @param str
     * @param keyword
     * @return
     */
    public static boolean isContainsKeywordIgnoreCase(String str, String keyword) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(keyword)) {
            return false;
        }

        if (str.length() < keyword.length()) {
            return false;
        }

        char[] strChars = str.toCharArray();
        char[] keyChars = keyword.toCharArray();
        int strStartIndex = 0, keyStartIndex = 0;

        for (int i = 0; i <= strChars.length - keyChars.length; i++) {

            for (int j = strStartIndex; j < strChars.length; j++) {
                char cs = strChars[j];
                boolean needSkip = false;

                for (int k = keyStartIndex; k < keyChars.length; k++) {
                    char ck = keyChars[k];
                    boolean isMatched = false;

                    if (Character.isLetter(cs)) {
                        //Ignore case
                        isMatched = String.valueOf(cs).equalsIgnoreCase(String.valueOf(ck));
                    } else {
                        isMatched = cs == ck;
                    }

                    if (isMatched) {
                        keyStartIndex++;
                        break;
                    } else {
                        keyStartIndex = 0;
                        strStartIndex++;
                        needSkip = true;
                        break;
                    }
                }

                if (keyStartIndex == keyChars.length) {
                    return true;
                }

                if (needSkip) {
                    break;
                }
            }
        }

        return false;
    }


    /**
     * 字符串开头空格
     *
     * @param strOri
     * @param spaceNum
     * @return
     */
    public static String addBeginSpace(String strOri, int spaceNum) {
        String spaceStep = "  ";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < spaceNum; i++) {
            result.append(spaceStep);
        }
        result.append(strOri);
        return result.toString();
    }

    /**
     * 针对TextView显示中文中出现的排版错乱问题，通过调用此方法得以解决
     *
     * @param str
     * @return 返回全部为全角字符的字符串
     */
    public static String toDBC(String str) {
        char[] c = str.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375) {
                c[i] = (char) (c[i] - 65248);
            }

        }
        return new String(c);
    }

}