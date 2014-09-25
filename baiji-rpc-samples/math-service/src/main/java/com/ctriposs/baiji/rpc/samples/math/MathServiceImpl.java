package com.ctriposs.baiji.rpc.samples.math;

import com.ctriposs.baiji.rpc.common.types.CheckHealthRequestType;
import com.ctriposs.baiji.rpc.common.types.CheckHealthResponseType;

public class MathServiceImpl implements MathService {
    @Override
    public GetFactorialResponseType getFactorial(GetFactorialRequestType request) throws Exception {
        if (request == null || request.number == null) {
            throw new IllegalArgumentException("Missing number parameter");
        }
        if (request.number < 0) {
            throw new IllegalArgumentException("No factorial for negative numbers: " + request.number);
        }
        long number = request.number.longValue();
        long result = 1;
        for (long i = 2; i <= number; ++i) {
            result *= i;
        }
        return new GetFactorialResponseType(null, result);
    }

    @Override
    public CheckHealthResponseType checkHealth(CheckHealthRequestType request) throws Exception {
        return new CheckHealthResponseType();
    }
}
