package com.xue;

import com.xue.bean.User;
import com.xue.mapper.UserMapper;
import com.xue.sqlSession.MySqlSession;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        MySqlSession sqlSession = new MySqlSession();
        UserMapper mapper =sqlSession.getMapper(UserMapper.class);
        User user = mapper.getUserById("1");
        System.out.println( user );
    }
}
