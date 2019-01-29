package com.lj.autoRemote.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lj.autoRemote.beans.ServerInfoBean;
import com.lj.autoRemote.dao.AutoRemoteDao;
import com.lj.utils.JsonUtil;
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
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
    @Value("${server.name}")
    String serverName;

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
     * 删除服务信息
     * @param serverInfoBean
     * @return
     */
	public Map<String,String> delServerInfo(ServerInfoBean serverInfoBean){
	    Map<String,String> map = new HashMap<String, String>();
	    try{
            autoRemoteDao.delServerInfoById(serverInfoBean);
            map.put("state","1001");
            map.put("bak","删除成功");
        }catch (Exception e){
            logger.error("delServerInfo is ERROR! "+serverInfoBean.toString() + e.getMessage(),e);
            map.put("state","9001");
            map.put("bak","删除失败，服务异常："+e.getMessage());
        }
        return map;
    }

    /**
     * 查询所有备案信息
     * @return
     */
	public Map<String,Object> queryServerInfoList(ServerInfoBean serverInfoBean){
        Map<String,Object> map = new HashMap<String, Object>();
	    try{
            List<ServerInfoBean> list = autoRemoteDao.queryServerInfoList(serverInfoBean);
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
     * 保存本程序节点信息
     * @param serverInfoBean
     * @return
     */
    public Map<String,String> saveNodeServerInfo(ServerInfoBean serverInfoBean){
        Map<String,String> map = new HashMap<String, String>();
        try{
            autoRemoteDao.saveNodeServerInfo(serverInfoBean);
            map.put("state","1001");
            map.put("bak","保存成功");
        }catch (Exception e){
            logger.error("saveNodeServerInfo is ERROR! "+serverInfoBean.toString() + e.getMessage(),e);
            map.put("state","9001");
            map.put("bak","保存失败，服务异常："+e.getMessage());
        }
        return map;
    }

    /**
     * 更新本程序节点信息
     * @param serverInfoBean
     * @return
     */
    public Map<String,String> updateNodeServerInfo(ServerInfoBean serverInfoBean){
        Map<String,String> map = new HashMap<String, String>();
        try{
            autoRemoteDao.updateNodeServerInfo(serverInfoBean);
            map.put("state","1001");
            map.put("bak","更新成功");
        }catch (Exception e){
            logger.error("updateNodeServerInfo is ERROR! "+serverInfoBean.toString() + e.getMessage(),e);
            map.put("state","9001");
            map.put("bak","更新失败，服务异常："+e.getMessage());
        }
        return map;
    }

    /**
     * 删除节点信息
     * @param id
     * @return
     */
    public Map<String,String> delNodeServerInfo(int id){
        Map<String,String> map = new HashMap<String, String>();
        try{
            autoRemoteDao.delMyselfInfoById(id);
            map.put("state","1001");
            map.put("bak","删除成功");
        }catch (Exception e){
            logger.error("delNodeServerInfo is ERROR! id:"+id + e.getMessage(),e);
            map.put("state","9001");
            map.put("bak","删除失败，服务异常："+e.getMessage());
        }
        return map;
    }

    /**
     * 查询本程序所有节点信息
     * @return
     */
    public Map<String,Object> queryNodeServerInfoList(){
        Map<String,Object> map = new HashMap<String, Object>();

        try{
            List<ServerInfoBean> list = autoRemoteDao.queryNodeServerInfoList();
//            for (ServerInfoBean serverInfoBean : list) {
//                try {
//                    String json = queryRemoteNodeServerRunState(serverInfoBean);
//                    JSONObject jsonObject = JSON.parseObject(json);
//                    String state = jsonObject.get("state")+"";
//                    String result = jsonObject.get("result")+"";
//                    String bak = jsonObject.get("bak")+"";
//                    if("1001".equals(state)){
//                        serverInfoBean.setState(result);
//                    }else {
//                        serverInfoBean.setState(bak);
//                    }
//                } catch (Exception e) {
//                    serverInfoBean.setState("请求远程服务异常,可能该服务未部署监控节点。 "+e.getMessage());
//                }
//            }
            map.put("state","1001");
            map.put("result",list);
            map.put("bak","查询成功");
        }catch (Exception e){
            logger.error("queryNodeServerInfoList is ERROR! "+ e.getMessage(),e);
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
        logger.info("request URL:" + url + ",param:" + serverInfoBean.toString() + ",Start");
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        //  输出结果
        String jsonStr = response.getBody();
        logger.info("request URL:" + url + ",param:" + serverInfoBean.toString() + ",Return:" + jsonStr);
        return jsonStr;
    }

    /**
     * 查看远程节点服务运行状态
     * @param serverInfoBean
     */
    public String queryRemoteNodeServerRunState(ServerInfoBean serverInfoBean){
        String url = "http://" + serverInfoBean.getIp() + ":" + port + "/autoRemote/apis/local/queryNodeServerRunState";
        HttpHeaders headers = new HttpHeaders();
        //  请勿轻易改变此提交方式，大部分的情况下，提交方式都是表单提交
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //  封装参数，千万不要替换为Map与HashMap，否则参数无法传递
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        //  也支持中文
        params.add("id", serverInfoBean.getId() + "");
        params.add("ip", serverInfoBean.getIp());
        params.add("state", serverInfoBean.getState());
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
        //  执行HTTP请求
        logger.info("request URL:" + url + ",param:" + serverInfoBean.toString() + ",Start");
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
        int id = serverInfoBean.getId();
        String ip = serverInfoBean.getIp();
        String serverName = serverInfoBean.getServerName();
        String serverPath = serverInfoBean.getServerPath();
        Map<String,Object> rsMap = new HashMap<String,Object>();
        rsMap.put("id",id);
        try {
            String pid = CmdToolkit.queryProcessPid(serverPath);
            rsMap.put("pid",pid);
            map.put("state","1001");
            map.put("result",rsMap);
            map.put("bak","进程号");
        } catch (Exception e) {
            map.put("state","9001");
            map.put("result",rsMap);
            map.put("bak","查询失败，执行异常："+e.getMessage());
            logger.error("queryServerRunState is ERROR! serverInfoBean:"+serverInfoBean.toString()+","+e.getMessage(),e);
        }
        return map;
    }

	/**
	 * 查询节点服务运行状态
     * @param serverInfoBean
	 */
	public Map<String,Object> queryNodeServerRunState(ServerInfoBean serverInfoBean){
        Map<String,Object> map = new HashMap<String, Object>();
        try {
            String pid = CmdToolkit.queryProcessPid(serverName);
            map.put("state","1001");
            map.put("result",pid);
            map.put("bak","进程号");
        } catch (Exception e) {
            map.put("state","9001");
            map.put("bak","查询失败，执行异常："+e.getMessage());
            logger.error("queryNodeServerRunState is ERROR! serverInfoBean:"+serverInfoBean.toString()+","+e.getMessage(),e);
        }
        return map;
    }

    /**
     * 向远程服务传输程序包
     */
    public Map<String,Object> serverUp(MultipartFile multipartFile)throws Exception{
        final Map<String,Object> map = new HashMap<String, Object>();
        List<ServerInfoBean> list = autoRemoteDao.queryNodeServerInfoList();
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
     * 向远程服务传输DB文件
     */
    public Map<String,Object> dbUp(MultipartFile multipartFile)throws Exception{
        final Map<String,Object> map = new HashMap<String, Object>();
        List<ServerInfoBean> list = autoRemoteDao.queryNodeServerInfoList();
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
                final String url = "http://" + serverInfoBean.getIp() + ":" + port + "/autoRemote/apis/local/synDB";
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
        List<ServerInfoBean> list = autoRemoteDao.queryNodeServerInfoList();
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
                            logger.info("request URL:" + url + ",param:" + serverInfoBean.toString() + ",Start");
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
     * 发送远程重启服务请求
     * @return
     * @throws Exception
     */
    public Map<String,Object> remoteRebootServer(String ip)throws Exception{
        Map<String,Object> map = new HashMap<String, Object>();
        String url = "http://" + ip + ":" + port + "/autoRemote/apis/local/rebootServer";
            try{
                HttpHeaders headers = new HttpHeaders();
                //  请勿轻易改变此提交方式，大部分的情况下，提交方式都是表单提交
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                //  封装参数，千万不要替换为Map与HashMap，否则参数无法传递
                MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
                //  也支持中文
                params.add("ip", ip);
                HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
                //  执行HTTP请求
                logger.info("request URL:" + url + ",param:ip=" + ip + ",Start");
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
                //  输出结果
                String jsonStr = response.getBody();
                logger.info("request URL:" + url + ",param:ip=" + ip + ",Return:" + jsonStr);
                map.put(ip+"",JSON.parse(jsonStr));
            }catch (Exception e){
                map.put(ip,ip+","+e.getMessage());
                logger.error("remoteRebootServer is ERROR! IP:"+ip,e);
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
        List<ServerInfoBean> list = autoRemoteDao.queryNodeServerInfoList();
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
                            logger.info("request URL:" + url + ",param:" + serverInfoBean.toString() + ",Start");
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
        List<ServerInfoBean> list = autoRemoteDao.queryNodeServerInfoList();
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

    /** 服务启动后，更新启动状态 **/
    public void startServer(){
        //获取当前进程号
        int processId = getProcessID();
        InetAddress address = null;//获取的是本地的IP地址 //PC-20140317PXKX/192.168.0.121
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            logger.error("获取当前服务器IP异常。ERROR! "+e.getMessage(),e);
        }
        String ip = address.getHostAddress();//192.168.0.121
//        autoRemoteDao.updateMyselfInfoByIp(ip,processId);
        System.out.println("start IP:" + ip + ",processId:" + processId);
    }

    public static final int getProcessID() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        return Integer.valueOf(runtimeMXBean.getName().split("@")[0]).intValue();
    }

}
