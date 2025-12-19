package com.example.convospherebackend.aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class CommonPointCuts {

    @Pointcut("execution(* com.example.convospherebackend.services..*(..))")
    public void serviceLayerExecution(){}

    @Pointcut("execution(* com.example.convospherebackend.services.ConversationService.*(..))")
    public void authenticateUser(){}
}
