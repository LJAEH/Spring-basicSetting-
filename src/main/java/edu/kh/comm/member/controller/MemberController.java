package edu.kh.comm.member.controller;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;

import edu.kh.comm.member.model.service.MemberService;
import edu.kh.comm.member.model.vo.Member;

// POJO 기반 프레임워크 : 외부 라이브러리 상속 X

// class : 객체를 만들기위한 설계도
// => 객체로 생성 되어야지 기능 수행 가능하다.
// ==> IOC(제어의 역전, 객체의 생명주기를 Spring이 관리) 를 이용하여 객체 생성
// ** 이 때, Spring이 생성한 객체를 bean이라고 한다.

// bean 등록 == "Spring이 객체로 만들어서 가지고 있어라" 라는 뜻

// @Component // 해당 클래스를 bean으로 등록하라고 프로그램에게 알려주는 주석 (Annotation)

@Controller // 생성된 bean이 Controller임을 명시 + bean 등록
@RequestMapping("/member") // localhost:8080/comm/member 이하의 요청을 처리하는 컨트롤러
@SessionAttributes({"loginMember"}) // java 배열로 작성 
											// Model에 추가된 값의 key와 어노테이션에 작성된 값이 같으면
											// 해당 값을 session scope로 이동시키는 역할
public class MemberController {
	
	private Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	// private MemberService service = new MemberServiceImpl();
	// IOC(제어의 역전) : new 연산자를 통해서 개발자가 직접 객체 생성하지 않는다
	
	@Autowired // bean 으로 등록된 객체중 타입이 같거나, 상속관계인 bean을 주입해주는 역할
	private MemberService service; // => DI ( 의존성 주입 )
	
	// 다형성 적용 impl 이 자식이고 memberSercie 가 부모임
	
	// Controller : 요청/응답을 제어하는 역할을 하는 클래스
	
	/* @RequestMapping : 클라이언트 요청(url)에 맞는 클래스 or 메서드를 연결 시켜주는 어노테이션
	 * [위치에 따른 해석]
	 * - 클래스 레벨 : 공통 주소
	 * - 메서드 레벨 : 공통 주소 외 나머지 주소
	 * 
	 * 단, 클래스레벨에 @RequestMapping이 존재하지 않는다면
	 * - 메서드 레벨 : 단독 요청 처리 주소
	 * 
	 * [작성법에 따른 해석]
	 * 
	 * 1) @RequestMapping("url")
	 * ==> 요청 방식(GET/POST) 관계 없이 url이 일치하는 요청 처리
	 * 
	 * 2) @RequestMapping(value = "url", method = RequestMethod.GET | POST)
	 * ==> 요청 방식에 따라 요청 처리함
	 * 
	 * ** 메서드 레벨에서 GET/POST 방식을 구분하여 매핑할 경우 **
	 * @GetMapping("url")
	 * @PostMapping("url")
	 * 사용하는 것이 일반적 (메서드 레벨에서만 작성 가능)
	 * */
	
	// 로그인
	// 요청시 파라미터를 얻어오는 방법 1
	// => HttpServletRequest 이용
	
	/*
	 * @RequestMapping("/login") 
	 * public String login(HttpServletRequest req) {
	 * logger.info("로그인요청됨");
	 * 
	 * String inputEmail = req.getParameter("inputEmmail"); String inputPw =
	 * req.getParameter("inputPw");
	 * 
	 * logger.debug("inputEmail : " + inputEmail); logger.debug("inputPw :" +
	 * inputPw ); return "redirect:/"; //sendRedirect 안써도됨; }
	 */
	
	// 요청시 파라미터를 얻어오는 방법 2
	// => @RequestParam 어노테이션 사용
	
	// @RequestParam("name속성값") 자료형 변수명
	// - 클라이언트 요청 시 같이 전달된 파라미터를 변수에 저장
	// ==> 어떤 파라미터를 변수에 저장할지는 "name속성값"을 이용해 지정
	
	// 매개변수 지정시 데이터 타입 파싱을 자유롭게 진행 할 수 있음 ex ) String => int 로 변환
	
	// [속성]
	// value : input 태그의 name 속성값
	// required : 입력된 name 속성값이 필수적으로 파라미터에 포함되어야 하는지 지정 
	//            required= true/false (기본값 true)
	// defaultValue : required 가 false인 상태에서 파라미터가 존재하지 않을 경우의 값 지정
	
