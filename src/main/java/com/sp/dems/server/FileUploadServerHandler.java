package com.sp.dems.server;

import com.sp.demsfile.netty.vo.FileUploadFile;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.io.RandomAccessFile;

public class FileUploadServerHandler extends ChannelInboundHandlerAdapter {
    private int byteRead;
    private volatile int start = 0;
    private String file_dir = "E:\\dems";
    private RandomAccessFile randomAccessFile;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FileUploadFile) {
            FileUploadFile ef = (FileUploadFile) msg;
            byte[] bytes = ef.getBytes();
            byteRead = ef.getEndPos();
            String md5 = ef.getFile_md5();//文件名
            String path = file_dir + File.separator + md5;
            File file = new File(path);
            if(!file.exists()){
                file.createNewFile();
            }
            randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(start);
            randomAccessFile.write(bytes);
            start = start + byteRead;
            System.out.println("start:"+start);
            System.out.println("byteRead:"+byteRead);
            if (byteRead > 0) {
                ctx.writeAndFlush(start);
            }
            randomAccessFile.close();
            if(start >= ef.getLength()){
                System.out.println("==============文件读完=============");
                randomAccessFile.close();
                ctx.close();
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
