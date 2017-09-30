    /** 
     * File Name£ºAppMain.java 
     * 
     * Copyright Defonds Corporation 2015  
     * All Rights Reserved 
     * 
     */  
    package com.shang.service;  
      
    import org.springframework.context.support.AbstractApplicationContext;  
    import org.springframework.context.support.ClassPathXmlApplicationContext;  
      
    /** 
     *  
     * Project Name£ºspring-quartz 
     * Type Name£ºAppMain 
     * Type Description£º 
     * Author£ºDefonds 
     * Create Date£º2015-10-29 
     * @version  
     *  
     */  
    public class AppMain {  
      
        public static void main(String args[]){  
            AbstractApplicationContext context = new ClassPathXmlApplicationContext("quartz-context.xml");  
        }  
      
    }  