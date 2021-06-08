package com.sp.data.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * mybatis 管理工具
 */
public class SessionUtil {
    private static InputStream inputStream = null;
    private static SqlSessionFactory sqlSessionFactory;
    static {
        String resource = "mybatis-config.xml";

        try {
            inputStream = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    }

    public static SqlSession getSession(){
       return sqlSessionFactory.openSession(true);
    }

    public static void closeSession(SqlSession sqlSession){
        sqlSession.close();
    }
}
