package edu.kh.comm.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import edu.kh.comm.board.model.service.ReplyService;


// REST(REpresentation State Transfer)
// - 자원을 이름으로 구분(REpresentation, 자원의 표현)하여 
//   자원의 상태(State)를 주고 받는 것(Transfer)

// => 특정한 이름(주소)로 요청이 오면 값으로 응답

// @RestController : 요청에 따른 응답이 모두 데이터(값) 자체인 컨트롤러
// => @Controller + @ResponseBody 
@RestController
@RequestMapping("/reply")
public class ReplyController {
	
	@Autowired
	private ReplyService service;
	
	// 댓글 목록 조회
	
	public List<Reply> replyList () {
		
	}
	
	// 댓글 등록
	
	// 댓글 수정
	
	// 댓글 삭제
	
}