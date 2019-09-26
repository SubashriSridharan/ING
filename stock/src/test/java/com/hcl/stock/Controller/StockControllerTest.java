package com.hcl.stock.Controller;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import com.hcl.stock.controller.StockController;
import com.hcl.stock.dto.StockResponseDTO;
import com.hcl.stock.service.StockServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class StockControllerTest {
	
	@Mock
	StockServiceImpl stockServiceImpl;

	@InjectMocks
	StockController stockController;
	
	@Test
	public void findingStockPrice() {
		String stockName="AAPL";
		final double DELTA = 1e-15;
		StockResponseDTO stockResponseDto=new StockResponseDTO();
		double price=220.96;
		Mockito.when(stockServiceImpl.getStockName(Mockito.anyLong())).thenReturn(stockName);
		Mockito.when(stockServiceImpl.getStockPriceFromExternalServer(Mockito.anyString(), Mockito.anyLong())).thenReturn(price);
		stockResponseDto.setStatucCode(200);
		stockResponseDto.setMessage("Fetched the Unit price of the stock");
		stockResponseDto.setStockPrice(price);		
		ResponseEntity <StockResponseDTO> stockResponse=stockController.findStockPrice(1L);
		assertEquals(price, stockResponseDto.getStockPrice(),DELTA);
		assertEquals("Fetched the Unit price of the stock", stockResponseDto.getMessage());
	}
	
}
