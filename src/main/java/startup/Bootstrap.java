package startup;

import gui.frame.MainFrame;

import javax.swing.*;

public class Bootstrap {
    public static void main(String[] args) throws Exception {
        //GUIUtil.useLNF();
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
//                MainFrame.instance.setVisible(true);
//                MainPanel.instance.workingPanel.show(SpendPanel.instance);
                MainFrame.instance.setVisible(true);
//                MainFrom.instance.setVisible(true);
            }
        });
    }


}


