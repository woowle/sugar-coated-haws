package com.woowle.sugarcoatedhaws.enums;

public enum IsDeleteEnum {

	//0 非删除，1 已删除
	IS_DELETE_TRUE(1),IS_DELETE_FALSE(0);
	
	Integer value;

	private IsDeleteEnum(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}
	
}
