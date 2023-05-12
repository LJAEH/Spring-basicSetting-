package edu.kh.comm.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(4)
public class AroundAspect {

	private Logger logger = LoggerFactory.getLogger(AroundAspect.class);
	
	// @Around : 전처리(@Before)와 후처리(@After)를 한번에 작성 가능한 advice
	@Around("CommonPointcut.implPointcut()")
	public Object runningTime(ProceedingJoinPoint jp) throws Throwable{
		// proceed() 메소드 호출 전  : @Before advice 작성
		// proceed() 메소드 호출 후  : @After advice 작성
		// 메소드 마지막에 proceed()의 반환값을 리턴해야함.
		
		// 밀리세컨드 단위로 시간 찍을거임
		// System.currentTimeMillis() : 
		// 1970/01/01 오전 9시 (한국 OS기준) 부터
		// 지금까지 지난 시간을 ms 단위로 나타낸 값 이라 겁나 길어서 long씀

		long startMs = System.currentTimeMillis();
		
		Object obj = jp.proceed(); // 전후처리를 나누는 기준
		
		long endMs = System.currentTimeMillis();

		logger.info("RunningTime : " + (endMs - startMs) + "ms");
	
		return obj;
	}	
}
