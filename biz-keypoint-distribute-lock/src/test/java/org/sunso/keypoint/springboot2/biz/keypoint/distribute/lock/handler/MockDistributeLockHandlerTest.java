package org.sunso.keypoint.springboot2.biz.keypoint.distribute.lock.handler;

import lombok.Data;

/**
 * @author sunso520
 * @Title:MockDistributeLockHandlerTest
 * @Description: <br>
 * @Created on 2024/3/5 11:09
 */
public class MockDistributeLockHandlerTest {
    private MockDistributeLockHandler mockDistributeLockHandler = new MockDistributeLockHandler();
    private MockService mockService = new MockService();

    @Data
    public static class Request {
        private String name;
    }

    @Data
    public static class Response {
        private String code = "1";
    }

    public Response callVoid(Request request) {
        return new Response();
    }

    public void handleCallVoidTest() {
        Request request = new Request();
        Response response = mockDistributeLockHandler.handle(null, this::callVoid, request);
        System.out.println(response.toString());
    }

    public void handleStaticMethodTest() {
        String result = mockDistributeLockHandler.handle(null, MockService::getString, "18773");
        System.out.println(result);
    }

    public void handleMethodTest() {
        long result = mockDistributeLockHandler.handle(null, mockService::getLong, 134L);
        System.out.println(result);
    }

    public static void main(String[] args) {
        MockDistributeLockHandlerTest test = new MockDistributeLockHandlerTest();
        test.handleStaticMethodTest();
        test.handleMethodTest();
        test.handleCallVoidTest();
    }
}
