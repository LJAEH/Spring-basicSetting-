package edu.kh.comm.member.model.service;

import java.util.List;

import edu.kh.comm.member.model.vo.Member;

//서비스는 왜 나눠서 쓸가? (interface, class)

/* ServiceInterface를 사용하는 이유
 * 
 * 1. 프로젝트에 규칙성을 부여하기 위해서
 * 2. Spring AOP 를 위해서 필요하다 
 * 3. 클래스간의 결합도를 약화 시키기 위해서 => 유지보수성 향상 
 **/


public interface MemberService {
	
	// interface는 모든 메서드가 추상 메서드(묵시적으로 public abstract)
	// 모든 필드는 상수임				  (묵시적으로 public static final)
	
	
	/** 로그인 서비스
	 * @param inputMember
	 * @return loginMember
	 */
	public abstract Member login(Member inputMember);

 	/** 이메일 중복 검사
	 * @param memberEmail
	 * @return
	 */
	public abstract int emailDupCheck(String memberEmail);

	/** 닉네임 중복 검사
	 * @param memberNickname
	 * @return
	 */
	public abstract int nickDupCheck(String memberNickname);

	public abstract int signUp(Member member);

	public abstract Member selectOne(String memberEmail);

	public abstract List<Member> selectAll();
}
