package com.woowle.sugarcoatedhaws.enums;

public enum LevelEnum {

	//0 非删除，1 已删除
	LEVEL_1(0),LEVEL_2(1),LEVEL_3(2);

	Integer value;

	private LevelEnum(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}
	
}
