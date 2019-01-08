package com.mingrn.keeper.mybatis.generator;

public class GenerateMain {

	private static final String JDBC_URL = "jdbc:mysql://localhost/helper-system";
	private static final String JDBC_USERNAME = "root";
	private static final String JDBC_PASSWORD = "admin";
	private static final String AUTHOR = "MinGRn <br > MinGRn97@gmail.com";
	private static final String PROJECT_PATH = System.getProperty("user.dir");
	private static final String PROJECT_PACKAGE = "com.mingrn.keeper.gateway";

	public static void main(String[] args) {

		System.out.println(PROJECT_PATH);
		String[][] tableNames = {
				{"sys_gateway", "SysGateway", "系统网关", "id", "String"}
		};

//		CodeUtil.create(tableNames, AUTHOR, JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD, PROJECT_PATH, PROJECT_PACKAGE);
	}
}