package gui.form;

import gui.frame.MainFrame;
import model.CmdAndResult;
import util.ExecelUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static util.SerialPortUtil.printHexString;

public class MainFrom {
    public static MainFrom instance = new MainFrom();

    public JList list1;
    private JButton bt_back;
    private Boolean stopListenSericalPort_flag;

    public JPanel getRootForm() {
        return rootForm;
    }

    private JPanel rootForm;
    private JTextArea textArea1;
    CmdAndResult[] cmdAndResults;
    private String pathname;

    public MainFrom() {
//        add(rootForm);
//        setTitle("串口检测");
//        setSize(800,600);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        bt_back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopListenSericalPort_flag = false;
                stopListenSerialPort();
            }
        });
    }

    private void stopListenSerialPort() {
        SettingForm.instance.chosenPort.closePort();
        MainFrame.instance.settingFormPanel.setEnabled(true);
        MainFrame.instance.settingFormPanel.setVisible(true);
        MainFrame.instance.mainFromPanel.setVisible(false);
        MainFrame.instance.mainFromPanel.setEnabled(false);
    }

    public void startListenSerialPort() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                stopListenSericalPort_flag = true;
                try {
                    System.out.println("null " + SettingForm.instance.filePath);
                    cmdAndResults = ExecelUtil.readExcelInfo(SettingForm.instance.filePath);
                    System.out.println("cmdAndResults[0]:" + cmdAndResults.length);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                updateShowResult();

                InputStream inputStream = SettingForm.instance.chosenPort.getInputStream();

                byte[] readB = new byte[50];
                int nBytes = 0;
                while (stopListenSericalPort_flag) {
                    try {
                        while (inputStream.available() > 0) {
                            nBytes = inputStream.read(readB);
                            printHexString(readB, nBytes);
                            //TODO
                            dealwithSerialPortValue(readB, nBytes);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }


        }).start();

    }

    private void updateShowResult(){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= cmdAndResults.length - 1; i++) {
            stringBuilder.append("命令词序号：").append(cmdAndResults[i].getCmdIndex())
                    .append("\t识别次数：").append(cmdAndResults[i].getReusltTimes())
                    .append("\t误唤醒：").append(cmdAndResults[i].getMissingTimes())
                    .append("\t\t漏唤醒:").append(cmdAndResults[i].getFalseWakeTimes()).append("\n");
        }
        textArea1.setText(stringBuilder.toString());
    }

    ArrayList<Byte> recognizedCmd = new ArrayList<Byte>();

    private void dealwithSerialPortValue(byte[] b, int nBytes) {
        boolean start = false;
        boolean last = false;
        //以excel中第一个命令的开头和结尾为基准，分割串口读取的byte[]。
        ArrayList<Byte> cmd = cmdAndResults[0].getSericalPortProtocolValue();
        for (int i = 0; i < nBytes; i++) {
            if (b[i] == cmd.get(0)) {
                start = true;
            }

            if (b[i] == cmd.get(cmd.size() - 1)) {
                last = true;
                start = false;
            } else {
                last = false;
            }

            if (start || last) {
                recognizedCmd.add(b[i]);
                if (last) {
                    boolean bingo = false;
                    for (CmdAndResult cmdAndResult : cmdAndResults) {
//                        for(int k = 0 ; k< cmdAndResults[j].getSericalPortProtocolValue().size();k++){
//                           if(cmd.get(k) == cmdAndResults[j].getSericalPortProtocolValue().get(k)) {
//
//                           }
//                        }
                        if (recognizedCmd.equals(cmdAndResult.getSericalPortProtocolValue())) {
                            cmdAndResult.setReusltTimes(cmdAndResult.getReusltTimes()+1);
                            System.out.println("equals");
                        }
                        System.out.println(recognizedCmd.toString()+" "+ cmdAndResult.getReusltTimes());
                    }
                    recognizedCmd.clear();
                    updateShowResult();
                }
            }
        }
    }
}
