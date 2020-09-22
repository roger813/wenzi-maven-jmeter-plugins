package pers.wenzi.jmeter.plugins.utils;

import org.quartz.listeners.JobChainingJobListener;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: DBUtil
 * @Description: TODO
 * @Author: liwenhuan
 * @Date: 2020/9/21
 */
public class IFR17DBUtil {

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URLSTR = "jdbc:mysql://rm-bp1utr02m6tp303p9.mysql.rds.aliyuncs.com:3306/ifrs_00?useUnicode=true&characterEncoding=UTF-8";
    private static final String USERNAME = "za_dev_16b05df";
    private static final String PASSWORD = "za_dev_16b05df_1a1c56";

    private ResultSet result = null;
    private Connection connection = null;
    private CallableStatement callable = null;
    private PreparedStatement statement = null;

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("加载驱动错误：" + e.getMessage());
            System.exit(1);
        }
    }

    public Connection getConnection() {
        try {
            connection = DriverManager.getConnection(URLSTR, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.err.println("建立连接错误：" + e.getMessage());
        }
        return connection;
    }

    public Object selectOne(String sql, Object[] params) {
        Object object = null;
        try {
            connection = this.getConnection();
            statement = connection.prepareStatement(sql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    statement.setObject(i + 1, params[i]);
                }
            }
            result = statement.executeQuery();
            if(result.next()) {
                object = result.getObject(1);
            }
        } catch (SQLException e) {
            System.err.println("查询出现错误：" + e.getMessage());
        } finally {
            close();
        }
        return object;
    }

    private ResultSet selectRS(String sql, Object[] params) {
        try {
            connection = this.getConnection();
            statement = connection.prepareStatement(sql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    statement.setObject(i + 1, params[i]);
                }
            }
            result = statement.executeQuery();
        } catch (SQLException e) {
            System.err.println("查询出现错误：" + e.getMessage());
        }
        return result;
    }

    public List<Object> selectList(String sql, Object[] params) {
        ResultSet rs = selectRS(sql, params);
        ResultSetMetaData rsmd = null;
        int column = 0;
        try {
            rsmd = rs.getMetaData();
            column = rsmd.getColumnCount();
        } catch (SQLException e) {
            System.err.println("查询出现错误：" + e.getMessage());
        }

        List<Object> list = new ArrayList<Object>();
        try {
            while (rs.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                for (int i = 1; i <= column; i++) {
                    map.put(rsmd.getColumnLabel(i), rs.getObject(i));
                }
                list.add(map);
            }
        } catch (SQLException e) {
            System.err.println("查询出现错误：" + e.getMessage());
        } finally {
            close();
        }
        return list;
    }

    private void close() {
        if (result != null) {
            try {
                result.close();
            } catch (SQLException e) {
                System.err.println("关闭结果错误：" + e.getMessage());
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                System.err.println("关闭查询错误：" + e.getMessage());
            }
        }
        if (callable != null) {
            try {
                callable.close();
            } catch (SQLException e) {
                System.err.println("关闭回调错误：" + e.getMessage());
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("关闭连接错误：" + e.getMessage());
            }
        }
    }


}
