package com.cv.date;

public class Information
{
	private Job job;
	private Company company;
	
	public Information()
	{
		super();
	}
	public Information(Job job, Company company)
	{
		super();
		this.job = job;
		this.company = company;
	}
	public Job getJob()
	{
		return job;
	}
	public void setJob(Job job)
	{
		this.job = job;
	}
	public Company getCompany()
	{
		return company;
	}
	public void setCompany(Company company)
	{
		this.company = company;
	}
	
}
