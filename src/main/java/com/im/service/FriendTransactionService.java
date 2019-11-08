package com.im.service;

import com.im.dbmodel.Relation;

public interface FriendTransactionService {

	// 同意好友申请（申请记录中的isagree变成1，同时添加相对关系）
	public void updateAndInsertFriendRelation(Relation relation1, Relation relation2);

//		//好友之间的用户钱包转账
//		public void friendTransferAndInsert(String senderHash, int senderBalance, String receiverHash, int receiverBalance, TXRecord txRecord);
}
