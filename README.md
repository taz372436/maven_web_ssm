# maven_web_ssm
ssm+spring_schdule+spring_quartz
这一步如果不知道怎么做可以参考博客《使用 Eclipse 的 Maven 2 插件开发一个 JEE 项目》。
步骤 2：第三方依赖包的引入
Maven pom.xml 编辑如下：
[html] view plain copy
print?

    <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"  
      xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">  
      <modelVersion>4.0.0</modelVersion>  
      
      <groupId>settle</groupId>  
      <artifactId>spring-quartz</artifactId>  
      <version>1.0.0</version>  
      <packaging>jar</packaging>  
      <name>spring-quartz</name>  
      <url>http://maven.apache.org</url>  
      
        <properties>  
            <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>  
            <springframework.version>4.2.2.RELEASE</springframework.version>  
            <quartz.version>2.2.2</quartz.version>  
        </properties>  
      
        <dependencies>  
            <dependency>  
                <groupId>org.springframework</groupId>  
                <artifactId>spring-core</artifactId>  
                <version>${springframework.version}</version>  
            </dependency>  
            <dependency>  
                <groupId>org.springframework</groupId>  
                <artifactId>spring-context-support</artifactId>  
                <version>${springframework.version}</version>  
            </dependency>  
            <!-- Transaction dependency is required with Quartz integration -->  
            <dependency>  
                <groupId>org.springframework</groupId>  
                <artifactId>spring-tx</artifactId>  
                <version>${springframework.version}</version>  
            </dependency>  
               
            <!-- Quartz framework -->  
            <dependency>  
                <groupId>org.quartz-scheduler</groupId>  
                <artifactId>quartz</artifactId>  
                <version>${quartz.version}</version>  
            </dependency>  
        </dependencies>  
    </project>  


步骤 3：Quartz Scheduler 配置作业
有两种方式在 Spring 中使用 Quartz 来配置一个作业。
A：使用 MethodInvokingJobDetailFactoryBean
这种方式在你想要调用特定 bean 的一个方法的时候很是方便，比另一种方法要简单的多。
[html] view plain copy
print?

    <!-- For times when you just need to invoke a method on a specific object -->  
    <bean id="simpleJobDetail" class="org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean">  
        <property name="targetObject" ref="myBean" />  
        <property name="targetMethod" value="printMessage" />  
    </bean>  


以上作业配置简单调用了 myBean 的 printMessage 方法，myBean 是一个简单的 POJO。
B：使用 JobDetailFactoryBean
如果你需要更高级的设置，需要给作业传递数据，想更加灵活的话就使用这种方式。
[html] view plain copy
print?

    <!-- For times when you need more complex processing, passing data to the scheduled job -->  
    <bean name="firstComplexJobDetail"    class="org.springframework.scheduling.quartz.JobDetailFactoryBean">  
        <property name="jobClass" value="com.defonds.scheduler.jobs.FirstScheduledJob" />  
        <property name="jobDataMap">  
            <map>  
                <entry key="anotherBean" value-ref="anotherBean" />  
            </map>  
        </property>  
        <property name="durability" value="true" />  
    </bean>  


