package util.json;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * JSON 字符串校验
 */
public class JsonValidator {

    private CharacterIterator it;

    private char c;

    private int col;

    /**
     * 验证一个字符串是否是合法的JSON串，注意：blank字符串返回结果为true
     * 注意：实例方法，每次校验需要创建实例
     * @param input 要验证的字符串
     * @return true-合法 ，false-非法
     */
    public boolean validate(String input) {
        input = input.trim();
        return valid(input);
    }

    private boolean valid(String input) {
        // 兼容不规范的JSON格式(使用单引号的方式),将单引号替换为双引号
        input = formatJson(input);
        if ("".equals(input))
            return true;
        boolean ret = true;
        it = new StringCharacterIterator(input);
        c = it.first();
        col = 1;
        if (!value()) {
            ret = error("value", 1);
        } else {
            skipWhiteSpace();
            if (c != CharacterIterator.DONE) {
                ret = error("end", col);
            }
        }
        return ret;
    }

    private boolean value() {
        return literal("true") || literal("false") || literal("null") ||
                string() || number() || object() || array();
    }

    private boolean literal(String text) {
        CharacterIterator ci = new StringCharacterIterator(text);
        char t = ci.first();
        if (c != t)
            return false;
        int start = col;
        boolean ret = true;
        for (t = ci.next(); t != CharacterIterator.DONE; t = ci.next()) {
            if (t != nextCharacter()) {
                ret = false;
                break;
            }
        }
        nextCharacter();
        if (!ret)
            error("literal " + text, start);
        return ret;
    }

    private boolean array() {
        return aggregate('[', ']', false);
    }

    private boolean object() {
        return aggregate('{', '}', true);
    }

    private boolean aggregate(char entryCharacter, char exitCharacter,
                              boolean prefix) {
        if (c != entryCharacter)
            return false;
        nextCharacter();
        skipWhiteSpace();
        if (c == exitCharacter) {
            nextCharacter();
            return true;
        }
        for (; ; ) {
            if (prefix) {
                int start = col;
                if (!string())
                    return error("string", start);
                skipWhiteSpace();
                if (c != ':')
                    return error("colon", col);
                nextCharacter();
                skipWhiteSpace();
            }
            if (value()) {
                skipWhiteSpace();
                if (c == ',') {
                    nextCharacter();
                } else if (c == exitCharacter) {
                    break;
                } else {
                    return error("comma or " + exitCharacter, col);
                }
            } else {
                return error("value", col);
            }
            skipWhiteSpace();
        }
        nextCharacter();
        return true;
    }

    private boolean number() {
        if (!Character.isDigit(c) && c != '-')
            return false;
        int start = col;
        if (c == '-')
            nextCharacter();
        if (c == '0') {
            nextCharacter();
        } else if (Character.isDigit(c)) {
            while (Character.isDigit(c))
                nextCharacter();
        } else {
            return error("number", start);
        }
        if (c == '.') {
            nextCharacter();
            if (Character.isDigit(c)) {
                while (Character.isDigit(c))
                    nextCharacter();
            } else {
                return error("number", start);
            }
        }
        if (c == 'e' || c == 'E') {
            nextCharacter();
            if (c == '+' || c == '-') {
                nextCharacter();
            }
            if (Character.isDigit(c)) {
                while (Character.isDigit(c))
                    nextCharacter();
            } else {
                return error("number", start);
            }
        }
        return true;
    }

    private boolean string() {
        if (c != '"')
            return false;
        int start = col;
        boolean escaped = false;
        for (nextCharacter(); c != CharacterIterator.DONE; nextCharacter()) {
            if (!escaped && c == '\\') {
                escaped = true;
            } else if (escaped) {
                if (!escape()) {
                    return false;
                }
                escaped = false;
            } else if (c == '"') {
                nextCharacter();
                return true;
            }
        }
        return error("quoted string", start);
    }

    private boolean escape() {
        int start = col - 1;
        if (" \\\"/bfnrtu".indexOf(c) < 0) {
            return error(
                    "escape sequence  \\\",\\\\,\\/,\\b,\\f,\\n,\\r,\\t  or  \\uxxxx ",
                    start);
        }
        if (c == 'u') {
            if (!ishex(nextCharacter()) || !ishex(nextCharacter()) ||
                    !ishex(nextCharacter())
                    || !ishex(nextCharacter())) {
                return error("unicode escape sequence  \\uxxxx ", start);
            }
        }
        return true;
    }

    private boolean ishex(char d) {
        return "0123456789abcdefABCDEF".indexOf(d) >= 0;
    }

    private char nextCharacter() {
        c = it.next();
        ++col;
        return c;
    }

    private void skipWhiteSpace() {
        while (Character.isWhitespace(c)) {
            nextCharacter();
        }
    }

    private boolean error(String type, int col) {
        System.out.printf("type: %s, col: %s%s",
                type,
                col,
                System.getProperty("line.separator"));
        return false;
    }

    /**
     * 格式化json字符串
     *
     * @param json
     * @return
     */
    public static String formatJson(String json) {
        String replaceAll = json.trim().replaceAll("'", "\"").replace(" ", "");
        char[] charArray = replaceAll.toCharArray();
        String regEx = "^[A-Za-z]+$";
        Pattern pat = Pattern.compile(regEx);
        List<Integer> list = Lists.newLinkedList();
        StringBuilder builder = new StringBuilder(replaceAll);
        for (int i = 0; i < charArray.length - 1; i++) {
            if (i < charArray.length) {
                String current = String.valueOf(charArray[i]).trim();
                if (StringUtils.isEmpty(current.trim())) {
                    builder.deleteCharAt(i);
                    continue;
                } else {
                    boolean flagcurrent = pat.matcher(current).find();
                    String last = String.valueOf(charArray[i + 1]).trim();
                    boolean flaglast = pat.matcher(last).find();
                    /*
                     * 如果当前是标点，则是false，如果是字母则是true，下一个(last)是字母的，则继续，如果是标点则检查是不是双引号，
                     * 不是则记下标志位
                     */
                    if (flagcurrent) {
                        if (!flaglast) {
                            if (!check(last))
                                list.add(i + 1);
                        }
                    } else {
                        /*
                         * 如果当前不是标点，则检查下一个(last)是不是字符串，如果是，则看当前标点是不是双引号，不是，则记录位置
                         * 如果当前不是字符串并且last是字符串的情况下，检查当前是不是引号
                         */
                        if (flaglast) {
                            if (!check(current)) {
                                list.add(i + 1);
                            }
                        }
                    }

                }
            }
        }
        //StringBuilder builder = new StringBuilder(replaceAll);
        for (int i = 0; i < list.size(); i++) {
            builder.insert(list.get(i) + i, "\"");
        }
        return builder.toString();
    }

    public static boolean check(String checkString) {
        boolean flag = true;
        switch (checkString) {
            case "{":
                flag = false;
                break;
            case "}":
                flag = false;
                break;
            case "[":
                flag = false;
                break;
            case ",":
                flag = false;
                break;
            case ":":
                flag = false;
                break;
            case "\"":
                flag = true;
                break;
        }
        return flag;
    }

    public static void main(String[] args) {
        String json =
                "{\"firstName\" : Brett, 'lastName':'McLaughlin', 'email': 'aaaa}";
        boolean validate = new JsonValidator().validate(json);
        System.out.println(validate);
        System.out.println(formatJson(json));
        String s = "   ";
        s = formatJson(s);
        System.out.println(s.length());
        System.out.println(new JsonValidator().validate("   "));
    }

}
