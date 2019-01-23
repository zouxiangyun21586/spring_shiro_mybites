package com.yr.shiro.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.yr.shiro.dao.AccountDao;
import com.yr.shiro.entity.AccountEntity;
import com.yr.shiro.mapper.AccountMapper;

@Repository
public class AccountDaoImpl implements AccountDao<AccountEntity>{

	@Autowired
	private AccountMapper accountMapper;

	@Override
	public List<AccountEntity> queryAccount() {
		List<AccountEntity> listAcc = accountMapper.queryAccount();
		return listAcc;
	}

	@Override
	public void addAccount(AccountEntity account) {
		accountMapper.addAccount(account);
	}

	@Override
	public void delAccount(Integer id) {
		accountMapper.delAccount(id);
	}

	@Override
	public AccountEntity getAccount(Integer id) {
		AccountEntity accountEntityList = accountMapper.getAccount(id);
		return accountEntityList;
	}

	@Override
	public void updAccount(AccountEntity account) {
		accountMapper.updAccount(account);
	}

	@Override
	public AccountEntity findAccountName(String name) {
		AccountEntity accountE = accountMapper.findAccountName(name);
		return accountE;
	}
	
}
