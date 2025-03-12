package com.ozan.currency.exchange.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("within(@org.springframework.stereotype.Service *) || " +
            "within(@org.springframework.stereotype.Repository *) || " +
            "within(@org.springframework.stereotype.Controller *) ||" +
            "within(@org.springframework.web.bind.annotation.RestController *)")
    public void applicationPackagePointcut() {}

    @Pointcut("!within(@org.springframework.web.bind.annotation.ControllerAdvice *)")
    public void excludeControllerAdvice() {}

    @Around("applicationPackagePointcut() && excludeControllerAdvice()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long duration = System.currentTimeMillis() - start;
        log.info("Executed {} in {} ms", joinPoint.getSignature().toShortString(), duration);

        return result;
    }
}
