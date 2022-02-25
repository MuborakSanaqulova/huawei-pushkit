package com.asgardia.notification.huaweipushkit.api;

import com.asgardia.notification.huaweipushkit.model.PushMessageResponse;
import com.asgardia.notification.huaweipushkit.model.TokenResponse;

public interface HuaweiClient {

     TokenResponse getToken();

     PushMessageResponse pushMessage();

}
