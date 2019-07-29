package com.xue.sqlSession;

import com.xue.config.Function;
import com.xue.config.MapperBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

public class MyMapperProxy implements InvocationHandler {
    private MySqlSession mySqlSession;

    private MyConfiguration myConfiguration;

    public MyMapperProxy(MyConfiguration myConfiguration, MySqlSession mySqlSession) {
        this.myConfiguration=myConfiguration;
        this.mySqlSession=mySqlSession;
    }

    public Object invoke(Object proxy, Method method, Object[] args){
        MapperBean mapperBean = myConfiguration.readMapper("UserMapper.xml");
        //是否是xml文件对应的接口
        if (!method.getDeclaringClass().getName().equals(mapperBean.getInterfaceName())){
            return null;
        }
        List<Function> list = mapperBean.getList();
        if (null!=list || 0!=list.size()){
            for (Function function :list){
                //id是否和方法名一样
                if (method.getName().equals(function.getFuncName())){
                    return mySqlSession.selectOne(function.getSql(),String.valueOf(args[0]));
                }
            }
        }
        return null;
    }
}
