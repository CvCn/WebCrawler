package com.cv.crawler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cv.date.Company;
import com.cv.date.Information;
import com.cv.date.Job;
import com.cv.date.OrlDate;

public class ZhiLian implements Runnable
{
	private String url;
	private String pattern;
	private int start;
	private int end;
	public ZhiLian(String url, String pattern, int start, int end)
	{
		super();
		this.url = url;
		this.pattern = pattern;
		this.start = start;
		this.end = end;
	}
	/**
	 * 对网址进行解析，
	 * 指定开始与结束
	 * @param start 开始页
	 * @param end 结束页
	 * @return 解析状态 0正常  -1错误
	 * @author xiaowei
	 */
	private int grap(int start, int end){
		if(start<=0 || end <=0 || start > end) return -1;
		CrawlStart.count++;
		Crawler c = new Crawler();
		for(int i=start; i<=end; i++){
			List<Job> listJob = new ArrayList<Job>();
			List<Company> listCompany = new ArrayList<Company>();
			
			System.out.println("------------page:" + i + "------------------");
			String u = url+i;
			List<String> list = c.rex(c.con(u), pattern, 0, -1, null);
			int n = 0;
			for(String s : list){
				System.out.println("------count："+ ++n +"------page:" + i + "------线程数:"+ (CrawlStart.count) +"---");
				Information infor = tsPage(s);
				Job job = infor.getJob();
				Company company = infor.getCompany();
				if(job!=null)
					listJob.add(job); 
				if(company!=null)
					listCompany.add(company);
			}
			if(listJob!=null && listJob.size()>0){
				new Thread(new OrlDate(listJob, "job")).start();
			}
			if(listCompany!=null && listCompany.size()>0){
				new Thread(new OrlDate(listCompany, "company")).start();
			}
		}
		return 0;
	}
	//具体的解析细节
	private Information tsPage(String url){
//		System.out.println(url);
		
		Job jobData = new Job();
		Company comData = new Company();
		jobData.setJurl(url);
		Crawler c = new Crawler();
//		Print pt = new Print();
		String page = c.con(url);
		//标题
		String title = "<h1>(.+?)</h1>";
		String titleStr = c._rex(page, title, 1, 1, null);
		jobData.setTitle(titleStr);
		//信息id
		String id = "var PositionExtID = \"(.+?)\"";
		String idInf = c._rex(page, id, 1, 1, null);
		jobData.setId(idInf);
		if(titleStr==idInf) return new Information(null, null);
		//公司id
		int n = idInf.indexOf('J');
		if(n>0){
			String comIdInfo = idInf.substring(0, n);
			comData.setId(comIdInfo);
		}
		//var Str_CompName = "北京喂呦科技有限公司";
		String company = "var Str_CompName = \"(.+?)\"";
		String companyStr = c._rex(page, company, 1, 1, null);
		comData.setCname(companyStr);
		jobData.setCompany(companyStr);
		//基本信息
		String inforLi = "<ul class=\"terminal-ul clearfix\">(.+?)</ul>";
		String listLi = c._rex(page, inforLi, 1,1, null);
		String inforIn = "<li>(.+?)</li>";
		List<String> listIn = c.rex(listLi, inforIn, 1, 6, null);
		String infor = "<strong>(.+?)</strong>";
		List<String> in = new ArrayList<String>();
		for(String str : listIn){
			in.add(c._rex(str, infor, 1, 1, null));
		}
		//月薪 时间 性质 经验 学历 人数
		in = infor(in);
		String wages = in.get(0);
//		System.out.println(wages);
		String w = "([0-9]+)-([0-9]+)元/月";
		String wagesL = c._rex(wages, w, 1, 1, null);
		String wagesH = c._rex(wages, w, 2, 1, null);
		try
		{
			jobData.setWagesL(Integer.valueOf(wagesL));
			jobData.setWagesH(Integer.valueOf(wagesH));
		} catch (NumberFormatException e)
		{
			jobData.setWagesL(0);
			jobData.setWagesH(0);
		}
		
		jobData.setDate(in.get(1));
		jobData.setJobType(in.get(2));
		jobData.setExperience(in.get(3));
		jobData.setEdu(in.get(4));
		jobData.setNumRecr(in.get(5));
		//正文
		String timeAg = "<!-- SWSStringCutStart -->(.+?)<!-- SWSStringCutEnd -->"
				+ "(.+?)<b>工作地址：</b>"
				+ "(.+?)<h2>(.+?)\"(.+?)\"";
		String text = c._rex(page, timeAg, 1, 1, null);
		if(text!=null){
			text = text.replaceAll("<.+?>", "");
			text = text.replaceAll("\\s*", "");
			text = text.replaceAll("&nbsp;", "");
		}
//		System.out.println(text);
		if(text!=null && text.length()>=2000) text = text.substring(0, 1999);
		jobData.setText(text);
		//地点
		String address = c._rex(page, timeAg, 4, 1, null);
		String address1 = c._rex(address, "\\b(.+?)\\b", 1, 1, null);
//		System.out.println(address1);
		jobData.setJaddress(address1);
		//公司
		String companyInfor = "<ul class=\"terminal-ul clearfix terminal-company mt20\">(.+?)</ul>";
		String compInf = c._rex(page, companyInfor, 1, 1, null);
		List<String> com = c.rex(compInf, "<strong>(.+?)</strong>", 1, 5, null);
		//规模 性质 行业 网址 地址
		com = companyInformation(com);
		comData.setScale(com.get(0));
		comData.setNature(com.get(1));
		comData.setIndustry(com.get(2));
		comData.setWebSite(com.get(3));
		comData.setCaddress(com.get(4));
		//公司连接 
		//<a rel="nofollow" href="http://company.zhaopin.com/CC425171939.htm" target="_blank">
		String companyhref = "<h5><a rel=\"nofollow\" href=\"(.+?)\"";
		String comhref = c._rex(page, companyhref, 1, 1, null);
		comData.setCurl(comhref);
		//简介
		String summary = "</h5>\\s+<p>\\s*(.+?)     ";
		String summ = c._rex(page, summary, 1, 1, null);
		if(summ!=null){
			summ = summ.replaceAll("<.+?>", "");
			summ = summ.replaceAll("\\s*", "");
			summ = summ.replaceAll("&nbsp;", "");
			summ = c._rex(summ, "\\b(.+)\\b", 1, 1, null);
		}
//		System.out.println(summ);
		if(summ!=null && summ.length()>=2000) summ = summ.substring(0, 1999);
		comData.setText(summ);
//		List<Company> list = new ArrayList<Company>();
//		list.add(comData);
//		new Thread(new OrlDate(list, "company")).start();
		return new Information(jobData, comData);
			
	}
	
