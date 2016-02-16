import javax.swing.*;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
/*import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Container;
import java.awt.FlowLayout;
*/
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.KeyEvent;
//import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.FileAlreadyExistsException;
import java.util.Random;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.SwingUtilities;
import java.awt.geom.AffineTransform;
import java.lang.Math;

public class TronFrame extends JFrame implements KeyListener//, ActionListener
{
	public static void main(String[] args) throws Exception {
		TronFrame gameFrame = new TronFrame();
		while(true) {
			Thread.sleep(100);
			//System.out.println("moving!");
			gameFrame.act();
			//gameFrame.repaint();
		}
		//gameFrame.setResizable(false);
		//Thread.sleep(1000);
		//gameFrame.setResizable(false);
	}

	public int[][] cellstates;
	private TronCycle blueCycle;
	private TronCycle orangeCycle;
	// 0 = empty
	// 1 = blue
	// 2 = orange
	public static final Color blueColor = new Color(0,0,255);
	public static final Color orangeColor = new Color(255,150,0);
	private TronPanel tp;

	public TronFrame() {
		super("Tron Test");
		blueCycle = new TronCycle(10,20,2,1);
		orangeCycle = new TronCycle(30,20,4,2);
		cellstates = new int[41][41];
		addKeyListener(this);
		//this.act();
		this.act();
		this.act();
		//this.act();
		/*System.out.println("matrix:");
		for(int i = 0; i < 41; i ++) {
			for(int j = 0; j < 41; j ++) {
				if(cellstates[i][j] != 0) {
					System.out.println(cellstates[i][j]);
				}
			}
		} 
		cellstates[0][0] = 1;*/
		this.setResizable(false);
		this.setSize(400,400); // somewhat irrelevant
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container contentPane = this.getContentPane();
		tp = new TronPanel();
		contentPane.add(tp);
		this.pack();
		// cellstates: x, y
		/*for(int i = 0; i < 41; i ++) {
			for(int j = 0; j < 41; j ++) {
				//if(i==0 || j==0 || i==40 || j==40)
				//	cellstates[i][j] = 1;
				cellstates[i][j] = (3819*i*(i+1)*j*(j+2)%17)+((i+19)*(j+21))%3;
			}
		}*/
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();

		switch(code) {
			case KeyEvent.VK_UP:
				if(orangeCycle.dir != 3 && !orangeCycle.hasChangedDir) {
					orangeCycle.dir = 1;
					orangeCycle.hasChangedDir = true;
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(orangeCycle.dir != 4 && !orangeCycle.hasChangedDir) {
					orangeCycle.dir = 2;
					orangeCycle.hasChangedDir = true;
				}
				break;
			case KeyEvent.VK_DOWN:
				if(orangeCycle.dir != 1 && !orangeCycle.hasChangedDir) {
					orangeCycle.dir = 3;
					orangeCycle.hasChangedDir = true;
				}
				break;
			case KeyEvent.VK_LEFT:
				if(orangeCycle.dir != 2 && !orangeCycle.hasChangedDir) {
					orangeCycle.dir = 4;
					orangeCycle.hasChangedDir = true;
				}
				break;
			case KeyEvent.VK_W:
				if(blueCycle.dir != 3 && !blueCycle.hasChangedDir) {
					blueCycle.dir = 1;
					blueCycle.hasChangedDir = true;
				}
				break;
			case KeyEvent.VK_D:
				if(blueCycle.dir != 4 && !blueCycle.hasChangedDir) {
					blueCycle.dir = 2;
					blueCycle.hasChangedDir = true;
				}
				break;
			case KeyEvent.VK_S:
				if(blueCycle.dir != 1 && !blueCycle.hasChangedDir) {
					blueCycle.dir = 3;
					blueCycle.hasChangedDir = true;
				}
				break;
			case KeyEvent.VK_A:
				if(blueCycle.dir != 2 && !blueCycle.hasChangedDir) {
					blueCycle.dir = 4;
					blueCycle.hasChangedDir = true;
				}
				break;

			default:
				break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	public void act() {
		//cellstates[blueCycle.X][blueCycle.Y] = 1;
		blueCycle.move(cellstates);
		orangeCycle.move(cellstates);
		this.repaint();
		//tp.repaint();
	}

	private class TronCycle {
		public int X;
		public int Y;
		public int dir;
		public int color;
		public boolean hasChangedDir;
		/*
		 *       1
		 *     4-+-2
		 *       3
		 */
		public TronCycle(int initX, int initY, int initDir, int initColor) {
			X = initX;
			Y = initY;
			dir = initDir;
			color = initColor;
			hasChangedDir = false;
		}

		public void move(int[][] cells) {
			//System.out.println("mark1 val: " + cells[X][Y]);
			//System.out.println("mycolor is " + color);
			//cells[X][Y] = color;
			//System.out.println("mark2 val: " + cells[X][Y]);
			//System.out.println(color + " was at " + X + "," + Y);
			switch(dir) {
				case 1: Y--; break;
				case 2: X++; break;
				case 3: Y++; break;
				case 4: X--; break;
				default: break;
			}
			cells[X][Y] = color;
			hasChangedDir = false;
		} 
	}


	private class TronPanel extends JPanel {
		public TronPanel() {
			super();
		}

		// 41 x 41
		public void paintComponent(Graphics g) {
			//System.out.println("paintComponent called!");
			super.paintComponent(g);
			g.setColor(new Color(70,70,70));
			g.fillRect(0,0,472,472);
			g.setColor(new Color(0,0,0));
			g.fillRect(30,30,412,412);
			g.setColor(new Color(20,20,70));
			for(int i = 0; i < 42; i ++) {
				g.drawLine(30,30+10*i,441,30+10*i);
				g.drawLine(30,30+10*i+1,441,30+10*i+1);
				g.drawLine(30+10*i,30,30+10*i,441);
				g.drawLine(30+10*i+1,30,30+10*i+1,441);
			}
			int tmp;
			for(int i = 0; i < 41; i ++) {
				for(int j = 0; j < 41; j ++) {
					tmp = cellstates[i][j];
					if(tmp == 1) {
						g.setColor(blueColor);
						g.fillRect(30+10*i+2,30+10*j+2,8,8);
					}
					else if(tmp == 2) {
						g.setColor(orangeColor);
						g.fillRect(30+10*i+2,30+10*j+2,8,8);
					}
				}
			}
			//for(int i = 30; i < 371; i += 5) {
			//	g.drawLine(30,i,370,i);
			//	g.drawLine(i,30,i,370);
			//}
			//g.setColor(new Color(0,0,0));
			//g.fillRect(30,30,212,212);
		}

		public Dimension getPreferredSize() {
			return new Dimension(472,472);
		}
	}
}