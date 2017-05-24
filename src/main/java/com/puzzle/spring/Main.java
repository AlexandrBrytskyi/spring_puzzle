package com.puzzle.spring;

import com.puzzle.spring.service.ArithmeticService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext appContext = new ClassPathXmlApplicationContext("spring-context.xml");

        System.out.println("ArithmeticService output:");

        ArithmeticService arithmeticService = (ArithmeticService) appContext.getBean("arithmeticServiceBean");

        System.out.println("result: " + arithmeticService.function(3, 2, 1));

        System.out.println();

        System.out.println("ExtraFastArithmeticService output:");

        ArithmeticService extraFastArithmeticService = (ArithmeticService) appContext.getBean("extraFastArithmeticServiceBean");

        System.out.println("result: " + extraFastArithmeticService.function(3, 2, 1));


        //Question 1: Why are output different for services if implementations are the same?
//        implementations are not same, because ExtraFastArithmeticService delegates methods, while
//        ArithmeticServiceImpl does not


        //Question 2: How to fix output?
//        if we want to see same output we need to either delegate both realizations or not


        //Question 3: Which alternatives can be used to achieve @LogPerformance functionality?
//        BeanPostProcessor to wrap beans with proxy
    }
}
