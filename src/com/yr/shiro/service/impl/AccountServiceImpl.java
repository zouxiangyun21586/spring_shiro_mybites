package com.yr.shiro.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yr.shiro.dao.AccountDao;
import com.yr.shiro.entity.AccountEntity;
import com.yr.shiro.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService<AccountEntity>{

	@Autowired
	private AccountDao<AccountEntity> accountDao;
	
	@Override
	public List<AccountEntity> queryAccount() {
		List<AccountEntity> listAcc = accountDao.queryAccount();
		return listAcc;
	}

	@Transactional
	@Override
	public Boolean addAccount(AccountEntity acc) {
		try {
			accountDao.addAccount(acc);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Transactional
	@Override
	public Boolean delAccount(Integer id) {
		try {
			accountDao.delAccount(id);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public AccountEntity getAccount(Integer id) {
		AccountEntity ac = accountDao.getAccount(id);
		return ac;
	}

	@Transactional
	@Override
	public Boolean updAccount(AccountEntity acc) {
		if(!acc.getAccount().equals("") && acc.getAccount() != null && !acc.getPassword().equals("") && acc.getPassword() != null){
			accountDao.updAccount(acc);
			return true;
		}else{
			return false;
		}
	}

	@Override
	public AccountEntity findAccountName(String name) {
		if(name!=null && !name.equals("")){
			AccountEntity accounEntity = accountDao.findAccountName(name);
			return accounEntity;
		}
		return null;
	}
	
}
