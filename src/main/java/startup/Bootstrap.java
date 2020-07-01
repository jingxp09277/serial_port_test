package startup;

import com.fazecast.jSerialComm.SerialPort;
import gui.form.MainFrom;
import gui.form.SettingForm;
import gui.frame.MainFrame;
import gui.panel.MainPanel;
import gui.panel.SpendPanel;
import util.ExecelUtil;
import util.GUIUtil;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class Bootstrap {
    public static void main(String[] args) throws Exception{
        //GUIUtil.useLNF();
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
//                MainFrame.instance.setVisible(true);
//                MainPanel.instance.workingPanel.show(SpendPanel.instance);
                SettingForm.instance.setVisible(true);
//                MainFrom.instance.setVisible(true);
            }
        });
    }


}


