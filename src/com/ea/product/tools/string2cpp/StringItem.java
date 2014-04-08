package com.ea.product.tools.string2cpp;

public class StringItem {
	private String stringID;
	private String string;
	
	public StringItem() {
		stringID = "";
		string = "";
	}

	public StringItem(String stringID,String string) {
		this.stringID = stringID;
		this.string = string;
	}
	
	public String getStringID() {
		return stringID;
	}

	public void setStringID(String stringID) {
		this.stringID = stringID;
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}
}
