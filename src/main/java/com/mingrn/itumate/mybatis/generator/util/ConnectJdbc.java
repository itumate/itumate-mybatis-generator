package com.mingrn.itumate.mybatis.generator.util;

import com.mingrn.itumate.mybatis.generator.domain.Column;
import com.mingrn.itumate.mybatis.generator.domain.Table;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * 新版驱动url默认地址为127.0.0.1:3306,所以访问本机mysql数据库地址可以用 /// 表示;
 * eg:
 * jdbc.url=jdbc:mysql://127.0.0.1:3306/test?characterEncoding=utf8&useUnicode=true&useSSL=false&serverTimezone=UTC
 * jdbc.url=jdbc:mysql:///test?characterEncoding=utf8&useUnicode=true&useSSL=false&serverTimezone=UTC
 * </p>
 * 另外,新版驱动类位置发生了改变:com.mysql.cj.jdbc.Driver
 * eg:
 * Class.forName("com.mysql.jdbc.Driver");
 * Class.forName("com.mysql.cj.jdbc.Driver");
 * </p>
 * <a href = "https://blog.csdn.net/tb_520/article/details/79676543"></a>
 * <p>
 * java.sql.SQLException: Unknown system variable 'query_cache_size'?
 * <p>
 * You Need
 *
 * @author MinGRn <br > MinGRn97@gmail.com
 * @date 01/10/2018 19:08
 */
public class ConnectJdbc {

    private static String dbUrl;
    private static String dbUser;
    private static String dbPassword;
    private static Connection connection = null;
    private static final String DEFAULT_CLASS_NAME = "com.mysql.jdbc.Driver";
    private static final String CLASS_NAME = "com.mysql.cj.jdbc.Driver";

    private static String ip;
    private static int port;
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectJdbc.class);

    static {
        try {
            Class.forName(DEFAULT_CLASS_NAME);
        } catch (ClassNotFoundException e) {
            try {
                Class.forName(CLASS_NAME);
            } catch (ClassNotFoundException e1) {
                LOGGER.error("can not load jdbc driver", e);
            }
        }

        ip = "localhost";
        port = 3306;
        dbUser = "root";
        dbPassword = "admin123";
        dbUrl = "jdbc:mysql://" + ip + ":" + port + "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
    }

    /**
     * 获取数据库连接
     *
     * @return Connection
     */
    public static void getConnection() {
        try {
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        } catch (SQLException e) {
            LOGGER.error("Get connection failure", e);
        }
    }

    /**
     * 关闭数据库连接
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.error("close connection failure", e);
            }
        }
    }

    /**
     * 查询库
     */
    @SuppressWarnings("unchecked")
    public static List<String> getDatabases() {
        getConnection();

        List<String> databases = new ArrayList<>();
        if (connection == null) {
            LOGGER.error("JDBC Connection Is Null, Exit");
            return Collections.EMPTY_LIST;
        }

        try {
            ResultSet resultSet = connection.prepareStatement("SHOW DATABASES;").executeQuery();
            while (resultSet.next()) {
                databases.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return databases;
    }

    /**
     * 获取指定库数据表
     *
     * @param database 数据库
     * @return 数据库数据表集合
     */
    public static List<Table> getTables(String database) {
        getConnection();

        List<Table> tables = new ArrayList<>();
        if (connection == null) {
            LOGGER.error("JDBC Connection Is Null, Exit");
            return tables;
        }

        try {
            // 不指定库查询所有库所有表
            // 查询数据库元数据信息
            // DatabaseMetaData metaData = connection.getMetaData();

            // 获取数据库 TABLE,这里会查
            // 询所有库的数据不表,返回结果:
            // INDEX 1: database name
            // INDEX 3: table name
			/*resultSet = metaData.getTables(null,null,null, new String[]{"TABLE"});

			while (resultSet.next()) {
				String dbName = resultSet.getString(1);
				String tableName = resultSet.getString(3);

				// 过滤指定库
				if (StringUtils.isNotBlank(database) && !database.equals(dbName)){
					continue;
				}
				tables.add(tableName);
			}*/

            // 指定数据库查询表
			/*PreparedStatement tables1 = connection.prepareStatement("SHOW TABLES from car_owner");
			ResultSet show = tables1.executeQuery();
			while (show.next()){
				System.out.println(show.getString(1));
			}*/

            // 查看数据表注释, 如果不指定 database 将会查询
            // 所有库的 table, 查询语句如下
            // SHOW TABLE STATUS FROM database;

            ResultSet tablesComment = connection.prepareStatement("SHOW TABLE STATUS FROM " + database, new String[]{"name", "comment", "create_time"}).executeQuery();
            while (tablesComment.next()) {
                String tableName = tablesComment.getString("name");
                String tableComment = tablesComment.getString("comment");
                Date createTime = tablesComment.getDate("create_time");
                String createTimeStr = new DateTime(createTime).toString("yyyy-MM-dd HH:mm:ss");
                LOGGER.info("{} - {} - {}", tableName, tableComment, createTimeStr);
                tables.add(new Table(tableName, tableComment, createTime));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return tables;
    }

    public static List<Column> getColumns(String database, String table) {
        getConnection();

        List<Column> columns = new ArrayList<>();
        if (connection == null) {
            LOGGER.error("JDBC Connection Is Null, Exit");
            return columns;
        }

        try {
            // 查询数据表字段及信息
            ResultSet columnSet = connection.prepareStatement("SELECT COLUMN_NAME name, COLUMN_TYPE type, COLUMN_KEY `key`, IS_NULLABLE isNullable, COLUMN_COMMENT `comment` "
                    + "FROM information_schema.columns WHERE TABLE_SCHEMA='" + database + "' AND TABLE_NAME = 'sys_administrative_region';").executeQuery();

            while (columnSet.next()) {
                String name = columnSet.getString("name");
                String type = columnSet.getString("type");
                String key = columnSet.getString("key");
                String isNullable = columnSet.getString("isNullable");
                String comment = columnSet.getString("comment");
                LOGGER.info("{} - {} - {} - {} - {}", name, type, key, isNullable, comment);
                columns.add(new Column(name, type, key, isNullable, comment));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return columns;
    }


    public static void main(String[] args) {

        getColumns("itumate_system", "sys_administrative_region");
    }
}