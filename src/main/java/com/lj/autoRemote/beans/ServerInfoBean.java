package com.lj.autoRemote.beans;

/**
 * 备案表服务信息
 */
public class ServerInfoBean {

    private Integer id;//ID
    private String ip;//IP
    private String serverName;//服务名称
    private String serverPath;//服务部署路径

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

    @Override
    public String toString() {
        return "ServerInfoBean{" +
                "id=" + id +
                ", ip='" + ip + '\'' +
                ", serverName='" + serverName + '\'' +
                ", serverPath='" + serverPath + '\'' +
                '}';
    }
}
