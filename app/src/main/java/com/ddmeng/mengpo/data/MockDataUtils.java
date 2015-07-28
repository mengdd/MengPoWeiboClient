package com.ddmeng.mengpo.data;

import java.util.ArrayList;
import java.util.List;

public class MockDataUtils {

    public static List<String> getMockStrings(int count) {
        List<String> mockStringList = new ArrayList<String>();
        for (int i = 0; i < count; ++i) {
            mockStringList.add("Data String : " + i);
        }
        return mockStringList;
    }
}
