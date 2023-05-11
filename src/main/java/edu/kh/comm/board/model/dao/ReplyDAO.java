package edu.kh.comm.board.model.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.kh.comm.board.model.vo.Reply;

@Repository
public class ReplyDAO {
	
	@Autowired
	private SqlSessionTemplate sqlSession;

	/** 댓글 목록 조회 서비스 DAO 
	 * @param boardNo
	 * @return rList
	 */
	public List<Reply> replyList(int boardNo) {		
		return sqlSession.selectList("replyMapper.replyList",boardNo);
	}

	/** 댓글 등록 서비스 DAO 
	 * @param reply
	 * @return
	 */
	public int insertReply(Reply reply) {
		return sqlSession.insert("replyMapper.insert",reply);
	}

	/** 댓글 수정 DAO
 	 * @param reply
	 * @return
	 */
	public int updateReply(Reply reply) {
		return sqlSession.update("replyMapper.update",reply);
		
	}

	/** 댓글 삭제 DAO
	 * @param boardNo
	 * @return
	 */
	public int deleteReply(int boardNo) {
		// TODO Auto-generated method stub
		return sqlSession.update("replyMapper.delete",boardNo);
	}

}
