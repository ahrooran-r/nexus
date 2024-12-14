package org.ahroo.nexus.configuration;

import net.ttddyy.dsproxy.ExecutionInfo;
import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.listener.logging.SLF4JLogLevel;
import net.ttddyy.dsproxy.listener.logging.SLF4JQueryLoggingListener;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.time.Duration;
import java.util.List;

@Component
public class SQLLoggerProxy implements BeanPostProcessor {

    private DataSource encloseWithLoggingProxy(DataSource actual) {
        Duration slowQueryThreshold = Duration.ofSeconds(5L);
        SLF4JQueryLoggingListener slowQueryListener = new SLF4JQueryLoggingListener() {
            @Override
            public void afterQuery(ExecutionInfo execInfo, List<QueryInfo> queryInfoList) {
                if (slowQueryThreshold.toMillis() <= execInfo.getElapsedTime()) {
                    super.afterQuery(execInfo, queryInfoList);
                }
            }
        };
        slowQueryListener.setLogLevel(SLF4JLogLevel.WARN);
        return ProxyDataSourceBuilder.create(actual).listener(slowQueryListener).logQueryBySlf4j(SLF4JLogLevel.TRACE).build();
    }

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof DataSource dataSource) {
            bean = this.encloseWithLoggingProxy(dataSource);
        }

        return bean;
    }
}
