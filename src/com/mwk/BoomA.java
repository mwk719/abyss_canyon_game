package com.mwk;
import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class BoomA extends Thread{
	JLabel bom;//爆炸图
	Xiagu xg;//窗体
	
	public BoomA(JLabel j,Xiagu k) {
		this.bom=j;this.xg=k;
	}
	//爆炸效果
	public void run(){
		for (int i = 1; i <= 15; i++) {
			this.bom.setIcon(new ImageIcon("images/explosion1-"+i+".png"));//循环加载爆炸图
			
			try {
				sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}//for()
		bom.setVisible(false);//爆炸图不可见
		xg.remove(bom);
	}//run()	
}
