package ca.uwo.city_london_app.model;


import java.io.Serializable;


public class Parameter implements Serializable, Comparable<Parameter>{
	
	
	private static final long serialVersionUID = 2721340807561333705L;
	private String name;		
	private String value;		
	
	
	public Parameter() {
		super();
	}

	
	public Parameter(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}
	
	
	

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		return super.equals(o);
	}
	
	@Override
	public int compareTo(Parameter another) {
		// TODO Auto-generated method stub
		return 0;
	}

}