jobClass 关联到一个继承自 QuartzJobBean 的类，它实现了 Quartz 作业接口。调用到这个作业的时候，它的 executeInternal 将被执行。
jobDataMap 允许我们给相关作业 bean 传递一些数据。在这个例子里，我们将 ScheduledJob 将要使用到的 'anotherBean' 传递给它。
以下是引用 jobclass(FirstScheduledJob)的具体实现。
com.defonds.scheduler.jobs.FirstScheduledJob
[java] view plain copy
print?

    /** 
     * File Name：ScheduledJob.java 
     * 
     * Copyright Defonds Corporation 2015  
     * All Rights Reserved 
     * 
     */  
    package com.defonds.scheduler.jobs;  
      
    import org.quartz.JobExecutionContext;  
    import org.quartz.JobExecutionException;  
    import org.springframework.scheduling.quartz.QuartzJobBean;  
      
    import com.defonds.scheduler.util.AnotherBean;  
      
    /** 
     *  
     * Project Name：spring-quartz 
     * Type Name：ScheduledJob 
     * Type Description： 
     * Author：Defonds 
     * Create Date：2015-10-29 
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


步骤 4：配置 Quartz 调度时要使用到的触发器
触发器用来定义调度器何时将会执行你的调度任务的那个时间。有两种可能的触发器类型：
A：简单触发器，使用 SimpleTriggerFactoryBean
你可以定义作业的启动时间、触发器之间的延迟时间以及 repeatInterval(频率)。
[html] view plain copy
print?

    <!-- Run the job every 2 seconds with initial delay of 1 second -->  
    <bean id="simpleTrigger"  class="org.springframework.scheduling.quartz.SimpleTriggerFactoryBean">  
        <property name="jobDetail" ref="simpleJobDetail" />  
        <property name="startDelay" value="1000" />  
        <property name="repeatInterval" value="2000" />  
    </bean>  


B：计划触发器，使用 CronTriggerFactoryBean
这种类型更加灵活，允许你针对特定实例选择计划方案以及将来要执行的频率。
[html] view plain copy
print?

    <!-- Run the job every 5 seconds -->  
    <bean id="cronTrigger"  class="org.springframework.scheduling.quartz.CronTriggerFactoryBean">  
        <property name="jobDetail" ref="firstComplexJobDetail" />  
        <!--<property name="cronExpression" value="0/5 * * ? * SAT-SUN" />-->  
        <property name="cronExpression" value="0/5 * * ? * *" />  
          
    </bean>  


步骤 5：配置创建定配置 Quartz 调度器的 SchedulerFactoryBean
SchedulerFactoryBean 将 jobDetails 和 triggers 整合在一起以配置 Quartz 调度器。
[html] view plain copy
print?

    <!-- Scheduler factory bean to glue together jobDetails and triggers to Configure Quartz Scheduler -->  
    <bean  class="org.springframework.scheduling.quartz.SchedulerFactoryBean">  
        <property name="jobDetails">  
            <list>  
                <ref bean="simpleJobDetail" />  
                <ref bean="firstComplexJobDetail" />  
                <ref bean="secondComplexJobDetail" />  
            </list>  
        </property>  
      
        <property name="triggers">  
            <list>  
                <ref bean="simpleTrigger" />  
                <ref bean="cronTrigger" />  
                <ref bean="secondCronTrigger" />  
            </list>  
        </property>  
    </bean>  


下面贴出我们示例的完整的上下文文件。
src/main/resources/quartz-context.xml
[html] view plain copy
print?

    <?xml version="1.0" encoding="UTF-8"?>  
    <beans xmlns="http://www.springframework.org/schema/beans"  
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context"  
        xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.0.xsd  
                                http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-4.0.xsd">  
       
        <context:component-scan base-package="com.defonds.scheduler" />  
       
       
        <!-- For times when you just need to invoke a method on a specific object -->  
        <bean id="simpleJobDetail" class="org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean">  
            <property name="targetObject" ref="myBean" />  
            <property name="targetMethod" value="printMessage" />  
        </bean>  
       
       
        <!-- For times when you need more complex processing, passing data to the scheduled job -->  
        <bean name="firstComplexJobDetail"    class="org.springframework.scheduling.quartz.JobDetailFactoryBean">  
            <property name="jobClass" value="com.defonds.scheduler.jobs.FirstScheduledJob" />  
            <property name="jobDataMap">  
                <map>  
                    <entry key="anotherBean" value-ref="anotherBean" />  
                </map>  
            </property>  
            <property name="durability" value="true" />  
        </bean>  
          
        <!-- Always run at 20:00 everyday -->  
        <bean name="secondComplexJobDetail"    class="org.springframework.scheduling.quartz.JobDetailFactoryBean">  
            <property name="jobClass" value="com.defonds.scheduler.jobs.SecondScheduledJob" />  
            <property name="durability" value="true" />  
        </bean>  
       
       
        <!-- Run the job every 2 seconds with initial delay of 1 second -->  
        <bean id="simpleTrigger"  class="org.springframework.scheduling.quartz.SimpleTriggerFactoryBean">  
            <property name="jobDetail" ref="simpleJobDetail" />  
            <property name="startDelay" value="1000" />  
            <property name="repeatInterval" value="2000" />  
        </bean>  
       
       
        <!-- Run the job every 5 seconds -->  
        <bean id="cronTrigger"  class="org.springframework.scheduling.quartz.CronTriggerFactoryBean">  
            <property name="jobDetail" ref="firstComplexJobDetail" />  
            <!--<property name="cronExpression" value="0/5 * * ? * SAT-SUN" />-->  
            <property name="cronExpression" value="0/5 * * ? * *" />  
              
        </bean>  
          
        <!-- Always run at 20:00 everyday -->  
        <bean id="secondCronTrigger"  class="org.springframework.scheduling.quartz.CronTriggerFactoryBean">  
            <property name="jobDetail" ref="secondComplexJobDetail" />  
            <property name="cronExpression" value="0 0 20 ? * *" />  
        </bean>  
       
       
        <!-- Scheduler factory bean to glue together jobDetails and triggers to Configure Quartz Scheduler -->  
        <bean  class="org.springframework.scheduling.quartz.SchedulerFactoryBean">  
            <property name="jobDetails">  
                <list>  
                    <ref bean="simpleJobDetail" />  
                    <ref bean="firstComplexJobDetail" />  
                    <ref bean="secondComplexJobDetail" />  
                </list>  
            </property>  
       
            <property name="triggers">  
                <list>  
                    <ref bean="simpleTrigger" />  
                    <ref bean="cronTrigger" />  
                    <ref bean="secondCronTrigger" />  
                </list>  
            </property>  
        </bean>  
       
    </beans>  


步骤 6：创建本文用到的几个简单 POJO 任务 Bean
com.defonds.scheduler.jobs.MyBean
[java] view plain copy
print?

    /** 
     * File Name：MyBean.java 
     * 
     * Copyright Defonds Corporation 2015  
     * All Rights Reserved 
     * 
     */  
    package com.defonds.scheduler.jobs;  
      
    import org.springframework.stereotype.Component;  
      
    /** 
     *  
     * Project Name：spring-quartz 
     * Type Name：MyBean 
     * Type Description： 
     * Author：Defonds 
     * Create Date：2015-10-29 
     * @version  
     *  
     */  
    @Component("myBean")  
    public class MyBean {  
       
        public void printMessage() {  
            System.out.println("I am MyBean. I am called by MethodInvokingJobDetailFactoryBean using SimpleTriggerFactoryBean");  
        }  
           
    }  


