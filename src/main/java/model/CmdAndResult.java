package model;

import java.util.ArrayList;
import java.util.HashSet;

public class CmdAndResult {
    int cmdIndex;
    String cmd;
    ArrayList<Byte> sericalPortProtocolValue;
    int reusltTimes;
    int falseWakeTimes;
    int missingTimes;
    HashSet<Integer> maskBit;

    public HashSet<Integer> getMaskBit() {
        return maskBit;
    }

    public void setMaskBit(HashSet<Integer> maskBit) {
        this.maskBit = maskBit;
    }  
    
    public int getCmdIndex() {
        return cmdIndex;
    }

    public void setCmdIndex(int cmdIndex) {
        this.cmdIndex = cmdIndex;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public ArrayList<Byte> getSericalPortProtocolValue() {
        return sericalPortProtocolValue;
    }

    public void setSericalPortProtocolValue(ArrayList<Byte> sericalPortProtocolValue) {
        this.sericalPortProtocolValue = sericalPortProtocolValue;
    }

    public int getReusltTimes() {
        return reusltTimes;
    }

    public void setReusltTimes(int reusltTimes) {
        this.reusltTimes = reusltTimes;
    }

    public int getFalseWakeTimes() {
        return falseWakeTimes;
    }

    public void setFalseWakeTimes(int falseWakeTimes) {
        this.falseWakeTimes = falseWakeTimes;
    }

    public int getMissingTimes() {
        return missingTimes;
    }

    public void setMissingTimes(int missingTimes) {
        this.missingTimes = missingTimes;
    }

}
