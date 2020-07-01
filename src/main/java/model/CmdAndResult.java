package model;

public class CmdAndResult {
    int cmdIndex;
    String cmd;
    String sericalPortProtocolValue;
    int reusltTimes;
    int falseWakeTimes;
    int missingTimes;

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

    public String getSericalPortProtocolValue() {
        return sericalPortProtocolValue;
    }

    public void setSericalPortProtocolValue(String sericalPortProtocolValue) {
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
