package com.bytearrays.onecthings.bean;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

/**
 * Created by dobrescu on 11/4/14.
 */

public final class AutowiringSpringBeanJobFactory
        extends SpringBeanJobFactory
        implements ApplicationContextAware {

    private transient AutowireCapableBeanFactory beanFactory;

    public void setApplicationContext(
            final ApplicationContext context) {
        beanFactory = context.getAutowireCapableBeanFactory();
    }

    @Override
    protected Object createJobInstance(
            final TriggerFiredBundle bundle)
            throws Exception {
        final Object job = super.createJobInstance(bundle);
        beanFactory.autowireBean(job);
        return job;
    }
}