package com.example.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.example.demo.entities.SKU;
import com.example.demo.promotions.BasePromotion;
import com.example.demo.promotions.NumberBasedPromotion;
import com.example.demo.promotions.dao.PromotionDB;
import com.example.demo.promotions.handlers.BestPriceSelector;
import com.example.demo.promotions.handlers.PromotionCombinationCreator;

class DemoApplicationTests {
	SKU A = new SKU("A", 50);
	SKU B = new SKU("B", 30);
	SKU C = new SKU("C", 20);
	SKU D = new SKU("D", 15);

	@Test
	void basePromotionTests() {
		try {
			Map<SKU, Integer> order = getFirstOrder();

			/* First promotion with first order */
			NumberBasedPromotion numberBasedPromotion = getFirstPromo();
			int numTimes = numberBasedPromotion.getNumTimesPromoCanBeApplied(order);
			int value = numberBasedPromotion.applyPromotion(order);
			numberBasedPromotion.applyPromotionAndRemoveItems(order);
			int count = order.get(new SKU("A"));
			Assertions.assertEquals(numTimes, 0);
			Assertions.assertEquals(value, 0);
			Assertions.assertEquals(count, 1);

			/* First promotion with second order */
			numberBasedPromotion = getFirstPromo();
			order = getSecondOrder();
			numTimes = numberBasedPromotion.getNumTimesPromoCanBeApplied(order);
			value = numberBasedPromotion.applyPromotion(order);
			numberBasedPromotion.applyPromotionAndRemoveItems(order);
			count = order.get(new SKU("A"));
			Assertions.assertEquals(numTimes, 1);
			Assertions.assertEquals(value, 20);
			Assertions.assertEquals(count, 2);

			/* First promotion with third order */
			numberBasedPromotion = getFirstPromo();
			order = getThirdOrder();
			numTimes = numberBasedPromotion.getNumTimesPromoCanBeApplied(order);
			value = numberBasedPromotion.applyPromotion(order);
			numberBasedPromotion.applyPromotionAndRemoveItems(order);
			count = order.get(new SKU("C"));
			Assertions.assertEquals(1, numTimes);
			Assertions.assertEquals(20, value);
			Assertions.assertEquals(1, count);

			/* Second promotion with first order */
			order = getFirstOrder();
			numberBasedPromotion = getSecondPromo();
			numTimes = numberBasedPromotion.getNumTimesPromoCanBeApplied(order);
			value = numberBasedPromotion.applyPromotion(order);
			numberBasedPromotion.applyPromotionAndRemoveItems(order);
			count = order.get(new SKU("B"));
			Assertions.assertEquals(numTimes, 0);
			Assertions.assertEquals(value, 0);
			Assertions.assertEquals(count, 1);

			/* Second promotion with second order */
			order = getSecondOrder();
			numberBasedPromotion = getSecondPromo();
			numTimes = numberBasedPromotion.getNumTimesPromoCanBeApplied(order);
			value = numberBasedPromotion.applyPromotion(order);
			numberBasedPromotion.applyPromotionAndRemoveItems(order);
			count = order.get(new SKU("B"));
			Assertions.assertEquals(numTimes, 2);
			Assertions.assertEquals(value, 15);
			Assertions.assertEquals(count, 3);

			/* Second promotion with third order */
			numberBasedPromotion = getSecondPromo();
			order = getThirdOrder();
			numTimes = numberBasedPromotion.getNumTimesPromoCanBeApplied(order);
			value = numberBasedPromotion.applyPromotion(order);
			numberBasedPromotion.applyPromotionAndRemoveItems(order);
			count = order.get(new SKU("C"));
			Assertions.assertEquals(numTimes, 2);
			Assertions.assertEquals(value, 15);
			Assertions.assertEquals(count, 1);
			
			
			/* third promotion with third order */
			numberBasedPromotion = getThirdPromo();
			order = getThirdOrder();
			numTimes = numberBasedPromotion.getNumTimesPromoCanBeApplied(order);
			value = numberBasedPromotion.applyPromotion(order);
			numberBasedPromotion.applyPromotionAndRemoveItems(order);
			count = order.get(new SKU("C"));
			Assertions.assertEquals(1, 1);
			Assertions.assertEquals(value, 5);
			Assertions.assertEquals(0, count);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	void promotionCombinationCreatorTest() {
		try {
			List<BasePromotion[]> result;
			BasePromotion[] promosArr;
			List<BasePromotion> promos;
			int value;
			BestPriceSelector ops = new BestPriceSelector();
			Map<SKU, Integer> order;

			/* promotions */
			List<BasePromotion> promotions = new ArrayList<BasePromotion>();
			promotions = getAllPromotions();
			PromotionDB promotionDB = PromotionDB.getInstance();
			promotionDB.setPromotions(promotions);
			PromotionCombinationCreator promotionCombinationCreator = new PromotionCombinationCreator();

			/* List of promotions which can be applied on the first order */
			order = getFirstOrder();
			promos = promotionCombinationCreator.getListOfPromotionsWhichCanBeApplied(order);
			Assertions.assertEquals(promos.size(), 0);
			promosArr = new BasePromotion[promos.size()];
			for (int i = 0; i < promos.size(); i++) {
				promosArr[i] = promos.get(i);
			}
			result = new ArrayList<BasePromotion[]>();
			promotionCombinationCreator.getAllOrdersOfPromoApplication(promosArr, 0, promos.size() - 1, result);
			Assertions.assertEquals(result.size(), 0);

			/* value of the first order */
			value = ops.getBestPrice(result, order);
			System.out.println(value);

			/* List of promotions which can be applied on the second order */
			order = getSecondOrder();
			promos = promotionCombinationCreator.getListOfPromotionsWhichCanBeApplied(order);
			Assertions.assertEquals(promos.size(), 3);
			promosArr = new BasePromotion[promos.size()];
			for (int i = 0; i < promos.size(); i++) {
				promosArr[i] = promos.get(i);
			}
			result = new ArrayList<BasePromotion[]>();
			promotionCombinationCreator.getAllOrdersOfPromoApplication(promosArr, 0, promos.size() - 1, result);
			Assertions.assertEquals(result.size(), 6);
			/* value of the second order */
			value = ops.getBestPrice(result, order);
			System.out.println(value);

			/* List of promotions which can be applied on the third order */
			order = getThirdOrder();
			promos = promotionCombinationCreator.getListOfPromotionsWhichCanBeApplied(order);
			Assertions.assertEquals(4, promos.size());
			promosArr = new BasePromotion[promos.size()];
			for (int i = 0; i < promos.size(); i++) {
				promosArr[i] = promos.get(i);
			}
			result = new ArrayList<BasePromotion[]>();
			promotionCombinationCreator.getAllOrdersOfPromoApplication(promosArr, 0, promos.size() - 1, result);
			Assertions.assertEquals(24,result.size());
			/* value of the third order */
			value = ops.getBestPrice(result, order);
			System.out.println(value);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	HashMap<SKU, Integer> getSecondOrder() {
		HashMap<SKU, Integer> order = new HashMap<SKU, Integer>();
		order.put(A, 5);
		order.put(B, 5);
		order.put(C, 1);
		return order;

	}

	HashMap<SKU, Integer> order = new HashMap<SKU, Integer>();

	HashMap<SKU, Integer> getFirstOrder() {
		order.put(A, 1);
		order.put(B, 1);
		order.put(C, 1);
		return order;

	}

	HashMap<SKU, Integer> getThirdOrder() {
		HashMap<SKU, Integer> order = new HashMap<SKU, Integer>();
		order.put(A, 3);
		order.put(B, 5);
		order.put(C, 1);
		order.put(D, 1);
		return order;
	}

	List<BasePromotion> getAllPromotions() {
		List<BasePromotion> promotions = new ArrayList<BasePromotion>();

		promotions.add(getFirstPromo());
		promotions.add(getSecondPromo());
		promotions.add(getThirdPromo());

		return promotions;
	}

	NumberBasedPromotion getFirstPromo() {
		Map<String, Integer> promo = new HashMap<String, Integer>();
		promo.put("A", 3);
		NumberBasedPromotion numberBasedPromotion = new NumberBasedPromotion();
		numberBasedPromotion.setValue(130);
		numberBasedPromotion.setSkuNeededById(promo);
		return numberBasedPromotion;

	}

	NumberBasedPromotion getSecondPromo() {
		Map<String, Integer> promo = new HashMap<String, Integer>();
		NumberBasedPromotion numberBasedPromotion = new NumberBasedPromotion();
		promo = new HashMap<String, Integer>();
		promo.put("B", 2);
		numberBasedPromotion = new NumberBasedPromotion();
		numberBasedPromotion.setValue(45);
		numberBasedPromotion.setSkuNeededById(promo);
		return numberBasedPromotion;

	}

	NumberBasedPromotion getThirdPromo() {
		Map<String, Integer> promo = new HashMap<String, Integer>();
		NumberBasedPromotion numberBasedPromotion = new NumberBasedPromotion();
		promo = new HashMap<String, Integer>();
		promo.put("C", 1);
		promo.put("D", 1);
		numberBasedPromotion = new NumberBasedPromotion();
		numberBasedPromotion.setValue(30);
		numberBasedPromotion.setSkuNeededById(promo);
		return numberBasedPromotion;

	}

}
