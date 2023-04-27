package edu.kh.comm.member.model.service;

import java.util.Map;

import edu.kh.comm.member.model.vo.Member;

public interface MyPageService {
	
	public abstract int updateInfo(int memberNo, Map<String, Object> paramMap, String newAddress);

	public abstract int changePw(Member loginMember, String currentPw, String newPw);

}
