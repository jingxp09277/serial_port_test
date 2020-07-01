package gui.panel;

import util.CenterPanel;
import util.GUIUtil;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    static {
        GUIUtil.useLNF();
    }

    public static MainPanel instance = new MainPanel();
    public CenterPanel workingPanel;
    public JButton bReport = new JButton("hellpo");

    private MainPanel() {

        workingPanel = new CenterPanel(0.8);
        //workingPanel.show(SpendPanel.instance);
        //workingPanel.add(bReport);
        setLayout(new BorderLayout());
        add(workingPanel, BorderLayout.CENTER);

    }

    public static void main(String[] args) {
        GUIUtil.showPanel(MainPanel.instance, 1);
    }

}
