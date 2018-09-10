package com.lj.autoRemote.actions;

import com.lj.autoRemote.beans.ServerInfoBean;
import com.lj.autoRemote.service.AutoRemoteService;
import com.lj.utils.CtfoJsonUtil;
import com.lj.utils.FileUtil;
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

	/** 保存备案表信息 */
	@RequestMapping(value = "/saveServerInfo", method = { RequestMethod.POST, RequestMethod.GET })
	public void saveServerInfo(HttpServletRequest request, HttpServletResponse response, ServerInfoBean serverInfoBean) {
		String returnStr = "";
		try {
			Map<String,String> map = autoRemoteService.saveServerInfo(serverInfoBean);
			returnStr = CtfoJsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
		} catch (Exception e) {
			logger.error("AutoRemoteController saveServerInfo() is ERROR!"+e.getMessage(),e);
		}
	}

	/** 更新备案表信息 */
	@RequestMapping(value = "/updateServerInfo", method = { RequestMethod.POST, RequestMethod.GET })
	public void updateServerInfo(HttpServletRequest request, HttpServletResponse response, ServerInfoBean serverInfoBean) {
		String returnStr = "";
		try {
			Map<String,String> map = autoRemoteService.updateServerInfo(serverInfoBean);
			returnStr = CtfoJsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
		} catch (Exception e) {
			logger.error("AutoRemoteController updateServerInfo() is ERROR!"+e.getMessage(),e);
		}
	}

	/** 查看备案表信息 */
	@RequestMapping(value = "/queryServerInfoList", method = { RequestMethod.POST, RequestMethod.GET })
	public void queryServerInfoList(HttpServletRequest request, HttpServletResponse response) {
		String returnStr = "";
		try {
			Map<String,Object> map = autoRemoteService.queryServerInfoList();
			returnStr = CtfoJsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
		} catch (Exception e) {
			logger.error("AutoRemoteController queryServerInfoList() is ERROR!"+e.getMessage(),e);
		}
	}


	/** 查看本机服务运行情况 */
	@RequestMapping(value = "/local/queryServerRunState", method = { RequestMethod.POST, RequestMethod.GET })
	public void queryServerRunState(HttpServletRequest request, HttpServletResponse response, ServerInfoBean serverInfoBean)throws Exception {
		String returnStr = "";
		try {
			Map<String,Object> map = autoRemoteService.queryServerRunState(serverInfoBean);
			returnStr = CtfoJsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
		} catch (Exception e) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("state","9001");
			map.put("bak","查询服务运行状况执行异常。 "+e.getMessage());
			returnStr = CtfoJsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
			logger.error("AutoRemoteController /local/queryServerRunState is ERROR!"+e.getMessage(),e);
		}
	}

	/** 查看远程主机服务运行情况 */
	@RequestMapping(value = "/queryServerRunState", method = { RequestMethod.POST, RequestMethod.GET })
	public void queryRemoteServerRunState(HttpServletRequest request, HttpServletResponse response,ServerInfoBean serverInfoBean)throws Exception {
		String returnStr = "";
		try {
			returnStr = autoRemoteService.queryRemoteServerRunState(serverInfoBean);
			retrunData(response, returnStr);
		} catch (Exception e) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("state","9001");
			map.put("bak","请求远程服务异常,可能该服务未部署监控节点。 "+e.getMessage());
			returnStr = CtfoJsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
			logger.error("AutoRemoteController /queryServerRunState is ERROR!"+e.getMessage(),e);
		}
	}

	/** 程序包推送给远程节点 */
	@RequestMapping(value = "/serverUp", method = { RequestMethod.POST, RequestMethod.GET })
	public void remoteServerUp(HttpServletRequest request, HttpServletResponse response,@RequestParam("file") MultipartFile file)throws Exception {
		String returnStr = "upload success";
		try {
			Map<String,Object> map = autoRemoteService.serverUp(file);
			returnStr = CtfoJsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
		} catch (Exception e) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("state","9001");
			map.put("bak","程序升级包上传异常。"+e.getMessage());
			returnStr = CtfoJsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
			logger.error("AutoRemoteController /serverUp is ERROR!"+e.getMessage(),e);
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
			map.put("bak", "程序升级包上传成功! RemoteHost:"+request.getRemoteHost()+" Path:"+filePathName);
			returnStr = CtfoJsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
		} catch (Exception e) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("state", "9001");
			map.put("bak", "程序升级包上传异常。" + e.getMessage());
			returnStr = CtfoJsonUtil.toCompatibleJSONString(map);
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
			returnStr = CtfoJsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
		} catch (Exception e) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("state", "9001");
			map.put("bak", "服务重启请求已发送。" + e.getMessage());
			returnStr = CtfoJsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
			logger.error("AutoRemoteController /local/rebootServer is ERROR!" + e.getMessage(), e);
		}
	}

	/**
	 * 重启服务（本地）
	 */
	@RequestMapping(value = "/local/rebootServer", method = {RequestMethod.POST, RequestMethod.GET})
	public void rebootServer(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String returnStr = "";
		try {
			autoRemoteService.rebootServer();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("state", "1001");
			map.put("bak", "服务重启请求已发送! RemoteHost:"+request.getRemoteHost()+",Host:"+request.getLocalAddr());
			returnStr = CtfoJsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
		} catch (Exception e) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("state", "9001");
			map.put("bak", "服务重启请求已发送。" + e.getMessage());
			returnStr = CtfoJsonUtil.toCompatibleJSONString(map);
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
			returnStr = CtfoJsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
		} catch (Exception e) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("state", "9001");
			map.put("bak", "服务重启请求已发送。" + e.getMessage());
			returnStr = CtfoJsonUtil.toCompatibleJSONString(map);
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
			returnStr = CtfoJsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
		} catch (Exception e) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("state", "9001");
			map.put("bak", "更新程序包请求已发送。" + e.getMessage());
			returnStr = CtfoJsonUtil.toCompatibleJSONString(map);
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
			returnStr = CtfoJsonUtil.toCompatibleJSONString(map);
			retrunData(response, returnStr);
		} catch (Exception e) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("state", "9001");
			map.put("bak", "更新程序包请求已发送。" + e.getMessage());
			returnStr = CtfoJsonUtil.toCompatibleJSONString(map);
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
            returnStr = CtfoJsonUtil.toCompatibleJSONString(map);
            retrunData(response, returnStr);
        } catch (Exception e) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("state", "9001");
            map.put("bak", "保存同步的DB文件到本地异常。"+",Host:"+request.getLocalAddr() + e.getMessage());
            returnStr = CtfoJsonUtil.toCompatibleJSONString(map);
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
