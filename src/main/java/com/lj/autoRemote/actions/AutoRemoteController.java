package com.lj.autoRemote.actions;

import com.lj.autoRemote.beans.ServerInfoBean;
import com.lj.autoRemote.service.AutoRemoteService;
import com.lj.utils.CtfoJsonUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
