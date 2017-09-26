package com.cv.date;


public class Job
{
	private String id;
	private String jurl;
	private String title;
	private int wagesL;
	private int wagesH;
	private String date;
	private String jobType;
	private String experience;
	private String edu;
	private String numRecr;
	private String text;
	private String jaddress;
	private String company;
	
	public Job()
	{
		super();
	}
	/**
	 * @param id 编号
	 * @param url 连接
	 * @param title 标题
	 * @param wagesL 最低工资
	 * @param wagesH 最高工资
	 * @param date 日期
	 * @param jobType 工作性质
	 * @param experience 工作经验
	 * @param edu 学历
	 * @param numRecr 招聘人数
	 * @param text 正文
	 * @param address 地址
	 * @param company
	 * @author xiaowei
	 */
	public Job(String id, String jurl, String title, int wagesL, int wagesH, String date,
			String jobType, String experience, String edu, String numRecr,
			String text, String jaddress, String company)
	{
		super();
		this.id = id;
		this.jurl = jurl;
		this.title = title;
		this.wagesL = wagesL;
		this.wagesH = wagesH;
		this.date = date;
		this.jobType = jobType;
		this.experience = experience;
		this.edu = edu;
		this.numRecr = numRecr;
		this.text = text;
		this.jaddress = jaddress;
		this.company = company;
	}
	public String getJurl()
	{
		return jurl;
	}
	public void setJurl(String jurl)
	{
		this.jurl = jurl;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public String getDate()
	{
		return date;
	}
	public void setDate(String date)
	{
		this.date = date;
	}
	public String getJobType()
	{
		return jobType;
	}
	public void setJobType(String jobType)
	{
		this.jobType = jobType;
	}
	public String getExperience()
	{
		return experience;
	}
	public void setExperience(String experience)
	{
		this.experience = experience;
	}
	public String getEdu()
	{
		return edu;
	}
	public void setEdu(String edu)
	{
		this.edu = edu;
	}
	public String getNumRecr()
	{
		return numRecr;
	}
	public void setNumRecr(String numRecr)
	{
		this.numRecr = numRecr;
	}
	public String getText()
	{
		return text;
	}
	public void setText(String text)
	{
		this.text = text;
	}
	public String getJaddress()
	{
		return jaddress;
	}
	public void setJaddress(String jaddress)
	{
		this.jaddress = jaddress;
	}
	public String getCompany()
	{
		return company;
	}
	public void setCompany(String company)
	{
		this.company = company;
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public int getWagesL()
	{
		return wagesL;
	}
	public void setWagesL(int wagesL)
	{
		this.wagesL = wagesL;
	}
	public int getWagesH()
	{
		return wagesH;
	}
	public void setWagesH(int wagesH)
	{
		this.wagesH = wagesH;
	}
	
	
	
}
