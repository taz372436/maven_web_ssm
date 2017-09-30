    /** 
     * File Name£ºMyBean.java 
     * 
     * Copyright Defonds Corporation 2015  
     * All Rights Reserved 
     * 
     */  
    package com.shang.service.jobs;  
      
    import org.springframework.stereotype.Component;  
      
    /** 
     *  
     * Project Name£ºspring-quartz 
     * Type Name£ºMyBean 
     * Type Description£º 
     * Author£ºDefonds 
     * Create Date£º2015-10-29 
     * @version  
     *  
     */  
    @Component("myBean")  
    public class MyBean {  
       
        public void printMessage() {  
            System.out.println("I am MyBean. I am called by MethodInvokingJobDetailFactoryBean using SimpleTriggerFactoryBean");  
        }  
           
    }  