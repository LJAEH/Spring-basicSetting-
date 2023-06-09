package edu.kh.comm.board.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.comm.board.model.service.BoardService;
import edu.kh.comm.board.model.service.ReplyService;
import edu.kh.comm.board.model.vo.BoardDetail;
import edu.kh.comm.board.model.vo.Reply;
import edu.kh.comm.common.Util;
import edu.kh.comm.member.model.vo.Member;

@Controller
@SessionAttributes({"loginMember"})
@RequestMapping("/board")
public class BoardController {
	
	@Autowired
	private BoardService service;
	
	@Autowired
	private ReplyService rService;
	
	private Logger logger = LoggerFactory.getLogger(BoardController.class);
	
	
	//게시글 목록 조회
	// @PathVariable("value") : URL 경로에 포함되어 있는 값을 변수로 사용할 수 있게하는 역할
	// => 자동으로 request scope에 등록됨 => jsp 에서 ${value} EL 작성 가능
	
	// PathVariable : 요청 자원을 식별하는 경우
	// QueryString : 정렬, 검색 등의 필터링 옵션
	@GetMapping("list/{boardCode}")
	public String boardList(@PathVariable("boardCode") int boardCode,
			@RequestParam(value="cp",required=false, defaultValue="1") int cp, // 페이지네이션 디폴트1
			Model model,
			@RequestParam Map<String,Object> paramMap
			) {
		// 게시글 목록 조회 서비스 호출
		// 1) 게시판 이름 조회 => 인터셉터에 application에 올려둔 boardTypeList를 사용 가능
		// 2) 페이지네이션 객체 생성 (listCount)
		// 3) 게시글 목록 조회
		
		
		Map<String,Object> map = null;

		if(paramMap.get("key") == null) { // 검색이 아닌 경우
			map = service.selectBoardList(cp, boardCode);
			
		}else { // 검색인 경우
			
			// 검색에 필요한 데이터를 paramMap에 모두 담아서 서비스 호출
			// -> key, query, cp, boardCode
			
			paramMap.put("cp", cp);  // 있으면 같으면 값으로 덮어쓰기, 없으면 추가
			paramMap.put("boardCode", boardCode);
			
			map = service.searchBoardList(paramMap);
			
		}
		
		
		model.addAttribute("map", map);
		
		return "board/boardList";
	}
	
	//게시글 상세조회하는 getMapping boardList 88번줄
	@GetMapping("/detail/{boardCode}/{boardNo}")
	public String boardDetail(@PathVariable("boardCode") int boardCode, 
							@PathVariable("boardNo") int boardNo, 
							@RequestParam(value="cp",required=false, defaultValue="1") int cp, // cp는 처음엔 없을수 있음
							Model model , HttpSession session,
							HttpServletResponse resp,
							HttpServletRequest req) {
		
		// 게시글 상세 조회 서비스 호출
		BoardDetail detail = service.selectBoardDetail(boardNo);
		
		// 쿠키를 이용한 조회수 중복 증가 방지 코드 + 본인의 글은 조회수 증가 x
		
		// @ModelAttribute("loginMember") Member loginMember (사용불가)
		// => @ModelAttribute는 별도의 required 속서이 없어서 무조건 필수
		// 	 => 세션에 loginMember가 없으면 예외 발생
		
		// 해결방법 : HttpSession을 이용
		// => session.getAttribute("loginMember")
	
		// 상세 조회 성공 시 
		// 세션이 있는지 없는지
		// 세션이 있으면 memberNo 세팅
			// 글쓴이와 현재 클라이언트가 같은지 아닌지
				// 같지 않으면 => 조회수 증가
					// 쿠키가 있는지 없는지
						// 있다면 쿠키 이름이 "readBoardNo" 있는지?
						// 없다면 만들어야함
						// 있다면 쿠키에 저장된 값 뒤쪽에 현재 조회된 게시글 번호 추가
						// 단 , 기존쿠키값에 중복되는 번호 없어야함
			// 이미 조회된 데이터 DB와 동기화
			// + 쿠키 maxAge 1시간
		
		
		if(detail != null) {
			
			/*
			 * // 댓글 목록 조회 List<Reply> rList = rService.replyList(boardNo);
			 * model.addAttribute("rList", rList);
			 */
			
			// 세션이 있는지 없는지
			// 세션이 있으면 memberNo 세팅
			Member loginMember = (Member)session.getAttribute("loginMember");
			
			int memberNo = 0;
			if(loginMember != null) {
				memberNo = loginMember.getMemberNo();
			}
	
			// 글쓴이와 현재 클라이언트가 같은지 아닌지 
			if( detail.getMemberNo() != memberNo ) {
				// 같지 않으면 -> 조회수 증가
				
				// 쿠키가 있는지 없는지
				//	있다면 쿠키 이름이 "readBoardNo" 있는지 ?
				//  없다면 만들어라
				//  있다면 쿠키에 저장된 값 뒤쪽에 현재 조회된 게시글 번호를 추가
				//	-> 단, 기존 쿠키값에 중복되는 번호 없어야함.
				
				Cookie cookie = null; // 기존에 존재하던 쿠키를 저장하는 변수
				
				Cookie[] cArr = req.getCookies(); // 쿠키 얻어오기
				
				if(cArr != null && cArr.length > 0) { // 얻어온 쿠키가 있을 경우
					for(Cookie c : cArr) {
						
						// 얻어온 쿠키중 이름이 "readBoardNo"가 있으면 얻어오기
						if(c.getName().equals("readBoardNo")) {
							cookie = c;
						}
					}
				}
				
				int result = 0;
				
				if (cookie == null) { // 기존에 "readBoardNo" 이름의 쿠키가 없던 경우
					
					cookie = new Cookie("readBoardNo", boardNo+"");
					result = service.updateReadCount(boardNo);// 조회 수 증가 서비스 호출
					
				} else { // 기존에 "readBoardNo" 이름의 쿠키가 있던 경우
					// "readBoardNo"  :   "1/2/3/5/30/500" + / + boardNo
					// -> 쿠키에 저장된 값 뒤쪽에 현재 조회된 게시글 번호 추가
					
					String[] temp = cookie.getValue().split("/");
					
					List<String> list = Arrays.asList(temp); // 배열 -> List 변환
					
					if(list.indexOf(boardNo+"") == -1) { // 기존 값에 같은 글번호가 없다면 추가
						cookie.setValue( cookie.getValue() + "/" + boardNo );
						result = service.updateReadCount(boardNo);// 조회 수 증가 서비스 호출
						
					}
					
				}
				
				// 이미 조회된 데이터 DB와 동기화
				if(result > 0) {
					
					detail.setReadCount( detail.getReadCount() + 1 );
					
					cookie.setPath(req.getContextPath());
					cookie.setMaxAge(60 * 60 * 1); // + 쿠키 maxAge 1시간
					resp.addCookie(cookie);
				}
				
			}
			
		}
		
		model.addAttribute("detail", detail);
		
		return "board/boardDetail";
		/*
		 * if(detail != null ) {
		 * 
		 * if ( loginMember == null || loginMember.getMemberNo() !=
		 * detail.getMemberNo()){
		 * 
		 * Cookie[] cookies = req.getCookies(); Cookie readCookie = null; if (cookies !=
		 * null) { for (Cookie cookie : cookies) { if
		 * (cookie.getName().equals("readBoardNo")) { readCookie = cookie; break; } } }
		 * if(readCookie == null) { readCookie = new
		 * Cookie("readBoardNo",String.valueOf(boardNo)); }
		 * if(!readCookie.getValue().contains(String.valueOf(boardNo))) {
		 * readCookie.setValue(readCookie.getValue()+"/"+boardNo); } if( readCookie !=
		 * null ) { readCookie.setMaxAge(3600); }
		 * readCookie.setPath(req.getContextPath());
		 * 
		 * resp.addCookie(readCookie);
		 * 
		 * int readCountResult = service.readCountUp(boardNo);
		 * 
		 * } }
		 * 
		 * model.addAttribute(detail);
		 */
		
	}
	
	
	@GetMapping("/write/{boardCode}")
	public String boardWriteForm(@PathVariable("boardCode") int boardCode,
			@RequestParam(value="no",required=false, defaultValue="0") int boardNo ,
			String mode, Model model
			) {
		
		if(mode.equals("update")) {
			// 게시글 상세 조회 서비스 호출(boardNo)
			BoardDetail detail = service.selectBoardDetail(boardNo);
			// 개행문자가가 <br> 로 되어있는 상태 =>  textarea 출력 예정이기 때문에 \n으로 변경
			
			detail.setBoardContent( Util.newLineClear(detail.getBoardContent()));
		}
		
		return "board/boardWriteForm";
	}
	
