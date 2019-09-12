package weben.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;


public class InitDB {

    public InitDB() {

    }

    public static String connmysql() {
        String res;
        String dbHost = System.getenv("MYSQLVMALL_SERVICE_HOST");
        String dbPort = System.getenv("MYSQLVMALL_SERVICE_PORT");
        if (System.getenv("MYSQLVMALL_SERVICE_HOST") == null || System.getenv("MYSQLVMALL_SERVICE_HOST").equals("")) {
            dbHost = "192.168.73.229";
            dbPort = "32441";
        }

        Connection conn;
        String url = "JDBC:mysql://" + dbHost + ":" + dbPort + "/mysql?useUnicode=true&characterEncoding=UTF-8";
        System.out.println("数据库：" + url);
        String username = "root";
        String password = "123456";

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(url, username, password);
            ScriptRunner runner = new ScriptRunner(conn);
            Resources.setCharset(Charset.forName("utf8")); //设置字符集,不然中文乱码插入错误
            runner.setLogWriter(null);//设置是否输出日志
            //在resouse中新建一个文件夹：然后放入sql文件

            runner.runScript(Resources.getResourceAsReader("vmall.sql"));
            //runner.runScript(Resources.getResourceAsReader("sql/CC21-01.sql"));
            runner.closeConnection();
            conn.close();
            res = "成功初始化数据库！";
        } catch (Exception e) {
            e.printStackTrace();
            res = "初始化数据库失败！";
        }
        return res;
    }


}
