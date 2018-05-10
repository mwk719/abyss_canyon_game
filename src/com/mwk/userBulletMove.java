package com.mwk;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class userBulletMove extends Thread{

	JLabel blt;//要移动的子弹
	Xiagu xg;//在哪移动
	int speed=5;//移动速度
	boolean isLive=true;//子弹是否活着
	int range=150;//子弹射程
	public userBulletMove() {}
	public userBulletMove(JLabel j,Xiagu x) {
		this.blt=j;this.xg=x;
	}//userBulletMove()
	
	
	
	public void run(){
		int x=0,y=0;
		switch(xg.userDirection){
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
			if(Math.sqrt(Math.pow((blt_x-blt.getX()), 2))>range) {isLive=false;break;}
			if(Math.sqrt(Math.pow((blt_y-blt.getY()), 2))>range) {isLive=false;break;}
			
			blt.setLocation(blt.getX()+x, blt.getY()+y);
			//判断子弹撞墙
			if(blt.getX()<0 || blt.getY()<0 ||
			   blt.getY()>xg.getHeight()-blt.getHeight()-26||
			   blt.getX()>xg.getWidth()-blt.getWidth()){
				isLive=false;
				break;
			}//if
			//判断user子弹是否碰道具墙
			for (int i = 0; i < xg.Wall.length; i++) {
				//墙的矩形					进入			玩家矩形
				if(isLive==true &&//子弹活着
						xg.Wall[i].getBounds().intersects(blt.getBounds())){
					isLive=false;
					break;
				}//
			}//for
			//判断user子弹是否碰炮塔
			for (int i = 0; i < xg.Turrents.length; i++) {
				if(isLive==true && 
						xg.Turrents[i].getBounds().intersects(blt.getBounds())){
					isLive=false;
					if (xg.turrentBlood[i]>90) {
						
						xg.Turrents[i].setVisible(false);
						break;
					}else{
						xg.turrentBlood[i]+=10;
						System.out.println((i+1)+"路炮塔血量："+(100-xg.turrentBlood[i]));
					}//if
					//添加爆炸图片
					JLabel bm=new JLabel(new ImageIcon("images/explosion1-15.png"));
					bm.setSize(128,128);
					int bm_x=blt.getX()-(bm.getWidth()-blt.getWidth())/2;
					int bm_y=blt.getY()-(bm.getHeight()-blt.getHeight())/2;
					bm.setLocation(bm_x, bm_y);
					xg.add(bm);
					bm.repaint();//重绘
					new BoomA(bm, xg).start();
					break;
				}//if
			}//for
			
			//判断user子弹是否碰enemy
			if(isLive==true && 
					xg.Enemys[0].getBounds().intersects(blt.getBounds())){
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
