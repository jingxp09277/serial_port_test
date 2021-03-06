package gui.form;

import gui.frame.MainFrame;
import model.CmdAndResult;
import music.AudioPlayer;
import util.ExecelUtil;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import static util.SerialPortUtil.printHexString;

public class MainFrom {
    public static MainFrom instance = new MainFrom();

    public JList list1;
    private JButton bt_back;
    private Boolean stopListenSericalPort_flag;
    private int identifyingCmd;
    private boolean isComfrimCmd = true;
    private boolean startPlayMusic_flag = true;

    public JPanel getRootForm() {
        return rootForm;
    }

    private JPanel rootForm;
    private JTextArea textArea1;
    private JButton bt_pause;
    CmdAndResult[] cmdAndResults;
    AudioPlayer player;
    Thread mPlayMusicThread;
    boolean Flag_one = true;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
    String logPath = null;

    String playNowName = null;

    public MainFrom() {
        Date nowDate = new Date();
        String str = sdf.format(nowDate);
        logPath = ".\\log " + str + ".txt";

        bt_back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopListenSericalPort_flag = false;
                stopListenSerialPort();
                startPlayMusic_flag = false;
                if (player != null) {
                    if (player.isPlaying()) {
                        player.stop();
                    }
                }
                if (mPlayMusicThread != null) {
                    mPlayMusicThread.interrupt();
                }
            }
        });

        bt_pause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (player != null && mPlayMusicThread != null) {
                    if (Flag_one) {
                        mPlayMusicThread.suspend();
                        bt_pause.setText("继续");
                        Flag_one = false;
                    } else {
                        //mPlayMusicThread.resumeThread();
                        mPlayMusicThread.resume();
                        bt_pause.setText("暂停");
                        Flag_one = true;
                    }
                }
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
                    System.out.println("null " + SettingForm.instance.execelfilePath);
                    cmdAndResults = ExecelUtil.readExcelInfo(SettingForm.instance.execelfilePath);
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
                            System.out.println();
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

    private void updateShowResult() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= cmdAndResults.length - 1; i++) {
            stringBuilder.append("命令词序号：").append(cmdAndResults[i].getCmdIndex())
                    .append("\t识别次数：").append(cmdAndResults[i].getReusltTimes())
                    .append("\t漏唤醒：").append(cmdAndResults[i].getMissingTimes())
                    .append("\t\t误唤醒:").append(cmdAndResults[i].getFalseWakeTimes()).append("\n");
        }
        textArea1.setText(stringBuilder.toString());
    }

    ArrayList<Byte> recognizedCmd = new ArrayList<Byte>();

    boolean start = false;
    boolean last = false;

    private void dealwithSerialPortValue(byte[] b, int nBytes) {

        //以excel中第一个命令的开头和结尾为基准，分割串口读取的byte[]。
        ArrayList<Byte> cmd = cmdAndResults[0].getSericalPortProtocolValue();
        for (int i = 0; i < nBytes; i++) {
            if (b[i] == cmd.get(0)) {
                start = true;
                recognizedCmd.clear();
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
                    //TODO 当前命令词识别（或误识别）后确认。更新识别结果

                    if (identifyingCmd > 0) {

                        if (isEqualsExcludeMaskBit(recognizedCmd, cmdAndResults[identifyingCmd - 1].getSericalPortProtocolValue(),
                                cmdAndResults[identifyingCmd - 1].getMaskBit()
                        )) {
                            cmdAndResults[identifyingCmd - 1].setReusltTimes(
                                    cmdAndResults[identifyingCmd - 1].getReusltTimes() + 1
                            );
                            isComfrimCmd = true;
                        } else {
                            for (CmdAndResult cmdAndResult : cmdAndResults) {
                                if (isEqualsExcludeMaskBit(recognizedCmd, cmdAndResult.getSericalPortProtocolValue(),
                                        cmdAndResult.getMaskBit()
                                )) {
                                    cmdAndResults[identifyingCmd - 1].setFalseWakeTimes(
                                            cmdAndResults[identifyingCmd - 1].getFalseWakeTimes() + 1);
                                    //wirte log
                                    try {
                                        writeLog2Logfile("当前命令：" + cmdAndResults[identifyingCmd - 1].getCmd() + " 误识别命令：" + cmdAndResult.getCmd() + " 音频：" + playNowName);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    isComfrimCmd = true;
                                    break;
                                }
                            }
                        }

                        updateShowResult();
                        System.out.println("identifingCmd: " + identifyingCmd + " recofinizedCmd: " +
                                recognizedCmd.toString() + "cmdAndResult:" + cmdAndResults[identifyingCmd - 1].getSericalPortProtocolValue().toString());

                    }
                }
            }
        }
    }

    public Boolean isEqualsExcludeMaskBit(ArrayList<Byte> alA, ArrayList<Byte> alB, HashSet<Integer> maskBit) {
        if (alA.size() != alB.size()) {
            return false;
        }
        for (int i = 0; i < alA.size(); i++) {
            if (!maskBit.contains(i)) {
                if (!alA.get(i).equals(alB.get(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    public void playAudio() {
        isComfrimCmd = true;
        startPlayMusic_flag = true;
        if (mPlayMusicThread == null) {
            System.out.println("test1122221");
            mPlayMusicThread = new Thread(
                    () -> {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                        System.out.println("test11111");

                        File[] musicFiles = new File(SettingForm.instance.musicFilePath).listFiles(new FileFilter() {
                            @Override
                            public boolean accept(File pathname) {
                                return pathname.getName().toLowerCase().endsWith(".wav");
                            }
                        });


                        //每播放一个音频前，先去和所有的命令词对比一遍，通过命令词确认是哪个命令词。
                        //确认后，下次播放前先检查是否有被确认，未被确认漏测加一。
                        if (musicFiles != null) {
                            for (File music : musicFiles) {
                                if (startPlayMusic_flag) {
                                    if (!isComfrimCmd) {
                                        if (identifyingCmd > 0) {
                                            cmdAndResults[identifyingCmd - 1].setMissingTimes(
                                                    cmdAndResults[identifyingCmd - 1].getMissingTimes() + 1
                                            );
                                            //wirte log
                                            try {
                                                writeLog2Logfile("漏识别命令：" + cmdAndResults[identifyingCmd - 1].getCmd() + " 音频：" + playNowName);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            updateShowResult();
                                        }
                                    }

                                    for (CmdAndResult cmdAndResult : cmdAndResults) {
                                        if (music.getName().contains(cmdAndResult.getCmd())) {
                                            identifyingCmd = cmdAndResult.getCmdIndex();

                                            isComfrimCmd = false;
                                            try {
                                                playNowName = music.getName();
                                                player = new AudioPlayer(music.toURI().toURL());
                                                player.play();
                                                System.out.println(new Date() + "Thread " + mPlayMusicThread.getName() + " player ");
                                                Thread.sleep(7000);
                                            } catch (IOException | InterruptedException | UnsupportedAudioFileException | LineUnavailableException e) {
                                                e.printStackTrace();
                                            }
                                            break;
                                        } else {
                                            identifyingCmd = 0;
                                        }
                                    }

                                }
                            }
                            if (!isComfrimCmd) {
                                if (identifyingCmd > 0) {
                                    cmdAndResults[identifyingCmd - 1].setMissingTimes(
                                            cmdAndResults[identifyingCmd - 1].getMissingTimes() + 1
                                    );
                                    updateShowResult();

                                    //wirte log
                                    try {
                                        writeLog2Logfile("漏识别命令：" + cmdAndResults[identifyingCmd - 1].getCmd() + " 音频：" + playNowName);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }

                        }
                    });
            mPlayMusicThread.start();
        }

    }

    void writeLog2Logfile(String str) throws IOException {
        File file = new File(logPath);
        if (!file.exists()) {
            file.createNewFile();
        }

        PrintWriter pw = new PrintWriter(new FileWriter(file, true));
        pw.println(str);
        pw.flush();
        pw.close();
    }
}
