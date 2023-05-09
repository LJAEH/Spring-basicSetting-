package edu.kh.comm.member.model.service;

import java.io.IOException;
import java.util.Map;

import edu.kh.comm.member.model.vo.Member;

public interface MyPageService {
	
	
	public abstract int changePw(Member loginMember, String currentPw, String newPw);

	public abstract int secession(Member loginMember);

	public abstract int updateInfo(Map<String, Object> paramMap);

	public abstract int updateProfile(Map<String, Object> map) throws IOException;

}
