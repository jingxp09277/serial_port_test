import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class Startup {
    public static void main(String[] args) {
        SerialPort[] ports = SerialPort.getCommPorts();
        System.out.println("Select a port:");
        int i = 1;
        for (SerialPort port : ports) {
            System.out.println(i++ + ". " + port.getSystemPortName());
        }

        Scanner scanner = new Scanner(System.in);
        int chosenPortID = scanner.nextInt();

        SerialPort chosenPort = ports[chosenPortID - 1];
        chosenPort.setComPortParameters(9600,8,1,0);
        chosenPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        if (chosenPort.openPort()) {
            System.out.println("Successfully opened the port");
        } else {
            System.out.println("Unable to open the port");
            return;
        }


        InputStream inputStream = chosenPort.getInputStream();

        byte[] readB = new byte[50];
        int nBytes = 0;
        while(true){
            try {
                while (inputStream.available()>0){
                    nBytes = inputStream.read(readB);
                    printHexString(readB,nBytes);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static void printHexString(byte[] b, int nBytes) {
        for (int i = 0; i < nBytes; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF)+" ";
            if (hex.length() == 2) {
                hex = '0' + hex;
            }
            System.out.print(hex.toUpperCase() );
        }
        //System.out.println("\t\n");
    }
}


