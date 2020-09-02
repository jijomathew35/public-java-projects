package com.example.demo.promotions.dao;

import java.util.List;

import com.example.demo.promotions.BasePromotion;

/*Singleton class to act as DB */
public class PromotionDB {
	private static volatile PromotionDB promotionDB;
	private List<BasePromotion> promotions;

	public List<BasePromotion> getPromotions() {
		return promotions;
	}

	public void setPromotions(List<BasePromotion> promotions) {
		this.promotions = promotions;
	}

	public static PromotionDB getInstance() {
		if (promotionDB == null) {
			synchronized (PromotionDB.class) {
				/* Double check within synchronized to handle multithreading issues */
				if (promotionDB == null)
					promotionDB = new PromotionDB();
			}
		}
		return promotionDB;
	}

}
