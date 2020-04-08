package com.mingrn.itumate.mybatis.generator;

import com.mingrn.itumate.mybatis.generator.util.CodeUtil;

public class GenerateMain {

	private static final String JDBC_URL = "jdbc:mysql://rm-bp12hk8xofys5ihdm973.mysql.rds.aliyuncs.com:3306/oneroad_uat?useUnicode=true&amp;characterEncoding=utf-8";
	private static final String JDBC_USERNAME = "yilutong_uat";
	private static final String JDBC_PASSWORD = "5M4IPc4zwsBSXHCc";
	private static final String AUTHOR = "zhangshilin <br > zhang.shilin@yilutong.com";
	private static final String PROJECT_PATH = "C:\\Users\\MinGR\\Downloads\\opera autoupdate";
	private static final String PROJECT_PACKAGE = "com.yilutong.crm";

	public static void main(String[] args) {

		System.out.println(PROJECT_PATH);
		String[][] tableNames = {
				{"crm_service_assessment", "CrmServiceAssessment", "服务评价数据表", "object_id", "Long"}
		};

		CodeUtil.create(tableNames, AUTHOR, JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD, PROJECT_PATH, PROJECT_PACKAGE);
	}
}