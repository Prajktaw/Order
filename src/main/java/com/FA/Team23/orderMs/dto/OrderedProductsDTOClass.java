package com.FA.Team23.orderMs.dto;

import java.util.Objects;

public class OrderedProductsDTOClass {
	private String buyerId;
	private String sellerId;
	private String productId;
	private Integer quantity;
	public String getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}
	public String getSellerId() {
		return sellerId;
	}
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	@Override
	public String toString() {
		return "OrderedProductsDTOClass [buyerId=" + buyerId + ", sellerId=" + sellerId + ", productId=" + productId
				+ ", quantity=" + quantity + "]";
	}
	@Override
	public int hashCode() {
		return Objects.hash(buyerId, productId, quantity, sellerId);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderedProductsDTOClass other = (OrderedProductsDTOClass) obj;
		return Objects.equals(buyerId, other.buyerId) && Objects.equals(productId, other.productId)
				&& Objects.equals(quantity, other.quantity) && Objects.equals(sellerId, other.sellerId);
	}
	
	
	
}

