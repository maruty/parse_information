package com.blog.marublo;

public class IpoDto {
	String meigaraName;
	String meigaraCode;
	//上場日
	String openDate;
	//BB期間
	String BBDate;



	public String getMeigaraName() {
		return meigaraName;
	}
	public String getMeigaraCode() {
		return meigaraCode;
	}
	public String getOpenDate() {
		return openDate;
	}
	public String getBBDate() {
		return BBDate;
	}
	public void setMeigaraName(String meigaraName) {
		this.meigaraName = meigaraName;
	}
	public void setMeigaraCode(String meigaraCode) {
		this.meigaraCode = meigaraCode;
	}
	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}
	public void setBBDate(String bBDate) {
		BBDate = bBDate;
	}


}
