package com.cv.webcrawler;

import javax.swing.JFrame;

public class Text1
{

	public static void main(String[] args)
	{
		WuBa wuBa = new WuBa();
		JFrame frame = new JFrame();
		frame.add(wuBa);
		frame.setSize(800, 700);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
	}

}
