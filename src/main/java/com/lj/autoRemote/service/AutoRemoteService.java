package com.lj.autoRemote.service;

import com.alibaba.fastjson.JSON;
import com.lj.autoRemote.beans.ServerInfoBean;
import com.lj.autoRemote.dao.AutoRemoteDao;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/** 数据修复业务类  */
@Service
public class AutoRemoteService {

	private Logger logger = Logger.getLogger(AutoRemoteService.class);

	@Autowired
	private AutoRemoteDao autoRemoteDao;

	@Autowired
    private RestTemplate restTemplate;

    @Value("${spring.profiles.active}")
    String active;
    @Value("${server.port}")
    String port;
    @Value("${server.tempPath}")
    String tempPath;

    @Value("${spring.datasource.sqllite.url}")
    private String dbUrl;

    /**
     * 保存服务信息
     * @param serverInfoBean
     * @return
     */
	public Map<String,String> saveServerInfo(ServerInfoBean serverInfoBean){
	    Map<String,String> map = new HashMap<String, String>();
	    try{
            autoRemoteDao.saveServerInfo(serverInfoBean);
            map.put("state","1001");
            map.put("bak","保存成功");
        }catch (Exception e){
            logger.error("saveServerInfo is ERROR! "+serverInfoBean.toString() + e.getMessage(),e);
            map.put("state","9001");
            map.put("bak","保存失败，服务异常："+e.getMessage());
        }
        return map;
    }

    /**
     * 更新服务信息
     * @param serverInfoBean
     * @return
     */
	public Map<String,String> updateServerInfo(ServerInfoBean serverInfoBean){
	    Map<String,String> map = new HashMap<String, String>();
	    try{
            autoRemoteDao.updateServerInfoById(serverInfoBean);
            map.put("state","1001");
            map.put("bak","更新成功");
        }catch (Exception e){
            logger.error("saveServerInfo is ERROR! "+serverInfoBean.toString() + e.getMessage(),e);
            map.put("state","9001");
            map.put("bak","更新失败，服务异常："+e.getMessage());
        }
        return map;
    }

    /**
     * 查询所有备案信息
     * @return
     */
	public Map<String,Object> queryServerInfoList(){
        Map<String,Object> map = new HashMap<String, Object>();

	    try{
            List<ServerInfoBean> list = autoRemoteDao.queryServerInfoList();
            map.put("state","1001");
            map.put("result",list);
            map.put("bak","查询成功");
        }catch (Exception e){
            logger.error("queryServerInfoList is ERROR! "+ e.getMessage(),e);
            map.put("state","9001");
            map.put("bak","查询失败，服务异常："+e.getMessage());
        }
        return map;
    }


    /**
     * 查看远程服务运行状态
     * @param serverInfoBean
     */
    public String queryRemoteServerRunState(ServerInfoBean serverInfoBean){
        String url = "http://" + serverInfoBean.getIp() + ":" + port + "/autoRemote/apis/local/queryServerRunState";
        HttpHeaders headers = new HttpHeaders();
        //  请勿轻易改变此提交方式，大部分的情况下，提交方式都是表单提交
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //  封装参数，千万不要替换为Map与HashMap，否则参数无法传递
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        //  也支持中文
        params.add("id", serverInfoBean.getId() + "");
        params.add("ip", serverInfoBean.getIp());
        params.add("serverName", serverInfoBean.getServerName());
        params.add("serverPath", serverInfoBean.getServerPath());
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
        //  执行HTTP请求
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        //  输出结果
        String jsonStr = response.getBody();
        logger.info("request URL:" + url + ",param:" + serverInfoBean.toString() + ",Return:" + jsonStr);
        return jsonStr;
    }

	/**
	 * 查询服务运行状态
     * @param serverInfoBean
	 */
	public Map<String,Object> queryServerRunState(ServerInfoBean serverInfoBean){
        Map<String,Object> map = new HashMap<String, Object>();
        String ip = serverInfoBean.getIp();
        String serverName = serverInfoBean.getServerName();
        String serverPath = serverInfoBean.getServerPath();
        try {
            String pid = CmdToolkit.queryProcessPid(serverPath);
            map.put("state","1001");
            map.put("result",pid);
            map.put("bak","进程号");
        } catch (Exception e) {
            map.put("state","9001");
            map.put("bak","查询失败，执行异常："+e.getMessage());
            logger.error("queryServerRunState is ERROR! serverInfoBean:"+serverInfoBean.toString()+","+e.getMessage(),e);
        }
        return map;
    }

