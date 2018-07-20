package com.e9w.skywalker.config;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import com.alibaba.druid.pool.DruidDataSource;
import com.e9w.skywalker.lang.Parameter;
import com.e9w.skywalker.util.WorkingResourceUtil;
import com.jfinal.plugin.activerecord.Config;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.DbKit;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Properties;

/**
 * Created by fc on 2016-11-29.
 */
public class Configuration {

    private static Parameter parameter;
    private static Charset charset;
    private static String upload_file_path;
    private static int max_post_file_length = -1;
    private static int timeout = -1;

    public static void init() {
        initLog();
        initEnv();
        initDb();
        initProvider();
    }

    protected static void initEnv() {
       try (InputStream is = WorkingResourceUtil.getInputStream("env.properties")) {
           Properties properties = new Properties();
           properties.load(is);
           parameter = new Parameter(properties);
       } catch (Throwable t) {
           throw new RuntimeException(t);
       }
    }

    protected static void initLog() {
        try {
            LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(context);
            context.reset();
            configurator.doConfigure(WorkingResourceUtil.getFile("logback.xml"));
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    protected static void initDb() {
       try {
            Map<String, String> jdbc = WorkingResourceUtil.loadPropertFile("jdbc.properties");
            DruidDataSource ds = new DruidDataSource();
            ds.setUsername(jdbc.get("user"));
            ds.setPassword(jdbc.get("password"));
            ds.setDriverClassName(jdbc.get("driverClass"));
            ds.setFilters(jdbc.get("stat"));
            ds.setUrl(jdbc.get("jdbcUrl"));
            Config config = new Config("jdbc.properties", ds);
            DbKit.addConfig(config);
            Method init = Db.class.getDeclaredMethod("init");
            init.setAccessible(true);
            init.invoke(null);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    protected static void initProvider() {

    }

    public static Charset getCharset() {
        if (null == charset) {
            charset = Charset.forName(parameter.s("charset", "UTF-8"));
        }
        return charset;
    }

    public static int getTimeout() {
        if (-1 == timeout) {
            timeout = parameter.i("timeout", 3000);
        }
        return timeout;
    }

    public static String getUploadFilePath() {
        if (null == upload_file_path) {
            upload_file_path = parameter.s("upload_file_path", "");
        }
        return upload_file_path;
    }

    public static int getMaxPostFileLength() {
        if (-1 == max_post_file_length) {
            max_post_file_length = parameter.i("max_post_file_length", 30000);
        }
        return max_post_file_length;
    }
}
