package wallettrial_2;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BoothUtil {
    private static final String BOOTH="WalletBooth.txt";

    public static List<Booth>readBooths(){
        List<Booth>booths=new ArrayList<>();
        try(BufferedReader reader=new BufferedReader(new FileReader(BOOTH))){
            String line;
            while((line=reader.readLine())!=null){
                booths.add(Booth.fromString(line));
            }
        }catch(FileNotFoundException e){
          e.printStackTrace();
        }catch(IOException e) {
            e.printStackTrace();
        }
        return booths;
    }

    public static boolean validateBooth(String boothNumber){
        List<Booth>booths=readBooths();
        for(Booth booth:booths){
            if(booth.getboothNumber().equals(boothNumber)){
                return true;
            }
        }
        return false;
    }
}
