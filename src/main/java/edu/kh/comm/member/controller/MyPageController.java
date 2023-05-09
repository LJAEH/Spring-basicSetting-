package edu.kh.comm.member.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
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

	
	// 프로필 수정
	@PostMapping("/profile")
	public String updateProfile(@ModelAttribute("loginMember") Member loginMember,
								@RequestParam("uploadImage") MultipartFile uploadImage, /* 업로일 파일 */
								@RequestParam Map<String, Object> map, /* delete 담겨있음 */
								HttpServletRequest req, /* 파일 저장 경로 탐색용 */
								RedirectAttributes ra ) throws IOException{
		
		
		// 경로 작성하기
		
		// 1) 웹 접근 경로 ( /comm/resources/images/memberProfile/ )
		String webPath = "/resources/images/memberProfile/";
		
		// 2) 서버 저장 폴더 경로 (C:\workspace\7_Framework\comm\src\main\webapp\resources\images\memberProfile)
		String folderPath = req.getSession().getServletContext().getRealPath(webPath);
		
		// map에 경로2개, 이미지, delete, 회원번호 담기
		map.put("webPath", webPath);
		map.put("folderPath", folderPath);
		map.put("uploadImage", uploadImage);
		map.put("memberNo", loginMember.getMemberNo());
		
		int result = service.updateProfile(map);
		
		String message = null;
		
		if(result > 0) {
			message = "프로필 이미지가 변경되었습니다";
			
			loginMember.setProfileImage( (String)map.get("profileImage") );
			
		} else {
			message = "프로필 이미지 변경 실패";
		}
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:profile";
		
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
			@RequestParam("currentPw") String currentPw , 
			@RequestParam("newPw") String newPw) {
		
		// session 확인
		logger.debug(loginMember.toString());
		logger.debug(newPw);
		logger.debug(currentPw);
		
		int result = service.changePw(loginMember,currentPw,newPw);
		
				
		if (result == 1) {
			ra.addFlashAttribute("message", "비밀번호가 변경되었습니다.");
			return "redirect:/";
		} else {
			ra.addFlashAttribute("message", "변경에 실패했습니다.");
			return "member/myPage-changePw";			
		}
		

	}

	// 회원 탈퇴
	@GetMapping("/secession")
	public String secession() {
		return "member/myPage-secession";
	}
	
	@PostMapping("/secession")
	public String secession(RedirectAttributes ra,
			@ModelAttribute("loginMember") Member loginMember,
			@RequestParam("memberPw") String memberPw,
			SessionStatus status,
			HttpServletRequest req,
			HttpServletResponse resp
			) {
		
		loginMember.setMemberPw(memberPw);
		
		int result = service.secession(loginMember);
		
		if (result == 1) {
			ra.addFlashAttribute("message","getOut");
			status.setComplete();
			
			Cookie cookie = new Cookie("saveId","");
			cookie.setMaxAge(0);
			cookie.setPath(req.getContextPath());
			resp.addCookie(cookie);
			return "redirect:/";	
		} else {
			ra.addFlashAttribute("message", "실패햇서요");
			return "member/myPage-secession";
		}	
	}
	
	// 회원 정보 수정
	@PostMapping("/info")
	public String updateInfo(@ModelAttribute("loginMember") Member loginMember ,
			@RequestParam Map<String, Object> paramMap,
			@RequestParam("updateMemberAddress") String[] updateMemberAddress,
			RedirectAttributes ra ,
			Model model
	)  {
		
		// 필요한 값
		// - 닉네임
		// - 전화번호
		// - 주소 (String[] 로 얻어와 String.join() 을 이용해서 문자열로 변경)
		// - 회원번호(Session => 로그인한 회원 정보를 통해서 얻어옴)
		//		=> @ModelAttribute, @SessionAttributes 필요
		
		// @SessionAttributes의 역할 2가지
		// 1) Model에 세팅 데이터의 key값을 @SessionAttributes에 작성하면
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
		
		// 파라미터를 저장한 paramMap에 회원번호, 주소를 추가
		
		String memberAddress = String.join(",,", updateMemberAddress);
		if(memberAddress.equals(",,,,")) {
			memberAddress = null;
		}
		
		paramMap.put("memberNo",loginMember.getMemberNo());
		paramMap.put("memberAddress", memberAddress);
		
		int result = service.updateInfo(paramMap);
		
		if (result > 0) {
			ra.addFlashAttribute("message", "수정되었습니다.");
			
			loginMember.setMemberNickname((String)paramMap.get("updateMemberNickName"));
			loginMember.setMemberTel((String)paramMap.get("updateMemberTel"));
			loginMember.setMemberAddress((String)paramMap.get("memberAddress"));
			
			return "member/myPage-info";	
		} else {
			ra.addFlashAttribute("message", "수정실패.");
			return "member/myPage-info";
			
		}
		
		
			
	}
}
