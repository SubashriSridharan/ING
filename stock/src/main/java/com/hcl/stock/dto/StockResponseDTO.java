package com.hcl.stock.dto;

public class StockResponseDTO {
	private int statucCode;
	private String message;
	private double stockPrice;
	public int getStatucCode() {
		return statucCode;
	}
	public void setStatucCode(int statucCode) {
		this.statucCode = statucCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public double getStockPrice() {
		return stockPrice;
	}
	public void setStockPrice(double stockPrice) {
		this.stockPrice = stockPrice;
	}
}
