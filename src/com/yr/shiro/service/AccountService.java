package com.yr.shiro.service;

import java.util.List;

import com.yr.shiro.entity.AccountEntity;

public interface AccountService<T> {
	// 查询部门信息
	public List<T> queryAccount();

	// 添加
	public Boolean addAccount(T t);

	// 删除
	public Boolean delAccount(Integer id);

	// 修改先根据id查询回显数据
	public T getAccount(Integer id);

	// 修改
	public Boolean updAccount(T t);
	
	public AccountEntity findAccountName(String name);
	
}
