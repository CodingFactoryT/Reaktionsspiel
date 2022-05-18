import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Comparator;
import java.util.Map;

public class HighScore{

    public ArrayList<Entry<String, Double>> highscoreTable = new ArrayList<>(); 

    public void printHighscoreTable(){
        System.out.println("      Name      |      Zeit      ");
        System.out.println("---------------------------------");

        for(int i = 0; i < highscoreTable.size(); i++){
            String space = "";
            String userName = highscoreTable.get(i).getKey();

            for(int j = userName.length(); j < 16; j++){
                space += " ";
            }
            System.out.println(userName + space + "|     " + highscoreTable.get(i).getValue() + " sek.");
        }

        System.out.println();
    }

    public void sortTable(){
        highscoreTable.sort(Comparator.comparing(Entry::getValue));
    }

    public void saveHighscoreTable(){
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter("highscores.txt"));
            for(int i = 0; i < highscoreTable.size(); i++){
                bw.write(highscoreTable.get(i).getKey() + "," + highscoreTable.get(i).getValue());
                bw.newLine();
            }
            bw.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void loadHighscoreTable(){
        try{
            BufferedReader br = new BufferedReader(new FileReader("highscores.txt"));
            String line = "";
            while((line = br.readLine()) != null){
                String[] KV_Pair = line.split(",");
                String userName = KV_Pair[0];
                Double timeNeeded =  Double.parseDouble(KV_Pair[1]);
                highscoreTable.add(Map.entry(userName,timeNeeded));
            }
            br.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}