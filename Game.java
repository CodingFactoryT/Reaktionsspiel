import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import javax.swing.JFrame;
import java.util.Scanner;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.Locale;

public class Game implements KeyListener{
    volatile String currentKey = "";        //volatile because KeyListener and rest of Game run in two different threads
    int rounds = 30;
    int mistakes = 0;
    boolean skipCountdown = false;          //for debugging purposes

    public Game(){
        HighScore highscore = new HighScore();
        highscore.loadHighscoreTable();

        Scanner scanner = new Scanner(System.in);

        String userName = "";
            do {
            System.out.println("Gib nun deinen Namen ein (bitte maximal 16 Zeichen, das Zeichen \",\" ist nicht erlaubt): ");

            userName = scanner.nextLine();
            } while(userName.length()  > 16 || userName.contains(","));
        
        scanner.close();
        
        Random random = new Random();
        String[] possibleOutputs = {"<", ">", "^", "v"};
        try{
            if(!skipCountdown){
                for(int i = 3; i > 0; i--){     //output countdown
                    System.out.print(i);
                    for(int j = 0; j < 4; j++){
                        Thread.sleep(200);
                        System.out.print(".");
                    }
                    Thread.sleep(200);
                }
                System.out.println("GOGOGO \n");
            }
        } catch(Exception e) {System.out.println("Something went wrong with the countdown at the beginning!");}
        
        JFrame frame = new JFrame();                //frame acts as a keylistener
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   //frame is actually closed when you press the red x on the top right corner
		frame.setTitle("KeyListener");
		frame.setResizable(false);         //frame shouldn´t be resizable because it´s only a keylistener and doesn´t act as a GUI
        frame.setVisible(true);                    //frame has to be visible in order to make the keylistener work
        frame.addKeyListener(this);
        
        long startingTime = System.currentTimeMillis();

        for(int i = 0; i < rounds; i++){
            int indexInArray = random.nextInt(4);   //output arrow that has to be pressed on the keyboard
            System.out.print(possibleOutputs[indexInArray]);
            frame.requestFocusInWindow();                 //focus the frame if the user accidentally focused the console before
            while(currentKey.equals("")){}      //wait until a key was pressed

            if((currentKey.equals("37") && indexInArray == 0) || (currentKey.equals("39") && indexInArray == 1) || (currentKey.equals("38") && indexInArray == 2) || (currentKey.equals("40") && indexInArray == 3)){
                System.out.println("\t Richtig!\n");
            } else{
                mistakes++;
                System.out.println("\t Falsch!\n");
            }

            currentKey = "";
        }
        long timeEllapsed_millis = System.currentTimeMillis() - startingTime;

        //DecimalFormat df = new DecimalFormat("#.##");
        //df.setRoundingMode(RoundingMode.DOWN);
        double timeEllapsed = Double.parseDouble(String.format(Locale.US, "%.2f", timeEllapsed_millis/1000.0));
        double timeNeeded = Double.parseDouble(String.format(Locale.US, "%.2f", timeEllapsed + mistakes*1.5));
        System.out.println("Du hast " + timeEllapsed + " Sekunden gebraucht und dabei " + mistakes + " Fehler gemacht, was eine Zeit von " + timeNeeded + " Sekunden ergibt \n\n");
        
        highscore.highscoreTable.add(Map.entry(userName,timeNeeded));

        highscore.sortTable();
        highscore.saveHighscoreTable();
        highscore.printHighscoreTable();
        
        frame.dispose();    //close window
    }
    
    @Override
	public void keyPressed(KeyEvent e) {
        currentKey = String.valueOf(e.getKeyCode());
	}

	@Override
	public void keyTyped(KeyEvent e) {}	

	@Override
	public void keyReleased(KeyEvent e){}
}
