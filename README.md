# SimpleMybatis
## 手写mybatis
 - 读取xml文件，建立连接
 -- MyConfiguration 读取xml后，将属性和连接数据库的操作封装在MyConfiguration对象中供后面的组件调用。本项目使用dom4j来读取xml文件。
 - 创建SqlSession,搭建Configuration和Executor之间的桥梁
 - 创建Executor,封装JDBC操作数据库
 - 创建MapperProxy,使用动态代理生成Mapper对象
