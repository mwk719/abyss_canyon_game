package com.mwk;

import javax.swing.JLabel;

public class turrentBullentMove extends Thread{

	JLabel blt;//要移动的子弹
	Xiagu xg;//在哪移动
	int speed=5;//移动速度
	boolean isLive=true;//子弹是否活着
	
	
	public turrentBullentMove() {}
	public turrentBullentMove(JLabel j,Xiagu x) {
		this.blt=j;this.xg=x;
	}//turrentBullentMove()
	public void run(){
			int x=0,y=0;
			switch(xg.bltDirection){
			 case 37: x=-speed; break;
			 case 38: y=-speed; break;
			 case 39: x=speed; break;
			 case 40: y=speed; break;
			}//switch
			//记录子弹初始位置  计算射程
			int blt_x=blt.getX();
			int blt_y=blt.getY();
			while(isLive){
				 //计算射程
				if(Math.sqrt(Math.pow((blt_x-blt.getX()), 2))>xg.range) {isLive=false;break;}
				if(Math.sqrt(Math.pow((blt_y-blt.getY()), 2))>xg.range) {isLive=false;break;}
				
				blt.setLocation(blt.getX()+x, blt.getY()+y);
				//判断炮弹撞墙
				if(blt.getX()<0 || blt.getY()<0 ||
				   blt.getY()>xg.getHeight()-blt.getHeight()-26||
				   blt.getX()>xg.getWidth()-blt.getWidth()){
					isLive=false;
					break;
				}//if
				try {
					sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}//while
			blt.setVisible(false);
			xg.remove(blt);
			System.gc();
	}//run()
}
