package com.lj.autoRemote.actions;

import com.lj.autoRemote.beans.ServerInfoBean;
import com.lj.autoRemote.service.AutoRemoteService;
import com.lj.utils.JsonUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 服务管控-控制层
 * 
 * @author James Date:2018年8月9日 16点50分
 */
@Controller
@RequestMapping("/autoRemote/apis")
public class AutoRemoteController {

	protected static Logger logger = Logger.getLogger(AutoRemoteController.class);

	@Autowired
	private AutoRemoteService autoRemoteService;

	@Value("${server.upPath}")
	String upPath;

    @Value("${spring.datasource.sqllite.url}")
    private String dbUrl;

	/** 另存备案表信息 */
	@RequestMapping(value = "/saveServerInfo", method = { RequestMethod.POST, RequestMethod.GET })
	public void saveServerInfo(HttpServletRequest request, HttpServletResponse response, ServerInfoBean serverInfoBean) {
		String returnStr = "";
		try {
			Map<String,String> map = autoRemoteService.saveServerInfo(serverInfoBean);
			returnStr = JsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
		} catch (Exception e) {
			logger.error("AutoRemoteController saveServerInfo() is ERROR!"+e.getMessage(),e);
		}
	}

	/** 更新保存备案表信息 */
	@RequestMapping(value = "/updateServerInfo", method = { RequestMethod.POST, RequestMethod.GET })
	public void updateServerInfo(HttpServletRequest request, HttpServletResponse response, ServerInfoBean serverInfoBean) {
		String returnStr = "";
		try {
			Map<String,String> map = autoRemoteService.updateServerInfo(serverInfoBean);
			returnStr = JsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
		} catch (Exception e) {
			logger.error("AutoRemoteController updateServerInfo() is ERROR!"+e.getMessage(),e);
		}
	}

	/** 删除保存备案表信息 */
	@RequestMapping(value = "/delServerInfo", method = { RequestMethod.POST, RequestMethod.GET })
	public void delServerInfo(HttpServletRequest request, HttpServletResponse response, ServerInfoBean serverInfoBean) {
		String returnStr = "";
		try {
			Map<String,String> map = autoRemoteService.delServerInfo(serverInfoBean);
			returnStr = JsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
		} catch (Exception e) {
			logger.error("AutoRemoteController delServerInfo() is ERROR!"+e.getMessage(),e);
		}
	}

	/** 查看备案表信息 */
	@RequestMapping(value = "/queryServerInfoList", method = { RequestMethod.POST, RequestMethod.GET })
	public void queryServerInfoList(HttpServletRequest request, HttpServletResponse response) {
		String returnStr = "";
		try {
			Map<String,Object> map = autoRemoteService.queryServerInfoList();
			returnStr = JsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
		} catch (Exception e) {
			logger.error("AutoRemoteController queryServerInfoList() is ERROR!"+e.getMessage(),e);
		}
	}


	/** 保存本程序节点信息 */
	@RequestMapping(value = "/saveNodeServerInfo", method = { RequestMethod.POST, RequestMethod.GET })
	public void saveNodeServerInfo(HttpServletRequest request, HttpServletResponse response, ServerInfoBean serverInfoBean) {
		String returnStr = "";
		try {
			Map<String,String> map = autoRemoteService.saveNodeServerInfo(serverInfoBean);
			returnStr = JsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
		} catch (Exception e) {
			logger.error("AutoRemoteController saveNodeServerInfo() is ERROR!"+e.getMessage(),e);
		}
	}

	/** 更新本程序节点信息 */
	@RequestMapping(value = "/updateNodeServerInfo", method = { RequestMethod.POST, RequestMethod.GET })
	public void updateNodeServerInfo(HttpServletRequest request, HttpServletResponse response, ServerInfoBean serverInfoBean) {
		String returnStr = "";
		try {
			Map<String,String> map = autoRemoteService.updateNodeServerInfo(serverInfoBean);
			returnStr = JsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
		} catch (Exception e) {
			logger.error("AutoRemoteController updateNodeServerInfo() is ERROR!"+e.getMessage(),e);
		}
	}

	/** 删除节点信息 */
	@RequestMapping(value = "/delNodeServerInfo", method = { RequestMethod.POST, RequestMethod.GET })
	public void delNodeServerInfo(HttpServletRequest request, HttpServletResponse response, int id) {
		String returnStr = "";
		try {
			Map<String,String> map = autoRemoteService.delNodeServerInfo(id);
			returnStr = JsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
		} catch (Exception e) {
			logger.error("AutoRemoteController delNodeServerInfo() is ERROR!"+e.getMessage(),e);
		}
	}

