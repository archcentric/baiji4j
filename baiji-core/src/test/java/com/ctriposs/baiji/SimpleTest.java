package com.ctriposs.baiji;

import com.ctriposs.baiji.convert.TypeConverter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SimpleTest {

    public static void main(String[] args) {
        List<BigDecimal> list = new ArrayList<>();
        list.add(new BigDecimal(1));
        list.add(new BigDecimal(1.5f));
        list.add(new BigDecimal(1234.5678d));
        list.add(new BigDecimal("1234"));
        List<BigDecimal> listTwo = new ArrayList<>();
        listTwo.add(new BigDecimal(1));
        listTwo.add(new BigDecimal(1.5f));
        listTwo.add(new BigDecimal(1234.5678d));
        listTwo.add(new BigDecimal("1234"));
        List<List<BigDecimal>> testList = new ArrayList<>();
        List<Set<Integer>> testSet = new ArrayList<>();
        testList.add(list);
        testList.add(listTwo);
        System.out.println("nihao");
    }
}
