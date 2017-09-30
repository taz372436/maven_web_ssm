    /** 
     * File Name£ºScheduledJob.java 
     * 
     * Copyright Defonds Corporation 2015  
     * All Rights Reserved 
     * 
     */  
    package com.shang.service.jobs;  
      
    import org.quartz.JobExecutionContext;  
import org.quartz.JobExecutionException;  
import org.springframework.scheduling.quartz.QuartzJobBean;  

import com.shang.service.util.AnotherBean;
      
    
      
    /** 
     *  
     * Project Name£ºspring-quartz 
     * Type Name£ºScheduledJob 
     * Type Description£º 
     * Author£ºDefonds 
     * Create Date£º2015-10-29 
     * @version  
     *  
     */  
    public class FirstScheduledJob extends QuartzJobBean {  
          
        private AnotherBean anotherBean;  
      
        @Override  
        protected void executeInternal(JobExecutionContext arg0)  
                throws JobExecutionException {  
            System.out.println("I am FirstScheduledJob");  
            this.anotherBean.printAnotherMessage();  
      
        }  
      
        public void setAnotherBean(AnotherBean anotherBean) {  
            this.anotherBean = anotherBean;  
        }  
    }  