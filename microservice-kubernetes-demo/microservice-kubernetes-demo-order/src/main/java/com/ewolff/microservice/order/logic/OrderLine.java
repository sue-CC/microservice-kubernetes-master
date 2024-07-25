package com.ewolff.microservice.order.logic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
public class OrderLine {

	@Column(name = "F_COUNT")
	private int count;

	private long itemId;

	private String note;

	@Id
	@GeneratedValue
	private long id;

	public void setCount(int count) {
		this.count = count;
	}

	public void setItemId(long item) {
		this.itemId = item;
	}

	public void setNote(String note) {this.note = note;}

	public OrderLine() {
	}

	public OrderLine(int count, long item, String note) {
		this.count = count;
		this.itemId = item;
		this.note = note;

	}

	public int getCount() {
		return count;
	}

	public long getItemId() {
		return itemId;
	}

	public String getNote() { return note;}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);

	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

}
