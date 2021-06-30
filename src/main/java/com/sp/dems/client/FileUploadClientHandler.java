package com.sp.dems.client;

import com.sp.dems.win.WinClientMain;
import com.sp.demsfile.netty.vo.FileUploadFile;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;

public class FileUploadClientHandler extends ChannelInboundHandlerAdapter {
    // 每一次发送预读取字节数量
    private int byteRead;
    // 每一个文件已发送的字节数量
    private volatile long start = 0;
    // 每一次发送实际取字节数量
    private volatile int lastLength = 0;
    public RandomAccessFile randomAccessFile;
    // 传输对象
    private FileUploadFile fileUploadFile;

    DecimalFormat df = new DecimalFormat("#.00");

    public FileUploadClientHandler(FileUploadFile ef) {
        if (ef.getFile().exists()) {
            if (!ef.getFile().isFile()) {
                System.out.println("Not a file :" + ef.getFile());
                return;
            }
        }
        this.fileUploadFile = ef;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        try {
            randomAccessFile = new RandomAccessFile(fileUploadFile.getFile(), "r");
            randomAccessFile.seek(fileUploadFile.getStarPos());
            long length = randomAccessFile.length() / 100;
            lastLength = length > (Integer.MAX_VALUE / 2) ? Integer.MAX_VALUE / 2 : (int)length;
            byte[] bytes = new byte[lastLength];
            if ((byteRead = randomAccessFile.read(bytes)) != -1) {
                fileUploadFile.setEndPos(byteRead);
                fileUploadFile.setBytes(bytes);
                ctx.writeAndFlush(fileUploadFile);
            } else {
                System.out.println("文件已经读完0000000000000000000");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Long) {
            start =  (Long)msg;
            if (start != -1) {
                randomAccessFile = new RandomAccessFile(fileUploadFile.getFile(), "r");
                WinClientMain.setPb((long) (Double.valueOf(start) / Double.valueOf(randomAccessFile.length()) * 100));
                randomAccessFile.seek(start);
                long a = randomAccessFile.length() - start;
                long length = randomAccessFile.length() / 100;
                int b = length > (Integer.MAX_VALUE / 2) ? Integer.MAX_VALUE / 2 : (int)length;

                System.out.println("本次块儿长度：" + b);
                System.out.println("剩余长度：" + a);
                System.out.println("文件总长度：" + randomAccessFile.length());

                if (a < b) {
                    lastLength = (int)a;
                }
                byte[] bytes = new byte[lastLength];
                if ((byteRead = randomAccessFile.read(bytes)) > 0 && a > 0) {
                    System.out.println("发送 byte 长度：" + bytes.length);
                    fileUploadFile.setEndPos(byteRead);
                    fileUploadFile.setBytes(bytes);
                    try {
                        ctx.writeAndFlush(fileUploadFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    randomAccessFile.close();
                    ctx.close();
                    System.out.println("文件已经读完--------" + byteRead);
                }
                randomAccessFile.close();
            }

        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
