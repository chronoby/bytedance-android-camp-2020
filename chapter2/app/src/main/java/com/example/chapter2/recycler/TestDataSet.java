package com.example.chapter2.recycler;

import java.util.ArrayList;
import java.util.List;

public class TestDataSet {

    public static List<TestData> getData() {
        List<TestData> result = new ArrayList();
        result.add(new TestData("xiaoA", "hello", "1:00"));
        result.add(new TestData("xiaoB", "hello", "0.59"));
        result.add(new TestData("xiaoC", "233333", "0.58"));
        result.add(new TestData("xiaoD", "233333", "0.57"));
        result.add(new TestData("xiaoE", "hahaha", "0.56"));
        result.add(new TestData("xiaoF", "hahaha", "0.55"));
        result.add(new TestData("xiaoG", "hahaha", "0.54"));
        result.add(new TestData("xiaoH", "hahaha", "0.53"));
        result.add(new TestData("xiaoI", "hahaha","0.52"));
        result.add(new TestData("xiaoJ", "555555", "0.51"));
        result.add(new TestData("xiaoK", "555555", "0.50"));
        return result;
    }
}
