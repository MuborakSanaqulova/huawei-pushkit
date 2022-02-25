package com.asgardia.notification.huaweipushkit.api;

import com.asgardia.notification.huaweipushkit.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Service
public class HuaweiClientImpl implements HuaweiClient {

    private final RestTemplate restTemplate = new RestTemplate();

    private String TOKEN_URL;

    @Value("${huawei.base.url}")
    private String BASE_URL;

    @Value("${huawei.get.token.url}")
    private String TOKEN;

    @Value("${huawei.grant.type}")
    private String GRAND_TYPE;

    @Value("${huawei.client.id}")
    private String CLIENT_ID;

    @Value("${huawei.client.secret}")
    private String CLIENT_SECRET;

    private static final String[] deviceToken = {"IQAAAACy0qiGAADGchzqaH6bU1ptLLN4JhejopXbV5A88t-NoBJrfrEQZawWE76_LPftKpUu658E3XSGRYUHXDxBVPUe8RyZZH23hyyC_8MJoi3mKQ"};

    public HuaweiClientImpl() {
    }

    @PostConstruct
    private void init() {
        TOKEN_URL = BASE_URL + TOKEN;
    }


    public HttpHeaders headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;

    }

    public HttpEntity<MultiValueMap<String, String>> generateEntity() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "client_credentials");
        map.add("client_id", "105440865");
        map.add("client_secret", "f4131e13741008b6ea10e418a5759c7eeaab08ebf336f6fb1e7576f5cad486a9");

        return new HttpEntity<>(map, headers());

    }


    @Override
    public TokenResponse getToken() {
        try {
            ResponseEntity<TokenResponse> response =
                    restTemplate.exchange("https://oauth-login.cloud.huawei.com/oauth2/v3/token",
                            HttpMethod.POST,
                            generateEntity(),
                            TokenResponse.class);
            return response.getBody();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new TokenResponse();
    }


    @Override
    public PushMessageResponse pushMessage() {
        TokenResponse tokenResponse = getToken();
        String accessToken = tokenResponse.getAccessToken();

        Notification notification = new Notification("title"
                , "Xabar yuborildi", new ClickAction(1,"#Intent;compo=com.rvr/.Activity;S.W=U;end"));
        Android android = new Android(notification);
        Message message = new Message(android, deviceToken);

        PushMessageRequest pushMessageRequest = new PushMessageRequest();
        pushMessageRequest.setValidateOnly(false);
        pushMessageRequest.setMessage(message);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", accessToken);


        ResponseEntity<PushMessageResponse> response =
                restTemplate.exchange("https://push-api.cloud.huawei.com/v1/105440865/messages:send",
                        HttpMethod.POST,
                        new HttpEntity<>(pushMessageRequest, headers),
                        PushMessageResponse.class);
        return response.getBody();

    }
}
