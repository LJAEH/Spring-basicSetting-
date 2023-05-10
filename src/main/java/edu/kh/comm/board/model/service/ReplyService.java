package edu.kh.comm.board.model.service;

import java.util.List;
import java.util.Map;

import edu.kh.comm.board.model.vo.Reply;

public interface ReplyService {

	/** 댓글 목록 조회 서비스
	 * @param boardNo
	 * @return rList 
	 */
	List<Reply> replyList(int boardNo);

	int insertReply(Reply reply);

}
