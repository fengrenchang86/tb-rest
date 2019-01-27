package com.turtlebone.rest.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

@Aspect
@Component
public class ControllerLogger {

	
	@Around("execution(* com.turtlebone.rest.controller.*.*(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
		String methodName = pjp.getSignature().getName();
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		Method method = signature.getMethod();
		
		String clazzMapping = "";
		String methodMapping = "";
		if (method.isAnnotationPresent(RequestMapping.class)) {			
			if (pjp.getTarget().getClass().isAnnotationPresent(RequestMapping.class)) {
				RequestMapping clzRM = pjp.getTarget().getClass().getAnnotation(RequestMapping.class);
				String clzRms[] = clzRM.value();
				clazzMapping = clzRms[0];
			}
			RequestMapping rm = method.getAnnotation(RequestMapping.class);
			String mappings[] = rm.value();
			methodMapping = mappings[0];
		}
		Logger logger = LoggerFactory.getLogger("ENTRY");
		logger.info("执行方法：{}, mapping={}{}", methodName, clazzMapping, methodMapping);
		Object rs = pjp.proceed();
		return rs;
	}
}
