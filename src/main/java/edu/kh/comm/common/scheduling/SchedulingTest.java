package edu.kh.comm.common.scheduling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

// 특정 시간마다 스프링이 알아서 코드를 수행할 수 있도록 bean 등록
@Component // 단순 bean등록을 위한 어노테이션
public class SchedulingTest {
	
	private Logger logger = LoggerFactory.getLogger(SchedulingTest.class);
	
	//@Scheduled(fixedRate = 5000)
	// cron ="초 분 시 일 월 요일 [년도]"
	// @Scheduled(cron = "0 * * * * *") // <= *** 매분0초마다 실행해라
	// @Scheduled(cron = " 0 0  * * * *") // <= 정시마다(12:00:00 / 13:00:00)
	// @Scheduled(cron = "0 0 12 * * * ") // 매일 12시 정각
	// @Scheduled(cron = "0 0 12 1, 11, 21 * * ") // 매달 1,11,21일 12시 정각마다
	// @Scheduled(cron = "0 0 14 * * 2") // <= 월요일 14시 마다 
	public void test() {
		logger.info("매분마다 출력");
	}
	

}
