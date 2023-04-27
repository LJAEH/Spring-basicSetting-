package edu.kh.comm.member.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.comm.member.model.service.MyPageService;
import edu.kh.comm.member.model.vo.Member;

@Controller
@RequestMapping("/member/myPage")
@SessionAttributes({"loginMember"})
public class MyPageController {
	private Logger logger = LoggerFactory.getLogger(MyPageController.class);
	
	@Autowired
	private MyPageService service;
	
	@GetMapping("/profile")
	public String profile() {
		return "member/myPage-profile";
	}
	
	// 회원 정보 조회
	@GetMapping("/info")
	public String info() {
		return "member/myPage-info";
	}
	
	@GetMapping("/changePw")
	public String changePw() {
		return "member/myPage-changePw";
	}

	// 비밀번호 변경
	@PostMapping("/changePw")
	public String changePw(RedirectAttributes ra ,
			@ModelAttribute("loginMember") Member loginMember ,
			String currentPw , String newPw) {
		
		// session 확인
		logger.debug(loginMember.toString());
		
		int result = service.changePw(loginMember,currentPw,newPw);
		
				
		if (result == 1) {
			ra.addFlashAttribute("message", "비밀번호가 변경되었습니다.");
			return "redirect:/";
		} else {
			ra.addFlashAttribute("message", "변경에 실패했습니다.");
			return "member/myPage-changePw";			
		}
		

	}
	
	@GetMapping("/secession")
	public String secession() {
		return "member/myPage-secession";
	}
	
	
	
	// 회원 탈퇴
	
	
	// 회원 정보 수정
	@PostMapping("/info")
	public String updateInfo(@ModelAttribute("loginMember") Member loginMember ,
			@RequestParam Map<String, Object> paramMap,
			String[] updateMemberAddress,
			RedirectAttributes ra
	)  {
		
		// 필요한 값
		// - 닉네임
		// - 전화번호
		// - 주소 (String[] 로 얻어와 String.join() 을 이용해서 문자열로 변경)
		// - 회원번호(Session => 로그인한 회원 정보를 통해서 얻어옴)
		//		=> @ModelAttribute, @SessionAttributes 필요
		
		// @SessionAttributes의 역할 2가지
		// 1) Model 에 세팅 데이터의 key값을 @SessionAttributes에 작성하면
		//   해당 key값과 같은 Model 에 세팅된 데이터를 request => session scope 로 이동
		
		// 2) 기존에 session scope 로 이동시킨 값을 얻어오는 역할
		//    @ModelAttribute("loginMember") Member loginMember
		//						[session key]
		//  => @SessionAttributes를 통해 session scope에 등록된 값을 얻어와
		//     오른쪽에 작성된 Member loginMember 변수에 대입
		//     단 , @SessionAttributes({"loginMember"}) 가 클래스 위에 작성되어 있어야 가능
		
		// **** 매개변수를 이용해서 session 파라미터 데이터를 동시에 얻어올때 문제점 ***
		
		// session에서 객체를 얻어오기 위해 매개변수에 작성한 상태
		// => @ModelAttribute("loginMember") Member loginMember
		
		// 파라미터의 name 속성값이 매개변수에 작성된 객체의 필드와 일치하면
		// => ex) name="memberNickname"
		// session에서 얻어온 객체의 필드에 덮어쓰기가 된다.
		
		// [해결 방법] 파라미터의 name 속성을 변경해서 얻어오면 문제해결
		// (필드명이 겹쳐서 문제니까 바꿔주삼)
		String newAddress = null;
		
		int memberNo = loginMember.getMemberNo();
		if(updateMemberAddress != null) {
			newAddress = String.join(null, updateMemberAddress);
		} else {
			updateMemberAddress = null;
			newAddress = String.join(null, updateMemberAddress);
		}
		
		logger.debug(newAddress);
		
		int result = service.updateInfo(memberNo,paramMap,newAddress);
		
		return null;
		
	}
}