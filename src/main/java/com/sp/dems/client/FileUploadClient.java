package com.sp.dems.client;

import com.sp.demsfile.netty.vo.FileUploadFile;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import javax.swing.*;
import java.io.File;

public class FileUploadClient {

    private static final int MIN_PROGRESS = 0;
    private static final int MAX_PROGRESS = 100;

    private static int currentProgress = MIN_PROGRESS;
    final static JProgressBar progressBar = new JProgressBar();

    public void connect(int port, String host, final FileUploadFile fileUploadFile) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true).handler(new ChannelInitializer<Channel>() {


                @Override
                protected void initChannel(Channel ch) throws Exception {
                    ch.pipeline().addLast(new ObjectEncoder());
                    ch.pipeline().addLast(new ObjectDecoder(ClassResolvers.weakCachingConcurrentResolver(null)));
                    ch.pipeline().addLast(new FileUploadClientHandler(fileUploadFile));
                }
            });
            ChannelFuture f = b.connect(host, port).sync();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        int port = 8082;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        try {
            FileUploadFile uploadFile = new FileUploadFile();
            File file = new File("E:/tt.mp4");
            String fileMd5 = file.getName();// 文件名
            uploadFile.setFile(file);
            uploadFile.setFile_md5(fileMd5);
            uploadFile.setStarPos(0);// 文件开始位置
            uploadFile.setLength(file.length());
            progressBar.setMaximum((int) file.length());
            new FileUploadClient().connect(port, "127.0.0.1", uploadFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void setV(int v){
        progressBar.setValue(v);
    }
}
