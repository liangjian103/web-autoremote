package com.lj.autoRemote.beans;

/**
 * 备案表服务信息
 */
public class ServerInfoBean {

    private Integer id;//ID
    private String ip;//IP
    private String serverName;//服务名称
    private String serverPath;//服务部署路径
    private String state;//启动状态（1:已启动，2:未启动，3:待重启）
    private String commandStart;//启动命令
    private Integer seq;//序号

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerPath() {
        return serverPath;
    }

    public void setServerPath(String serverPath) {
        this.serverPath = serverPath;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCommandStart() {
        return commandStart;
    }

    public void setCommandStart(String commandStart) {
        this.commandStart = commandStart;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    @Override
    public String toString() {
        return "ServerInfoBean{" +
                "id=" + id +
                ", ip='" + ip + '\'' +
                ", serverName='" + serverName + '\'' +
                ", serverPath='" + serverPath + '\'' +
                ", state='" + state + '\'' +
                ", commandStart='" + commandStart + '\'' +
                ", seq=" + seq +
                '}';
    }
}
