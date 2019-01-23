package com.yr.shiro.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class AccountEntity implements Serializable {
	private int id;
	private String account;
	private String password;
	private List<RoleEntity> listRole = new ArrayList<RoleEntity>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<RoleEntity> getListRole() {
		return listRole;
	}

	public void setListRole(List<RoleEntity> listRole) {
		this.listRole = listRole;
	}

}
