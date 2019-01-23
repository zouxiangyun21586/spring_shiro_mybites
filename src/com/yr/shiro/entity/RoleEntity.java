package com.yr.shiro.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class RoleEntity implements Serializable {
	private int id;
	private String name;
	private List<PowerEntity> listPower = new ArrayList<PowerEntity>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<PowerEntity> getListPower() {
		return listPower;
	}

	public void setListPower(List<PowerEntity> listPower) {
		this.listPower = listPower;
	}

}
