package com.ndnPackage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Name implements Comparable<Name>{
	private String prefix;
	private String urlName;
	private String dataName;
	public static final String URLFLAG = "/prefix";
	public static final String DATANAMEFLAG = "/dataName";
	public String getUrlName() {
		return urlName;
	}

	public void setUrlName(String urlName) {
		this.urlName = urlName;
	}

	public String getDataName() {
		return dataName;
	}

	public void setDataName(String dataName) {
		this.dataName = dataName;
	}

	@Override
	public String toString() {
		return "Name [prefix=" + prefix + "]";
	}

	public Name(String prefix){
		this.prefix = prefix;
		if(prefix.startsWith(URLFLAG) && prefix.contains(DATANAMEFLAG)){
			Pattern p = Pattern.compile(URLFLAG+"(.*?)"+DATANAMEFLAG);
			Matcher m = p.matcher(prefix);
			this.setUrlName(m.group(0));
			this.setDataName(prefix.split(DATANAMEFLAG)[prefix.split(DATANAMEFLAG).length-1]);
		}
	}
	
	public boolean match(String prefix){
		if (this.prefix.equals(prefix)) return true;
		else return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((prefix == null) ? 0 : prefix.hashCode());
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
		Name other = (Name) obj;
		if (prefix == null) {
			if (other.prefix != null)
				return false;
		} else if (!prefix.equals(other.prefix))
			return false;
		return true;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	@Override
	public int compareTo(Name o) {
		// TODO Auto-generated method stub
		return this.prefix.compareTo(o.prefix);
	}
}
