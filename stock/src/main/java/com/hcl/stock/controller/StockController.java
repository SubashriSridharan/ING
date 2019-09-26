package com.hcl.stock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.hcl.stock.dto.StockResponseDTO;
import com.hcl.stock.service.StockService;


@RestController
@RequestMapping("/stock")
public class StockController {
	
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	StockService stockService;
	
	@GetMapping("/price")
	public ResponseEntity<StockResponseDTO> findStockPrice(@RequestParam("stockId") Long stockId){
		StockResponseDTO stockResponseDto=new StockResponseDTO();
		String stockName=stockService.getStockName(stockId);
		double price = stockService.getStockPriceFromExternalServer(stockName, stockId);
			
				stockResponseDto.setStatucCode(200);
				stockResponseDto.setMessage("Fetched the Unit price of the stock");
				stockResponseDto.setStockPrice(price);
				return new ResponseEntity<>(stockResponseDto,HttpStatus.OK);
		
	}
	
	

}
