package com.sooraj.transaction.statistics.controller;

import static org.junit.Assert.assertEquals;

import java.time.Instant;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.sooraj.transaction.statistics.StatisticsApplication;
import com.sooraj.transaction.statistics.model.Transaction;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {StatisticsApplication.class},
webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StatisticControllerIntegrationTest {

	@LocalServerPort
	private int port;
	
	TestRestTemplate restTemplate = new TestRestTemplate();
	
	HttpHeaders headers = new HttpHeaders();
	
	
	@Test
	public void testStatistics() throws JSONException {
		
		long epochCurrentMilli = Instant.now().toEpochMilli();
		Transaction transaction = new Transaction(100.0, epochCurrentMilli);
		HttpEntity<Transaction> entity = new HttpEntity<Transaction>(transaction, headers);
		
		restTemplate.exchange(
				createURLWithPort("/transactions"),HttpMethod.POST,entity,String.class);
		
		transaction = new Transaction(200.0, epochCurrentMilli);
		entity = new HttpEntity<Transaction>(transaction, headers);
		restTemplate.exchange(
				createURLWithPort("/transactions"),HttpMethod.POST,entity,String.class);
		
		transaction = new Transaction(9.0, epochCurrentMilli);
		entity = new HttpEntity<Transaction>(transaction, headers);
		restTemplate.exchange(
				createURLWithPort("/transactions"),HttpMethod.POST,entity,String.class);
		
		
		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/statistics"),HttpMethod.GET,entity,String.class);
		
	
		String expectedJSON = "{\"sum\":309.0,\"avg\":103.0,\"max\":200.0,\"min\":9.0,\"count\":3}";
		
		JSONAssert.assertEquals(expectedJSON, response.getBody(), false);
	}
	
	@Test
	public void testOutOfRangeTime() {
		Transaction transaction = new Transaction(100.0, 123445556L);
		HttpEntity<Transaction> entity = new HttpEntity<Transaction>(transaction, headers);
		
		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/transactions"),HttpMethod.POST,entity,String.class);
		assertEquals(response.getStatusCode(),HttpStatus.NO_CONTENT);
	}
	
	
	private String createURLWithPort(String uri) {
		return "http://localhost:"+port+uri;
	}
}
