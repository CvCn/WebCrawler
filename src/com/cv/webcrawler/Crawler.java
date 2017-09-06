package com.cv.webcrawler;

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
	private String patternStr;
	private String web;
	private URL readurl;
	
	
	public Crawler(String url) throws MalformedURLException{
		readurl = new URL(url);
	}
	
	public void setUrl(String patternStr){
		this.patternStr = patternStr;
		web = null;
	}
	
	private synchronized void con() throws IOException{
		BufferedReader br = null;
	
		URLConnection conn = readurl.openConnection();
		conn.connect();
		//BufferedReader默认使用系统默认编码读取，这里要指明编码方式，避免乱码
		br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
		String line = null;
		while((line = br.readLine()) != null){
			
			if(line != null)
				web += line;
		}
		
	
		
	
		if(br != null)
			br.close();
			
	}
	
	public String crawler() throws IOException{
		con();
		if(web == null) return null;
		String str = null;
		Pattern compile = Pattern.compile(patternStr);
		Matcher matcher = compile.matcher(web);
		while(matcher.find()){
			str += matcher.group(1);
		}
		if(str == null)
			return null;
		else
			return str.substring(4);
	}
	public List<String> crawlerList() throws IOException{
		con();
		List<String> list = new ArrayList<String>();
		Pattern compile = Pattern.compile(patternStr);
		Matcher matcher = compile.matcher(web);
		int n = 0;
		while(matcher.find()){
			n++;
			if(n%2 == 0) continue;
			String str = matcher.group(1);
			list.add(str);
		}
		return list;
	}
}
