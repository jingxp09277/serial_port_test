package gui.form;

import model.CmdAndResult;
import util.ExecelUtil;

import javax.swing.*;

public class MainFrom extends JFrame{
    public static MainFrom instance = new MainFrom();

    public JList list1;
    private JButton bt_back;
    private JPanel rootForm;
    private JTextArea textArea1;
    CmdAndResult[] cmdAndResults;

    public String getPathname() {
        return pathname;
    }

    public void setPathname(String pathname) {
        this.pathname = pathname;
    }

    private String pathname;

    public MainFrom(){
        add(rootForm);
        setTitle("串口检测");
        setSize(800,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            System.out.println("null "  +pathname);
            cmdAndResults = ExecelUtil.readExcelInfo(pathname);
            System.out.println("cmdAndResults[0]:"+cmdAndResults.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuilder stringBuilder = new StringBuilder();
        for(int i =0;i<=cmdAndResults.length-1;i++){
            stringBuilder.append("命令词序号：").append(cmdAndResults[i].getCmdIndex()).append("\t识别次数：0").append("\t误唤醒：0").append("\t\t漏唤醒:0\n");
        }

        textArea1.setText(stringBuilder.toString());
    }
}
