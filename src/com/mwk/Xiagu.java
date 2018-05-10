package com.mwk;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Xiagu extends JFrame {
	int userDirection=38;//user行驶方向
	long turStep=1000;//发射间隔
	long turLast=0;//上次发射
	long userStep=500;//发射间隔
	long userLast=0;//上次发射
	int bltDirection=37;//炮弹方向
	int range=150;//炮弹射程
	int turrentLife=100;//炮塔生命
	
	//int //user与炮塔的间隔误差
	//创建地图 加载图
	JLabel bgMap=new JLabel(new ImageIcon("images/map.png") );
	//创建自己
	JLabel User=new JLabel(new ImageIcon("images/user0.png") );
	//创建enemy
	JLabel Enemys[]={
			new JLabel(new ImageIcon("images/enemy0.png") ),
			new JLabel(new ImageIcon("images/enemy0.png") ),
			};
	//创建3个炮台
	JLabel Turrents[]={
			new JLabel(new ImageIcon("images/turrent.png")),
			new JLabel(new ImageIcon("images/turrent.png")),
			new JLabel(new ImageIcon("images/turrent.png")),
	};
	//创建炮台血量
	int turrentBlood[]=new int[3];
	//创建4个墙
	JLabel Wall[]={
			new JLabel(new ImageIcon("images/wall.png")),
			new JLabel(new ImageIcon("images/wall.png")),
			new JLabel(new ImageIcon("images/wall.png")),
			new JLabel(new ImageIcon("images/wall.png")),
	};
	
	JLabel Turrent;
	public Xiagu() {
		
		this.setTitle("深渊峡谷");
		this.setSize(517, 635);
		this.setLocation(400, 50);
		
		this.setLayout(null);//去掉窗体默认布局
		//添加user
		User.setSize(30, 75);
		User.setLocation(230, 520);
		this.add(User);
		//添加敌人
		Enemys[0].setSize(36, 60);
		Enemys[0].setLocation(230, 0);
		this.add(Enemys[0]);
		new enemysMove(Enemys[0], this).start();
		//添加加载地图图
		this.bgMap.setBounds(0, 0, 500, 600);//设置地图坐标和大小
		this.getLayeredPane().add(bgMap,new Integer(Integer.MIN_VALUE));//将bgMap放入分层面板
		((JComponent) this.getContentPane()).setOpaque(false);//设置透明
		
		//----------------------------------控制user时判断键盘输入方向
		this.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				int x=0,y=0,z=5;
				switch(e.getKeyCode()){
				 case 37:User.setIcon(new ImageIcon("images/user1.png")); userDirection=37; 
				 	x=-z;break;
				 case 38:User.setIcon(new ImageIcon("images/user1.png")); userDirection=38; 
			 		y=-z;break;
				 case 39:User.setIcon(new ImageIcon("images/user0.png")); userDirection=39; 
			 		x=z;break;
				 case 40:User.setIcon(new ImageIcon("images/user0.png")); userDirection=40; 
			 		y=z;break;
				 case 32:userFire();
				}//switch
				//user移动范围判断
				//判断user是否碰道具墙
				for (int i = 0; i < Wall.length; i++) {
					//墙的矩形					进入			user矩形
					if(Wall[i].getBounds().intersects(User.getBounds())){
						if(User.getX()>Wall[i].getX())//判断人在墙的右边
							User.setLocation(Wall[i].getX()+User.getWidth(), User.getY()+y);
						if(User.getX()<Wall[i].getX())//判断人在墙的左边
							User.setLocation(Wall[i].getX()-User.getWidth(), User.getY()+y);
					}//
				}//for
				
				//判断user是否接近炮塔
				for (int i = 0; i < Turrents.length; i++) {
					//user中心坐标(user_x,user_x)
					int user_x=User.getX()+User.getWidth()/2;//user中心X坐标
					int user_y=User.getY()+User.getHeight()/2;//user中心Y坐标
					//turrent中心坐标(tur_x,tur_y)
					int tur_x=Turrents[i].getX()+Turrents[i].getWidth()/2;//tur中心X坐标
					int tur_y=Turrents[i].getY()+Turrents[i].getHeight()/2;//tur中心Y坐标
					
					//判断人进入炮塔射程 横
					if(Math.sqrt(Math.pow((user_x-tur_x), 2)+Math.pow((user_y-tur_y), 2))<range){
						//判断炮弹方向
						if(user_x>tur_x && Math.pow((user_x-tur_x), 2)>Math.pow((user_y-tur_y), 2)){
							bltDirection=39;
						}//if
						if(user_x<tur_x && Math.pow((user_x-tur_x), 2)>Math.pow((user_y-tur_y), 2)){
							bltDirection=37;
						}//if
						if(user_y>tur_y && Math.pow((user_y-tur_y), 2)>Math.pow((user_x-tur_x), 2)){
							bltDirection=40;
							//System.out.println("向下");
						}//if
						if(user_y<tur_y && Math.pow((user_y-tur_y), 2)>Math.pow((user_x-tur_x), 2)){
							bltDirection=38;
						}//if
						Turrent=Turrents[i];
						turrentFire();
						break;
					}//if
				}//for
				//判断user是否碰边框
				User.setLocation(User.getX()+x, User.getY()+y);
				if(	User.getX()<0 || User.getY()<-5 ||
					User.getX()>Xiagu.this.getWidth()-User.getWidth()-20 ||
					User.getY()>Xiagu.this.getHeight()-User.getHeight()-30
						){
					//user回退
					User.setLocation(User.getX()-x, User.getY()-y);
				}//if
			}//keyPressed(KeyEvent e)
		});//addKeyListener
		
		//添加炮台
		for (int i = 0; i < Turrents.length; i++) {
			Turrents[i].setBounds(180*i+30, 260, 85, 75);
			this.add(Turrents[i]);
		}
		//添加上墙
		for (int i = 0; i < Wall.length/2; i++) {
			Wall[i].setBounds(180*i+145, 70, 30, 185);
			this.add(Wall[i]);
		}
		//添加下墙
		for (int i = 0; i < Wall.length/2; i++) {
			Wall[i+2].setBounds(180*i+145, 340, 30, 185);
			this.add(Wall[i+2]);
		}
		
		//------------------------------------------
		
		this.setVisible(true);
	}//Xiagu()
	
	//user子弹行驶方向
	//user开火
	public void userFire(){
		long now=System.currentTimeMillis();//当前时间
		if(now-userLast<userStep) return;//子弹间隔时间
		userLast=now;
		//添加子弹图片对象
		JLabel bullet=new JLabel(new ImageIcon("images/user_bullent.png"));
		bullet.setSize(30, 30);
		
		int x=0,y=0;//user子弹坐标
		switch(userDirection){
			case 37:
				x=User.getX()-bullet.getWidth();
				y=User.getY();
				break;
			case 38:
				x=User.getX()+(User.getWidth()-bullet.getWidth())/2;
				y=User.getY()-bullet.getHeight();
				break;
			case 39:
				x=User.getX()+User.getWidth();
				y=User.getY();
				break;
			case 40:
				x=User.getX()+(User.getWidth()-bullet.getWidth())/2;
				y=User.getY()+User.getHeight();
				break;
		}//switch
		
		bullet.setLocation(x, y);
		this.add(bullet);
		bullet.repaint();
		
		new userBulletMove(bullet,this).start();
		
	}//userFire()
	
	//炮塔开火
	public void turrentFire(){
	
		long now=System.currentTimeMillis();//当前时间
		if(now-turLast<turStep) return;//子弹间隔时间
		turLast=now;
		//添加子弹图片对象
		JLabel bullet=new JLabel(new ImageIcon("images/turrent_bullet.png"));
		bullet.setSize(50, 50);
		int x=0,y=0;//炮弹坐标
		switch(bltDirection){
			case 37:
				x=Turrent.getX()-bullet.getWidth();
				y=Turrent.getY();
				break;
			case 38:
				x=Turrent.getX()+(Turrent.getWidth()-bullet.getWidth())/2;
				y=Turrent.getY()-bullet.getHeight();
				break;
			case 39:
				x=Turrent.getX()+bullet.getWidth();
				y=Turrent.getY();
				break;
			case 40:
				//System.out.println("向下开火");
				x=Turrent.getX()+(Turrent.getWidth()-bullet.getWidth())/2;
				y=Turrent.getY()+bullet.getHeight();
				break;
		}//switch
		//System.out.println(x+"\t"+y);
		bullet.setLocation(x, y);
		this.add(bullet);
		bullet.repaint();
		
		new turrentBullentMove(bullet,this).start();
	}//turrentFire()
	
	public static void main(String[] args) {
		Xiagu xg=new Xiagu();
		//xg.turrentFire();

	}

}
