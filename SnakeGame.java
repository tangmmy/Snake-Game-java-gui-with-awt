package snakegame;

/**
 *
 * @author tangm
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.math.*;
public class SnakeGame {
    private Frame frame=new Frame("Game");
    private final int TABLE_WIDTH=300;
    private final int TABLE_HEIGHT=400;
    private int ballCnt=1;
    private final int BALL_SIZE=10;
    /*
    private int ballX=120;
    private int ballY=60;
    private int speedY = 10;
    private int speedX= 0;
    */
    private int foodX=0;
    private int foodY=0;
    private boolean isOver=false;
    private int points=0;
    private Timer timer;
    private boolean[][] Table= new boolean[300][400];
    private class Ball{
        public int ballX,ballY,speedX,speedY;
        public Ball(){
            ballX=0;ballY=0;speedX=0;speedY=0;
        }

        public Ball(int ballX, int ballY, int speedX, int speedY) {
            this.ballX = ballX;
            this.ballY = ballY;
            this.speedX = speedX;
            this.speedY = speedY;
        }
        
        public void move(){
            ballX+=speedX;
            ballY+=speedY;
            if(ballX<=0) ballX=TABLE_WIDTH-BALL_SIZE-1;
            if(ballX>=(TABLE_WIDTH-BALL_SIZE)) ballX=0;
            if(ballY<=0) ballY=TABLE_HEIGHT-BALL_SIZE-1;
            if(ballY>=(TABLE_HEIGHT-BALL_SIZE)) ballY=0;    
        }
        public void changeSpeed(int spX,int spY){
            this.speedX=spX;
            this.speedY=spY;
        }
        public boolean collide(Ball B){
            return Math.abs(this.ballX-B.ballX)+Math.abs(this.ballY-B.ballY)<=1;
        }
    }
    //ArrayList<Ball> Snake;
    Ball[] Snake=new Ball[100];
    private class myCanvas extends Canvas{
        @Override
        public void paint(Graphics g){
            if(isOver){
                g.setColor(Color.red);
                g.setFont(new Font("Times",Font.BOLD,30));
                g.drawString("Game over",50,200);
                g.drawString("Your points: "+points, 50, 300);
            }
            else{
                g.setColor(Color.blue);
                //g.fillOval(10, 20, BALL_SIZE,BALL_SIZE);
                for(int i=0;i<ballCnt;i++){
                    g.fillOval(Snake[i].ballX, Snake[i].ballY, BALL_SIZE, BALL_SIZE);
                }
                g.setColor(Color.red);   
                g.fillOval(foodX, foodY, BALL_SIZE, BALL_SIZE);
            }
        }
    }
    myCanvas drawArea= new myCanvas();
    public void init(){
        Snake[0]= new Ball(10,20,10,0);
        /*
        Snake[1]= new Ball(26,20,10,0);
        Snake[2]= new Ball(42,20,10,0);
        Snake[3]= new Ball(58,20,10,0);
        */
        ballCnt=1;
        for(int i=0;i<300;i++){
            for(int j=0;j<400;j++){
                Table[i][j]=false;
            }
        }
        Table[10][20]=true;
        Random rand = new Random();
        foodX = rand.nextInt(300);
        foodY = rand.nextInt(400);
        KeyListener listener = new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                int keyCode = e.getKeyCode();
                if(keyCode== KeyEvent.VK_LEFT){
                    Snake[0].changeSpeed(-10, 0);
                }
                else if(keyCode==KeyEvent.VK_RIGHT){
                    Snake[0].changeSpeed(10, 0);
                }
                else if( keyCode== KeyEvent.VK_UP){
                    Snake[0].changeSpeed(0, -10);
                }
                else if(keyCode == KeyEvent.VK_DOWN){
                    Snake[0].changeSpeed(0, 10);
                }
                else if(keyCode== KeyEvent.VK_ENTER){
                    isOver=true;
                }
                
            }
        };
        frame.addKeyListener(listener);
        drawArea.addKeyListener(listener);
        ActionListener task= new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                Table[Snake[ballCnt-1].ballX][Snake[ballCnt-1].ballY]=false;
                for (int i = ballCnt-1; i > 0; i--) {
                    Snake[i].ballX = Snake[i - 1].ballX;
                    Snake[i].ballY = Snake[i - 1].ballY;
                }
                Snake[0].move();
                if(Table[Snake[0].ballX][Snake[0].ballY]){
                    isOver=true;
                }
                Table[Snake[0].ballX][Snake[0].ballY]=true;
                if(Math.abs(Snake[0].ballX-foodX)+ Math.abs(Snake[0].ballY-foodY)<=10){
                    Snake[ballCnt]=new Ball(Snake[ballCnt-1].ballX,Snake[ballCnt-1].ballY,Snake[ballCnt-1].speedX,Snake[ballCnt-1].speedX);
                    ballCnt+=1;
                    foodX = rand.nextInt(290);
                    foodY = rand.nextInt(390);
                    points+=1;
                }
                drawArea.repaint();         
            }
        };
        timer = new Timer(100,task);
        timer.start();
        drawArea.setPreferredSize(new Dimension(TABLE_WIDTH,TABLE_HEIGHT));
        frame.add(drawArea);   
        frame.pack();
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent we){
                System.exit(0);
            }
        });
    }
    public static void main(String[] args) {
        new SnakeGame().init();
    }
}
