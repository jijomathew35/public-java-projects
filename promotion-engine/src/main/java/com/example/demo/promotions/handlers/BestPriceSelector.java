package com.example.demo.promotions.handlers;

import java.util.List;
import java.util.Map;

import com.example.demo.entities.SKU;
import com.example.demo.promotions.BasePromotion;

public class BestPriceSelector {
	/**Applies promotions in all the possible combinations and returns the best price*/
	public int getBestPrice(List<BasePromotion[]> promotions, Map<SKU, Integer> orders) {
		int maxValue = orders.entrySet().stream().map(x -> x.getKey().getPrice() * x.getValue()).reduce(0,
				(a, b) -> a + b);
		int minValue = maxValue;
		for (int i = 0; i < promotions.size(); i++) {
			int tempValue = maxValue;
			BasePromotion[] promotionOrder = promotions.get(i);
			for (int j = 0; j < promotionOrder.length; j++) {
				BasePromotion promotion = promotionOrder[j];
				tempValue -= promotion.applyPromotionAndRemoveItems(orders);
			}
			if (tempValue < minValue) {
				minValue = tempValue;
			}
		}
		return minValue;
	}
}
