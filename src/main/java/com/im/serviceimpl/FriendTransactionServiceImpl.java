package com.im.serviceimpl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.im.dao.RelationMapper;
import com.im.dbmodel.Relation;
import com.im.service.FriendTransactionService;

//此部分用于事务管理
@Service //bean的作用域为会话session模式，INTERFACES允许该bean注入到单例的bean中@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.INTERFACES) 
@Transactional
public class FriendTransactionServiceImpl implements FriendTransactionService {

	@Autowired
	private RelationMapper relationMapper;
	
	@Override
	public void updateAndInsertFriendRelation(Relation relation1, Relation relation2) {

		relationMapper.updateIsAgreeByUserIdAndFriendId(relation1);
		relationMapper.addToRelation(relation2);
	}

}
