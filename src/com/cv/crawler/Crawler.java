package com.cv.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Crawler
{
	/**
	 * 连接指定的网址
	 * @param url 需要连接的网址
	 * @return 连接到的网址源码
	 * @author xiaowei
	 */
	public String con(String url){
		URL u = null;
		try
		{
			u = new URL(url);
		} catch (MalformedURLException e1)
		{
			e1.printStackTrace();
		}
		if(url == null) return null;
		URLConnection conn = null;
		String str = null;
		BufferedReader bf = null;
		try
		{
			conn = u.openConnection();
			bf = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line = null;
			while((line = bf.readLine()) != null){
				
				if(line != null)
					str += line;
			}
			
		} catch (IOException e)
		{
			System.out.println("------------无效数据!--------------");
			return null;
		}finally{
			try
			{
				if(bf != null)
					bf.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return str;
	}
	/**
	 * 从传入的内容中进行匹配
	 * @param web 传入的网址
	 * @param pattern 匹配规则
	 * @param group 捕获组
	 * @param n 捕获次数
	 * @param pt 这个参数接受一个PrintText接口
	 * @return 所有匹配通过的字符串列表
	 * @author xiaowei
	 */
	public List<String> rex(String web, String pattern, int group, int n, PrintText pt){
		if(pattern == null || web == null) return null;
		List<String> list = new ArrayList<>();
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(web);
		for(int i=0; m.find(); i++)
		{
			if(i>=n && n>0) break;
			String str = m.group(group);
			if(pt!=null)
				pt.print(str);
			list.add(str);
		}
		return list;
	}
	/**
	 * 从传入的内容中进行匹配
	 * @param web 传入的网址
	 * @param pattern 匹配规则
	 * @param group 捕获组
	 * @param n 捕获次数
	 * @param pt 这个参数接受一个PrintText接口
	 * @return 所有匹配通过的字符串
	 * @author xiaowei
	 */
	public String _rex (String web, String pattern, int group, int n, PrintText pt){
		List<String> rex = rex(web, pattern, group, n, pt);
		if(rex==null) return null;
		for (int i = 0; i < rex.size(); i++)
		{
			if(rex.get(i)!=null) return rex.get(i);
		}
		return null;
	}

}
