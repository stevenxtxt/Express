package com.silent.model;

public class PhoneModel {

	private String	imgSrc;		// ��ʾͷ��
	private String	name;			// ��ʾ�����
	private String	sortLetters;	// ��ʾ���ƴ��������ĸ
    private String  phone;
	public String getImgSrc() {
		return imgSrc;
	}

	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
}
