package com.puzzle.spring.service.impl;


import com.puzzle.spring.service.ArithmeticService;

public class DelegatingArithmeticService extends ArithmeticServiceImpl implements ArithmeticService {

    private ArithmeticService arithmeticService;

    @Override
    public int add(int arg1, int arg2) {
        return arithmeticService.add(arg1, arg2);
    }

    @Override
    public int inc(int arg) {
        return arithmeticService.inc(arg);
    }

    public void setArithmeticService(ArithmeticService arithmeticService) {
        this.arithmeticService = arithmeticService;
    }
}
