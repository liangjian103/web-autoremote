package com.lj.autoRemote.beans;

/**
 * 备案表服务信息
 */
public class ServerInfoBean {

    private Integer id;//ID
    private String ip;//IP
    private String serverName;//服务名称
    private String serverPath;//服务部署路径
    private String upDir;//上传程序包位置
    private String bakDir;//老程序包备份位置
    private String psCommand;//查看进程命令
    private String runCommand;//启动命令
    private String state;//启动状态

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

    public String getUpDir() {
        return upDir;
    }

    public void setUpDir(String upDir) {
        this.upDir = upDir;
    }

    public String getBakDir() {
        return bakDir;
    }

    public void setBakDir(String bakDir) {
        this.bakDir = bakDir;
    }

    public String getPsCommand() {
        return psCommand;
    }

    public void setPsCommand(String psCommand) {
        this.psCommand = psCommand;
    }

    public String getRunCommand() {
        return runCommand;
    }

    public void setRunCommand(String runCommand) {
        this.runCommand = runCommand;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "ServerInfoBean{" +
                "id=" + id +
                ", ip='" + ip + '\'' +
                ", serverName='" + serverName + '\'' +
                ", serverPath='" + serverPath + '\'' +
                ", upDir='" + upDir + '\'' +
                ", bakDir='" + bakDir + '\'' +
                ", psCommand='" + psCommand + '\'' +
                ", runCommand='" + runCommand + '\'' +
                '}';
    }
}
