package edu.kh.comm.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebFilter(filterName="initFilter", urlPatterns = "/*")
public class InitFilter extends HttpFilter implements Filter {
	
	// print 구문 쓰지마
	// => Logger 사용
	
	// Logger 객체 생성 (해당 클래스에 대한 log를 출력하는 객체)
	private Logger logger = LoggerFactory.getLogger(InitFilter.class);
	
	// 필터가 생성될 때 실행
	public void init(FilterConfig fConfig) throws ServletException {
		// logger를 이용해서 출력하는 방법
		// trace - debug - info - warn - error
		
		// debug : 개발의 흐름을 파악 (실행이 되었는지 여부, parametor가 현재 무슨 값인지 확인)
		// info : 메서드 실행
		
		// 이필터가 생성이 되었다는 정보를 출력할때 info 가 제일 적절함
		logger.info("초기화 필터 생성");
	}

	
	// 필터가 파괴될 때 실행 (서버는 켜져 있으나 백엔드 코드가 수정될 때)
	public void destroy() {

		logger.info("초기화 필터 파괴");
	}
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		// 모든 요청의 문자 인코딩을 UTF-8로 설정
		request.setCharacterEncoding("UTF-8");
		
		
		// 모든 응답의 문자 인코딩을 UTF-8로 설정
		response.setCharacterEncoding("UTF-8");
		
		
		// application scope로 최상위 경로를 얻어올 수 있는 값 세팅
		
		// application 내장 객체 얻어오기
		ServletContext application = request.getServletContext();
		
		// 최상위 주소 얻어오기
		String contextPath =  ( (HttpServletRequest)request ).getContextPath();
									// 다운캐스팅
		
		// 세팅
		application.setAttribute("contextPath", contextPath);
		
		// 연결된 다음 필터 수행(없으면 Servlet 수행)
		chain.doFilter(request, response);

	}


}
