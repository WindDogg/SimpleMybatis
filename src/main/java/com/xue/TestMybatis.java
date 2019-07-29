package com.xue;

import com.xue.bean.User;
import com.xue.mapper.UserMapper;
import com.xue.sqlSession.MySqlSession;

public class TestMybatis {
    public static void main(String[] args) {
        MySqlSession mySqlSession = new MySqlSession();
        UserMapper userMapper = mySqlSession.getMapper(UserMapper.class);
        User user = userMapper.getUserById("1");
        System.out.println(user.toString());
    }
}