	//基本信息
	private List<String> infor(List<String> in){
		Crawler c = new Crawler();
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < 6; i++)
		{
			list.add(null);
		}
		//工资
		String pr = in.get(0);
		if(pr!=null){
			pr = c._rex(pr, "(.+?)&", 1, 1, null);
			in.set(0, pr);
		}
		//发布时间
		String t = in.get(2);
		if(t!=null){
			t = c._rex(t, ">(.+?)</span>", 1, 1, null);
			t = "0002-01-01 00:00:00";
			if(t.equals("0002-01-01 00:00:00")){
				t = new SimpleDateFormat("YYYY-MM-dd").format(new Date());
				t += " 0:0:0";
			}
			in.set(2, t);
		}
		in.remove(1);
		for(int i=0; i<in.size(); i++){
			list.set(i, in.get(i));
//			System.out.println(in.get(i));
		}
		return list;
	}
	
	//公司信息
	private List<String> companyInformation(List<String> company){
		Crawler c = new Crawler();
		if(company.size()<=0) return null;
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < 5; i++)
		{
			list.add(null);
		}
		list.set(0, company.get(0));
		list.set(1, company.get(1));
		//行业
		String type = company.get(2);
		if(type!=null){
			type = c._rex(type, ">(.+?)</a>", 1, 1, null);
			list.set(2, type);
		}
		int i = 3;
		if(company.size()>4){
			//公司网址
			String url = company.get(i);
			if(url!=null){
				url = c._rex(url, "href=\"(.+?)\"", 1, 1, null);
				list.set(3, url);
			}
			i++;
		}
		//地址
		String address = company.get(i);
		if(address!=null){
			address = c._rex(address, "\\b(.+?)<br>", 1, 1, null);
			list.set(4, address);
		}
//		for(String s : list){
//			System.out.println(s);
//		}
		return list;
	}
	@Override
	public void run()
	{
		if(grap(start, end) == -1){
			CrawlStart.count--;
			throw new IllegalArgumentException("ZhiLian 参数必须大于零，并且end>=shart");
		}
		CrawlStart.count--;
	}
	
	
}
