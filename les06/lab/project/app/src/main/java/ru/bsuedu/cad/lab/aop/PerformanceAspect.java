package ru.bsuedu.cad.lab.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PerformanceAspect {
    
    @Around("execution(* ru.bsuedu.cad.lab.parser.CSVParser.parse(..))")
    public Object measureParsingTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        
        try {
            // Выполняем метод parse
            return joinPoint.proceed();
        } finally {
            long endTime = System.currentTimeMillis();
            System.out.println("Время парсинга CSV файла: " + (endTime - startTime) + " мс");
        }
    }
} 