	// 게시글 작성(삽입/수정)
	
	@PostMapping("/write/{boardCode}")
	public String boardWrite(@PathVariable("boardCode") int boardCode,
							BoardDetail detail ,// boardTitle, boardContent, boardNo
							String mode,
							@RequestParam(value="images", required = false) List<MultipartFile> imageList, // 업로드 파일 (이미지) 리스트
							@ModelAttribute("loginMember") Member loginMember,
							@RequestParam(value="deleteList" , required = false) String deleteList,
							@RequestParam(value="cp",required = false , defaultValue = "1") int cp,
							HttpServletRequest req,
							RedirectAttributes ra
			) throws IOException {
		// 1) 로그인한 회원번호 얻어와서 detail에 세팅
		detail.setMemberNo(loginMember.getMemberNo());
		// 2) 이미지 저장 경로 얻어오기 (webPath, folderPath)
		String webPath = "/resources/images/board/";
		String folderPath = req.getSession().getServletContext().getRealPath(webPath);
		//C:\Users\Admin\Desktop\SpringSpace\comm\src\main\webapp\resources\images\board
		
		if(mode.equals("insert")) { // 삽입
			// insert 가 2번일어남 
			
			// 게시글 부분삽입(제목,내용,회원번호,게시판코드 등)
			// => 삽입된 게시글 번호 반환(boardNo) 반환 (삽입후 상세조회로 redirect 할거임)

			// 게시글에 포함된 이미지 정보 삽입(0~5개, 게시글 번호 필요)
			// => 실제 파일로 변환해서 서버에 저장(transferTo())
			
			// 두번의 insert중 하나라도 실패하면 전체 rollback 하는 트랜잭션 처리가 필요함
			int boardNo = service.insertBoard(detail,imageList,webPath,folderPath);
			
			String path = null;
			String message = null;
			
			if(boardNo > 0) {
				// /board/write/1
				// /baord/detail/1/1500
				
				
				path = "../detail/" + boardCode + "/" + boardNo;
				message = "게시글이 등록되었습니다";
			} else {
				path = req.getHeader("referer");
				
			}
			ra.addFlashAttribute("message",message);
			
			return "redirect:" + path;
			
		} else { // 수정
			int result = service.updateBoard(detail,imageList, webPath, folderPath, deleteList);
			
			String path = null;
			String message = null;
			
			if(detail.getBoardNo() > 0) {
				// /board/write/1
				// /baord/detail/1/1500
				
				
				path = "../detail/" + boardCode + "/" + detail.getBoardNo();
				message = "게시글이 등록되었습니다";
			} else {
				path = req.getHeader("referer");
				
			}
			ra.addFlashAttribute("message",message);
			
			return "redirect:" + path;
			
		}
		
	
		
	}	
	
}
