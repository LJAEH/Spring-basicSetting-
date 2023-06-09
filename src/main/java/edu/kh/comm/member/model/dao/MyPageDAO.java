package edu.kh.comm.member.model.dao;

import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.kh.comm.member.model.vo.Member;

@Repository
public class MyPageDAO {
	
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	private Logger logger = LoggerFactory.getLogger(MyPageDAO.class);
	


	public int changePw(Member loginMember) {
		
		logger.debug(loginMember.getMemberPw() + " // 로그인멤버 객체 내에 새로운 비번 DAO");
		
		return sqlSession.update("myPageMapper.changePw",loginMember);
	}

	public String checkPw(Member loginMember) {
		logger.debug(loginMember.getMemberNo() + "  / checkPw  제발가라");
		return sqlSession.selectOne("myPageMapper.checkPw",loginMember);
	}


	public int secession(Member loginMember) {
		return sqlSession.update("myPageMapper.secession",loginMember);
	}


	
	public int updateInfo(Map<String, Object> paramMap) {
			
		return sqlSession.update("myPageMapper.updateInfo",paramMap);
	}


	/** 프로필 이미지 수정
	 * @param map
	 * @return result
	 */
	public int updateProfile(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return sqlSession.update("myPageMapper.updateProfile", map);
	}
	


}
