    /**  
     * File Name£ºAnotherBean.java  
     *  
     * Copyright Defonds Corporation 2015   
     * All Rights Reserved  
     *  
     */  
    package com.shang.service.util;  
      
    import org.springframework.stereotype.Component;
      
    /**  
     *   
     * Project Name£ºspring-quartz  
     * Type Name£ºAnotherBean  
     * Type Description£º  
     * Author£ºDefonds  
     * Create Date£º2015-10-29  
     * @version   
     *   
     */  
    @Component("anotherBean")  
    public class AnotherBean {  
           
        public void printAnotherMessage(){  
            System.out.println("I am AnotherBean. I am called by Quartz jobBean using CronTriggerFactoryBean");  
        }  
           
    }  