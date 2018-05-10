package com.mwk;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class enemysMove extends Thread {

	JLabel enemy;
	Xiagu xg;
	int speed=1;//移动速度
	int range=150;//enemy射程
	int bltDirection=0;//enemy子弹方向
	long enemyStep=500;//发射间隔
	long enemyLast=0;//上次发射
	
	public enemysMove() {}
	public enemysMove(JLabel j,Xiagu x) {
		this.enemy=j;this.xg=x;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run(){
		int x=0,y=0;
		
		while(true){
			//------------------------随机敌人
			int enemyDirection=(int)(Math.random()*4)+37;//enemy行驶方向
			int enemyState=1;//1可移动2碰撞了
			//1智能转向
			int count=(int)(Math.random()*10)+1;//随机坐标个数
			int enemy_x[]=new int[count];
			int enemy_y[]=new int[count];
			for (int i = 0; i < count; i++) {
				enemy_x[i]=(int)(Math.random()*600);
				enemy_y[i]=(int)(Math.random()*600);
			}//for
			//2 记录初始位置
			int en_x=enemy.getX();
			int en_y=enemy.getY();
			int movLe=(int)(Math.random()*300);//移动间距
			while(enemyState==1){
				//System.out.println(xg.enemyDirection);
				switch(enemyDirection){
					case 37: enemy.setIcon(new ImageIcon("images/enemy1.png")); 
					x=-speed; break;
					case 38: enemy.setIcon(new ImageIcon("images/enemy1.png")); 
					y=-speed; break;
					case 39: enemy.setIcon(new ImageIcon("images/enemy0.png")); 
					x=speed; break;
					case 40: enemy.setIcon(new ImageIcon("images/enemy0.png")); 
					y=speed; break;
				}//switch
				//enemy移动范围判断
				//判断enemy是否碰道具墙
				for (int i = 0; i < xg.Wall.length; i++) {
					//墙的矩形					进入			玩家矩形
					if(xg.Wall[i].getBounds().intersects(enemy.getBounds())){
						if(enemy.getX()>xg.Wall[i].getX())//判断人在墙的右边
							enemy.setLocation(xg.Wall[i].getX()+enemy.getWidth(), enemy.getY()+y);
						if(enemy.getX()<xg.Wall[i].getX())//判断人在墙的左边
							enemy.setLocation(xg.Wall[i].getX()-enemy.getWidth(), enemy.getY()+y);
						enemyState=2;
					}
				}//for
				enemy.setLocation(enemy.getX()+x, enemy.getY()+y);
				//判断user是否碰边框
				if(	enemy.getX()<0){
					enemy.setLocation(enemy.getX()-2*x, enemy.getY());
					enemyState=2;
				}
				if(	enemy.getY()<0){
					enemy.setLocation(enemy.getX(), enemy.getY()-2*y);
					enemyState=2;
				}
				if(	enemy.getX()>xg.getWidth()-enemy.getWidth()-20){
					enemy.setLocation(enemy.getX()-2*x, enemy.getY());
					enemyState=2;
				}
				if(	enemy.getY()>xg.getHeight()-enemy.getHeight()-40){
					enemy.setLocation(enemy.getX(), enemy.getY()-2*y);
					enemyState=2;
				}
				try {
					sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				x=0;y=0;
				
				//任意位置产生随机数转向
				for (int i = 0; i < count; i++) {
					if((enemy.getX()==enemy_x[i] || enemy.getY()==enemy_y[i]) &&
							enemyState!=2	){
						enemyDirection=(int)(Math.random()*4)+37;//移动方向
						break;
					}//if
				}
				//产生任意间距转向
				if(Math.sqrt(Math.pow((en_x-enemy.getX()), 2))>movLe) {
					enemyDirection=(int)(Math.random()*4)+37;//移动方向
					break;
				}//if
				if(Math.sqrt(Math.pow((en_y-enemy.getY()), 2))>movLe) {
					enemyDirection=(int)(Math.random()*4)+37;//移动方向
					break;
				}//if
				//--------------------------------攻击判断
				//判断user是否接近enemy
				//user中心坐标(user_x,user_x)
				int user_x=xg.User.getX()+xg.User.getWidth()/2;//user中心X坐标
				int user_y=xg.User.getY()+xg.User.getHeight()/2;//user中心Y坐标
				//enemy中心坐标(ene_x,ene_y)
				int ene_x=enemy.getX()+enemy.getWidth()/2;//enemy中心X坐标
				int ene_y=enemy.getY()+enemy.getHeight()/2;//enemy中心Y坐标
				
				//判断user进ene_y入enemy射程
				if(Math.sqrt(Math.pow((user_x-ene_x), 2)+Math.pow((user_y-ene_y), 2))<range){
					//判断炮弹方向
					if(user_x>ene_x && Math.pow((user_x-ene_x), 2)>Math.pow((user_y-ene_y), 2)){
						bltDirection=37;
					}//if
					if(user_x<ene_x && Math.pow((user_x-ene_x), 2)>Math.pow((user_y-ene_y), 2)){
						bltDirection=39;
					}//if
					if(user_y>ene_y && Math.pow((user_y-ene_y), 2)>Math.pow((user_x-ene_x), 2)){
						bltDirection=40;
						//System.out.println("向下");
					}//if
					if(user_y<ene_y && Math.pow((user_y-ene_y), 2)>Math.pow((user_x-ene_x), 2)){
						bltDirection=38;
					}//if
					enemyFire();
					continue;
				}//if
				//---------------------------------------------------------
			}//while
			try {
				sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}//while(true)
	}//run()
	
	public void enemyFire(){
		long now=System.currentTimeMillis();//当前时间
		if(now-enemyLast<enemyStep) return;//子弹间隔时间
		enemyLast=now;
		//添加子弹图片对象
		JLabel bullet=new JLabel(new ImageIcon("images/enemy_bullet.png"));
		bullet.setSize(30, 30);

		int x=0,y=0;//user子弹坐标
		switch(bltDirection){
			case 37:
				x=enemy.getX()-bullet.getWidth();
				y=enemy.getY();
				break;
			case 38:
				x=enemy.getX()+(enemy.getWidth()-bullet.getWidth())/2;
				y=enemy.getY()-bullet.getHeight();
				break;
			case 39:
				x=enemy.getX()+enemy.getWidth();
				y=enemy.getY();
				break;
			case 40:
				x=enemy.getX()+(enemy.getWidth()-bullet.getWidth())/2;
				y=enemy.getY()+enemy.getHeight();
				break;
		}//switch
	
		bullet.setLocation(x, y);
		xg.add(bullet);
		bullet.repaint();
		
		new userBulletMove(bullet,xg).start();
	}//enemyFire
	
}//enemysMove
