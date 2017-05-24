package com.puzzle.spring.component;


import com.puzzle.spring.service.ArithmeticService;
import com.puzzle.spring.service.impl.ArithmeticServiceImpl;
import com.puzzle.spring.service.impl.DelegatingArithmeticService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;


public class LogConfigBeanFactoryPostProcessor implements BeanFactoryPostProcessor {


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        BeanDefinition asBeanDefinition = beanFactory.getBeanDefinition("arithmeticServiceBean");

        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
        registry.registerBeanDefinition(
                "arithmeticServiceImpl",
                BeanDefinitionBuilder.genericBeanDefinition(ArithmeticServiceImpl.class).getBeanDefinition());


        Object arithmeticServiceImplBean = beanFactory.getBean("arithmeticServiceImpl");
        beanFactory.applyBeanPostProcessorsAfterInitialization(arithmeticServiceImplBean, "arithmeticServiceImpl");
//
        asBeanDefinition.setBeanClassName(DelegatingArithmeticService.class.getName());

        beanFactory.addBeanPostProcessor(new ArithmeticServiceInjector(arithmeticServiceImplBean));


    }


    private class ArithmeticServiceInjector implements BeanPostProcessor {

        private Object arithmeticServiceImpl;

        public ArithmeticServiceInjector(Object arithmeticServiceImpl) {
            this.arithmeticServiceImpl = arithmeticServiceImpl;
        }

        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

            Class<?> beanClass = bean.getClass();
            if (bean instanceof ArithmeticService) {
                Field arithmeticServiceField = ReflectionUtils.findField(beanClass, "arithmeticService", ArithmeticService.class);
                if (arithmeticServiceField != null) {
                    arithmeticServiceField.setAccessible(true);
                    ReflectionUtils.setField(arithmeticServiceField,
                            bean, arithmeticServiceImpl);
                }
            }
            return bean;
        }
    }


}
