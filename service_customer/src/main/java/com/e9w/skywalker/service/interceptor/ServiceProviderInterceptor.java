package com.e9w.skywalker.service.interceptor;

import com.e9w.skywalker.util.StringUtil;
import com.netflix.appinfo.InstanceInfo;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by fc on 2016-12-09.
 */
public class ServiceProviderInterceptor implements MethodInterceptor {

    private final static String METHOD = "method";
    private final static String RESPONSE_TYPE = "clazz";
    private final static String INSTANCE_INFO = "info";
    private final static Logger logger = LoggerFactory.getLogger(ServiceProviderInterceptor.class);
    private InstanceInfo info;

    @Override
    public Object invoke(MethodInvocation invocation) throws Exception {
        Method method = invocation.getMethod();
        Annotation[][] annotations = method.getParameterAnnotations();
        Object[] arguments = invocation.getArguments();
        MultiValueMap<String, Object> annotationMultiValueMap = new LinkedMultiValueMap<>();
        for (int i = 0; i < annotations.length; i ++) {
            annotationMultiValueMap.set(((RequestParam)annotations[i][0]).name(), arguments[i]);
        }
        info = (InstanceInfo) annotationMultiValueMap.remove(INSTANCE_INFO).get(0);
        String url = (String) annotationMultiValueMap.remove(METHOD).get(0);
        Class<?> clazz = (Class<?>) annotationMultiValueMap.remove(RESPONSE_TYPE).get(0);
        Object result;
        if (url.contains("?") || url.contains("&")) {
            result = getForEntityBase(url, clazz, annotationMultiValueMap);
        } else {
            result = postForEntityBase(url, clazz, annotationMultiValueMap);
        }
        return result;
    }

    private Object postForEntityBase(String url, Class<?> responseType, Object request) throws Exception {
        try {
            RestTemplate template = new RestTemplate();
            ResponseEntity entity = template.postForEntity(url, request, responseType);
            getStatusCode(entity);
            return entity.getBody();
        } catch (Throwable t) {
            logger.error("provider info. appName={}, instanceId={}, IPAddr={}, homePageUrl={}, healthCheckUrl={}, hostname={}, status={}, exception={}",
                    info.getAppName(), info.getInstanceId(), info.getIPAddr(), info.getHomePageUrl(),
                    info.getHealthCheckUrl(), info.getHostName(), info.getStatus(), t.getMessage());
            throw t;
        }
    }

    private Object getForEntityBase(String url, Class<?> responseType, Object request) throws Exception {
        try {
            RestTemplate template = new RestTemplate();
            url += url.contains("?") ? StringUtil.EMPTY_STRING : "?";
            final String[] goUrl = {""};
            if (!url.contains("=")) {
                goUrl[0] = url;
                MultiValueMap<String, Object> annotation = (LinkedMultiValueMap<String, Object>) request;
                annotation.keySet().stream().forEach((String tar) -> {
                    goUrl[0] += tar;
                    goUrl[0] += "=";
                    final String[] param = {""};
                    annotation.get(tar).stream().forEach(target -> param[0] += target + ",");
                    goUrl[0] += param[0].contains(",") ? param[0].substring(0, param[0].length() - 1) : null;
                    goUrl[0] += "&";
                });
                goUrl[0] = goUrl[0].substring(0, goUrl[0].length() - 1);
            }
            ResponseEntity entity = template.getForEntity(goUrl[0], responseType);
            getStatusCode(entity);
            return entity.getBody();
        } catch (Throwable t) {
            logger.error("provider info. appName={}, instanceId={}, IPAddr={}, homePageUrl={}, healthCheckUrl={}, hostname={}, status={}",
                    info.getAppName(), info.getInstanceId(), info.getIPAddr(), info.getHomePageUrl(),
                    info.getHealthCheckUrl(), info.getHostName(), info.getStatus());
            t.printStackTrace();
            throw t;
        }
    }

    private void getStatusCode(ResponseEntity entity) throws Exception {
        HttpStatus status = entity.getStatusCode();
        if (status.is1xxInformational()) {
            throw new Exception("请求信息不完整");
        } else if (status.is3xxRedirection()) {
            throw new Exception("跳转路径不正确");
        } else if (status.is4xxClientError()) {
            throw new Exception("客户端错误");
        } else if (status.is5xxServerError()) {
            throw new Exception("服务器内部错误");
        }
    }
}
