package com.lj.autoRemote.service;

import com.lj.autoRemote.beans.ServerInfoBean;
import com.lj.autoRemote.dao.AutoRemoteDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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



}
