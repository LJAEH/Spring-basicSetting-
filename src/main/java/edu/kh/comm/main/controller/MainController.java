package edu.kh.comm.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
	
	@RequestMapping("/main")
	public String mainForward() {
		
		return "common/main"; // dispatcherServlet이 받음 => ViewResolver (/WEB-INF/ .jsp 붙여서 forward 해줌)
	}

}
