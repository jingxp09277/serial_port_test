package gui.frame;

import javax.swing.*;

import gui.form.MainFrom;
import gui.form.SettingForm;
import gui.panel.MainPanel;
import gui.panel.SpendPanel;

import java.awt.*;

public class MainFrame extends JFrame{
    public static MainFrame instance = new MainFrame();
    public JPanel mainFramePanel = new JPanel();

    public JPanel mainFromPanel = MainFrom.instance.getRootForm();
    public JPanel settingFormPanel  = SettingForm.instance.getRootForm();

    private MainFrame(){
        this.setSize(800,600);
        this.setTitle("串口测试");
        //this.setContentPane(MainPanel.instance);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFramePanel.add(mainFromPanel);
        mainFramePanel.add(settingFormPanel);
        mainFromPanel.setVisible(false);
        this.add(mainFramePanel);
    }

    public static void main(String[] args) {
        instance.setVisible(true);
    }

}
