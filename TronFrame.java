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
		gameFrame.playGame();
	}

	public int[][] cellstates;
	private TronCycle blueCycle;
	private TronCycle orangeCycle;
	// 0 = empty
	// 1 = blue
	// 2 = orange
	// 3 = death
	public static final int DEATH_COLOR = 3;
	// 4 = bluerecent
	// 5 = orangerecent
	public static final Color blueColor = new Color(0,0,255);
	public static final Color orangeColor = new Color(255,150,0);
	public static final Color blueRecentColor = new Color(50,50,255);
	public static final Color orangeRecentColor = new Color(255,180,50);
	public static final Color deathColor = new Color(200,0,0);
	private TronPanel tp;

	public TronFrame() {
		super("Tron Test");
		addKeyListener(this);
		this.setResizable(false);
		this.setSize(400,400); // somewhat irrelevant
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container contentPane = this.getContentPane();
		tp = new TronPanel();
		contentPane.add(tp);
		this.pack();
		blueCycle = new TronCycle(10,20,2,1,4);
		orangeCycle = new TronCycle(30,20,4,2,5);
		cellstates = new int[41][41];
		// cellstates: x, y
	}

	public void playGame() throws Exception {
//		playGame(25);
		playGame(75);
	}

	public void playGame(int tickspeed) throws Exception {
		this.act();
		while(blueCycle.isAlive && orangeCycle.isAlive) {
			Thread.sleep(tickspeed);
			this.act();
		}
		if(blueCycle.isAlive) {
			System.out.println("Blue Cycle wins!");
		}
		else if(orangeCycle.isAlive) {
			System.out.println("Orange Cycle wins!");
		}
		else {
			System.out.println("It's a tie!");
		}
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
		blueCycle.move(cellstates);
		orangeCycle.move(cellstates);
		this.repaint();
		if(orangeCycle.X == blueCycle.X && orangeCycle.Y == blueCycle.Y) {
			cellstates[orangeCycle.lX][orangeCycle.lY] = orangeCycle.color;
			cellstates[blueCycle.lX][blueCycle.lY] = blueCycle.color;
			cellstates[blueCycle.X][blueCycle.Y] = TronFrame.DEATH_COLOR;
			blueCycle.isAlive = false;
			orangeCycle.isAlive = false;
		}
	}

	private class TronCycle {
		public int X;
		public int Y;
		private int lX;
		private int lY;
		public int dir;
		public int color;
		public int scolor;
		public boolean hasChangedDir;
		public boolean isAlive;
		/*
		 *       1
		 *     4-+-2
		 *       3
		 */
		public TronCycle(int initX, int initY, int initDir, int rColor, int tColor) {
			X = initX;
			Y = initY;
			dir = initDir;
			color = rColor;
			scolor = tColor;
			hasChangedDir = false;
			isAlive = true;
		}

		private void moveForward() {
			switch(dir) {
				case 1: Y--; break;
				case 2: X++; break;
				case 3: Y++; break;
				case 4: X--; break;
				default: break;
			}
		}

		private void moveBackward() {
			switch(dir) {
				case 1: Y++; break;
				case 2: X--; break;
				case 3: Y--; break;
				case 4: X++; break;
				default: break;
			}
		}

		public void move(int[][] cells) {
			cells[X][Y] = color;
			lX = X;
			lY = Y;
			moveForward();
			if(X >= 0 && Y >= 0 && X < cells.length && Y < cells[X].length) {
				if(cells[X][Y] == 0) {
					cells[X][Y] = scolor;
				}
				else {	// hit trail
					//moveBackward();
					cells[lX][lY] = TronFrame.DEATH_COLOR;
					//moveForward();
					isAlive = false;
				}
			}
			else { // out of bounds
				isAlive = false;
			}
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
					else if(tmp == 3) {
						g.setColor(deathColor);
						g.fillRect(30+10*i+2,30+10*j+2,8,8);
					}
					else if(tmp == 4) {
						g.setColor(blueRecentColor);
						g.fillRect(30+10*i+2,30+10*j+2,8,8);
					}
					else if(tmp == 5) {
						g.setColor(orangeRecentColor);
						g.fillRect(30+10*i+2,30+10*j+2,8,8);
					}
				}
			}
		}

		public Dimension getPreferredSize() {
			return new Dimension(472,472);
		}
	}
}