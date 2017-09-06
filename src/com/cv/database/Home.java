package com.cv.database;

/**
 * @author xiaowei
 *
 */
public class Home
{
	private String name;//100
	private String phone;//15
	private String price;//10
	private String payType;//20
	private String url;//4000
	private String user;//4000;
	private String area;//100
	public Home()
	{
		super();
	}
	public Home(String name, String phone, String price, String payType, String url, String user, String area)
	{
		super();
		this.name = name;
		this.phone = phone;
		this.price = price;
		this.payType = payType;
		this.url = url;
		this.user = user;
		this.area = area;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getPhone()
	{
		return phone;
	}
	public void setPhone(String phone)
	{
		this.phone = phone;
	}
	public String getPrice()
	{
		return price;
	}
	public void setPrice(String price)
	{
		this.price = price;
	}
	public String getPayType()
	{
		return payType;
	}
	public void setPayType(String payType)
	{
		this.payType = payType;
	}
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
	public String getUser()
	{
		return user;
	}
	public void setUser(String user)
	{
		this.user = user;
	}
	public String getArea()
	{
		return area;
	}
	public void setArea(String area)
	{
		this.area = area;
	}
	
	
	
}
