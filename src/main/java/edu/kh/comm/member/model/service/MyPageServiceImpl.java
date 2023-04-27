package edu.kh.comm.member.model.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import edu.kh.comm.member.model.dao.MyPageDAO;
import edu.kh.comm.member.model.vo.Member;

@Service
public class MyPageServiceImpl implements MyPageService{
	
	@Autowired
	private MyPageDAO dao;
	
	private BCryptPasswordEncoder bcrypt;
	
	@Override
	public int updateInfo(int memberNo, Map<String, Object> paramMap, String newAddress) {
		// TODO Auto-generated method stub
		int result = dao.updateInfo(memberNo,paramMap,newAddress);
		
		return result;
	}


	@Override
	public int changePw(Member loginMember, String currentPw, String newPw) {
		// TODO Auto-generated method stub
		
		int result = 0;
		
		if(bcrypt.matches(currentPw,dao.checkPw(loginMember.getMemberNo()))) {
			String bcryptPw = bcrypt.encode(newPw);
			result = dao.changePw(bcryptPw,loginMember);
		} 
		
		return result;
	}

}
