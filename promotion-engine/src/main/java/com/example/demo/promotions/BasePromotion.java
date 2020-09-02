package com.example.demo.promotions;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.example.demo.entities.SKU;

public abstract class BasePromotion {
	/*
	 * The Map will contain values of the format A,2 B,1 This means this promotion
	 * needs 2As and 1B
	 */
	protected Map<String, Integer> skuNeededById;

	/**
	 * Applies the promotion to the order and returns the value to be added to the
	 * bill--just for the promotion <br>
	 * And removes the items involved in promotion from the bill.<br>
	 * <br>
	 * If order is<br>
	 * A 5<br>
	 * B 2<br>
	 * <br>
	 * and skuNeededById is<br>
	 * A 1<br>
	 * B 1<br>
	 * and value of the promotion is 50 <br>
	 * (this means 1 A and 1 B for 50) <br>
	 * the method will return 50 and reduce 1A and 1B from the bill.
	 * 
	 */
	public final int applyPromotionAndRemoveItems(Map<SKU, Integer> order) {
		int promotionValue = applyPromotion(order);
		if (promotionValue > 0) {
			reduceItemsAfterApplyingPromotion(order);
		}
		return promotionValue;

	}

	public abstract int applyPromotion(Map<SKU, Integer> order);

	private final void reduceItemsAfterApplyingPromotion(Map<SKU, Integer> order) {
		for (Entry<String, Integer> skuNeeded : skuNeededById.entrySet()) {
			order.put(new SKU(skuNeeded.getKey()), order.get(new SKU(skuNeeded.getKey())) - skuNeeded.getValue());
		}
	}

	public int getNumTimesPromoCanBeApplied(Map<SKU, Integer> order) {
		order = getCloneOfOrder(order);
		int numTimes = 0;
		while (true) {
			for (Entry<String, Integer> entry : skuNeededById.entrySet()) {
				Integer numSKUInBill = order.get(new SKU(entry.getKey()));
				if(numSKUInBill==null) {
					return 0;
				}
				if (numSKUInBill < entry.getValue()) {
					return numTimes;
				}
				numSKUInBill -=entry.getValue();
				order.put(new SKU(entry.getKey()), numSKUInBill);
			}
			numTimes++;
		}
	}

	private Map<SKU, Integer> getCloneOfOrder(Map<SKU, Integer> order) {
		Map<SKU, Integer> clonedOrder = new HashMap<SKU, Integer>();
		for (Entry<SKU, Integer> entry : order.entrySet()) {
			clonedOrder.put(entry.getKey(), entry.getValue());
		}
		return clonedOrder;
	}

	public Map<String, Integer> getSkuNeededById() {
		return skuNeededById;
	}

	public void setSkuNeededById(Map<String, Integer> skuNeededById) {
		this.skuNeededById = skuNeededById;
	}
	
	

}
