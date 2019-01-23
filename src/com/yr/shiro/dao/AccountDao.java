package com.yr.shiro.dao;

import java.util.List;

import com.yr.shiro.entity.AccountEntity;

public interface AccountDao<T> {
	// 查询部门信息
	public List<T> queryAccount();
	
	// 添加
	public void addAccount(T t);

	// 删除
	public void delAccount(Integer id);

	// 修改先根据id查询回显数据
	public T getAccount(Integer id);

	// 修改
	public void updAccount(T t);
	
	public AccountEntity findAccountName(String name);
	
}
