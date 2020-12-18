package cn.cansluck.model;

public class SocketMsg {
    private String msg;        // 消息内容
    private String acceptUser; // 接受消息方
    private String sendUser;   // 发送消息方
    private String sendType;   // 发送类型：0私聊；1群聊
    private String msgType;    // 内容类型：0文本；1图片

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getAcceptUser() {
        return acceptUser;
    }

    public void setAcceptUser(String acceptUser) {
        this.acceptUser = acceptUser;
    }

    public String getSendUser() {
        return sendUser;
    }

    public void setSendUser(String sendUser) {
        this.sendUser = sendUser;
    }

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }
}
