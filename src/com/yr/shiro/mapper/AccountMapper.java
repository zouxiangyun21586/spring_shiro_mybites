package com.yr.shiro.mapper;

import java.util.List;

import com.yr.shiro.entity.AccountEntity;

public interface AccountMapper {
	// 查询部门信息
	public List<AccountEntity> queryAccount();

	// 添加
	public void addAccount(AccountEntity accountEntity);

	// 删除
	public void delAccount(Integer id);

	// 修改先根据id查询回显数据
	public AccountEntity getAccount(Integer id);

	// 修改
	public void updAccount(AccountEntity accountEntity);
	
	AccountEntity findAccountName(String name);
	
}
