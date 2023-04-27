package edu.kh.comm.member.model.dao;

import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.kh.comm.member.model.vo.Member;

@Repository
public class MyPageDAO {
	
	private SqlSessionTemplate sqlSession;
	private Logger logger = LoggerFactory.getLogger(MyPageDAO.class);
	
	public int updateInfo(int memberNo, Map<String, Object> paramMap, String newAddress) {
		// TODO Auto-generated method stub
		
		String newNick = (String) paramMap.get("updateMemberNickname");
		String newTel = (String) paramMap.get("updateMemberTel");
		
		Member member = new Member();
		
		member.setMemberNo(memberNo);
		member.setMemberNickname(newNick);
		member.setMemberTel(newTel);
		member.setMemberAddress(newAddress);
		
		int result = sqlSession.update("myPageMapper.updateInfo",member);
		
		return result;
	}

	public String checkPw(int memberNo) {
		
		return sqlSession.selectOne("myPageMapper.checkPw",memberNo);
	}

	public int changePw(String bcryptPw, Member loginMember) {
		
		loginMember.setMemberPw(bcryptPw);
		
		return sqlSession.update("myPageMapper.changePw",loginMember);
	}

}
