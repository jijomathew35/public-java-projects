package com.example.demo.entities;

public class SKU {
	private String id;
	private int price;

	public SKU() {
		// TODO Auto-generated constructor stub
	}

	public SKU(String id, int price) {
		super();
		this.id = id;
		this.price = price;
	}

	public SKU(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object arg0) {
		return id.equals(((SKU) arg0).getId());
	}
}
