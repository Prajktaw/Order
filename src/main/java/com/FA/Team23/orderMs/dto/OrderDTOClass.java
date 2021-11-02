package com.FA.Team23.orderMs.dto;

import java.time.LocalDate;
import java.util.Objects;

public class OrderDTOClass {
	private String orderId;
	private String buyerId;
	private Float amount;
	private LocalDate date;
	private String address;
	private String status;
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}
	public Float getAmount() {
		return amount;
	}
	public void setAmount(Float amount) {
		this.amount = amount;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String addrerss) {
		this.address = addrerss;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String string) {
		this.status = string;
	}
	@Override
	public String toString() {
		return "OrderDTOClass [orderId=" + orderId + ", buyerId=" + buyerId + ", amount=" + amount + ", date=" + date
				+ ", address=" + address + ", status=" + status + "]";
	}
	@Override
	public int hashCode() {
		return Objects.hash(address, amount, buyerId, date, orderId, status);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderDTOClass other = (OrderDTOClass) obj;
		return Objects.equals(address, other.address) && Objects.equals(amount, other.amount)
				&& Objects.equals(buyerId, other.buyerId) && Objects.equals(date, other.date)
				&& Objects.equals(orderId, other.orderId) && Objects.equals(status, other.status);
	}	
	
	
}