	/*
	 * @RequestMapping("/login") public String login( @RequestParam("inputEmail")
	 * String inputEmail ,
	 * 
	 * @RequestParam("inputPw") String inputPw ,
	 * 
	 * @RequestParam(value = "inputName", required = false , defaultValue = "홍길동")
	 * String inputName) {
	 * 
	 * logger.debug("email" + inputEmail); logger.debug("pw:" + inputPw);
	 * logger.debug("name:" + inputName);
	 * 
	 * return "redirect:/"; }
	 */
	
	
	// 요청 시 파라미터를 얻어오는 방법 3
	// => @ModelAttribute 어노테이션 사용
	
	// @ModelAttribute VO타입 변수명
	// => 파라미터 중 name 속성값의 VO 의 필드와 일치하면
	// 해당 VO 객체의 필드에 값을 세팅
	
	// *** @ModelAttribute 를 이용해서 객체에 값을 직접 담는 경우에 대한 주의 사항 ***
	// -- 반드시 필요한 내용
	// - VO 기본 생성자 
	// - VO 필드에 대한 Setter
	
	 @PostMapping("/login") 
	 public String login(/*@ModelAttribute*/ Member inputMember, // 변수명 같으면 생략가능
			 				Model model,
			 				RedirectAttributes ra, // 리다이렉트 될때 잠깐 세션에 올렷다가 리퀴스트로 가져오기 위해 사용 (메시지)
			 				HttpServletResponse resp,
			 				HttpServletRequest req,
			 				@RequestParam(value="saveId",required=false) String saveId
			 ) {
		 // 커맨드 객체
		 // @ModelAttribute 생략된 상태에서 파라미터가 필드에 세팅된 객체

		  logger.info("로그인 기능 수행됨");
		  
		  // 아이디, 비밀번호가 일치하는 회원 정보를 조회하는 Service 호출 후 결과 반환받기
		  Member loginMember = service.login(inputMember);
		  		 
		 /* Model : 데이터를 맵형식 (K:V) 형태로 담아 전달하는 용도의 객체
		  * => request, session을 대체하는 객체
		  * - 기본 scope : request
		  * - session scope로 변환하고 싶은 경우
		  * 클래스 레벨로 @SessionAttributes 를 작성하면 된다.
		  * 
		  * @SessionAttributes 미작성 => request scope
		  */
		 if(loginMember != null) { // 로그인 성공 시
			 model.addAttribute("loginMember", loginMember); 
			 // == req.setAttribute("loginMember", loginMember);
			 // 로그인 성공 시 무조건 쿠키 생성
			 // 단, 아이디 저장 체크 여부에 따라서 쿠키의 유지시간을 조정
			 
			 Cookie cookie = new Cookie("saveId",loginMember.getMemberEmail());
			 
			 if( saveId != null ) { // 아이디 저장 체크 되었을때
				 cookie.setMaxAge(60*60*24*365); // 초단위 지정(1년) 
			 } else {
				 cookie.setMaxAge(0); // 생성되자마자 사라짐
			 }
			 
			 // 쿠키가 적용될 범위(경로) 지정
			 cookie.setPath(req.getContextPath()); // comm 이하로 전부 적용 // 이러면 프로젝트 이름이 바뀌어도 항상 적용
			 // 쿠키를 응답 시 클라이언트에게 전달
			 resp.addCookie(cookie);
			 
		 } else {
			 // model.addAttribute("message", "아이디 또는 비밀번호가 일치하지 않습니다.");
			 ra.addFlashAttribute("message", "아이디 또는 비밀번호가 일치하지 않습니다.");
				 
			 // redirect 시에도 request scope로 세팅된 데이터가 유지될 수 있도록 하는 방법을 
			 // spring에서 제공함
			 // => RedirectAttributes 객체 (컨트롤러 매개변수에 작성하면 사용 가능)
		 }
		 
		  return "redirect:/"; 
	 }

	 @GetMapping("/logout") 
	 public String logout(SessionStatus status) {
		 // 로그아웃 == 세션을 없애는 것
		 
		 // * @SessionAttributes를 이용해서 session scope에 배치된 데이터는
		 //   SessionStatus 라는 별도 객체를 이용해야만 없앨 수 있다.
		 
		 logger.info("로갓");
		 
		 // HttpSession session // session.invalidate(); 기존 세션 무효화 방식으로는 x
		 
		 status.setComplete(); // 세션이 할 일이 완료됨 => 없앰
		 
	     return "redirect:/"; // 메인페이지 재요청
	}
		
	 
	 // 회원가입 화면 전환
	 
