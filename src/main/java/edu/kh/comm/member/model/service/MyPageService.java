package edu.kh.comm.member.model.service;

import java.util.Map;

import edu.kh.comm.member.model.vo.Member;

public interface MyPageService {
	
	
	public abstract int changePw(Member loginMember, String currentPw, String newPw);

	public abstract int secession(Member loginMember);

	public abstract int updateInfo(Member loginMember);

}
