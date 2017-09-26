package com.cv.date;

public class Company
{
	private String id;
	private String cname;
	private String scale;
	private String nature;
	private String industry;
	private String caddress;
	private String curl;
	private String webSite;
	private String text;
	
	public Company()
	{
		super();
	}
	/**
	 * @param id 编号
	 * @param cname 公司名称
	 * @param scale 规模
	 * @param nature 公司性质
	 * @param industry 行业
	 * @param caddress 地址
	 * @param curl 连接
	 * @param webSite 官网 
	 * @author xiaowei
	 */
	public Company(String id, String cname, String scale, String nature, String industry,
			String caddress, String curl, String webSite, String text)
	{
		super();
		this.id = id;
		this.cname = cname;
		this.scale = scale;
		this.nature = nature;
		this.industry = industry;
		this.caddress = caddress;
		this.curl = curl;
		this.webSite = webSite;
		this.text = text;
	}
	public String getCname()
	{
		return cname;
	}
	public void setCname(String cname)
	{
		this.cname = cname;
	}
	public String getScale()
	{
		return scale;
	}
	public void setScale(String scale)
	{
		this.scale = scale;
	}
	public String getNature()
	{
		return nature;
	}
	public void setNature(String nature)
	{
		this.nature = nature;
	}
	public String getIndustry()
	{
		return industry;
	}
	public void setIndustry(String industry)
	{
		this.industry = industry;
	}
	public String getCaddress()
	{
		return caddress;
	}
	public void setCaddress(String caddress)
	{
		this.caddress = caddress;
	}
	public String getCurl()
	{
		return curl;
	}
	public void setCurl(String curl)
	{
		this.curl = curl;
	}
	public String getWebSite()
	{
		return webSite;
	}
	public void setWebSite(String webSite)
	{
		this.webSite = webSite;
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getText()
	{
		return text;
	}
	public void setText(String text)
	{
		this.text = text;
	}
	
	
	
}
