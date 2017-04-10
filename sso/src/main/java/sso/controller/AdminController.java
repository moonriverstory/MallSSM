package sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import common.utils.ExceptionUtil;
import common.utils.MallResult;
import po.TbAdminUser;
import sso.service.AdminService;

@Controller
@RequestMapping("/user/admin")
public class AdminController {
	
	@Autowired
	private AdminService userService;
	
	
	@RequestMapping("/check/{param}/{type}")
	@ResponseBody
	public Object checkData(@PathVariable String param, @PathVariable Integer type, String callback) {
		
		MallResult result = null;
		
		//参数有效性校验
		if (StringUtils.isBlank(param)) {
			result = MallResult.build(400, "校验内容不能为空");
		}
		if (type == null) {
			result = MallResult.build(400, "校验内容类型不能为空");
		}
		if (type != 1 && type != 2 && type != 3 ) {
			result = MallResult.build(400, "校验内容类型错误");
		}
		//校验出错
		if (null != result) {
			if (null != callback) {
				MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
				mappingJacksonValue.setJsonpFunction(callback);
				return mappingJacksonValue;
			} else {
				return result; 
			}
		}
		//调用服务
		try {
			result = userService.checkData(param, type);
			
		} catch (Exception e) {
			result = MallResult.build(500, ExceptionUtil.getStackTrace(e));
		}
		
		if (null != callback) {
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		} else {
			return result; 
		}
	}
	
	
	//创建用户
	@RequestMapping(value="/register",method=RequestMethod.POST)
	@ResponseBody
	public MallResult createUser(TbAdminUser user){
		try {
			MallResult result = userService.createUser(user);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return MallResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}
	
	//接收表单，包含用户名和密码
	@RequestMapping(value="/login",method=RequestMethod.POST)
	@ResponseBody
	public MallResult userLogin(String username, String password,
                                HttpServletRequest  request, HttpServletResponse response){
		try {
			MallResult result = userService.userLogin(username, password,request,response);
			return result;
		} catch (Exception e) {	
			e.printStackTrace();
			return MallResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}
	
	//接收token调用service返回用户信息 
	
	@RequestMapping("/token/{token}")
	@ResponseBody
	public Object getUserByToken(@PathVariable  String token,String callback){
		
		MallResult result=null;
		
		try {
			result = userService.getUserByToken(token);
			
		} catch (Exception e) {	
			e.printStackTrace();
			return MallResult.build(500, ExceptionUtil.getStackTrace(e));
		}
		
		//判断是否为json调用
		if(StringUtils.isBlank(callback)){
			return result;
		}else{
			MappingJacksonValue  mappingJacksonValue=new MappingJacksonValue(result);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		}
	}
	
}