	 @GetMapping("/signUp") // GET 방식 : /comm/member/signUp 요청
	 public String signUp() {
		 
		 
		 
		 return "member/signUp";
	 }
	 
	 // 이메일 중복 검사 메서드 
	 
	 @ResponseBody
	 @GetMapping("/emailDupCheck")
	 //public String emailDupCheck(@RequestParam("memberEmail") String memberEmail) { 
	 public int emailDupCheck(String memberEmail) { // 파라미터 key값과 저장하려는 변수 명이 같으면 생략가능
		 
	
		 // 컨트롤러에서 반환되는 값은 forward 또는 redirect 를 위한 경로인 경우가 일반적 
		 // => 반환되는 값은 경로로 인식됨 (디스페쳐에 의해 뷰리졸버로 /WEB-INF/ .jsp 붙여서 경로로 쏨
		 
		 // -> 이를 해결하기 위한 어노테이션 @ResonseBody 가 존재함
		 
		 // @ResponseBody : 반환되는 값을 응답의 몸통(body)에 추가하여 
		 //					이전 요청 주소로 돌아감
		 // => 컨트롤러에서 반환되는 값이 경로가 아닌 "값 자체" 로 인식됨
		 
		 return service.emailDupCheck(memberEmail);
	 }
	 
	 @ResponseBody
	 @GetMapping("/nickDupCheck")
	 public int nickDupCheck(String memberNickname) {
		 return service.nickDupCheck(memberNickname);
	 }
	 
	 
	 @PostMapping("/signUp")
	 public String signUp(RedirectAttributes ra, 
			 	Member inputMember,
				String[] memberAddress
			 ) {
		 // 커맨드 객체를 이용해서 입력된 회원 정보를 잘 받아옴m
		 // 단, 같은 name 을 가진 주소가 하나의 문자열로 합쳐서 세팅되어 들어옴.
		 // => 도로명 주소에 "," 기호가 포함되는 경우가 있어 이를 구분자로 사용할 수 없다.
		 
		 inputMember.setMemberAddress(String.join(",,", memberAddress));
		
		 // String.join은("구분자", 배열)
		 // 배열을 하나의 문자열로 합치는 메서드
		 // 값 중간중간에 들어가서 하나의 문자열로 합쳐줌
		 // [a,b,c] => join진행 => "a,,b,,c"
		 
		 if(inputMember.getMemberAddress().equals(",,,,")) {
			 inputMember.setMemberAddress(null);
		 }
		 
		 
		 
		 int result = service.signUp(inputMember);
		 
		 String resultWord = null;
		 
		 String path = null;
		 String message = null;
		 
		 if(result == 1) {
			 message = "안뇽";
			 path = "redirect:/";
		 } else {
			 message = "다시";
			 path = "redirect:/member/signUp";
		 }
		 ra.addFlashAttribute("message", path);
 
		 return "redirect:/";

	 }
	 
	 //회원 1명 정보 조회(ajax)
	 
	 @ResponseBody
	 @PostMapping("/selectOne")
	 public String selectOne (@RequestParam("memberEmail") String memberEmail) {
		 
		 Member member = service.selectOne(memberEmail);
		 
		 return new Gson().toJson(member);
	 }
	 
	 
	 
	 //회원 목록 조회(ajax)

	 @ResponseBody
	 @RequestMapping("/selectAll")
	 public String selectAll() {
		 List<Member> memberList = service.selectAll();
		 
		 return new Gson().toJson(memberList);
	 }
	 
	 
	 
	 // Spring 예외처리 방법 (3가지 , 중복 사용 가능)
	 
	 /* 1 순위 : 메서드 별로 예외 처리(try-catch / throws)
	  * 
	  * 2 순위 : 하나의 컨트롤러에서 발생하는 예외를 모아서 처리 
	  * 		=> @ExceptionHandler (메서드에 작성)
	  * 3 순위 : 전역(웹 어플리케이션)에서 발생하는 예외를 모아서 처리
	  * 		=> @ControllerAdvice (클래스에 작성)
	  */
	
	 // 회원 컨트롤러에서 발생하는 모든 예외를 모아서 처리
		/*
		 * @ExceptionHandler(Exception.class) public String exceptionHandler(Exception
		 * e, Model model) { e.printStackTrace();
		 * 
		 * model.addAttribute("errorMessage","서비스 이용 중 문제가 발생했습니다.");
		 * model.addAttribute("e",e); return "common/error"; }
		 */
	 
	 
}