    /**
     * 向远程服务传输程序包
     */
    public Map<String,Object> serverUp(MultipartFile multipartFile)throws Exception{
        final Map<String,Object> map = new HashMap<String, Object>();
        List<ServerInfoBean> list = autoRemoteDao.queryMyselfList();
        if(list!=null&&list.size()>0){
            String tempPathDir = tempPath+File.separator+ UUID.randomUUID().toString()+File.separator;
            File tempDirectory = new File(tempPathDir);
            tempDirectory.mkdirs();
            final String tempFilePath = tempPathDir + multipartFile.getOriginalFilename();
            File tempFile = new File(tempFilePath);

            try {
                //保存临时文件
                multipartFile.transferTo(tempFile);
            } catch (IOException e) {
                String message = "写临时文件出错! tempFilePath:"+tempFilePath;
                logger.error(message,e);
                throw new IOException(message,e);
            }

            final CountDownLatch latch = new CountDownLatch(list.size());// 同步辅助类
            for (final ServerInfoBean serverInfoBean : list) {
                final String url = "http://" + serverInfoBean.getIp() + ":" + port + "/autoRemote/apis/local/serverUp";
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        try {
                            FileSystemResource fileSystemResource = new FileSystemResource(tempFilePath);
                            //设置HTTP头信息
                            HttpHeaders headers = new HttpHeaders();
                            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
                            headers.add("Content-Disposition", "filename=\"" + fileSystemResource.getFilename() + "\"");
                            //HTTP参数设置
                            MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
                            params.add("file", fileSystemResource);
                            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
                            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
                            //返回结果
                            String responseBody = response.getBody();
                            logger.info("sendRemoteFile,URL:"+url+",responseBody:"+responseBody);
                            map.put(serverInfoBean.getId()+"",JSON.parse(responseBody));
                        } catch (Exception e) {
                            String message = "发送文件到远程服务节点失败! URL:"+url+","+e.getMessage();
                            logger.error(message,e);
                            map.put(serverInfoBean.getId()+"",message);
                        }
                        latch.countDown();// 计数减一
                    }
                }).start();
            }
            latch.await();// 等待子线程结束
            //删除临时文件夹
            FileUtils.deleteDirectory(tempDirectory);
        }else {
            map.put("info","DB没有web-autoremote服务部署节点");
        }
        return map;
    }

    /**
     * 重启服务
     * @return
     * @throws Exception
     */
    public void rebootServer()throws Exception{
        logger.info("rebootServer active:" + active );
        CmdToolkit.rebootServer(active);
    }

    /**
     * 发送远程重启服务请求
     * @return
     * @throws Exception
     */
    public Map<String,Object> remoteRebootServer()throws Exception{
        final Map<String,Object> map = new HashMap<String, Object>();
        List<ServerInfoBean> list = autoRemoteDao.queryMyselfList();
        if(list!=null&&list.size()>0){
            final CountDownLatch latch = new CountDownLatch(list.size());// 同步辅助类
            for (final ServerInfoBean serverInfoBean : list) {
                final String url = "http://" + serverInfoBean.getIp() + ":" + port + "/autoRemote/apis/local/rebootServer";
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        try{
                            HttpHeaders headers = new HttpHeaders();
                            //  请勿轻易改变此提交方式，大部分的情况下，提交方式都是表单提交
                            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                            //  封装参数，千万不要替换为Map与HashMap，否则参数无法传递
                            MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
                            //  也支持中文
                            params.add("id", serverInfoBean.getId() + "");
                            params.add("ip", serverInfoBean.getIp());
                            params.add("serverName", serverInfoBean.getServerName());
                            params.add("serverPath", serverInfoBean.getServerPath());
                            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
                            //  执行HTTP请求
                            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
                            //  输出结果
                            String jsonStr = response.getBody();
                            logger.info("request URL:" + url + ",param:" + serverInfoBean.toString() + ",Return:" + jsonStr);
                            map.put(serverInfoBean.getId()+"",JSON.parse(jsonStr));
                        }catch (Exception e){
                            map.put(serverInfoBean.getId()+"",serverInfoBean.getIp()+","+e.getMessage());
                            logger.error("remoteRebootServer is ERROR! IP:"+serverInfoBean.getIp(),e);
                        }
                        latch.countDown();// 计数减一
                    }
                }).start();
            }
            latch.await();// 等待子线程结束
        }else{
            map.put("info","DB没有web-autoremote服务部署节点");
        }
        return map;
    }

    /**
     * 更新程序包
     * @return
     * @throws Exception
     */
    public void setupServer()throws Exception{
        logger.info("setupServer active:" + active );
        CmdToolkit.setupServer(active);
    }

    /**
     * 远程更新程序包
     * @return
     * @throws Exception
     */
    public Map<String,Object> remoteSetupServer()throws Exception{
        final Map<String,Object> map = new HashMap<String, Object>();
        List<ServerInfoBean> list = autoRemoteDao.queryMyselfList();
        if(list!=null&&list.size()>0){
            final CountDownLatch latch = new CountDownLatch(list.size());// 同步辅助类
            for (final ServerInfoBean serverInfoBean : list) {
                final String url = "http://" + serverInfoBean.getIp() + ":" + port + "/autoRemote/apis/local/setupServer";
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        try{
                            HttpHeaders headers = new HttpHeaders();
                            //  请勿轻易改变此提交方式，大部分的情况下，提交方式都是表单提交
                            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                            //  封装参数，千万不要替换为Map与HashMap，否则参数无法传递
                            MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
                            //  也支持中文
                            params.add("id", serverInfoBean.getId() + "");
                            params.add("ip", serverInfoBean.getIp());
                            params.add("serverName", serverInfoBean.getServerName());
                            params.add("serverPath", serverInfoBean.getServerPath());
                            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
                            //  执行HTTP请求
                            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
                            //  输出结果
                            String jsonStr = response.getBody();
                            logger.info("request URL:" + url + ",param:" + serverInfoBean.toString() + ",Return:" + jsonStr);
                            map.put(serverInfoBean.getId()+"",JSON.parse(jsonStr));
                        }catch (Exception e){
                            map.put(serverInfoBean.getId()+"",serverInfoBean.getIp()+","+e.getMessage());
                            logger.error("remoteSetupServer is ERROR! IP:"+serverInfoBean.getIp(),e);
                        }
                        latch.countDown();// 计数减一
                    }
                }).start();
            }
            latch.await();// 等待子线程结束
        }else{
            map.put("info","DB没有web-autoremote服务部署节点");
        }
        return map;
    }

    /**
     * 同步数据库文件到远程所有监控节点
     * @return
     * @throws Exception
     */
    public Map<String,Object> remoteSynDB()throws Exception{
        final Map<String,Object> map = new HashMap<String, Object>();
        List<ServerInfoBean> list = autoRemoteDao.queryMyselfList();
        //jdbc:sqlite:/home/yanfa_ro/autoRemoteSqlite.db
        final String dbFilePath = dbUrl.split(":")[2];
        if(list!=null&&list.size()>0){
            final CountDownLatch latch = new CountDownLatch(list.size());// 同步辅助类
            for (final ServerInfoBean serverInfoBean : list) {
                final String url = "http://" + serverInfoBean.getIp() + ":" + port + "/autoRemote/apis/local/synDB";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FileSystemResource fileSystemResource = new FileSystemResource(dbFilePath);
                            //设置HTTP头信息
                            HttpHeaders headers = new HttpHeaders();
                            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
                            headers.add("Content-Disposition", "filename=\"" + fileSystemResource.getFilename() + "\"");
                            //HTTP参数设置
                            MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
                            params.add("file", fileSystemResource);
                            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
                            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
                            //返回结果
                            String responseBody = response.getBody();
                            logger.info("sendRemoteDBFile,URL:"+url+",responseBody:"+responseBody);
                            map.put(serverInfoBean.getId()+"",JSON.parse(responseBody));
                        } catch (Exception e) {
                            String message = "发送DB文件到远程服务节点失败! URL:"+url+","+e.getMessage();
                            logger.error(message,e);
                            map.put(serverInfoBean.getId()+"",message);
                        }
                        latch.countDown();// 计数减一
                    }
                }).start();
            }
            latch.await();// 等待子线程结束
        }else{
            map.put("info","DB没有web-autoremote服务部署节点");
        }
        return map;
    }

}
