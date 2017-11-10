package com.hst.Carlease.http.bean;
/**
 * 
 * @author lyq
 *
 */
public class SecondCarBean1 {
private	String tokenID;
private int brandID;
private double minPrice;
private double maxPrice;
private int minYear;
private int maxYear;
private int pageindex;
private int pagesize;
public String getTokenID() {
	return tokenID;
}
public void setTokenID(String tokenID) {
	this.tokenID = tokenID;
}
public int getBrandID() {
	return brandID;
}
public void setBrandID(int brandID) {
	this.brandID = brandID;
}
public double getMinPrice() {
	return minPrice;
}
public void setMinPrice(double minPrice) {
	this.minPrice = minPrice;
}
public double getMaxPrice() {
	return maxPrice;
}
public void setMaxPrice(double maxPrice) {
	this.maxPrice = maxPrice;
}
public int getMinYear() {
	return minYear;
}
public void setMinYear(int minYear) {
	this.minYear = minYear;
}
public int getMaxYear() {
	return maxYear;
}
public void setMaxYear(int maxYear) {
	this.maxYear = maxYear;
}
public int getPageindex() {
	return pageindex;
}
public void setPageindex(int pageindex) {
	this.pageindex = pageindex;
}
public int getPagesize() {
	return pagesize;
}
public void setPagesize(int pagesize) {
	this.pagesize = pagesize;
}

}
