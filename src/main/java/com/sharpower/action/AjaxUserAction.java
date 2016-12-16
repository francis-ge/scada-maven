package com.sharpower.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.dao.DataIntegrityViolationException;

import com.opensymphony.xwork2.ActionSupport;
import com.sharpower.entity.User;
import com.sharpower.service.UserService;

public class AjaxUserAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private User user;
	private UserService userService;
	private List<User> users = new ArrayList<>();
	private String result;
	private String ids;
	private String updatePassword;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	public String getResult() {
		return result;
	}
	public List<User> getUsers() {
		return users;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public void setUpdatePassword(String updatePassword) {
		this.updatePassword = updatePassword;
	}
	
	public String allUsers(){
		try {
			users = userService.findAllEntities();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String addOrUpdateUser(){
		if (user.getId()==null) {
			user.setCreatDate(new Date());
			user.setPassword(new SimpleHash("MD5", user.getPassword(), new Md5Hash(user.getName()+user.getName()), 3).toHex() );
			 
		}else{
			User user1 = userService.getEntity(user.getId());

			if (user1!=null) {
				if (!user.getPassword().equals(user1.getPassword())) {
					user.setPassword(new SimpleHash("MD5", user.getPassword(), new Md5Hash(user.getName()+user.getName()), 3).toHex());
				}
			}
		}
		
		try {			
			userService.saveOrUpdateEntity(user);
			
			result = "操作成功！";
		} catch (Exception e) {
			e.printStackTrace();
			result = "操作失败！"+e.getMessage();
		}	
		
		return SUCCESS;
	}
	
	public String deleteUser(){
		String idString[] = ids.split(",");
		
		try {
			for (String idStr : idString) {
				User user = new User();
				user.setId(Integer.parseInt(idStr));
				userService.deleteEntity(user);
			}
			result = "删除成功！";
		}catch (DataIntegrityViolationException e) {
			
			e.printStackTrace();
			result = "存在相关数据记录，无法删除！" + e.getMessage();
		}catch (Exception e) {
			e.printStackTrace();
			result = "删除失败！"+ e.getMessage();
		}
		
		return SUCCESS;
	}
	
	public String login(){
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(user.getName(), user.getPassword());

		if(!subject.isAuthenticated()){
			try {
				//执行认证操作. 
				subject.login(token);
				result = "验证成功！";
			}catch(DisabledAccountException de){
				result = "此帐户已禁用！";
			}catch (UnknownAccountException ue) {
				result = "账户错误！";
			}catch (IncorrectCredentialsException ie) {
				result = "密码错误！";
			}catch (AuthenticationException ae) {
				result = "登陆失败！"+ae.getMessage();
				ae.printStackTrace();
			}
			
		}else{
			result="验证成功！";
		}
		return SUCCESS;
	}
	
	public String loadCurrentUserName(){
		Subject subject = SecurityUtils.getSubject();
		PrincipalCollection collection = subject.getPrincipals();
		
        if (null != collection && !collection.isEmpty()){  
           result = (String) collection.iterator().next();
        }
        
		return SUCCESS;
	}
	
	public String loadCurrentUser(){
		Subject subject = SecurityUtils.getSubject();
		Object obj = subject.getPrincipal();
		
		if (obj!=null) {
			String userName = (String)obj;
			
			List<User> users = userService.findEntityByHQL("from User u where u.name=?", userName);
			
			if (users!=null && !users.isEmpty()) {
				user = users.iterator().next();
			}
			
		}
		return SUCCESS;
	}
	
	public String updatePassword(){
		user.setPassword(new SimpleHash("MD5", user.getPassword(), new Md5Hash(user.getName()+user.getName()), 3).toHex());
		User user1 = userService.getEntity(user.getId());
		
		if (user1!=null) {
			if (!user.getPassword().equals(user1.getPassword())) {
				result = "原密码错误！";
			}else{
				try {		
					user.setPassword( new SimpleHash("MD5", updatePassword, new Md5Hash(user.getName()+user.getName()), 3).toHex());
					userService.saveOrUpdateEntity(user);
					result = "修改成功！";
				} catch (Exception e) {
					e.printStackTrace();
					result = "修改失败！"+e.getMessage();
				}	
			}
		}else {
			result="用户错误！";
		}
		
		return SUCCESS;
	}
	
}
