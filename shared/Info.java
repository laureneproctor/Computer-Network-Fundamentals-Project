package shared;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Info
 {

    static Info instance;

    public static int NumberOfPreferredNeighbors;
    public static int UnchokingInterval;
    public static int OptimisticUnchokingInterval;
    public static String FileName;
    public static long FileSize;
    public static long PieceSize;


    private Info()
    {
        readCommonConfig();
    }

    public static Info getInstance() 
    {
        if (instance == null) 
        {
            return new Info();
        }

        return instance;
    }

    public static void readCommonConfig() 
    {
        Properties prop = new Properties();
        String file = "./project_config_file_small/Common.cfg";
        
        try (FileInputStream fis = new FileInputStream(file)) 
        {
            prop.load(fis);
        } 
        catch (FileNotFoundException ex) 
        {
            System.out.println("File not found");
        } 
        catch (IOException ex) 
        {
            System.out.println("File could not be opened");
        }

        NumberOfPreferredNeighbors = Integer.parseInt(prop.getProperty("NumberOfPreferredNeighbors"));
        UnchokingInterval = Integer.parseInt(prop.getProperty("UnchokingInterval"));
        OptimisticUnchokingInterval = Integer.parseInt(prop.getProperty("OptimisticUnchokingInterval"));
        FileName = prop.getProperty("FileName");
        FileSize = Integer.parseInt(prop.getProperty("FileSize"));
        PieceSize = Integer.parseInt(prop.getProperty("PieceSize"));
        
    }

    public static void printVariables() 
    {
        System.out.println("NumberOfPreferredNeighbors: " + NumberOfPreferredNeighbors);
        System.out.println("UnchokingInterval: " + UnchokingInterval);
        System.out.println("OptimisticUnchokingInterval: " + OptimisticUnchokingInterval);
        System.out.println("FileName: " + FileName);
        System.out.println("FileSize: " + FileSize);
        System.out.println("PieceSize: " + PieceSize);
        System.out.println();
    }
}
