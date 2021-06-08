package com.sp.data;

import java.io.File;
import java.io.IOException;

public class TestMain {
    public static void main(String[] args) throws IOException {
//        SqlSession sqlSession = SessionUtil.getSession();
//        TestMapper mapper = sqlSession.getMapper(TestMapper.class);
//        List<Test> list = mapper.listTest();
//        list.forEach(item ->{
//            System.out.println(item);
//        });
//        SessionUtil.closeSession(sqlSession);

        File file = new File("E:\\dems\\test.txt");
        if(!file.exists()){
            file.createNewFile();
        }

    }
}
