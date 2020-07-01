package gui.frame;

import javax.swing.JFrame;

import gui.panel.MainPanel;
import gui.panel.SpendPanel;

public class MainFrame extends JFrame{
    public static MainFrame instance = new MainFrame();

    private MainFrame(){
        this.setSize(800,600);
        this.setTitle("串口测试");
        this.setContentPane(MainPanel.instance);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        instance.setVisible(true);
    }

}