com.defonds.scheduler.util.AnotherBean
[html] view plain copy
print?

    /**  
     * File Name：AnotherBean.java  
     *  
     * Copyright Defonds Corporation 2015   
     * All Rights Reserved  
     *  
     */  
    package com.defonds.scheduler.util;  
      
    import org.springframework.stereotype.Component;  
      
    /**  
     *   
     * Project Name：spring-quartz  
     * Type Name：AnotherBean  
     * Type Description：  
     * Author：Defonds  
     * Create Date：2015-10-29  
     * @version   
     *   
     */  
    @Component("anotherBean")  
    public class AnotherBean {  
           
        public void printAnotherMessage(){  
            System.out.println("I am AnotherBean. I am called by Quartz jobBean using CronTriggerFactoryBean");  
        }  
           
    }  


com.defonds.scheduler.jobs.SecondScheduledJob
[java] view plain copy
print?

    /** 
     * File Name：SecondScheduledJob.java 
     * 
     * Copyright Defonds Corporation 2015  
     * All Rights Reserved 
     * 
     */  
    package com.defonds.scheduler.jobs;  
      
    import org.quartz.JobExecutionContext;  
    import org.quartz.JobExecutionException;  
    import org.springframework.scheduling.quartz.QuartzJobBean;  
      
    /** 
     *  
     * Project Name：spring-quartz 
     * Type Name：SecondScheduledJob 
     * Type Description： 
     * Author：Defonds 
     * Create Date：2015-10-29 
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


为了演示多个执行计划的一起调度，我们写了两个 JobDetailFactoryBean，于是就有了 SecondScheduledJob。
步骤 7：创建执行程序的 Main 类
[java] view plain copy
print?

    /** 
     * File Name：AppMain.java 
     * 
     * Copyright Defonds Corporation 2015  
     * All Rights Reserved 
     * 
     */  
    package com.defonds.scheduler;  
      
    import org.springframework.context.support.AbstractApplicationContext;  
    import org.springframework.context.support.ClassPathXmlApplicationContext;  
      
    /** 
     *  
     * Project Name：spring-quartz 
     * Type Name：AppMain 
     * Type Description： 
     * Author：Defonds 
     * Create Date：2015-10-29 
     * @version  
     *  
     */  
    public class AppMain {  
      
        public static void main(String args[]){  
            AbstractApplicationContext context = new ClassPathXmlApplicationContext("quartz-context.xml");  
        }  
      
    }  


