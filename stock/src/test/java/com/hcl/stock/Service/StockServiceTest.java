package com.hcl.stock.Service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.hcl.stock.entity.Stock;
import com.hcl.stock.repository.StockRepository;
import com.hcl.stock.service.StockServiceImpl;


@RunWith(MockitoJUnitRunner.class)
public class StockServiceTest {
	@InjectMocks
	StockServiceImpl stockServiceImpl;
	
	@Mock
	StockRepository stockRepository;
	@Mock
	RestTemplate restTemplate;
	
	@Test
	public void testRetrieveStockName() {
		String stockNameInfo=null;
		Stock stock=new Stock();
		stock.setDescription("abc");
		stock.setStockId(1L);
		stock.setStockName("WFC");
		stock.setUnitPrice(48.92);
		Optional<Stock> stockData=Optional.of(stock);
		Mockito.when(stockRepository.findById(Mockito.anyLong())).thenReturn(stockData);
		String stockName=stockServiceImpl.getStockName(1L);
		if(stockData.isPresent()) {
			stockNameInfo=stockData.get().getStockName();
		}
		assertNotNull(stockName);
		assertEquals(stockName,stockNameInfo);
		
	}
	
	@Test
	public void testNullStockName() {
		Stock stock=null;
		Optional<Stock> stockData=Optional.ofNullable(stock);
		Mockito.when(stockRepository.findById(6L)).thenReturn(stockData);
		String stockName=stockServiceImpl.getStockName(6L);
		assertNull(stockName);
	}
	@Test
	public void gettingPriceFromExternalServer() {
		final double DELTA = 1e-15;
		String stockName="AAPL";
		
		String json="{\"Global Quote\": { \"01. symbol\": \"AAPL\",\"02. open\": \"222.0100\","
		       +" \"05. price\": \"220.9600\", \"06. volume\": \"21398993\" }}";
		
		ResponseEntity<String> response = new ResponseEntity<String>(json,HttpStatus.OK);
		Mockito.when(restTemplate.exchange("https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + stockName
				+ "&apikey=KFJWFM57YUCK6IWR", HttpMethod.GET, null, String.class)).thenReturn(response);
	
		double price=stockServiceImpl.getStockPriceFromExternalServer("AAPL",1L);
		assertEquals(220.96, price,DELTA);
		
	
	}
	
	@Test
	public void testGetPriceFromExternalServer() {
		final double DELTA = 1e-15;
		String stockName="YES Bank";
		Stock stock=new Stock();
		stock.setStockId(2L);
		stock.setDescription("Bank");
		stock.setStockName("YES Bank");
		stock.setUnitPrice(66.32);
		Optional<Stock> stockObject=Optional.of(stock);
		String json="{\"Error Message\": \"Invalid API call. Please retry or visit the documentation (https://www.alphavantage.co/documentation/) for GLOBAL_QUOTE.\"}";		
		ResponseEntity<String> response = new ResponseEntity<String>(json,HttpStatus.OK);
		Mockito.when(restTemplate.exchange("https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + stockName
				+ "&apikey=KFJWFM57YUCK6IWR", HttpMethod.GET, null, String.class)).thenReturn(response);
		Mockito.when(stockRepository.findById(2l)).thenReturn(stockObject);
		double price=stockServiceImpl.getStockPriceFromExternalServer("YES Bank",stock.getStockId());
		assertEquals(stockObject.get().getUnitPrice(), price,DELTA);
		assertEquals("Bank", stock.getDescription());
		
	
	}
	@Test
	public void testGetNullPriceFromExternalServer() {
		final double DELTA = 1e-15;
		String stockName="test";
		Stock stock=null;
		Optional<Stock> stockObject=Optional.ofNullable(stock);
		String json="{\"Error Message\": \"Invalid API call. Please retry or visit the documentation (https://www.alphavantage.co/documentation/) for GLOBAL_QUOTE.\"}";		
		ResponseEntity<String> response = new ResponseEntity<String>(json,HttpStatus.OK);
		Mockito.when(restTemplate.exchange("https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + stockName
				+ "&apikey=KFJWFM57YUCK6IWR", HttpMethod.GET, null, String.class)).thenReturn(response);
		Mockito.when(stockRepository.findById(6L)).thenReturn(stockObject);
		double price=stockServiceImpl.getStockPriceFromExternalServer("test",6L);
		assertEquals(0.0, price,DELTA);
		
	
	}

}
