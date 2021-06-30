package com.sp.data;

import java.io.IOException;
import java.text.DecimalFormat;

public class TestMain {

    static DecimalFormat df = new DecimalFormat("#.00");


    public static void main(String[] args) throws IOException {
//        SqlSession sqlSession = SessionUtil.getSession();
//        TestMapper mapper = sqlSession.getMapper(TestMapper.class);
//        List<Test> list = mapper.listTest();
//        list.forEach(item ->{
//            System.out.println(item);
//        });
//        SessionUtil.closeSession(sqlSession);
        long length = 11L;
        long aa = 24L;

        int b = 12;
        int d = 24;
        System.out.println((long) (Double.valueOf(length) / Double.valueOf(aa) * 100));
            System.out.println(b/d);

    }
}
