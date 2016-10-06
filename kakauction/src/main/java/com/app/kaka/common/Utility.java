package com.app.kaka.common;

import com.app.kaka.work.crypt.SHA256;

public class Utility {
	public static final int RECORD_COUNT_PER_PAGE = 20;
	public static final int BLOCK_SIZE = 10;
	
	public static final int REPLY_RECORD_COUNT_PER_PAGE = 50;
	
	public static final int MAUCLIST_COUNT_PER_PAGE = 20;
	
	public static final int AUCTION_MEMBER_COUNT_PER_PAGE = 15;
	
	public static final int SCHEDULE_COUNT_PER_PAGE=10;
	
	public static String hashEncryption(String str) throws Exception{
		//SHA256을 이용한 암호화
		SHA256 sha = SHA256.getInsatnce();
		String shaStr = sha.getSha256(str.getBytes());
		
		//BCrypt를 이용한 재암호화
		//String bcryptStr = BCrypt.hashpw(shaStr, BCrypt.gensalt());
		
		return shaStr;
	}
}
