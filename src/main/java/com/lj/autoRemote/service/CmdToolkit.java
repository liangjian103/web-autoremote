package com.lj.autoRemote.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 控制台处理工具箱
 */
public final class CmdToolkit {
    private static Logger log = Logger.getLogger(CmdToolkit.class);

    private CmdToolkit() {
    }

    /**
     * 读取控制命令的输出结果,支持管道命令
     * @param cmd 命令
     * @return 控制命令的输出结果
     * @throws IOException
     */
    public static String execShell(String cmd) throws IOException {
        StringBuffer cmdout = new StringBuffer();
        log.info("执行命令：" + cmd);
        Process process = Runtime.getRuntime().exec(new String[] {"/bin/sh", "-c", cmd});     //执行一个系统命令
        InputStream fis = process.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        String line = null;
        while ((line = br.readLine()) != null) {
            cmdout.append(line);
        }
        fis.close();
        log.info("执行系统命令后的结果为：\n" + cmdout.toString());
        return cmdout.toString().trim();
    }

    /**
     * 根据服务名称，查询进程编号
     * @param serviceName
     * @return
     */
    public static String queryProcessPid(String serviceName)throws IOException{
        String bashCommand = "ps -ef|grep "+serviceName+"|grep -v grep|grep -v PPID|grep -v tail|awk '{print $2}'";
        String pid = CmdToolkit.execShell(bashCommand);
        return pid;
    }

    /**
     * 杀进程根据进程编号
     * @param serviceName
     * @return
     */
    public static String killProcessPid(String serviceName)throws IOException{
        String pid = queryProcessPid(serviceName);
        String bashCommand = "kill -9 "+pid;
        CmdToolkit.execShell(bashCommand);
        return pid;
    }

    /**
     * 重启服务
     * @param active 环境
     */
    public static void rebootServer(String active)throws IOException{
        String bashCommand = "nohup bash rebootServer.sh "+active+" &";
        CmdToolkit.execShell(bashCommand);
    }

    /**
     * 更新程序包
     * @param active 环境
     */
    public static void setupServer(String active)throws IOException{
        String bashCommand = "nohup bash setup.sh "+active+" &";
        CmdToolkit.execShell(bashCommand);
    }

}