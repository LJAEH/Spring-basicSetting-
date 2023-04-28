package edu.kh.comm.member.model.service;

import java.util.Map;

import org.apache.logging.log4j.core.Filter.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import edu.kh.comm.member.controller.MyPageController;
import edu.kh.comm.member.model.dao.MyPageDAO;
import edu.kh.comm.member.model.vo.Member;

@Service
public class MyPageServiceImpl implements MyPageService{
	
	private Logger logger = LoggerFactory.getLogger(MyPageController.class);

	@Autowired
	private MyPageDAO dao;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	


	@Override
	public int changePw(Member loginMember, String currentPw, String newPw) {
		// TODO Auto-generated method stub
		
		int result = 0;
		
		
		logger.debug("dbPw 가지러 가기 전");
		String dbPw = dao.checkPw(loginMember);
		logger.debug(dbPw + " // 디비에서 기존 비번 가져온 거");
		
		if(bcrypt.matches(currentPw,dbPw)) {
			String bcryptPw = bcrypt.encode(newPw);
			
			loginMember.setMemberPw(bcryptPw);
			
			result = dao.changePw(loginMember);
			
			logger.debug(loginMember.getMemberPw() + " // 로그인멤버 객체 내에 새로운 비번");
	
		} else {
			result = 0;
		}
		
		return result;
	}


	@Override
	public int secession(Member loginMember) {
		int result = 0;
		if (bcrypt.matches(loginMember.getMemberPw(), dao.checkPw(loginMember))) {
			result = dao.secession(loginMember);
		} else {
			result = 0;
		}
		
		return result;
	}


	@Override
	public int updateInfo(Map<String, Object> paramMap) {
		
		return dao.updateInfo(paramMap);
	}

}
