package com.mingrn.itumate.mybatis.generator;

import com.mingrn.itumate.mybatis.generator.util.CodeUtil;
import org.junit.Test;

public class GenerateMain {

    private static final String JDBC_URL = "jdbc:mysql://127.0.1:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=false";
    private static final String JDBC_USERNAME = "root";
    private static final String JDBC_PASSWORD = "admin123";
    private static final String AUTHOR = "zhangshilin <br > zhang.shilin@yilutong.com";
    private static final String PROJECT_PATH = "<!-- FilesOutputDir -->";
    private static final String PROJECT_PACKAGE = "com.yilutong.crm";

    @Test
    public void generate() {
        String[][] tableNames = {
                {"test_tinyint", "", "测试示例", "id", "Long"},
        };

        CodeUtil.create(tableNames, AUTHOR, JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD, PROJECT_PATH, PROJECT_PACKAGE);
    }
}