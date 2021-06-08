package com.sp.dems.win;



import com.sp.dems.client.FileUploadClient;
import com.sp.demsfile.netty.vo.FileUploadFile;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WinClientMain {
    private static List<JPanel> jPanelList = new ArrayList<JPanel>();
    private static JPanel[] jPanels = new JPanel[10];
    private static List<JLabel> labels = new ArrayList<JLabel>();
    private static JLabel label = new JLabel();
    private static JProgressBar pb = new JProgressBar();
    public static void main(String[] args) {
        JPanel jPanel=new JPanel();
//        for(int i=0;i<1;i++){
//            JPanel panel = new JPanel();
//            JLabel label = new JLabel();
//            label.setText("label"+i);
//            labels.add(label);
//            panel.add(label);
//            jPanelList.add(panel);
//            jPanel.add(panel);
//        }
        label.setText("暂无文件");
        pb.setMinimum(0);
        jPanel.add(label);
        jPanel.add(pb);
        // 添加进度改变通知
        pb.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                System.out.println("当前进度值: " + pb.getValue() + "; " +
                        "进度百分比: " + pb.getPercentComplete());
            }
        });
        JFrame jFrame = new JFrame("文件上传");
        jFrame.setSize(1000,600);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //向JPanel添加FlowLayout布局管理器，将组件间的横向和纵向间隙都设置为20像素
        jFrame.setContentPane(jPanel);
        jFrame.setVisible(true);

        String url = "D:\\Downloads";
        File file = new File(url);
        File[] tempList = file.listFiles();

        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                System.out.println(tempList[i].getName());
                setText("正在上传:"+tempList[i].getName()+"文件....");
                FileUploadFile uploadFile = new FileUploadFile();
                String fileMd5 = tempList[i].getName();// 文件名
                uploadFile.setFile(tempList[i]);
                uploadFile.setFile_md5(fileMd5);
                uploadFile.setStarPos(0);// 文件开始位置
                uploadFile.setLength(tempList[i].length());
               pb.setMaximum((int) tempList[i].length());
                try {
                    new FileUploadClient().connect(8082, "127.0.0.1", uploadFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        setText("暂未上传文件");
        pb.setMaximum(0);
    }
    public static void setPb(long v){
        pb.setValue((int)v);
    }
    public static void setText(String text){
        label.setText(text);
    }
}
