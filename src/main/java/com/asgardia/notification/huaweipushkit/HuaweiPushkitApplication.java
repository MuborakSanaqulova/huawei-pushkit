package com.asgardia.notification.huaweipushkit;

import com.asgardia.notification.huaweipushkit.api.HuaweiClientImpl;
import com.asgardia.notification.huaweipushkit.model.PushMessageResponse;
import com.asgardia.notification.huaweipushkit.model.TokenResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HuaweiPushkitApplication {

	public static void main(String[] args) {
		SpringApplication.run(HuaweiPushkitApplication.class, args);
		HuaweiClientImpl huaweiClient = new HuaweiClientImpl();
		PushMessageResponse pushMessageResponse = huaweiClient.pushMessage();
		System.out.println(pushMessageResponse.getMsg());
	}

}
