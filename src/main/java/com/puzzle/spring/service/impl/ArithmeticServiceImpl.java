package com.puzzle.spring.service.impl;

import com.puzzle.spring.service.ArithmeticService;

public class ArithmeticServiceImpl implements ArithmeticService {


    public int add(int arg1, int arg2) {
        return arg1 + arg2;
    }

    public int inc(int arg) {
        return add(arg, 1);
    }

    public int function(int arg1, int arg2, int arg3) {
        return add(arg1, arg2) - inc(arg3);
    }

}
