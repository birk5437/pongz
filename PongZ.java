/*
AUTHOR: Burke Ramsey
COURSE: CIS 297
PROJECT: PongZ.java
DUE DATE: 12/15/03
SUBMISSION DATE: n/a
URL: n/a

SUMMARY: A game that is similar
	 to the game "pong" but with
	 some elements of pinball
 	 added in.
	
INPUT: keystrokes from the user, mouse

BAD DATA CHECKING: some try/catch phrases where
			   needed

OUTPUT: animation and
	other aspects of the game.


CLASS HIERARCHY: java.lang.Object -> Component ->
		 Container -> Window -> Frame ->
                 JFrame -> PongZ

ASSUMPTIONS: none so far

NOTE: There are still some bugs to work
	   out, but overall the program works ok.
*/

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.net.*;
import java.io.*;


public class PongZ extends JFrame implements ActionListener,
       ImageObserver, ChangeListener, KeyListener, WindowListener, 
	 MouseMotionListener
{
   
  
   int strLength = 0;

   int charNum = 1;
   
   File file = new File("high");


   int delayCounter = 5; 
   int score = 0;

   FileReader reader;

   String sqColor2 = "green";
   String sqColor = "green";	

   JComboBox musicMenu = new JComboBox();
   JComboBox levelMenu = new JComboBox();   

   BufferedImage bg = new BufferedImage
      (700,500, BufferedImage.TYPE_INT_RGB);
   Graphics imgGraphics = bg.createGraphics();

   int xChange = 5;
   int yChange = 5;
   JFrame gameFrame = new JFrame();
   JFrame helpFrame = new JFrame("help");
   JPanel helpPanel = new JPanel();
   JTextArea controlHelp = new JTextArea("", 10, 25);

   JSlider speedSlider = new JSlider(1,10,10);

   AudioClip paddleSnd;
   AudioClip mario3Underworld;
   AudioClip michaelJ;
   AudioClip prince;
   AudioClip marioMain;
  
   JButton snd = new JButton("Play Music");
   boolean paused = false;
   JComboBox helpTopics = new JComboBox();
   JScrollPane scrollPane = new JScrollPane(controlHelp);
   AnimationPane aniPane = new AnimationPane();
   Timer timer;

   JFrame highFrame;   
   JTextArea highText = new JTextArea("");


   public PongZ()
   {


      try
      {
         reader = new FileReader(file);
      }
      catch (IOException io){}

      
      controlHelp.setWrapStyleWord(true);

	JFrame loadFrame = new JFrame();
	highFrame = new JFrame("High Scores");
	JPanel highPanel = new JPanel();
	highFrame.setSize(300,300);
	highPanel.setLayout(new GridLayout(2,1));
	highFrame.getContentPane().add(highPanel);
	JLabel highLabel = new JLabel("    ---High Scores---");
	highLabel.setFont(new Font("font1", 1, 27));
	highPanel.add(highLabel);
	highPanel.add(highText);
	JPanel loadPanel = new JPanel();	
	JLabel loadLabel = new JLabel("Loading...");
	loadLabel.setFont(new Font("font", 1, 24));
	loadPanel.add(loadLabel);
	loadFrame.getContentPane().add(loadPanel);
	loadFrame.setSize(150,150);	
	loadFrame.setVisible(true);
	

      
      try
      {
         prince = Applet.newAudioClip(new URL("file:prince_rasberryBeret.mid"));
	   paddleSnd = Applet.newAudioClip(new URL("file:click_x.wav"));
	   mario3Underworld = Applet.newAudioClip(new URL("file:mario3under.mid"));
	   michaelJ = Applet.newAudioClip(new URL("file:mj.mid"));
	   marioMain = Applet.newAudioClip(new URL("file:mariomain.mid"));
	   
      }//try
 
      catch (MalformedURLException e) {}

      speedSlider.addChangeListener(this);	

	snd.addActionListener(this);

      
      timer = new Timer(10, this);
	
	JPanel controlPanel2 = new JPanel();
	JPanel pausePanel = new JPanel();
	JPanel gamePanel = new JPanel();
	gamePanel.setLayout(new BorderLayout());
	JPanel controlPanel = new JPanel();
	gamePanel.add(aniPane, BorderLayout.CENTER);
     	JButton pause = new JButton("Pause/Un-Pause");
        JButton mainMenu = new JButton("Main Menu");
	mainMenu.addActionListener(this);
	mainMenu.addKeyListener(this);
	pause.addActionListener(this);
	pause.addKeyListener(this);
	pausePanel.add(pause);
	pausePanel.add(mainMenu);
	gamePanel.add(pausePanel, BorderLayout.SOUTH);
	gameFrame.addMouseMotionListener(this);
	gameFrame.getContentPane().add(gamePanel);	
        gameFrame.setSize(700,525);
	gameFrame.setVisible(false);
	gameFrame.addWindowListener(this);
	gameFrame.setTitle("PongZ Game Window");
	

	
      ImageIcon title = new ImageIcon("title.jpg");

      controlHelp.setLineWrap(true);
      
      helpTopics.addActionListener(this);
      helpTopics.addItem("About");
      helpTopics.addItem("Controls");
      helpTopics.addItem("Scoring");

      levelMenu.addItem("one");
      levelMenu.addItem("two");
	levelMenu.addItem("three");      
      
      JButton start = new JButton("New Game");
      start.addActionListener(this);
      JButton help = new JButton("Help");
      help.addActionListener(this);
      JButton exit = new JButton("Exit");
      exit.addActionListener(this);
       
      
      JPanel picture = new JPanel();
      picture.add(new JLabel(title));
     
      JPanel top = new JPanel();
      top.add(new JLabel("By: Burke Ramsey"));      
            
      JPanel middle = new JPanel();
      middle.add(new JLabel("speed: "));
      middle.add(new JLabel("1"));
      middle.add(speedSlider);
      middle.add(new JLabel("10"));
      middle.add(new JLabel("    Level: "));
      middle.add(levelMenu);

      

      JPanel low = new JPanel();
	musicMenu.addItem("Prince - Rasberry Beret");
	musicMenu.addItem
		("Michael Jackson - Don't Stop 'Till You Get Enough");
	musicMenu.addItem("Super Mario 3 - Underground Theme");
	musicMenu.addItem("Super Mario Bros. - Classic Theme");
	low.add(musicMenu);
      
     
      JPanel bottom = new JPanel();
      JButton highScores = new JButton("High Scores");
      highScores.addActionListener(this);
      bottom.add(start);
	bottom.add(snd);
      bottom.add(help);
      bottom.add(highScores);
      bottom.add(exit);
      
               
   
      JPanel main = new JPanel();
      main.setLayout(new GridLayout(6,3));
      main.add(picture);
      main.add(top);
      main.add(middle);
      main.add(low);
      main.add(bottom);

      getContentPane().add(main);
   
      
      helpPanel.add(new JLabel("Help Topics:"));
      helpPanel.add(helpTopics);
      helpPanel.add(scrollPane);
      scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
      scrollPane.setVisible(false);
      helpFrame.setContentPane(helpPanel);
	loadFrame.setVisible(false);
    }//PongZ



   public void mouseDragged (MouseEvent e) {}

   public void mouseMoved (MouseEvent e)
   {
      aniPane.paddleXPos = e.getX();
   }//MouseMoved



   public void windowActivated (WindowEvent e) 
   {
      timer.restart();
   }//windowActivated

   public void windowClosed (WindowEvent e) {}
   public void windowClosing (WindowEvent e) {}

   public void windowDeactivated (WindowEvent e)
   {
      timer.stop();
   }//windowDeactivated

   public void windowDeiconified (WindowEvent e) {}
   public void windowIconified (WindowEvent e) {}
   public void windowOpened (WindowEvent e) {}


   public String readFile()
   {
      try
      {
         while(reader.read() != -1)
	 {
	    strLength++;
	 }//while

	 reader.close();

      }//try

      catch(IOException ioe){}

      char ch[];
      ch = new char[strLength];
	 
      try
      {
         FileReader reader2 = new FileReader(file);
	 reader2.read(ch);
	 reader2.close();
      }//try

      catch(IOException ioe){}
      String str;
      str = new String(ch);
      return str;
   }//readFile




   public void colorChange()
   {
      if (sqColor == "green")
	{
	   imgGraphics.setColor(Color.BLUE);
	   sqColor = "not green";
	}//if

	else
	{
	   imgGraphics.setColor(Color.GREEN);
	   sqColor = "green";
	}//else
   }//colorChange

   public void colorChange2()
   {
      if (sqColor2 == "green")
	{
	   imgGraphics.setColor(Color.BLUE);
	   sqColor2 = "not green";
	}//if

	else
	{
	   imgGraphics.setColor(Color.GREEN);
	   sqColor2 = "green";
	}//else
   }//colorChange2


   public void checkBallPosition()
   {
      if ((aniPane.xPos == 105) &
	   (aniPane.yPos >= 75 & aniPane.yPos <= 175))
	{
	   xChange = xChange * -1;
	   colorChange();
	   score = score + (timer.getDelay() - 212) * -1;
	   imgGraphics.fillRect(100,75,100,100);
	}//if

	else if ((aniPane.xPos == 195) &
		  (aniPane.yPos >= 75) & (aniPane.yPos <= 175))
	{
	   xChange = xChange * -1;
	   colorChange();
	   score = score + (timer.getDelay() - 212) * -1;
	   imgGraphics.fillRect(100,75,100,100);
	}//else if

	else if ((aniPane.yPos == 75) &
		  (aniPane.xPos >= 100 & aniPane.xPos <= 200))
	{
	   yChange = yChange * -1;
	   colorChange();
	   score = score + (timer.getDelay() - 212) * -1;
	   imgGraphics.fillRect(100,75,100,100);
	}//else if

	else if ((aniPane.yPos == 175) &
		  (aniPane.xPos >= 100 & aniPane.xPos <= 200))
	{
	   yChange= yChange * -1;
	   colorChange();
	   score = score + (timer.getDelay() - 212) * -1;
	   imgGraphics.fillRect(100,75,100,100);
	}//else if



	else if ((aniPane.xPos == 495) &
		  (aniPane.yPos >= 75) & (aniPane.yPos <= 175))
	{
	   xChange = xChange * -1;
	   colorChange2();
	   score = score + (timer.getDelay() - 212) * -1;
	   imgGraphics.fillRect(500,75,100,100);
	}//else if

	else if ((aniPane.xPos == 595) &
		  (aniPane.yPos >= 75) & (aniPane.yPos <= 175))
	{
	   xChange = xChange * -1;
	   colorChange2();
	   score = score + (timer.getDelay() - 212) * -1;
	   imgGraphics.fillRect(500,75,100,100);
	}//else if

	else if ((aniPane.yPos == 75) &
		  (aniPane.xPos >= 500) & (aniPane.xPos <= 600))
	{
	   yChange = yChange * -1;
	   colorChange2();
	   score = score + (timer.getDelay() - 212) * -1;
	   imgGraphics.fillRect(500,75,100,100);
	}//else if

	else if ((aniPane.yPos == 175) &
		  (aniPane.xPos >= 500 & aniPane.xPos <= 600))
	{
	   yChange= yChange * -1;
	   colorChange2();
	   score = score + (timer.getDelay() - 212) * -1;
	   imgGraphics.fillRect(500,75,100,100);
	}//else if
   }//checkBallPosition

   
   public void checkBallPosition2()
   {
      if ((aniPane.xPos == 295) & (aniPane.yPos >= 95) & (aniPane.yPos <= 245))
      {
         xChange = xChange * -1;
	}//if

	else if ((aniPane.xPos == 395) & (aniPane.yPos >= 95) & (aniPane.yPos <= 245))
	{
	   xChange = xChange * -1;
	}//else if
   
      else if ((aniPane.yPos == 95) & (aniPane.xPos >= 300) & (aniPane.xPos <= 400))
      {
         yChange = yChange * -1;
	}//else if

      else if ((aniPane.yPos == 245) & (aniPane.xPos >= 300) & (aniPane.xPos <= 400))
      {
         yChange = yChange * -1;
      }//else if
   }//checkBallPosition2
	
   

   public void actionPerformed(ActionEvent e)
   {
      if(e.getActionCommand() == "Help")
      {
         helpFrame.setSize(300,300);
         helpFrame.setVisible(true);
      }//if

        
      else if(e.getActionCommand() == "Main Menu")
      {
         gameFrame.setVisible(false);
	 timer.stop();
      }//else if


      else if(e.getActionCommand() == "High Scores")
      {

         highText.setText(readFile());
	 highFrame.setVisible(true);
      

      }//else if


      else if(e.getActionCommand() == "New Game")
      {
	   xChange = 5;
	   yChange = 5;
	   score = 0;
	   aniPane.xPos = 75;
	   aniPane.yPos = 25;
	   aniPane.paddleXPos = 75;
         if (levelMenu.getSelectedIndex() == 0)
	 {
          imgGraphics.setColor(Color.BLACK);
	    imgGraphics.fillRect(0, 0, 700, 500);
	    imgGraphics.setColor(Color.GREEN);
	    imgGraphics.fillRect(100,75,100,100);
	    imgGraphics.fillRect(500,75,100,100);
	 }//if
	

 	 else if(levelMenu.getSelectedIndex() == 2)
	 {
	    imgGraphics.setColor(Color.BLACK);
	    imgGraphics.fillRect(0,0,700,500);
	    imgGraphics.setColor(Color.GREEN);
	    imgGraphics.fillRect(100,75,100,100);
	    imgGraphics.fillRect(500,75,100,100);
	    imgGraphics.setColor(Color.ORANGE);
	    imgGraphics.fillRect(300,95,100,150);
	    imgGraphics.setColor(Color.GREEN);
	    imgGraphics.fillRect(0,305,150,20);
	    imgGraphics.fillRect(550, 305, 100, 20);
	 }//else if

	 else
	 {
	    imgGraphics.setColor(Color.BLACK);
	    imgGraphics.fillRect(0, 0, 700, 500);
	    imgGraphics.setColor(Color.GREEN);
	    imgGraphics.fillRect(100,75,100,100);
	    imgGraphics.fillRect(500,75,100,100);
	    imgGraphics.setColor(Color.ORANGE);
	    imgGraphics.fillRect(300,95,100,150);
	    imgGraphics.setColor(Color.GREEN);
	 }//else
          gameFrame.setVisible(true);
          timer.start();	 
	 }//else if
      

	
      else if(e.getSource() instanceof Timer)
      {
	   if (delayCounter == 5)
	   {
		imgGraphics.setColor(Color.BLACK);
		imgGraphics.fillRect(0,390,100,10);
		imgGraphics.setColor(Color.YELLOW);
		imgGraphics.drawString("Score: " + score, 0, 400);
		delayCounter = -1;
	   }//if

	   delayCounter++;

         if (aniPane.yPos <= 5)
            yChange = yChange * -1;
	

	   else if (aniPane.yPos >= 425)
	   {
		imgGraphics.setColor(Color.RED);
		imgGraphics.drawString("GAME OVER", 300, 170);
	      aniPane.repaint();
	  	timer.stop();
	   }//else if

         else if (aniPane.xPos >= 689 || aniPane.xPos <= 5)
	   {
	      xChange = xChange * -1;
	   }//else if

	   switch (levelMenu.getSelectedIndex())
	   {
	      case 0:
		   checkBallPosition();
		break;
		
		case 1:
		   checkBallPosition();
		   checkBallPosition2();
		break;

		case 2:
		   checkBallPosition();
		   checkBallPosition2();
		   if ((aniPane.yPos == 300) & (aniPane.xPos >= 0) & (aniPane.xPos <= 150))
		   {
		      yChange = yChange * -1;
			if (aniPane.yPos % 10 == 0)
			{
			   aniPane.yPos = aniPane.yPos + yChange;
			   yChange = 5;
			   yChange += 5;
			}//if			
			
			else
			{
			   aniPane.yPos = aniPane.yPos + yChange;
			   yChange = 5;
			   yChange += 5;
			}//else
	
			
		   }//if
		
		   else if ((aniPane.yPos == 325) & (aniPane.xPos >= 0) & (aniPane.xPos <= 150))
		   {
			yChange = yChange * -1;
		   }//else if

		   else if ((aniPane.xPos == 150) & (aniPane.yPos >= 300) & (aniPane.yPos <= 325))
		   {
			xChange = xChange * -1;
		   }//else if

		   else if ((aniPane.yPos == 305) & (aniPane.xPos >= 550) & (aniPane.xPos < 650))
		   {
			yChange = yChange * -1;
		   }//else if
	 	break;

	   }//switch


	   if ((aniPane.yPos >= 355) & (aniPane.yPos <= 365) &
		(aniPane.xPos >= aniPane.paddleXPos)
		& (aniPane.xPos <= aniPane.paddleXPos + 20))
	   {
		if (xChange == 5)
		{
		   xChange = xChange * -1;
   		   yChange = yChange * -1;
		}//if
		
		else
		   yChange = yChange * - 1;
	      paddleSnd.play();
	   }//if

	   else if ((aniPane.yPos >= 355) & (aniPane.yPos <= 365) &
			   (aniPane.xPos <= aniPane.paddleXPos + 50)
			  &(aniPane.xPos > aniPane.paddleXPos + 30))

	   {
		if (xChange == -5)
		{
		   xChange = xChange * -1;
		   yChange = yChange * -1;
		}//if

		else
		   yChange = yChange * -1;

	      paddleSnd.play();
	   }//else if


	   else if ((aniPane.yPos >= 355 & aniPane.yPos <= 365)
		     &(aniPane.xPos >= aniPane.paddleXPos)
		     &(aniPane.xPos <= aniPane.paddleXPos + 50))
                    {

   	      yChange = yChange * -1;
	      paddleSnd.play();
	  }



	   aniPane.xPos = aniPane.xPos + xChange;
	   aniPane.yPos = aniPane.yPos + yChange;
         aniPane.repaint();
      }//else if

      

	else if (e.getActionCommand() == "Pause/Un-Pause")
	{
	   if (paused)
	   {
		paused = false;
		timer.restart();
	   }//if

	   else
	   {
		paused = true;
		timer.stop();
	   }//else
	}//else if

	else if (e.getActionCommand() == "Stop Music")
	{
         prince.stop();
	   paddleSnd.stop();
	   mario3Underworld.stop();
	   michaelJ.stop();
	   marioMain.stop();
	   snd.setText("Play Music");
	   
	}//else if

	else if (e.getActionCommand() == "Play Music")
	{
	   switch (musicMenu.getSelectedIndex())
	   {

		case 0:
		   prince.loop();
	         snd.setText("Stop Music");
		break;
		
		case 1:
		   michaelJ.loop();
		   snd.setText("Stop Music");
		break;

		case 2:
		   mario3Underworld.loop();
		   snd.setText("Stop Music");
		break;

		case 3:
		   marioMain.loop();
		   snd.setText("Stop Music");
		break;
	   }//switch
	}//else if


	else if (helpTopics.getSelectedIndex() == 0)
	{
         scrollPane.setVisible(true);
	   controlHelp.setVisible(true);
	   helpFrame.pack();
         helpFrame.setSize(300,300);
	   controlHelp.setText("");
	   controlHelp.append("          PongZ");
	   controlHelp.append("\n      By Burke Ramsey");
	   controlHelp.append("\nbirk5437@hotmail.com");
	}//else if

      else if (helpTopics.getSelectedIndex() == 1)
      {   
         
         scrollPane.setVisible(true);
	   controlHelp.setVisible(true);
	   helpFrame.pack();
         helpFrame.setSize(300,300);
	   controlHelp.setText("");
	   controlHelp.append("Left Arrow = Move Paddle Left");
	   controlHelp.append("\nRight Arrow = Move paddle right");
	   controlHelp.append("\n*Note: you can also move the mouse");
	   controlHelp.append(" to move the paddle as long as the mouse");
	   controlHelp.append(" is within the game window.");
      }//else if        
        
      else
      {
 	   scrollPane.setVisible(true);
	   controlHelp.setVisible(true);
	   helpFrame.pack();
         helpFrame.setSize(300,300);
	   controlHelp.setText("");
         controlHelp.append("Points are added to your ");
	   controlHelp.append("score when the ball bounces");
	   controlHelp.append("off of certain objects.  ");
         controlHelp.append("The ammount of points added");
	   controlHelp.append(" is based on the speed set by");
	   controlHelp.append(" the user.");
	}//else

      if(e.getActionCommand() == "Exit")
         System.exit(0);	   
            
      
   }//actionPerformed



   public void keyPressed(KeyEvent key)
   {
      switch (key.getKeyCode())
	{
	   case 37:
	      aniPane.paddleXPos -= 20;
	   break;
	   
	   case 39:
		aniPane.paddleXPos += 20;
	   break;
	}//switch
   }//keyPressed

   public void keyTyped(KeyEvent key) {}
   public void keyReleased(KeyEvent key) {}


   public void stateChanged(ChangeEvent e)
   {
	timer.setDelay(((12 - speedSlider.getValue()) * 10) - 20);
   }//stateChanged
   

   private class AnimationPane extends JPanel
   {
      int xPos = 75;
      int paddleXPos = 75;
      int yPos = 25;
	
      public void paintComponent(Graphics g)
      {
         super.paintComponent(g);
	   g.setColor(Color.RED);
	   g.drawImage(bg, 0, 0, this);
	   g.fillOval(xPos, yPos, 5, 5);
	   g.setColor(Color.BLUE);
	   g.fillRect(paddleXPos, 360, 50, 10);
      }//paintComponent

   }//AnimationPane


   


   public static void main(String args[])
   {
    
      PongZ frame = new PongZ();  
      frame.setSize(450,450);
      frame.setVisible(true);
	frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
	frame.setTitle("Main Menu");

   }//main
}//PongZ