这时整个项目目录结构如下图所示：
项目结构.png
执行这个 Main 类，输出结果如下：
I am FirstScheduledJob
I am AnotherBean. I am called by Quartz jobBean using CronTriggerFactoryBean
I am MyBean. I am called by MethodInvokingJobDetailFactoryBean using SimpleTriggerFactoryBean
I am MyBean. I am called by MethodInvokingJobDetailFactoryBean using SimpleTriggerFactoryBean
I am FirstScheduledJob
I am AnotherBean. I am called by Quartz jobBean using CronTriggerFactoryBean
I am MyBean. I am called by MethodInvokingJobDetailFactoryBean using SimpleTriggerFactoryBean
I am MyBean. I am called by MethodInvokingJobDetailFactoryBean using SimpleTriggerFactoryBean
I am MyBean. I am called by MethodInvokingJobDetailFactoryBean using SimpleTriggerFactoryBean
I am FirstScheduledJob
I am AnotherBean. I am called by Quartz jobBean using CronTriggerFactoryBean
I am MyBean. I am called by MethodInvokingJobDetailFactoryBean using SimpleTriggerFactoryBean
I am MyBean. I am called by MethodInvokingJobDetailFactoryBean using SimpleTriggerFactoryBean
I am FirstScheduledJob
I am AnotherBean. I am called by Quartz jobBean using CronTriggerFactoryBean
I am SecondScheduledJob
I am MyBean. I am called by MethodInvokingJobDetailFactoryBean using SimpleTriggerFactoryBean
I am MyBean. I am called by MethodInvokingJobDetailFactoryBean using SimpleTriggerFactoryBean
I am MyBean. I am called by MethodInvokingJobDetailFactoryBean using SimpleTriggerFactoryBean
I am FirstScheduledJob
I am AnotherBean. I am called by Quartz jobBean using CronTriggerFactoryBean
I am MyBean. I am called by MethodInvokingJobDetailFactoryBean using SimpleTriggerFactoryBean
I am MyBean. I am called by MethodInvokingJobDetailFactoryBean using SimpleTriggerFactoryBean
I am FirstScheduledJob
I am AnotherBean. I am called by Quartz jobBean using CronTriggerFactoryBean
I am MyBean. I am called by MethodInvokingJobDetailFactoryBean using SimpleTriggerFactoryBean
I am MyBean. I am called by MethodInvokingJobDetailFactoryBean using SimpleTriggerFactoryBean
I am MyBean. I am called by MethodInvokingJobDetailFactoryBean using SimpleTriggerFactoryBean
可以看到，简单触发器调用的作业每隔两秒执行一次，而计划触发器一的则是每隔五秒钟执行一次，计划触发器二则是固定只执行了一次(晚上八点整，红色字体部分)。
后记

    大多数情况下都会使用 JobDetailFactoryBean 进行任务调度配置；
    每个 JobDetailFactoryBean 都有一个触发器与之匹配。配置多个调度计划，需要配置多个这种匹配对；
    由触发器指向 JobDetailFactoryBean；
