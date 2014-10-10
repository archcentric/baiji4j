package com.ctriposs.baiji.rpc.testservice;

import com.ctriposs.baiji.rpc.common.types.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by yqdong on 2014/9/24.
 */
public class TestServiceImpl implements TestService {

    private static final String DATA_XML = "/Items.xml";
    private static final List<Item> ITEMS;

    static {
        ITEMS = getItemsFromResource();
    }

    private static List<Item> getItemsFromResource() {
        InputStream stream = null;
        try {
            stream = TestServiceImpl.class.getResourceAsStream(DATA_XML);
            return DataXmlParser.parse(stream);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>(0);
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public GetItemsResponseType getItems(GetItemsRequestType request) throws Exception {
        if (request.generateRandomException == Boolean.TRUE) {
            Random random = new Random();
            int randomValue = random.nextInt(10);
            switch (randomValue) {
                case 1:
                    throw new Exception();
                case 2:
                    throw new IllegalStateException();
                case 3:
                    throw new IllegalArgumentException();
                case 4:
                    throw new NullPointerException();
                case 5:
                    throw new IndexOutOfBoundsException();
                case 6:
                    throw new UnsupportedOperationException();
                case 7:
                    throw new IOException();
                case 8:
                    throw new ClassCastException();
            }
        }

        GetItemsResponseType response = new GetItemsResponseType();

        List<Item> items = new ArrayList<>();
        int take = request.take != null ? request.take.intValue() : 0;
        int times = take / ITEMS.size();
        if (times < 0) {
            times = 0;
        }
        int left = take - times * ITEMS.size();
        for (int i = 0; i < times; i++) {
            items.addAll(ITEMS);
        }
        for (int i = 0; i < left; i++) {
            items.add(ITEMS.get(i));
        }

        response.items = items;

        if (request.sleep != null && request.sleep > 0) {
            Thread.sleep(request.sleep);
        }

        if (request.returnWrappedErrorResponse == Boolean.TRUE) {
            response.responseStatus = new ResponseStatusType();
            response.responseStatus.ack = AckCodeType.FAILURE;
            response.responseStatus.errors = new ArrayList<ErrorDataType>();
            ErrorDataType error = new ErrorDataType();
            error.errorCode = "xxx";
            error.stackTrace = "line 11111";
            response.responseStatus.errors.add(error);
        }

        return response;
    }

    @Override
    public CheckHealthResponseType checkHealth(CheckHealthRequestType request) throws Exception {
        return new CheckHealthResponseType();
    }
}
