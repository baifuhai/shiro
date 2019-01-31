package com.test.config;

import com.test.realm.MyRealm;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

//@Component
public class MyRealmBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println(beanName);
        if (bean instanceof MyRealm) {
            MyRealm myRealm = (MyRealm) bean;
            CredentialsMatcher credentialsMatcher = myRealm.getCredentialsMatcher();
            if (credentialsMatcher instanceof HashedCredentialsMatcher) {
                HashedCredentialsMatcher hashedCredentialsMatcher = (HashedCredentialsMatcher) credentialsMatcher;
                String hashAlgorithmName = hashedCredentialsMatcher.getHashAlgorithmName();
                //myRealm.setHashAlgorithmName(hashAlgorithmName);
            }

        }
        return bean;
    }

}
