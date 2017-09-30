    /** 
     * File Name£ºSecondScheduledJob.java 
     * 
     * Copyright Defonds Corporation 2015  
     * All Rights Reserved 
     * 
     */  
    package com.shang.service.jobs;  
      
    import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
      
    /** 
     *  
     * Project Name£ºspring-quartz 
     * Type Name£ºSecondScheduledJob 
     * Type Description£º 
     * Author£ºDefonds 
     * Create Date£º2015-10-29 
     * @version  
     *  
     */  
    public class SecondScheduledJob extends QuartzJobBean {  
      
        @Override  
        protected void executeInternal(JobExecutionContext arg0)  
                throws JobExecutionException {  
            System.out.println("I am SecondScheduledJob");  
      
        }  
      
    }  