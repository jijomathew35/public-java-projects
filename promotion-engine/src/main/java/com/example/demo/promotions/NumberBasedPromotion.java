package com.example.demo.promotions;

import java.util.Map;
import java.util.Map.Entry;

import com.example.demo.entities.SKU;

public class NumberBasedPromotion extends BasePromotion {

	private int value;

	@Override
	public int applyPromotion(Map<SKU, Integer> order) {
		int orderValue = 0;
		for (Entry<String, Integer> entry : skuNeededById.entrySet()) {
			
			Integer numSKUInBill = order.get(new SKU(entry.getKey()));
			if (numSKUInBill == null) {
				return 0;
			}
			if (numSKUInBill < entry.getValue()) {
				return 0;
			}
			int priceOfsku = order.entrySet().stream().filter(x -> x.getKey().getId() == entry.getKey())
					.map(x -> x.getKey().getPrice()).reduce(0, (a, b) -> a + b);
			orderValue += priceOfsku * entry.getValue();
			
		}
		return orderValue - value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
