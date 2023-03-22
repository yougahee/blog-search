package com.gahui.blogsearch.aop.concurrent;

import com.gahui.blogsearch.setting.exceptions.SearchException;
import domain.enums.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Aspect
@Component
public class ConcurrentAspect {

    private final ReentrantLock lock = new ReentrantLock();

    @Around(value = "@annotation(com.gahui.blogsearch.aop.concurrent.annotation.ConcurrentAop)")
    public Object concurrentAop(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("concurrentAop start");

        return proc(joinPoint);
    }

    private Object proc(ProceedingJoinPoint joinPoint) throws Throwable {

        try {
            lock.lock();
            return joinPoint.proceed();
        } catch (Exception e) {
            log.error("[settingHashMap] ReentrantLock lock failed.", e);
            throw new SearchException(ResponseCode.EXCEPTION_ERROR);
        } finally {
            lock.unlock();
        }
    }

}
