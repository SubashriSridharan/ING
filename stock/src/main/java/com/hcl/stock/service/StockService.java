package com.hcl.stock.service;

public interface StockService {
	String getStockName(Long stockId);
	double getStockPriceFromExternalServer(String stockName,Long stockId);
}
