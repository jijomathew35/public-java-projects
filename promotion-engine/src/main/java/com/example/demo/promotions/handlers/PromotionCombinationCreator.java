package com.example.demo.promotions.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.demo.entities.SKU;
import com.example.demo.promotions.BasePromotion;
import com.example.demo.promotions.dao.PromotionDB;

/*If promotions x,x,y can be applied on the bill, then this will return all the combinations of x x y so that
 * all combinations can be tried to arrive at the best combination of the promotion*/
public class PromotionCombinationCreator {
	PromotionDB promotionDB = PromotionDB.getInstance();

	/**
	 * Get all the possible combinations in which the promtions can be applied <br>
	 * If there are three promotions A,B,C <br>
	 * then it would return ABC,ACB,BCA,BAC,CBA,CAB
	 */
	public List<BasePromotion> getListOfPromotionsWhichCanBeApplied(Map<SKU, Integer> order) {
		List<BasePromotion> promotions = new ArrayList<BasePromotion>();
		for (BasePromotion bp : promotionDB.getPromotions()) {
			int numTimes = bp.getNumTimesPromoCanBeApplied(order);
			if (numTimes > 0) {
				for (int i = 0; i < numTimes; i++) {
					promotions.add(bp);
				}
			}
		}
		
		System.out.println("returning the list of promotions");
		System.out.println("1st of conflict--no issue in merge");
		return promotions;
	}

	/* Can be improved by removing repetitions- but will function well even now */
	public void getAllOrdersOfPromoApplication(BasePromotion[] promotions, int l, int r, List<BasePromotion[]> result) {
		if (l == r)
			result.add(promotions.clone());
		else {
			for (int i = l; i <= r; i++) {
				promotions = swap(promotions, l, i);
				getAllOrdersOfPromoApplication(promotions, l + 1, r, result);
				promotions = swap(promotions, l, i);
			}
		}
	}

	/**
	 * Swap Characters at position
	 */
	public BasePromotion[] swap(BasePromotion[] promotions, int i, int j) {
		BasePromotion temp;
		temp = promotions[i];
		promotions[i] = promotions[j];
		promotions[j] = temp;
		return promotions;
	}

}
