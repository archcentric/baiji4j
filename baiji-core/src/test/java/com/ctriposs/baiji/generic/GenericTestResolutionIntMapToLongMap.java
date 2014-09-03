package com.ctriposs.baiji.generic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;

public class GenericTestResolutionIntMapToLongMap extends GenericTestBase {

    @Test
    public void testResolutionIntMapToLongMap() throws IOException {
        testResolution("{\"type\":\"map\", \"values\":\"int\"}", makeMap(new Object[]{"a", 10, "b", 20}),
                makeMap(new Object[]{"a", 10, "b", 20}));
    }
}
