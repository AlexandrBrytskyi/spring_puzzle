package com.puzzle.spring.component;

import com.puzzle.spring.annotation.LogPerformance;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class LogPerformanceBeanPostProcessor implements BeanPostProcessor {

    private Map<String, Map.Entry<Class, Set<String>>> classesWithAnnotatedMethods = new HashMap<>();


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class beanClaz = bean.getClass();
        Method[] methods = beanClaz.getMethods();

        Set<String> annotatedMethods = new HashSet<>();
        for (Method method : methods) {

//            System.out.println("watching method " + method.getName());
            if (method.isAnnotationPresent(LogPerformance.class) || classInterfaceIsAnnotated(method, LogPerformance.class)) {
                annotatedMethods.add(method.getName());
//                System.out.println("added method to set " + method);
            }

        }

        if (annotatedMethods != null && !annotatedMethods.isEmpty()) {
            classesWithAnnotatedMethods.put(beanName, new ClassSetMethodsEntry(beanClaz, annotatedMethods));
//            System.out.println("Annotated methods " + annotatedMethods);
        }

        return bean;
    }

    private boolean classInterfaceIsAnnotated(Method method, Class<LogPerformance> annotationClass) {
        Class<?>[] declareClassInterfaces = method.getDeclaringClass().getInterfaces();
        for (Class<?> declareClassInterface : declareClassInterfaces) {
            try {
                Method interfaceMethod = declareClassInterface.getMethod(method.getName(), method.getParameterTypes());
                return interfaceMethod.isAnnotationPresent(annotationClass);
            } catch (NoSuchMethodException e) {
//                e.printStackTrace();
                return false;
            }
        }
        return false;
    }


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("incoming bean " + beanName);
        if (!classesWithAnnotatedMethods.containsKey(beanName)) return bean;

        Map.Entry<Class, Set<String>> classWithAnnotatedMethods = classesWithAnnotatedMethods.get(beanName);
        Class beanClass = classWithAnnotatedMethods.getKey();

        System.out.println("returning proxy for bean " + beanName + ", class " + bean.getClass());

        return Proxy.newProxyInstance(beanClass.getClassLoader(), beanClass.getInterfaces(), new InvocationHandler() {


            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                System.out.println("invocation in proxy of bean " + beanName + ", " + beanClass + " of method " + method);
                Set<String> methods = classWithAnnotatedMethods.getValue();

                if (!methods.contains(method.getName())) return method.invoke(bean, args);

                System.out.println("want to call method from set " + method.getName() + " of bean " + beanName);

                System.out.println("method start " + method.getName() + " of bean " + beanName);
                Object toReturn = method.invoke(bean, args);
                System.out.println("method worked " + method.getName() + " of bean " + beanName);
                return toReturn;
            }
        });
    }

    private class ClassSetMethodsEntry implements Map.Entry<Class, Set<String>> {

        private Class key;
        private Set<String> value;

        public ClassSetMethodsEntry(Class key, Set<String> value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public Class getKey() {
            return key;
        }

        @Override
        public Set<String> getValue() {
            return value;
        }

        @Override
        public Set<String> setValue(Set<String> value) {
            Set<String> old = this.value;
            this.value = value;
            return old;
        }
    }
}


