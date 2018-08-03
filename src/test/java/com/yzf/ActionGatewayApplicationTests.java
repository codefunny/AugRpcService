package com.yzf;

import com.yzf.domain.ActionRequest;
import com.yzf.domain.ActionResponse;
import com.yzf.service.ActionClientService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActionGatewayApplicationTests {

	@Autowired
	private ActionClientService clientService;

	@Test
	public void contextLoads() {
	}

	@Test
	public void actionTest() {
		ActionRequest request = new ActionRequest();
		request.setAction("acction");
		request.setMerNo("12323123");
		request.setVersion("123");
		request.setOrgNo("231231");
		request.setData("12312312312312");
		request.setEncryptkey("231231234sdfg");
		request.setSign("sign");

		String actionService = "action_service";

		ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(200);
		for(int i = 0; i < 2000; i++) {
			newFixedThreadPool.submit(new Callable<Object>() {

				@Override
				public Object call() throws Exception {
					ActionResponse resp =  clientService.action(request,actionService);
					return resp;
				}
			});
		}
		newFixedThreadPool.shutdown();

		try {
			newFixedThreadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
		}
	}
}
