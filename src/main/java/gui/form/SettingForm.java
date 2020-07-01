package gui.form;

import com.fazecast.jSerialComm.SerialPort;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingForm extends JFrame {
    public static SettingForm instance = new SettingForm();

    private JTextField tf_folderName;
    private JButton bt_updateFolder;
    private JComboBox<String> cb_port;
    private JButton bt_openPort;
    private JPanel rootForm;
    private JComboBox<Integer> cb_Baudrate;
    private JComboBox<Integer> cb_DataBytes;
    private JButton refreshPort;

    private int[] baudrate = {9600, 19200, 38400, 57600, 115200};
    private int[] dataBytes = {5,6,7,8};
    SerialPort[] ports;

    public SettingForm() {
        add(rootForm);
        setTitle("串口检测");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponent();


        bt_openPort.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(cb_port.getItemCount()!=0){
                    SerialPort chosenPort = ports[cb_port.getSelectedIndex()];
                    chosenPort.setComPortParameters((int)cb_Baudrate.getSelectedItem(),(int)cb_DataBytes.getSelectedItem(),1,0);
                    chosenPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
                    if (chosenPort.openPort()) {
                        System.out.println("gettext:"+tf_folderName.getText());
                        MainFrom.instance.setPathname(tf_folderName.getText());

                        MainFrom.instance.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(rootForm, "打开串口失败", "Failure",JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });
        refreshPort.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateStatue();

            }
        });
    }

    private void initComponent() {
        updateStatue();
        for (int value : baudrate) {
            cb_Baudrate.addItem(value);
        }
        cb_Baudrate.setSelectedIndex(0);
        for (int value : dataBytes) {
            cb_DataBytes.addItem(value);
        }
        cb_DataBytes.setSelectedItem(8);
        tf_folderName.setText("C:\\Users\\LWB\\Desktop\\cmd.xls");
    }

    private void updateStatue(){
        cb_port.removeAllItems();
        ports = SerialPort.getCommPorts();
        for (SerialPort port : ports) {
            cb_port.addItem(port.getSystemPortName());
        }
    }

}
