package com.cv.webcrawler;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;









import com.cv.database.Home;

public class WuBa extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String url;
	private JTextArea text;
	private JScrollPane scroll;
	private JTextField jUrl;
	private JTextField jStart;
	private JTextField jEnd;
	private JButton jAction;
	private JPanel panel;
	
	private static String start = null;
	private static String end = null;
	
	private static boolean state;
	
	public WuBa(){
		super();
		state = false;
		text = new JTextArea();
		text.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		text.setEditable(false);
		scroll = new JScrollPane(text);
		jUrl = new JTextField(30);
		jUrl.setText("http://bj.58.com/chuzu/pn");
		jStart = new JTextField(3);
		jStart.setText("15");
		jEnd = new JTextField(3);
		jEnd.setText("30");
		jAction = new JButton("开始");
		jAction.setEnabled(false);
		panel = new JPanel();
		panel.add(jUrl);
		panel.add(jStart);
		panel.add(jEnd);
		panel.add(jAction);
		setLayout(new BorderLayout());
		add(panel, BorderLayout.NORTH);
		add(scroll);
		
		
		jAction.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String url = jUrl.getText();
				start = jStart.getText();
				end = jEnd.getText();
				setUrl(url);
				new Thread(new Runnable()
				{
					
					@Override
					public void run()
					{
						state = true;
						action(Integer.parseInt(start), Integer.parseInt(end));
					}
				}).start();
			}
		});
		
	}
	
	private void setUrl(String url){
		this.url = url;
	}
	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		if(!jUrl.getText().trim().equals("") 
				&& !jStart.getText().trim().equals("")
				&& !jEnd.getText().trim().equals("") 
				&& isNumeric(jStart.getText()) 
				&& isNumeric(jEnd.getText())
				&& isHttp(jUrl.getText())
				&& state == false){
			jAction.setEnabled(true);
		}else{
			jAction.setEnabled(false);
		}
		repaint();
	}

	private void action(int start, int end){

		Connection conn = null;
		Statement sta = null;
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "cv"
					, "123456");
			
			//String patternStr = "http://jxjump.58.com";
			String patternStr = "http://";
			//String patternStr2 = "http://bj.58.com";
			String url;
			String next = "href=\"" + patternStr + "(.+?)\"";
			//String next2 = "href=\"" + patternStr2 + "(.+?)\"";
			state = true;
			for (int i = start; i <= end; i++)
			{
				conn.setAutoCommit(false);
				System.out.println("============================" + "page" + i+ "============================");
				text.append("============================" + "page" + i+ "============================" + "\n");
				text.setCaretPosition(text.getText().length());
				url = this.url+i;
				List<String> list = singleAll(url, next);
				int n = 0;
				for (int j = 0; j < list.size(); j++)
				{
					PreparedStatement ps = null;
					try
					{
						if(j == 200){
							conn.commit();
						}
						
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
						System.out.println("-------------------------重复数据---------------------------");
						text.append("-------------------------重复数据---------------------------" + "\n");
						text.setCaretPosition(text.getText().length());
					}finally{
						if(ps != null)
							ps.close();
					}
					System.out.println("--------------------------------"+	
							"抓取数据：" + j + "条" + 
							"----------" + "有效数据：" +  ++n + "条" + 
							"----------" + "page:" + i + "-------------------" + "\n");
					text.append("--------------------------------"+	
							"抓取数据：" + j + "条" + 
							"----------" + "有效数据：" +  ++n + "条" + 
							"----------" + "page:" + i + "-------------------" + "\n");
					text.setCaretPosition(text.getText().length());
					
				}
				conn.commit();
			}
			System.out.println("==============end==============");
			text.append("==============end==============" + "\n");
			text.setCaretPosition(text.getText().length());
			state = false;
			
			
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
			text.append(e.toString());
			text.setCaretPosition(text.getText().length());
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
			text.append(e.toString());
			text.setCaretPosition(text.getText().length());
		}
		try
		{
			if(sta != null)
				sta.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
			text.append(e.toString());
			text.setCaretPosition(text.getText().length());
		}
		try
		{
			if(conn != null)
				conn.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
			text.append(e.toString());
			text.setCaretPosition(text.getText().length());
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
			text.append(e.toString());
			text.setCaretPosition(text.getText().length());
		} catch (IOException e)
		{
			e.printStackTrace();
			text.append(e.toString());
			text.setCaretPosition(text.getText().length());
		}
		
		return list;
	}
	private Home single(String url){
		Crawler crawler = null;;
		try
		{
			crawler = new Crawler(url);
			Home home = new Home();
			//<h1 class="c_333 f20">为你而选 北苑 奥森 地铁 拂林园精装3居 业主实在配置全</h1>
			//<h1 class="c_333 f20">(单间出租)常营民族家园 2室1厅1卫 限女生(个人)</h1>
			String name = "h1 class=\"c_333 f20\">(.+?)</h1>";
			crawler.setUrl(name);
			String str;
			if((str = crawler.crawler()) != null){
				System.out.println(str);
				
				text.append(str + "\n");
				text.setCaretPosition(text.getText().length());
				home.setName(str);
			}
				
			
			//<span class="house-chat-txt">13520867687</span>
			String phone = "<span class=\"house-chat-txt\">(.+?)</span>";
			crawler.setUrl(phone);
			if((str = crawler.crawler()) != null){
				System.out.println(str);
				
				text.append(str + "\n");
				text.setCaretPosition(text.getText().length());
				home.setPhone(str);
			}

			//<b class="f36">11000</b>
			String price = "class=\"f36\">(.+?)</b>";
			crawler.setUrl(price);
			if((str = crawler.crawler()) != null){
				System.out.println(str);
				
				text.append(str + "\n");
				text.setCaretPosition(text.getText().length());
				home.setPrice(str);
			}

			//<a class="c_000" href="http://houserent.58.com/landlord/center?infoId=31306024144207" target="_blank" title="点击查看ta的信用" onclick="clickLog('from=fcpc_detail_bj_xingming')">郭女士(个人)</a>
			String user = "class=\"c_000\" (.+?)</a>";
			crawler.setUrl(user);
			if((str = crawler.crawler()) != null){
				str = str.replaceAll("[^\u4e00-\u9fa5]", "");
				int n = -1;
				if((n = str.indexOf("点击查看的信用")) >=0){
					str = str.substring(n+7);
				}
				System.out.println(str);
				
				text.append(str + "\n");
				text.setCaretPosition(text.getText().length());
				home.setUser(str);
			}
			
			
			//<a class="c_333 ah" href="/xihongmen/zufang/" target="_blank" onclick="clickLog('from=fcpc_detail_bj_shangquan0')">西红门</a>
	        //<em class="dt c_888 f12">距离地铁大兴线西红门站513米</em>
			String area = "class=\"c_333 ah\" (.+?)</a>";
			crawler.setUrl(area);
			if((str = crawler.crawler()) != null){
				str = str.replaceAll("[^\u4e00-\u9fa5]", "");
				//str = reSameStr(str);
				System.out.println(str);
				
				text.append(str + "\n");
				text.setCaretPosition(text.getText().length());
				home.setArea(str);
			}
			
			//<span class="c_333">半年付</span>
			String payType = "class=\"c_333\">(.+?)</span>";
			crawler.setUrl(payType);
			if((str = crawler.crawler()) != null){
				if(str.length() >= 20) str = null;
				System.out.println(str);
				System.out.println(url);
				
				text.append(str + "\n");
				text.append(url + "\n");
				text.setCaretPosition(text.getText().length());
				home.setPayType(str); 
				home.setUrl(url);
				return home;
			}
		} catch (MalformedURLException e)
		{
			e.printStackTrace();
			text.append(e.toString());
			text.setCaretPosition(text.getText().length());
		} catch (IOException e)
		{
			e.printStackTrace();
			text.append(e.toString());
			text.setCaretPosition(text.getText().length());
		}
		
		return null;
			
	}
	
	private boolean isNumeric(String str)
	{
		if(str == null) return false;
		Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
	}
	
	private boolean isHttp(String str){
		if(str == null) return false;
		Pattern pattern = Pattern.compile("^([hH][tT]{2}[pP]:/*|[hH][tT]{2}[pP][sS]:/*|[fF][tT][pP]:/*)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+(\\?{0,1}(([A-Za-z0-9-~]+\\={0,1})([A-Za-z0-9-~]*)\\&{0,1})*)$");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
	}
	
//	private String reSameStr(String s){
//		if (s == null)
//            return s;
//        StringBuilder sb = new StringBuilder();
//        int i = 0, len = s.length();
//        while (i < len) {
//            char c = s.charAt(i);
//            sb.append(c);
//            i++;
////            while (i < len && s.charAt(i) == c) {
////                i++;
////            }
//            for (int j = 0; j < len; j++)
//			{
//				if(s.charAt(j) == c)
//					i++;
//			}
//        }
//        return sb.toString();
//		
//	}
	
}
