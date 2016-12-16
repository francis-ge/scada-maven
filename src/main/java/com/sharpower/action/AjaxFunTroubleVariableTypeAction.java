package com.sharpower.action;

import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.sharpower.entity.TroubleType;
import com.sharpower.service.FunTroubleVariableTypeService;

public class AjaxFunTroubleVariableTypeAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

	private FunTroubleVariableTypeService funTroubleVariableTypeService;
	private List<TroubleType> troubleTypes = new ArrayList<>();

	private String result;
	
	public void setFunTroubleVariableTypeService(FunTroubleVariableTypeService funTroubleVariableTypeService) {
		this.funTroubleVariableTypeService = funTroubleVariableTypeService;
	}

	public List<TroubleType> getTroubleTypes() {
		return troubleTypes;
	}
	
	public String getResult() {
		return result;
	}
	
	public String all(){
		try {
			troubleTypes = funTroubleVariableTypeService.findAllEntities();
			result = "获取成功！";
		} catch (Exception e) {
			result = "获取失败！"+e.getMessage();
		}
		
		return SUCCESS;
	}
	

}
