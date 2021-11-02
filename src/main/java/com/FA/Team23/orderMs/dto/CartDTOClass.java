package com.FA.Team23.orderMs.dto;

import java.util.Objects;

public class CartDTOClass {
	
	private String buyerId;
	private String prodId;
	private Integer quantity;
	
	public String getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}
	public String getProdId() {
		return prodId;
	}
	public void setProdId(String prodId) {
		this.prodId = prodId;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	@Override
	public String toString() {
		return "CartDTOClass [buyerId=" + buyerId + ", prodId=" + prodId + ", quantity=" + quantity + "]";
	}
	@Override
	public int hashCode() {
		return Objects.hash(buyerId, prodId, quantity);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CartDTOClass other = (CartDTOClass) obj;
		return Objects.equals(buyerId, other.buyerId) && Objects.equals(prodId, other.prodId)
				&& Objects.equals(quantity, other.quantity);
	}
	
	
	

}