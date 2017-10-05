package com.ndnPackage;

public class Data implements Pack ,Cloneable{
	public static final int NORMAL = 0;
	public static final int NEWDATARRESPONSE = 2;
	
	private int type = DATA;
	private int size = DATADEFAULTSIZE;
	public int dataType = NORMAL;
	private int srcAddr;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Data other = (Data) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	public Name name;
	private String content;
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	public Data(){};
	
	public int getDataType() {
		return dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	public Data(Name name){
		this.setName(name);
	}
	
	public Data(String str){
		Name name = new Name(str);
		this.setName(name);
	}
	public int getSrcAddr() {
		return srcAddr;
	}
	public void setSrcAddr(int srcAddr) {
		this.srcAddr = srcAddr;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public void setName(Name name){
		this.name = name;
	}
	public Name getName(){
		return this.name;
	}
}
