package com.xue.sqlSession;

import com.xue.bean.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MyExcutor implements Excutor {

    private MyConfiguration xmlConfiguration = new MyConfiguration();

    @Override
    public <T> T query(String sql, Object parameter) {
        Connection connection = getConnection();
        ResultSet set = null;
        PreparedStatement pre = null;

        try {
            pre = connection.prepareStatement(sql);
            //设置参数
            pre.setString(1,parameter.toString());
            set = pre.executeQuery();
            User u = new User();
            //遍历结果
            while (set.next()){
                u.setId(set.getString(1));
                u.setUsername(set.getString(2));
                u.setPassword(set.getString(3));
            }
            return (T)u;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
            if (set!=null){
                    set.close();
                }
            if (pre!=null){
                pre.close();
            }
            if (connection!=null){
                connection.close();
            }
            }catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return null;
    }

    private Connection getConnection() {
        try {
            Connection connection = xmlConfiguration.build("config.xml");
            return connection;
        }catch (Exception e){
            e.printStackTrace();
        }
       return null;
    }
}
