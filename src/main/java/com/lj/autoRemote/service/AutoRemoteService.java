package com.lj.autoRemote.service;

import com.lj.autoRemote.beans.ServerInfoBean;
import com.lj.autoRemote.dao.AutoRemoteDao;
import org.apache.log4j.Logger;
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
import java.util.*;

/** 数据修复业务类  */
@Service
public class AutoRemoteService {

	private Logger logger = Logger.getLogger(AutoRemoteService.class);

	@Autowired
	private AutoRemoteDao autoRemoteDao;

	@Autowired
    private RestTemplate restTemplate;

    @Value("${server.port}")
    String port;

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
     * 向远程服务发起更新
     */
    public String serverUp(MultipartFile file){
        String jsonStr = "";
        List<ServerInfoBean> list = new ArrayList<ServerInfoBean>();
        ServerInfoBean serverInfoBean2 = new ServerInfoBean();
        serverInfoBean2.setIp("127.0.0.1");
        serverInfoBean2.setId(123);
        serverInfoBean2.setServerName("web-tools");
        serverInfoBean2.setServerPath("/opt/web_app/web_tools");
        list.add(serverInfoBean2);

        //后续扩展为多个机器,再改造，目前为一个机器
        for (ServerInfoBean serverInfoBean : list) {
            String url = "http://" + serverInfoBean.getIp() + ":" + port + "/autoRemote/apis/local/serverUp";
            HttpHeaders headers = new HttpHeaders();
            //  请勿轻易改变此提交方式，提交方式文件流
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            //  封装参数，千万不要替换为Map与HashMap，否则参数无法传递
            MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
            //  也支持中文
            params.add("id", serverInfoBean.getId() + "");
            params.add("ip", serverInfoBean.getIp());
            params.add("serverName", serverInfoBean.getServerName());
            params.add("serverPath", serverInfoBean.getServerPath());

            try {
                jsonStr = uploadFile(url,file,"C:\\Users\\liang\\");
            } catch (Exception e) {
                e.printStackTrace();
            }
//            params.add("file",file);
//            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
//            //  执行HTTP请求
//            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
//            //  输出结果
//            jsonStr = response.getBody();
            logger.info("request URL:" + url + ",param:" + serverInfoBean.toString() + ",Return:" + jsonStr);
        }
        return jsonStr;
    }

    public String uploadFile(String url,MultipartFile jarFile,String tempPath)throws Exception{
//        String tempFileName = UUID.randomUUID()+ jarFile.getOriginalFilename().substring(jarFile.getOriginalFilename().lastIndexOf("."));
        String tempFilePath = tempPath+"/" + jarFile.getOriginalFilename();
        File tempFile = new File(tempFilePath);
        //保存临时文件
        jarFile.transferTo(tempFile);
        //设置HTTP头信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add("Content-Disposition", "filename=\"" + jarFile.getOriginalFilename() + "\"");
        //HTTP参数设置
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
        params.add("file", new FileSystemResource(tempFilePath));
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        try {
            //删除临时文件
            tempFile.delete();
        } catch (Exception e) {
            logger.error("删除临时文件失败! "+tempFilePath+" "+e.getMessage(),e);
        }
        return response.getBody();
    }

}
