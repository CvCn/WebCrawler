package com.cv.webcrawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.List;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;

import com.cv.database.Home;

public class WuBa
{
	private String url;
	
	public WuBa(String url){
		this.url = url;
	}

	public void action(int start, int end){

		Connection conn = null;
		Statement sta = null;
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "cv"
					, "123456");
			conn.setAutoCommit(false);
			String patternStr = "http://";
			String url;
			String next = "href=\"" + patternStr + "(.+?)\"";
			for (int i = start; i <= end; i++)
			{
				System.out.println("============================" + "page" + i+ "============================");
				url = this.url+i;
				List<String> list = singleAll(url, next);
				int n = 1;
				int s = 1;
				for (int j = 0; j < list.size(); j++)
				{
					PreparedStatement ps = null;
					try
					{
						String str = patternStr + list.get(j);
						Home home = single(str);
						if(home == null) continue;
						ps = conn.prepareStatement("insert into home values(?, ?, ?, ?, ?, ?, ?)");
						ps.setString(1, home.getName());
						ps.setString(2, home.getPhone());
						ps.setString(3, home.getPrice());
						ps.setString(4, home.getPayType());
						ps.setString(5, home.getUrl());
						ps.setString(6, home.getUser());
						ps.setString(7, home.getArea());
						ps.executeUpdate();
					} catch (SQLIntegrityConstraintViolationException e)
					{
						System.out.println("////////////////////重复数据/////////////////////");
					}finally{
						if(ps != null)
							ps.close();
					}
					n++;
					s++;
					if(s>=50){
						conn.commit();
						s = 1;
					} 
					System.out.println("-------------"+	
							"抓取数据：" + j + "条" + 
							"----------" + "有效数据：" + n + "条" + 
							"----------" + "page:" + i + "-------------------" + "\n");
				}
				conn.commit();
			}
			System.out.println("==============end==============");
			
			
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		} catch (SQLException e)
		{
			try
			{
				if(conn != null)
					conn.rollback();
			} catch (SQLException e1)
			{
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		try
		{
			if(sta != null)
				sta.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		try
		{
			if(conn != null)
				conn.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	
	private List<String> singleAll(String url, String next){
		Crawler crawler = null;;
		List<String> list = null;
		try
		{
			crawler = new Crawler(url);
			crawler.setUrl(next);
			list = crawler.crawlerList();
		} catch (MalformedURLException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return list;
	}
	public Home single(String url){
		Crawler crawler = null;;
		try
		{
			crawler = new Crawler(url);
			Home home = new Home();
			String name = "h1 class=\"c_333 f20\">(.+?)</h1>";
			crawler.setUrl(name);
			String str;
			if((str = crawler.crawler()) != null){
				System.out.println(str);
				
				home.setName(str);
			}
			String phone = "<span class=\"house-chat-txt\">(.+?)</span>";
			crawler.setUrl(phone);
			if((str = crawler.crawler()) != null){
				System.out.println(str);
				
				home.setPhone(str);
			}
			String price = "class=\"f36\">(.+?)</b>";
			crawler.setUrl(price);
			if((str = crawler.crawler()) != null){
				System.out.println(str);
				
				home.setPrice(str);
			}
			String user = "class=\"c_000\" (.+?)</a>";
			crawler.setUrl(user);
			if((str = crawler.crawler()) != null){
				str = str.replaceAll("[^\u4e00-\u9fa5]", "");
				int n = -1;
				if((n = str.indexOf("点击查看的信用")) >=0){
					str = str.substring(n+7);
				}
				System.out.println(str);
				
				home.setUser(str);
			}
			String area = "class=\"c_333 ah\" (.+?)</a>";
			crawler.setUrl(area);
			if((str = crawler.crawler()) != null){
				if(str.length()>=780)
					str = str.substring(0, 780);
				String area2 = "\">(.+?) ";
				if((str = crawler.crawler(str, area2)) != null){
					System.out.println(str);
					
					home.setArea(str);
				}
			}
			
			//<span class="c_333">半年付</span>
			String payType = "class=\"c_333\">(.+?)</span>";
			crawler.setUrl(payType);
			if((str = crawler.crawler()) != null){
				if(str.length() >= 20) str = null;
				System.out.println(str);
				System.out.println(url);
				
				home.setPayType(str); 
				home.setUrl(url);
				return home;
			}
		} catch (MalformedURLException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return null;
			
	}
	
//	private boolean isNumeric(String str)
//	{
//		if(str == null) return false;
//		Pattern pattern = Pattern.compile("[0-9]*");
//        Matcher isNum = pattern.matcher(str);
//        if( !isNum.matches() ){
//            return false;
//        }
//        return true;
//	}
//	
//	private boolean isHttp(String str){
//		if(str == null) return false;
//		Pattern pattern = Pattern.compile("^([hH][tT]{2}[pP]:/*|[hH][tT]{2}[pP][sS]:/*|[fF][tT][pP]:/*)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+(\\?{0,1}(([A-Za-z0-9-~]+\\={0,1})([A-Za-z0-9-~]*)\\&{0,1})*)$");
//        Matcher isNum = pattern.matcher(str);
//        if( !isNum.matches() ){
//            return false;
//        }
//        return true;
//	}
}
