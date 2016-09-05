package com.app.kaka.email.model;

import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.app.kaka.member.model.MemberVO;
import com.sun.mail.imap.protocol.Namespaces.Namespace;

public class EmailDAOMybatis extends SqlSessionDaoSupport implements EmailDAO {
	private String namespace = "config.mybatis.mapper.oracle.member";

	@Override
	public String getPwd(MemberVO memberVo) {
		return getSqlSession().selectOne(namespace+".getPwd", memberVo);
	}
	
	/*@Override
	public String getPwd(Map<String, Object> paramMap) {
		return getSqlSession().selectOne(namespace+".getPwd", paramMap);
	}*/
	
}
