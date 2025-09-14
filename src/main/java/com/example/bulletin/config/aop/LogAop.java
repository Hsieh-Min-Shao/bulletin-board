package com.example.bulletin.config.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LogAop {

    @Around("execution(* com.example.bulletin.controller..*(..)) || execution(* com.example.bulletin.service..*(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        long t0 = System.currentTimeMillis();
        Signature sig = pjp.getSignature();
        Throwable err = null;
        Object ret = null;
        try {
            ret = pjp.proceed();
            return ret;
        } catch (Throwable e) {
            err = e;
            throw e;
        } finally {
            long ms = System.currentTimeMillis() - t0;
            if (err == null) {
                log.info("[{}] cost={}ms", sig, ms);
            } else {
                log.warn("[{}] cost={}ms ERROR={}:{}", sig, ms, err.getClass().getSimpleName(), err.getMessage());
            }
        }
    }
}
