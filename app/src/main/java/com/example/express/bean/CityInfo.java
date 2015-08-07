package com.example.express.bean;

import java.io.UnsupportedEncodingException;

/**
 * 本地数据库，数据
 * @author shufei
 *
 */
public class CityInfo {

	private String code;
	
	private String name;
	
	private String pcode;
	
	private String num;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		//return name;
		try {
			return new String(name.getBytes(),"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPcode() {
		return pcode;
	}

	public void setPcode(String pcode) {
		this.pcode = pcode;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	@Override
	public String toString() {
		return "CityInfo [code=" + code + ", name=" + name + ", pcode=" + pcode
				+ ", num=" + num + "]";
	}
}
