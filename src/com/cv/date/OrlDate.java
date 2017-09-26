package com.cv.date;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.cv.crawler.CrawlStart;

public class OrlDate implements Runnable
{
	private List<?> list;
	private String orl;
	private Object obj;
	public OrlDate()
	{
		super();
	}

	public OrlDate(List<?> list, String orl)
	{
		super();
		this.list = list;
		this.orl = orl;
	}

	private int insertInto(List<?> list, String orl){
		if(list==null || list.size()<=0 || orl==null) return -1;
		CrawlStart.count++;
		Connection conn = null;
		Statement sta = null;
		PreparedStatement ps = null;
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "cv"
					, "123456");
			conn.setAutoCommit(false);
			if(orl.equals("company")){
				ps = conn.prepareStatement("INSERT INTO COMPANY VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)");
			}else if(orl.equals("job")){
				ps = conn.prepareStatement("INSERT INTO JOB VALUES(?, ?, ?, TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS'), ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			}
			for(Object obj:list){
				if(obj==null) return -1;
				this.obj = obj;
				int i = 1;
				if(obj instanceof Company){
					ps.setString(i++, ((Company) obj).getId());
					ps.setString(i++, ((Company) obj).getCname());
					ps.setString(i++, ((Company) obj).getScale());
					ps.setString(i++, ((Company) obj).getNature());
					ps.setString(i++, ((Company) obj).getIndustry());
					ps.setString(i++, ((Company) obj).getCaddress());
					ps.setString(i++, ((Company) obj).getCurl());
					ps.setString(i++, ((Company) obj).getWebSite());
					ps.setString(i++, ((Company) obj).getText());
				}else if(obj instanceof Job){
					ps.setString(i++, ((Job) obj).getTitle());
					ps.setInt(i++, ((Job) obj).getWagesL());
					ps.setInt(i++, ((Job) obj).getWagesH());
					ps.setString(i++, ((Job) obj).getDate());
					ps.setString(i++, ((Job) obj).getJobType());
					ps.setString(i++, ((Job) obj).getExperience());
					ps.setString(i++, ((Job) obj).getEdu());
					ps.setString(i++, ((Job) obj).getNumRecr());
					ps.setString(i++, ((Job) obj).getText());
					ps.setString(i++, ((Job) obj).getJaddress());
					ps.setString(i++, ((Job) obj).getCompany());
					ps.setString(i++, ((Job) obj).getJurl());
					ps.setString(i++, ((Job) obj).getId());
				}
				try
				{
					ps.execute();
				} catch (SQLIntegrityConstraintViolationException e)
				{
					System.out.print(e.getMessage()+"---"+",");
				}
			}
			conn.commit();
			System.out.println("---"+list.get(0).getClass().getName()+"---"+"写入完成"+"----------------");
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
			if(obj instanceof Company){
				System.out.println(((Company) this.obj).getCurl());
			}else{
				System.out.println(((Job) this.obj).getJurl());
			}
			System.exit(-1);
		}finally{
			try
			{
				if(ps != null)
				ps.close();
			} catch (SQLException e1)
			{
				e1.printStackTrace();
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
		return 0;
	}

	@Override
	public void run()
	{
		if(insertInto(list, orl)==-1){
			CrawlStart.count--;
			throw new NullPointerException();
		}
		CrawlStart.count--;
		if(CrawlStart.count == 0){
			Date d2 = new Date();
			long e = d2.getTime()-CrawlStart.d1.getTime();
			String s = new SimpleDateFormat("HH:mm:ss").format(e-8*60*60*1000);
			System.out.println("-------end！------用时："+s+"---");
		}
	}
}
