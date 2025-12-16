package com.example.convospherebackend.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class LoggingAspect {

     @Around("com.example.convospherebackend.aspects.CommonPointCuts.serviceLayerExecution()")
     public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
         String message  = joinPoint.getSignature().toShortString();
         long startTime = System.nanoTime();
         log.debug("-> Starting {}",message);
         try {
             return joinPoint.proceed();
         }catch (Throwable e){
             log.error("Exception in {}: {}",message,e.getMessage());
             throw e;
         }finally {
          long durations = (System.nanoTime() - startTime)/1000000;
          log.debug("<- Finished {} ({} ms)",message,durations);
         }
     }
}
