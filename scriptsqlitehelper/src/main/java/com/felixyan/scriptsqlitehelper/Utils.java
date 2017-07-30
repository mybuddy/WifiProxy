package com.felixyan.scriptsqlitehelper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 工具类
 *
 * Created by yanfei on 2017/07/30.
 */
class Utils {
    /**
     * 按分隔符拆分SQL语句
     *
     * @param script SQL脚本
     * @param delimiter SQL语句分隔符
     * @return
     */
    static List<String> splitSqlScript(String script, char delimiter) {
        ArrayList<String> statements = new ArrayList<>(); // 按行存放SQL语句
        StringBuilder sb = new StringBuilder(); // 一行SQL语句
        boolean inLiteral = false;
        char[] content = script.toCharArray();

        for(int i = 0; i < script.length(); ++i) {
            // 双引号
            if(content[i] == 34) {
                inLiteral = !inLiteral;
            }

            if(content[i] == delimiter && !inLiteral) {
                if(sb.length() > 0) {
                    statements.add(sb.toString().trim());
                    sb = new StringBuilder();
                }
            } else {
                sb.append(content[i]);
            }
        }

        if(sb.length() > 0) {
            statements.add(sb.toString().trim());
        }

        return statements;
    }

    /**
     * 将流转换为字符串
     *
     * @param is 输入流
     * @return
     */
    static String convertStreamToString(InputStream is) {
        return (new Scanner(is)).useDelimiter("\\A").next();
    }
}
