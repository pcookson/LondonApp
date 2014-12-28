package ca.uwo.web.model;


public class News
{
	private int nid;
	private int cid;
	private String title;
	private String digest;
	private String body;
	private String source;
	private int commentCount;
	private String ptime;
	private String imgSrc;
	private boolean deleted;

	
	private String fromDate;
	private String toDate;
	private String address;
	private String phone;
	private String urlTitle;
	
	private String category;
	private String name;
	private double xcoord;
	private double ycoord;
	
	private String stopNumber;
	private String stopName;
	private String bus;
	
	public News()
	{
		super();
	}

	
	public News(int nid, int cid, String title, String digest, String body, String source, int commentCount, String ptime, String imgSrc, boolean deleted, String fromDate, String toDate, String address, String phone, String urlTitle, String category, String name, double xcoord, double ycoord, String stopNumber, String stopName, String bus)
	{
		super();
		this.nid = nid;
		this.cid = cid;
		this.title = title;
		this.digest = digest;
		this.body = body;
		this.source = source;
		this.commentCount = commentCount;
		this.ptime = ptime;
		this.imgSrc = imgSrc;
		this.deleted = deleted;
		
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.address = address;
		this.phone = phone;
		this.urlTitle = urlTitle;
		
		this.category = category;
		this.name = name;
		this.xcoord = xcoord;
		this.ycoord = ycoord;
		
		this.stopNumber = stopNumber;
		this.stopName = stopName;
		this.bus = bus;
	}

	public int getNid()
	{
		return nid;
	}

	public void setNid(int nid)
	{
		this.nid = nid;
	}

	public int getCid()
	{
		return cid;
	}

	public void setCid(int cid)
	{
		this.cid = cid;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getDigest()
	{
		return digest;
	}

	public void setDigest(String digest)
	{
		this.digest = digest;
	}

	public String getBody()
	{
		return body;
	}

	public void setBody(String body)
	{
		this.body = body;
	}

	public String getSource()
	{
		return source;
	}

	public void setSource(String source)
	{
		this.source = source;
	}

	public int getReplyCount()
	{
		return commentCount;
	}

	public void setCommentCount(int replyCount)
	{
		this.commentCount = replyCount;
	}

	public String getPtime()
	{
		return ptime;
	}

	public void setPtime(String ptime)
	{
		this.ptime = ptime;
	}

	public String getImgSrc()
	{
		return imgSrc;
	}

	public void setImgSrc(String imgSrc)
	{
		this.imgSrc = imgSrc;
	}

	public boolean isDeleted()
	{
		return deleted;
	}

	public void setDeleted(boolean deleted)
	{
		this.deleted = deleted;
	}
	
	public void setFromDate(String fromDate)
	{
		this.fromDate = fromDate;
	}
	
	public void setToDate(String toDate)
	{
		this.toDate = toDate;
	}
	
	public void setAddress(String address)
	{
		this.address = address;
	}
	
	public void setPhone(String phone)
	{
		this.phone = phone;
	}
	
	public String getFromDate()
	{
		return fromDate;
	}
	
	public String getToDate()
	{
		return toDate;
	}
	
	public String getAddress()
	{
		return address;
	}
	
	public String getPhone()
	{
		return phone;
	}
	
	public String getUrlTitle()
	{
		return urlTitle;
	}

	public void setUrlTitle(String urlTitle)
	{
		this.urlTitle = urlTitle;
	}
	
	public String getCategory()
	{
		return category;
	}

	public void setCategory(String category)
	{
		this.category = category;
	}
	
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	public double getXcoord()
	{
		return xcoord;
	}

	public void setXcoord(double xcoord)
	{
		this.xcoord = xcoord;
	}
	
	public double getYcoord()
	{
		return ycoord;
	}

	public void setYcoord(double ycoord)
	{
		this.ycoord = ycoord;
	}
		
	public String getStopNumber()
	{
		return stopNumber;
	}

	public void setStopNumber(String stopNumber)
	{
		this.stopNumber = stopNumber;
	}
	
	public String getStopName()
	{
		return stopName;
	}

	public void setStopName(String stopName)
	{
		this.stopName = stopName;
	}
	
	public String getBus()
	{
		return bus;
	}

	public void setBus(String bus)
	{
		this.bus = bus;
	}
	
}
