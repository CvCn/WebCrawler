package com.cv.crawler;

import java.util.Date;

public class CrawlStart
{
	public static int count = 0;
	public static Date d1 = new Date();
	public static void main(String[] args) throws InterruptedException
	{
//		Date d1 = new Date();

		String url = "http://sou.zhaopin.com/jobs/searchresult.ashx?jl=%E5%8C%97%E4%BA%AC&kw=java&isadv=0&sg=8a07a406368349f79d2b4100a6933e95&p=1";
		String pattern = "http://jobs.zhaopin.com/(.+?)htm";
		for(int i=100; i<=200; i+=10){
			new Thread(new ZhiLian(url, pattern, i, i+9)).start();;
			Thread.sleep(10000);
		}
//		while(true){
//			int c = count;
//			if(c == 0){
//				Date d2 = new Date();
//				long e = d2.getTime()-d1.getTime();
//				String s = new SimpleDateFormat("HH:mm:ss").format(e-8*60*60*1000);
//				System.out.println("-------end！------用时："+s+"---");
//				break;
//			}
//		}

		
		
//		Map<Thread, StackTraceElement[]> maps = Thread.getAllStackTraces();
//		while(true){
//			System.out.println(maps.size());
//		}
//		zl.tsPage("http://jobs.zhaopin.com/135536556251211.htm?ssidkey=y&ff=02&ss=101");
//		Calendar c = Calendar.getInstance();
//		System.out.println(c.toString());
//		c.set(2017, 8, 16, 16, 5, 1);
//		Timestamp ts = new Timestamp(c.getTimeInMillis());
//		System.out.println(ts);
//		System.out.println(c.getTime());
//		Date d = new Date();
//		String s1 = new SimpleDateFormat("HH:mm:ss").format(5000-8*60*60*1000);
//		System.out.println(s1);
		
//		Map<Thread, StackTraceElement[]> all = Thread.getAllStackTraces();
//		Set<Thread> ks = all.keySet();
//		System.out.println(ks);
		
//		List<Company> list = new ArrayList<>(); 
//		Job j = new Job("qwe"
//				, "ddd"
//				, "dd"
//				, c
//				, "wew"
//				, "dddae"
//				, "z"
//				, "sss"
//				, "dawr"
//				, "dggh");
//		list.add(j);
//		Company co = new Company("asad", "ddddd", "sfgh", "xcv", "s", "c");
//		Company co2 = new Company("asad1", "ddddd", "sfgh", "xcv", "s", "ce");
//		list.add(co);
//		list.add(co2);
//		OrlDate od = new OrlDate(list, "company");
//		new Thread(od).start();
		
		
//		List<String> list = new ArrayList<String>();
//		list.add(null);
//		list.add(null);
//		list.add(null);
//		list.add(null);
//		System.out.println(list);
		
//		String str = "w";
//		if(str instanceof String){
//			System.out.println(str);
//		}
	}
}
