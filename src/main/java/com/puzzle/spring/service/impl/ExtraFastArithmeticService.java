package com.puzzle.spring.service.impl;

import com.puzzle.spring.service.ArithmeticService;

public class ExtraFastArithmeticService implements ArithmeticService {

    private ArithmeticService arithmeticService;

    public int add(int arg1, int arg2) {
        return arithmeticService.add(arg1, arg2);
    }

    public int inc(int arg) {
        return arithmeticService.inc(arg);
    }

    public int function(int arg1, int arg2, int arg3) {
        System.out.println("New extra fast calculations");
        return arithmeticService.function(arg1, arg2, arg3);
//        return arithmeticService.add(arg1, arg2) - arithmeticService.inc(arg3);
    }

    public void setArithmeticService(ArithmeticService arithmeticService) {
        this.arithmeticService = arithmeticService;
    }

}
