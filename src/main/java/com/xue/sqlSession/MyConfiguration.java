package com.xue.sqlSession;

import com.xue.config.Function;
import com.xue.config.MapperBean;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;

public class MyConfiguration {

    private static ClassLoader loader = ClassLoader.getSystemClassLoader();

    /**
     * 读取xml信息并处理
     */
    public Connection build(String resource){
        InputStream stream = loader.getResourceAsStream(resource);
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(stream);
            Element root = document.getRootElement();
            return evalDataSource(root);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private Connection evalDataSource(Element node) throws ClassNotFoundException{
        if (!node.getName().equals("database")){
            throw new RuntimeException("root should be <database>");
        }
        String driverClassName = null;
        String url = null;
        String username = null;
        String password = null;

        //获取属性节点
        for (Object item :node.elements("property")){
            Element i =(Element) item;
            String value = getValue(i);
            String name = i.attributeValue("name");
            if(null == name || null == value){
                throw new RuntimeException("[database]:<property> should contain name and value");
            }
            //赋值
            switch(name) {
                case "url":
                    url = value;
                    break;
                case "username":
                    username = value;
                    break;
                case "password":
                    password = value;
                    break;
                case "driverClassName":
                    driverClassName = value;
                    break;
                default:
                    throw new RuntimeException("[database]:<property> unknown name");
            }
        }
        Class.forName(driverClassName);
        Connection connection = null;

        try {
            //建立数据库连接
            connection = DriverManager.getConnection(url,username,password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    //获取property属性的值,如果有value的值，则读取，没有设置value,则读取内容
    private String getValue(Element node){
        return node.hasContent() ? node.getText() : node.attributeValue("value");
    }

    public MapperBean readMapper(String path){
        MapperBean mapperBean = new MapperBean();

        try {
            InputStream stream = loader.getResourceAsStream(path);
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(stream);
            Element root = document.getRootElement();
            //把mapper节点的nameSpace存为接口名
            mapperBean.setInterfaceName(root.attributeValue("nameSpace").trim());
           //用来存储方法的list
            List<Function> list = new ArrayList<Function>();
            //遍历根节点下所有的子节点
            for (Iterator rootIter = root.elementIterator();rootIter.hasNext();){
                Function fun = new Function();
                Element e = (Element)rootIter.next();
                String sqltype = e.getName().trim();
                String funcName = e.attributeValue("id").trim();
                String sql =e.getText().trim();
                String resultType = e.attributeValue("resultType").trim();
                fun.setSqltype(sqltype);
                fun.setFuncName(funcName);
                Object newInstance = null;
                try {
                    newInstance = Class.forName(resultType).newInstance();
                } catch (InstantiationException e1) {
                    e1.printStackTrace();
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
                fun.setResultType(newInstance);
                fun.setSql(sql);
                list.add(fun);
            }
            mapperBean.setList(list);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return mapperBean;
    }




}
