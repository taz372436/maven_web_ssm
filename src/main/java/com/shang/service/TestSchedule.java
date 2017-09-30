package com.shang.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class TestSchedule {
	@Scheduled(cron="0/10 * *  * * ? ")   //每5秒执行一次
	public void getSupplier(){
		System.out.println(System.currentTimeMillis());
	}
}
