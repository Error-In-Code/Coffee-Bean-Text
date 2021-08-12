import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileHandler {
    private static String data = "";
    private static File file;
    private static Scanner fileScanner;
    private static FileWriter fileWriter;

    public static void openFile(String filePath){
        data = "";
        try{
            file = new File(filePath);
            if(!file.createNewFile()){
                fileScanner = new Scanner(file);
                while(fileScanner.hasNextLine()){
                    data = data + fileScanner.nextLine() + "\n";
                }
            }

        }catch(FileNotFoundException e){
            System.err.println("FILE NOT FOUND");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openFile(File givenFile){
        data = "";
        try{
            file = givenFile;
            if(!file.createNewFile()){
                fileScanner = new Scanner(file);

                while(fileScanner.hasNextLine()){
                    data = data + fileScanner.nextLine() + "\n";
                }
            }


        }catch(FileNotFoundException e){
            System.err.println("FILE NOT FOUND");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getFileData(){
        return data;
    }

    public static void saveToFile(String data){
        try {
            fileWriter = new FileWriter(file);
            fileWriter.write(data);
            fileWriter.close();
        }catch(IOException e){
            System.err.println("COULD NOT WRITE TO FILE");
            e.printStackTrace();
        }
    }

}