	/** 查看本程序节点信息 */
	@RequestMapping(value = "/queryNodeServerInfoList", method = { RequestMethod.POST, RequestMethod.GET })
	public void queryNodeServerInfoList(HttpServletRequest request, HttpServletResponse response) {
		String returnStr = "";
		try {
			Map<String,Object> map = autoRemoteService.queryNodeServerInfoList();
			returnStr = JsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
		} catch (Exception e) {
			logger.error("AutoRemoteController queryNodeServerInfoList() is ERROR!"+e.getMessage(),e);
		}
	}

	/** 查看本机服务运行情况 */
	@RequestMapping(value = "/local/queryServerRunState", method = { RequestMethod.POST, RequestMethod.GET })
	public void queryServerRunState(HttpServletRequest request, HttpServletResponse response, ServerInfoBean serverInfoBean)throws Exception {
		String returnStr = "";
		try {
			Map<String,Object> map = autoRemoteService.queryServerRunState(serverInfoBean);
			returnStr = JsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
		} catch (Exception e) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("state","9001");
			map.put("bak","查询服务运行状况执行异常。 "+e.getMessage());
			returnStr = JsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
			logger.error("AutoRemoteController /local/queryServerRunState is ERROR!"+e.getMessage(),e);
		}
	}

	/** 查看远程主机服务运行情况 */
	@RequestMapping(value = "/queryServerRunState", method = { RequestMethod.POST, RequestMethod.GET })
	public void queryRemoteServerRunState(HttpServletRequest request, HttpServletResponse response,ServerInfoBean serverInfoBean)throws Exception {
		String returnStr = "";
		int id = serverInfoBean.getId();
		Map<String,Object> rsMap = new HashMap<String,Object>();
        rsMap.put("id",id);
		try {
			returnStr = autoRemoteService.queryRemoteServerRunState(serverInfoBean);
			retrunData(response, returnStr);
		} catch (Exception e) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("state","9001");
			map.put("result",rsMap);
			map.put("bak","请求远程服务异常,可能该服务未部署监控节点。 "+e.getMessage());
			returnStr = JsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
			logger.error("AutoRemoteController /queryServerRunState is ERROR!"+e.getMessage(),e);
		}
	}

	/** 查看本机节点服务运行情况 */
	@RequestMapping(value = "/local/queryNodeServerRunState", method = { RequestMethod.POST, RequestMethod.GET })
	public void queryNodeServerRunState(HttpServletRequest request, HttpServletResponse response, ServerInfoBean serverInfoBean)throws Exception {
		String returnStr = "";
		try {
			Map<String,Object> map = autoRemoteService.queryNodeServerRunState(serverInfoBean);
			returnStr = JsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
		} catch (Exception e) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("state","9001");
			map.put("bak","查询服务运行状况执行异常。 "+e.getMessage());
			returnStr = JsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
			logger.error("AutoRemoteController /local/queryNodeServerRunState is ERROR!"+e.getMessage(),e);
		}
	}

	/** 查看远程主机节点服务运行情况 */
	@RequestMapping(value = "/queryNodeServerRunState", method = { RequestMethod.POST, RequestMethod.GET })
	public void queryRemoteNodeServerRunState(HttpServletRequest request, HttpServletResponse response,ServerInfoBean serverInfoBean)throws Exception {
		String returnStr = "";
		try {
			returnStr = autoRemoteService.queryRemoteNodeServerRunState(serverInfoBean);
			retrunData(response, returnStr);
		} catch (Exception e) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("state","9001");
			map.put("bak","请求远程服务异常,可能该服务未部署监控节点。 "+e.getMessage());
			returnStr = JsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
			logger.error("AutoRemoteController /queryNodeServerRunState is ERROR!"+e.getMessage(),e);
		}
	}

	/** 程序包推送给远程节点 */
	@RequestMapping(value = "/serverUp", method = { RequestMethod.POST, RequestMethod.GET })
	public void remoteServerUp(HttpServletRequest request, HttpServletResponse response,@RequestParam("file") MultipartFile file)throws Exception {
		String returnStr = "upload success";
		try {
			Map<String,Object> map = autoRemoteService.serverUp(file);
			returnStr = JsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
		} catch (Exception e) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("state","9001");
			map.put("bak","程序升级包上传异常。"+e.getMessage());
			returnStr = JsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
			logger.error("AutoRemoteController /serverUp is ERROR!"+e.getMessage(),e);
		}
	}

	/** DB文件推送给远程节点 */
	@RequestMapping(value = "/dbUp", method = { RequestMethod.POST, RequestMethod.GET })
	public void remoteDbUp(HttpServletRequest request, HttpServletResponse response,@RequestParam("file") MultipartFile file)throws Exception {
		String returnStr = "upload success";
		try {
			Map<String,Object> map = autoRemoteService.dbUp(file);
			returnStr = JsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
		} catch (Exception e) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("state","9001");
			map.put("bak","DB文件上传异常。"+e.getMessage());
			returnStr = JsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
			logger.error("AutoRemoteController /dbUp is ERROR!"+e.getMessage(),e);
		}
	}

	/**
	 * 程序升级包上传(本地)
	 */
	@RequestMapping(value = "/local/serverUp", method = {RequestMethod.POST, RequestMethod.GET})
	public void serverUp(HttpServletRequest request, HttpServletResponse response, @RequestParam("file") MultipartFile file) throws Exception {
		String returnStr = "";
		try {
			new File(upPath).mkdirs();
			//保存到本地
			String filePathName = upPath + File.separator + file.getOriginalFilename();
			file.transferTo(new File(filePathName));
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("state", "1001");
			map.put("bak", "程序升级包上传成功! Host:"+request.getLocalAddr()+", RemoteHost:"+request.getRemoteHost()+" Path:"+filePathName);
			returnStr = JsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
		} catch (Exception e) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("state", "9001");
			map.put("bak", "程序升级包上传异常。" + e.getMessage());
			returnStr = JsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
			logger.error("AutoRemoteController /local/serverUp is ERROR!" + e.getMessage(), e);
		}
	}

	/**
	 * 重启服务（远程）
	 */
	@RequestMapping(value = "/rebootServer", method = {RequestMethod.POST, RequestMethod.GET})
	public void remoteRebootServer(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String returnStr = "";
		try {
			Map<String, Object> map = autoRemoteService.remoteRebootServer();
			map.put("state", "1001");
			map.put("bak", "远程服务重启请求已发送! RemoteHost:"+request.getRemoteHost()+",Host:"+request.getLocalAddr());
			returnStr = JsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
		} catch (Exception e) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("state", "9001");
			map.put("bak", "服务重启请求已发送。" + e.getMessage());
			returnStr = JsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
			logger.error("AutoRemoteController /local/rebootServer is ERROR!" + e.getMessage(), e);
		}
	}
	/**
	 * 重启服务（远程）
	 */
	@RequestMapping(value = "/rebootServerByIp", method = {RequestMethod.POST, RequestMethod.GET})
	public void rebootServerByIp(HttpServletRequest request, HttpServletResponse response,String ip) throws Exception {
		String returnStr = "";
		try {
			Map<String, Object> map = autoRemoteService.remoteRebootServer(ip);
			map.put("state", "1001");
			map.put("bak", "远程服务重启请求已发送! RemoteHost:"+request.getRemoteHost()+",Host:"+request.getLocalAddr());
			returnStr = JsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
		} catch (Exception e) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("state", "9001");
			map.put("bak", "服务重启请求已发送。" + e.getMessage());
			returnStr = JsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
			logger.error("AutoRemoteController /local/rebootServer is ERROR!" + e.getMessage(), e);
		}
	}

	/**
	 * 重启服务（本地）
	 */
	@RequestMapping(value = "/local/rebootServer", method = {RequestMethod.POST, RequestMethod.GET})
	public void rebootServer(final HttpServletRequest request, HttpServletResponse response) throws Exception {
        String returnStr = "";
        final Map<String, Object> map = new HashMap<String, Object>();
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        autoRemoteService.rebootServer();
                    }catch (Exception e){
                        map.put("state", "9001");
                        map.put("bak", "服务重启请求已发送。" + e.getMessage());
                        logger.error("AutoRemoteController /local/rebootServer is ERROR!" + e.getMessage(), e);
                    }
                }
            }).start();
            map.put("state", "1001");
            map.put("bak", "服务重启请求已发送! RemoteHost:"+request.getRemoteHost()+",Host:"+request.getLocalAddr());
			returnStr = JsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
		} catch (Exception e) {
			map.put("state", "9001");
			map.put("bak", "服务重启请求已发送。" + e.getMessage());
			returnStr = JsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
			logger.error("AutoRemoteController /local/rebootServer is ERROR!" + e.getMessage(), e);
		}
	}

	/**
	 * 更新程序包（本地）
	 */
	@RequestMapping(value = "/local/setupServer", method = {RequestMethod.POST, RequestMethod.GET})
	public void setupServer(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String returnStr = "";
		try {
			autoRemoteService.setupServer();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("state", "1001");
			map.put("bak", "服务重启请求已发送! RemoteHost:"+request.getRemoteHost()+",Host:"+request.getLocalAddr());
			returnStr = JsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
		} catch (Exception e) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("state", "9001");
			map.put("bak", "服务重启请求已发送。" + e.getMessage());
			returnStr = JsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
			logger.error("AutoRemoteController /local/rebootServer is ERROR!" + e.getMessage(), e);
		}
	}

	/**
	 * 更新程序包（远程）
	 */
	@RequestMapping(value = "/setupServer", method = {RequestMethod.POST, RequestMethod.GET})
	public void remoteSetupServer(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String returnStr = "";
		try {
			Map<String, Object> map = autoRemoteService.remoteSetupServer();
			map.put("state", "1001");
			map.put("bak", "更新程序包请求已发送! RemoteHost:"+request.getRemoteHost()+",Host:"+request.getLocalAddr());
			returnStr = JsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
		} catch (Exception e) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("state", "9001");
			map.put("bak", "更新程序包请求已发送。" + e.getMessage());
			returnStr = JsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
			logger.error("AutoRemoteController /local/rebootServer is ERROR!" + e.getMessage(), e);
		}
	}

	/**
	 * 同步数据库到所有监控节点（远程）
	 */
	@RequestMapping(value = "/synDB", method = {RequestMethod.POST, RequestMethod.GET})
	public void remoteSynDB(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String returnStr = "";
		try {
			Map<String, Object> map = autoRemoteService.remoteSynDB();
			map.put("state", "1001");
			map.put("bak", "更新程序包请求已发送! RemoteHost:"+request.getRemoteHost()+",Host:"+request.getLocalAddr());
			returnStr = JsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
		} catch (Exception e) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("state", "9001");
			map.put("bak", "更新程序包请求已发送。" + e.getMessage());
			returnStr = JsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
			logger.error("AutoRemoteController /local/rebootServer is ERROR!" + e.getMessage(), e);
		}
	}

    /**
     * 保存同步的DB文件到本地
     */
    @RequestMapping(value = "/local/synDB", method = {RequestMethod.POST, RequestMethod.GET})
    public void synDB(HttpServletRequest request, HttpServletResponse response, @RequestParam("file") MultipartFile file) throws Exception {
        String returnStr = "";
        try {
            //jdbc:sqlite:/home/yanfa_ro/autoRemoteSqlite.db
            String dbFilePathName = dbUrl.split(":")[2];
            String upPath = dbFilePathName.substring(0,dbFilePathName.lastIndexOf(File.separator));
            new File(upPath).mkdirs();
            //保存到本地
            String filePathName = dbFilePathName;
            file.transferTo(new File(filePathName));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("state", "1001");
            map.put("bak", "保存同步的DB文件到本地成功! RemoteHost:"+request.getRemoteHost()+",Host:"+request.getLocalAddr()+", Path:"+filePathName);
            returnStr = JsonUtil.toCompatibleJSONString(map);
            retrunData(response, returnStr);
        } catch (Exception e) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("state", "9001");
            map.put("bak", "保存同步的DB文件到本地异常。"+",Host:"+request.getLocalAddr() + e.getMessage());
            returnStr = JsonUtil.toCompatibleJSONString(map);
            retrunData(response, returnStr);
            logger.error("AutoRemoteController /local/synDB is ERROR!" + e.getMessage(), e);
        }
    }

	/**
	 * 返回结果
	 * 
	 * @param response
	 * @param dataStr
	 * @throws Exception
	 */
	private void retrunData(HttpServletResponse response, String dataStr) throws Exception {
		byte[] strBytes = dataStr.getBytes("utf-8");
		response.setContentType("text/html;charset=utf-8");
		response.setContentLength(strBytes.length);
		response.getOutputStream().write(strBytes);
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}

}
