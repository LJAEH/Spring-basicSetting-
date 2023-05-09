package edu.kh.comm.member.model.service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.logging.log4j.core.Filter.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.comm.common.Util;
import edu.kh.comm.member.controller.MyPageController;
import edu.kh.comm.member.model.dao.MyPageDAO;
import edu.kh.comm.member.model.vo.Member;

@Service
public class MyPageServiceImpl implements MyPageService{
	
	private Logger logger = LoggerFactory.getLogger(MyPageController.class);

	@Autowired
	private MyPageDAO dao;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	


	@Override
	public int changePw(Member loginMember, String currentPw, String newPw) {
		// TODO Auto-generated method stub
		
		int result = 0;
		
		
		logger.debug("dbPw 가지러 가기 전");
		String dbPw = dao.checkPw(loginMember);
		logger.debug(dbPw + " // 디비에서 기존 비번 가져온 거");
		
		if(bcrypt.matches(currentPw,dbPw)) {
			String bcryptPw = bcrypt.encode(newPw);
			
			loginMember.setMemberPw(bcryptPw);
			
			result = dao.changePw(loginMember);
			
			logger.debug(loginMember.getMemberPw() + " // 로그인멤버 객체 내에 새로운 비번");
	
		} else {
			result = 0;
		}
		
		return result;
	}


	@Override
	public int secession(Member loginMember) {
		int result = 0;
		if (bcrypt.matches(loginMember.getMemberPw(), dao.checkPw(loginMember))) {
			result = dao.secession(loginMember);
		} else {
			result = 0;
		}
		
		return result;
	}


	@Override
	public int updateInfo(Map<String, Object> paramMap) {
		
		return dao.updateInfo(paramMap);
	}



	// 프로필 이미지 수정 서비스 구현
	@Override
	public int updateProfile(Map<String, Object> map) throws IOException{
							// webPath, folderPath, uploadImage, delete(String) , memberNo
		
		// 자주 쓰는 값 변수에 저장
		MultipartFile uploadImage = (MultipartFile)map.get("uploadImage");
		String delete = (String)map.get("delete"); // "0" (변경) / "1" (삭제)
		
		// 프로필 이미지 삭제여부를 확인해서
		// 삭제가 아닌 경우(== 새 이미지로 변경) -> 업로드된 파일명 변경
		// 삭제된 경우 -> NULL 값을 준비
		
		String renameImage = null; // 변경된 파일명 저장 변수
		
		if( delete.equals("0") ) { // 이미지가 변경된 경우
			
			// 파일명 변경
			// uploadImage.getOriginalFilename() : 원본 파일명
			renameImage = Util.fileRename( uploadImage.getOriginalFilename() );
			
			map.put("profileImage", map.get("webPath") + renameImage);
									//  /resources/images/memberProfile/20220624122315.png
			
		} else {
			map.put("profileImage", null);		
		}
		
		// DAO를 호출해서 프로필 이미지 수정
		int result = dao.updateProfile(map);
		
		// DB 수정 성공 시 메모리에 임시 저장되어있는 파일을 서버에 저장
		if( result > 0 && map.get("profileImage") != null) {
			uploadImage.transferTo( new File( map.get("folderPath") + renameImage) );
		}
		
		
		
		return result;
	}

}
