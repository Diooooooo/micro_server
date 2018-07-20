package com.e9w.skywalker.controller;

import com.e9w.skywalker.config.Configuration;
import com.e9w.skywalker.controller.render.Result;
import com.e9w.skywalker.lang.PageList;
import com.e9w.skywalker.util.JsonUtil;
import com.jfinal.render.RenderException;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by fc on 2016-12-02.
 */
public class ApiController {

    private Result result;
    private boolean forIE = false;
    private final static String CONTENT_TYPE = "application/json; charset=" + Configuration.getCharset().toString();
    private final static String content_type_forie = "text/html; charset=" + Configuration.getCharset().toString();

    @Autowired
    private HttpServletResponse response;
    @Autowired
    private EurekaClient eurekaClient;
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    public void forIE() {
        forIE = true;
    }

    public void renderData(Object obj) {
        renderData(obj, null);
    }

    public void renderData(Object obj, String statusMessage) {
        buildRender(obj, statusMessage);
        render();
    }

    private void buildRender(Object obj, String statusMessage) {
        if (obj instanceof PageList<?>) {
            result = new Result((PageList<?>) obj, statusMessage);
        } else if (obj instanceof List<?>) {
            result = new Result((List<?>) obj, statusMessage);
        } else {
            result = new Result(obj, statusMessage);
        }
    }

    private void render() {
        String jsonText = JsonUtil.toJson(result);
        PrintWriter writer = null;
        try {
            response.setHeader("Pragma", "no-cache"); // HTTP/1.0 caches might  not implement  Cache-Control and  might only implement  Pragma: no-cache
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);

            response.setContentType(forIE ? content_type_forie : CONTENT_TYPE);
            writer = response.getWriter();
            writer.write(jsonText);
            writer.flush();
        } catch (IOException e) {
            throw new RenderException(e);
        } finally {
            if (writer != null)
                writer.close();
        }
    }

    public void renderException(String statusMessage) {
        renderData(null, null);
    }

    public void renderException(String statusMessage, Throwable t) {
        renderData(null, null);
    }

    public void renderData() {
        renderData(null, null);
    }


    public InstanceInfo getInstanceInfo(String serviceProvider, boolean secure){
        return eurekaClient.getNextServerFromEureka(serviceProvider, secure);
    }

    public ServiceInstance getServiceInstance(String servicePorvider) {
        return loadBalancerClient.choose(servicePorvider);
    }

}
