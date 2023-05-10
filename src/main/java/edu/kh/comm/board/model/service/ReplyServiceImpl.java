package edu.kh.comm.board.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.kh.comm.board.model.dao.ReplyDAO;
import edu.kh.comm.board.model.vo.Reply;
import edu.kh.comm.common.Util;

@Service
public class ReplyServiceImpl implements ReplyService{
	
	@Autowired
	private ReplyDAO dao;
	
	// 게시판 코드, 이름 조회
	@Override
	public List<Reply> replyList(int boardNo) {
		
		return dao.replyList(boardNo);
	}


	@Override
	public int insertReply(Reply reply) {
		// XSS, 개행문자 처리 
		reply.setReplyContent(Util.XSSHandling(reply.getReplyContent()));
		reply.setReplyContent(Util.newLineHandling(reply.getReplyContent()));
		return dao.insertReply(reply);
	}

}
