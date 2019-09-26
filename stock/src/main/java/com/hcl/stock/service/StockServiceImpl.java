package com.hcl.stock.service;

import java.util.Optional;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.hcl.stock.entity.Stock;
import com.hcl.stock.repository.StockRepository;

@Service
public class StockServiceImpl implements StockService {
	@Autowired
	StockRepository stockRepository;
	@Autowired
	RestTemplate restTemplate;

	@Override
	public String getStockName(Long stockId) {
		Optional<Stock> stockInfo = stockRepository.findById(stockId);
		if (stockInfo.isPresent()) {
			Stock stockData = stockInfo.get();
			return stockData.getStockName();
		} else {
			return null;
		}

	}

	@Override
	public double getStockPriceFromExternalServer(String stockName, Long stockId) {
		String price = null;
		Double unitPrice;
		String stock = restTemplate.exchange("https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol="
				+ stockName + "&apikey=KFJWFM57YUCK6IWR", HttpMethod.GET, null, String.class).getBody();

		JSONObject obj = new JSONObject(stock);
		if (obj.isNull("Error Message")) {
			JSONObject x = obj.getJSONObject("Global Quote");
			price = (String) x.get("05. price");
			unitPrice = Double.valueOf(price);
			return unitPrice;

		} else {
			Optional<Stock> stockInfo = stockRepository.findById(stockId);
			if (stockInfo.isPresent()) {
				Stock stockData = stockInfo.get();
				return stockData.getUnitPrice();
			} else {
				return 0.0;
			}
		}

	}

}
