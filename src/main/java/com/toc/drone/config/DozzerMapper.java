package com.toc.drone.config;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DozzerMapper {

    @Bean
    public Mapper beanMapper(){
        return new DozerBeanMapper();
    }
}
