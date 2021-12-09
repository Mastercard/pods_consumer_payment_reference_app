package com.mastercard.consumerreferenceapp.repository;

import com.mastercard.consumerreferenceapp.model.ApiError;
import com.mastercard.consumerreferenceapp.model.ApiErrorMessage;
import com.mastercard.consumerreferenceapp.model.Contract;
import com.mastercard.consumerreferenceapp.model.MastercardUrl;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class ApiRepositoryTest {
    private ApiRepository apiRepository;

    private String TEST_URL = "/test/";

    private MockWebServer mockWebServer = new MockWebServer();

    @Before
    public void setup() throws IOException {
        apiRepository = ApiRepository.getInstance("http://0.0.0.0");
        mockWebServer.start();
        HttpUrl url = mockWebServer.url(TEST_URL);
        apiRepository.updateApiEndpoint(url);
    }

    @After
    public void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    private String getContractId() {
        return "f730fb62-c2fb-4764-846a-31c9eb14efbe";
    }

    private String getContractResponseBody() {
        return "{\n" +
                "    \"contractId\": "+ getContractId() +",\n" +
                "    \"deviceName\": \"Samsung Phone\",\n" +
                "    \"shopId\": \"Parther Shop\",\n" +
                "    \"customerName\": \"Partner's Customer\",\n" +
                "    \"tenure\": \"10\",\n" +
                "    \"tenureUnit\": \"month\",\n" +
                "    \"tenureBalance\": \"5\",\n" +
                "    \"downPayment\": \"50.00\",\n" +
                "    \"balanceDue\": \"450.00\",\n" +
                "    \"currency\": \"UGX\",\n" +
                "    \"paymentDueDate\": \"2021-02-24T14:05:41+08:00\"\n" +
                "}";
    }

    @Test
    public void getContract_200() throws InterruptedException {
        MockResponse mockResponse = new MockResponse();
        mockResponse.addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Cache-Control", "no-cache").setResponseCode(200);
        mockResponse.setBody(getContractResponseBody());
        mockWebServer.enqueue(mockResponse);
        final CountDownLatch latch = new CountDownLatch(1);
        Consumer<Contract> successConsumer = contract -> {
            Assert.assertEquals(getContractId(), contract.getContractId());
            latch.countDown();
        };
        apiRepository.getContract(successConsumer, mock(Consumer.class));
        mockWebServer.takeRequest();
        assertTrue(latch.await(5, SECONDS));
    }

    @Test
    public void getContract_400() throws InterruptedException {
        MockResponse mockResponse = new MockResponse();
        mockResponse.addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Cache-Control", "no-cache").setResponseCode(400);
        mockResponse.setBody(getContractResponseBody());
        mockWebServer.enqueue(mockResponse);
        final CountDownLatch latch = new CountDownLatch(1);
        Consumer<ApiError> errorConsumer = apiError -> {
            Assert.assertEquals(ApiErrorMessage.BAD_REQUEST, apiError.getErrorMessage());
            latch.countDown();
        };
        apiRepository.getContract(mock(Consumer.class), errorConsumer);
        mockWebServer.takeRequest();
        assertTrue(latch.await(5, SECONDS));
    }

    @Test
    public void getContract_500() throws InterruptedException {
        MockResponse mockResponse = new MockResponse();
        mockResponse.addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Cache-Control", "no-cache").setResponseCode(500);
        mockResponse.setBody(getContractResponseBody());
        mockWebServer.enqueue(mockResponse);
        final CountDownLatch latch = new CountDownLatch(1);
        Consumer<ApiError> errorConsumer = apiError -> {
            Assert.assertEquals(ApiErrorMessage.SERVER_ERROR, apiError.getErrorMessage());
            latch.countDown();
        };
        apiRepository.getContract(mock(Consumer.class), errorConsumer);
        mockWebServer.takeRequest();
        assertTrue(latch.await(5, SECONDS));
    }

    private String getToken() {
        return "token";
    }

    private String getMasterCardUrlResponseBody() {
        return "{\n" +
                "    \"url\": \"url\",\n" +
                "    \"accessToken\": " + getToken() + "\n" +
                "}";
    }

    @Test
    public void getPaymentUrl_200() throws InterruptedException {
        MockResponse mockResponse = new MockResponse();
        mockResponse.addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Cache-Control", "no-cache").setResponseCode(200);
        mockResponse.setBody(getMasterCardUrlResponseBody());
        mockWebServer.enqueue(mockResponse);
        final CountDownLatch latch = new CountDownLatch(1);
        Consumer<MastercardUrl> successConsumer = mastercardUrl -> {
            Assert.assertEquals(getToken(), mastercardUrl.getAccessToken());
            latch.countDown();
        };
        apiRepository.getPaymentUrl("contractId", successConsumer, mock(Consumer.class));
        mockWebServer.takeRequest();
        assertTrue(latch.await(5, SECONDS));
    }

    @Test
    public void getPaymentUrl_400() throws InterruptedException {
        MockResponse mockResponse = new MockResponse();
        mockResponse.addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Cache-Control", "no-cache").setResponseCode(400);
        mockResponse.setBody(getMasterCardUrlResponseBody());
        mockWebServer.enqueue(mockResponse);
        final CountDownLatch latch = new CountDownLatch(1);
        Consumer<ApiError> apiErrorConsumer = apiError -> {
            Assert.assertEquals(ApiErrorMessage.BAD_REQUEST, apiError.getErrorMessage());
            latch.countDown();
        };
        apiRepository.getPaymentUrl("contractId", mock(Consumer.class), apiErrorConsumer);
        mockWebServer.takeRequest();
        assertTrue(latch.await(5, SECONDS));
    }

    @Test
    public void getPaymentUrl_500() throws InterruptedException {
        MockResponse mockResponse = new MockResponse();
        mockResponse.addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Cache-Control", "no-cache").setResponseCode(500);
        mockResponse.setBody(getMasterCardUrlResponseBody());
        mockWebServer.enqueue(mockResponse);
        final CountDownLatch latch = new CountDownLatch(1);
        Consumer<ApiError> apiErrorConsumer = apiError -> {
            Assert.assertEquals(ApiErrorMessage.SERVER_ERROR, apiError.getErrorMessage());
            latch.countDown();
        };
        apiRepository.getPaymentUrl("contractId", mock(Consumer.class), apiErrorConsumer);
        mockWebServer.takeRequest();
        assertTrue(latch.await(5, SECONDS));
    }


    @Test
    public void getManageCardUrl_200() throws InterruptedException {
        MockResponse mockResponse = new MockResponse();
        mockResponse.addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Cache-Control", "no-cache").setResponseCode(200);
        mockResponse.setBody(getMasterCardUrlResponseBody());
        mockWebServer.enqueue(mockResponse);
        final CountDownLatch latch = new CountDownLatch(1);
        Consumer<MastercardUrl> successConsumer = mastercardUrl -> {
            Assert.assertEquals(getToken(), mastercardUrl.getAccessToken());
            latch.countDown();
        };
        apiRepository.getManageCardUrl("contractId", successConsumer, mock(Consumer.class));
        mockWebServer.takeRequest();
        assertTrue(latch.await(5, SECONDS));
    }

    @Test
    public void getManageCardUrl_400() throws InterruptedException {
        MockResponse mockResponse = new MockResponse();
        mockResponse.addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Cache-Control", "no-cache").setResponseCode(400);
        mockResponse.setBody(getMasterCardUrlResponseBody());
        mockWebServer.enqueue(mockResponse);
        final CountDownLatch latch = new CountDownLatch(1);
        Consumer<ApiError> apiErrorConsumer = apiError -> {
            Assert.assertEquals(ApiErrorMessage.BAD_REQUEST, apiError.getErrorMessage());
            latch.countDown();
        };
        apiRepository.getManageCardUrl("contractId", mock(Consumer.class), apiErrorConsumer);
        mockWebServer.takeRequest();
        assertTrue(latch.await(5, SECONDS));
    }

    @Test
    public void getManageCardUrl_500() throws InterruptedException {
        MockResponse mockResponse = new MockResponse();
        mockResponse.addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Cache-Control", "no-cache").setResponseCode(500);
        mockResponse.setBody(getMasterCardUrlResponseBody());
        mockWebServer.enqueue(mockResponse);
        final CountDownLatch latch = new CountDownLatch(1);
        Consumer<ApiError> apiErrorConsumer = apiError -> {
            Assert.assertEquals(ApiErrorMessage.SERVER_ERROR, apiError.getErrorMessage());
            latch.countDown();
        };
        apiRepository.getManageCardUrl("contractId", mock(Consumer.class), apiErrorConsumer);
        mockWebServer.takeRequest();
        assertTrue(latch.await(5, SECONDS));
    }
}