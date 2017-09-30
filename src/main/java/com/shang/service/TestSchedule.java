package com.shang.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class TestSchedule {
	@Scheduled(cron="0/10 * *  * * ? ")   //ÿ5��ִ��һ��
	public void getSupplier(){
		System.out.println(System.currentTimeMillis());
	}
}
