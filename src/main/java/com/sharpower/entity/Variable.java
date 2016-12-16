package com.sharpower.entity;

public class Variable{
	private Integer id;
	private String name;
	private String dbName;
	private String showNameCN;
	
	private PlcType plcType;
	private VariableType type;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	
	public String getShowNameCN() {
		return showNameCN;
	}
	public void setShowNameCN(String showName) {
		this.showNameCN = showName;
	}
	
	public PlcType getPlcType() {
		return plcType;
	}
	public void setPlcType(PlcType plcType) {
		this.plcType = plcType;
	}
	
	public VariableType getType() {
		return type;
	}
	public void setType(VariableType type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "Variable [id=" + id + ", name=" + name + ", dbName=" + dbName + ", type=" + type + "]";
	}
	
	public Variable() {
		super();
	}
	
	public Variable(int id, String name, String dbName, String showNameCN, int variableTypeId, String variableTypePlcType,  String variableType, int plcTypeId,String plcType){
		super();
		this.id=id;
		this.name= name;
		this.dbName = dbName;
		this.showNameCN = showNameCN;
		
		this.type = new VariableType();
		this.type.setId(variableTypeId);
		this.type.setPlcType(variableTypePlcType);
		this.type.setType(variableType);
		
		this.plcType = new PlcType();
		this.plcType.setId(plcTypeId);
		this.plcType.setName(plcType);
		
	}
		
}
