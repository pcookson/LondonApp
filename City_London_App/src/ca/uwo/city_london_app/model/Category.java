package ca.uwo.city_london_app.model;

public class Category {
//	private int cid;
	private String cid;
	private String title;
	private String sequnce;
	
	public Category() {
		super();
	}

//	public Category(int cid, String title) {
	public Category(String cid, String title) {
		super();
		this.cid = cid;
		this.title = title;
	}

//	public Category(int cid, String title, String sequnce) {
	public Category(String cid, String title, String sequnce) {
		super();
		this.cid = cid;
		this.title = title;
		this.sequnce = sequnce;
	}

//	public int getCid() {
	public String getCid() {
		return cid;
	}

//	public void setCid(int cid) {
	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSequnce() {
		return sequnce;
	}

	public void setSequnce(String sequnce) {
		this.sequnce = sequnce;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return title;
	}
}
