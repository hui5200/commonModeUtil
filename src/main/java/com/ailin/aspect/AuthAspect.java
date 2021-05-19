package com.ailin.aspect;

import com.ailin.anotation.Auth;
import com.ailin.mode.ResultMode;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class AuthAspect {

    private final static Logger log = LoggerFactory.getLogger(AuthAspect.class);

    /**
     * 定义切入点
     */
    @Pointcut("@annotation(com.ailin.anotation.Auth)")
//    @Pointcut("execution(* com.ailin.contoller.*())")
    public void authPointCut(){}

//    @Before("authPointCut()")
    public void doBefore(JoinPoint joinPoint){

        MethodSignature sign = (MethodSignature) joinPoint.getSignature();
        Method method = sign.getMethod();

        Auth auth = method.getAnnotation(Auth.class);

        if(auth.isAuth()){
            log.info("开始进行授权操作===========");
            //获取参数
            Object[] args = joinPoint.getArgs();
            String[] parameterNames = sign.getParameterNames();
            if(args == null || args.length == 0 || parameterNames == null || parameterNames.length == 0){
                log.info("授权不通过");
            }
        }
    }

    @Around("authPointCut()")
    public Object around(ProceedingJoinPoint pj) throws Throwable {

        MethodSignature sign = (MethodSignature) pj.getSignature();
        Method method = sign.getMethod();

        Auth auth = method.getAnnotation(Auth.class);

        if(auth.isAuth()){
            log.info("开始进行授权操作===========");
            //获取参数
            Object[] args = pj.getArgs();
            String[] parameterNames = sign.getParameterNames();
            if(args == null || args.length == 0 || parameterNames == null || parameterNames.length == 0){
                log.info("授权不通过");
            }

            for (int i = 0; i < parameterNames.length; i++) {
                if("token".equals(parameterNames[i])){
                    assert args != null;
                    String value = args[i].toString();
                    if("test".equals(value)){
                        log.info("授权通过");
                        return pj.proceed();
                    }
                }
            }
        }
        log.info("授权不通过");
        return ResultMode.FAIL_AUTH();
    }

}
