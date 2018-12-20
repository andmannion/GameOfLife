import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

/*
	Use this class to change the positioning and scaling of the board in the GUI panel.
	Obviously if you are not Andrew Mannion you will need to use different filepaths that are relative, or 
	specific to your own machine.
*/

public class ConvertFileString {
    public static final String DESIRED_VALUES_FILE = "C:\\Users\\Andrew Mannion\\Google Drive\\Stage 5\\Software Engineering\\BoardLayoutScripting\\updatedTileLayout_useBottom.txt";
    public static final String CONFIG_FILE = "C:\\Users\\Andrew Mannion\\Google Drive\\Stage 5\\Software Engineering\\BoardLayoutScripting\\GameBoardConfig.json";
    public static final String OUTPUT_FILE = "C:\\Users\\Andrew Mannion\\Google Drive\\Stage 5\\Software Engineering\\Project\\src\\main\\resources\\LogicGameBoard\\";

    public static final String XLocString="\t\t\t\t\"xLocation\": \"";
    public static final String YLocString="\t\t\t\t\"yLocation\": \"";
    public static final String XDimString="\t\t\t\t\"xDimension\": \"";
    public static final String YDimString="\t\t\t\t\"yDimension\": \"";
    public static final String TerminateLineString="\",";
    public static final String TerminateLastString=",";

    public static final String searchString = "\"gameBoardTile\": {";

    public static void main(String[] args) {

        // Read in file containing desired strings
        String fileAsString = "";
        try{
            fileAsString = new String(Files.readAllBytes(Paths.get(DESIRED_VALUES_FILE)), StandardCharsets.UTF_8);
        } catch(IOException e){
            System.err.println(e.toString());
            System.exit(-1);
        }

        ArrayList<String> lines = new ArrayList<>(Arrays.asList(fileAsString.split("\\r?\\n")));
        ArrayList<TileInfo> tileInfoList = new ArrayList<>();

        for(String line : lines){
            line = line.replace("\n", "");
            line = line.replace("\"", "");
            line = line.replaceAll("\\s+","");

            String[] separatedLine = line.split(",");
            //System.out.println("x: " + Integer.parseInt(separatedLine[0]));
            TileInfo tileInfo = new TileInfo(Double.parseDouble(separatedLine[0]), Double.parseDouble(separatedLine[1]), Double.parseDouble(separatedLine[2]), Double.parseDouble(separatedLine[3]));
            tileInfoList.add(tileInfo);
        }

        // Must get .json config file
        String configFileAsString = "";
        try{
            configFileAsString = new String(Files.readAllBytes(Paths.get(CONFIG_FILE)), StandardCharsets.UTF_8);
        } catch(IOException e){
            System.err.println(e.toString());
            System.exit(-1);
        }

        // At the nth occurrence of the string, we want to replace it with string + formatted stuff
        int i = 1;
        for (TileInfo tI : tileInfoList) {
            StringBuilder str = new StringBuilder(configFileAsString);
            int index = ordinalIndexOf(configFileAsString, searchString, i) + searchString.length();

            double yDim = tI.getyDimension();

            if(yDim == 0.5 || yDim == 1){
                yDim*=2;
            }

            // Shift the x coordinates over by 1 unit
            double xLoc = tI.getxLocation();
            xLoc = (xLoc + 0.8)/20;

            // Flip the y coordinates
            double yLoc = tI.getyLocation() + 6;

            yLoc = Math.abs(1.25-(yLoc-1+yDim)/23);

            // Insert the desired values
            String stringToInsert = "\n" + XLocString + xLoc + TerminateLineString +
                                    "\n" + YLocString + yLoc + TerminateLineString +
                                    "\n" + XDimString + tI.getxDimension()/20 + TerminateLineString +
                                    "\n" + YDimString + yDim/23 + TerminateLineString;

            str.insert(index, stringToInsert);

            i++;

            configFileAsString = str.toString();
        }

        File outputFile = new File(OUTPUT_FILE + "GameBoardConfig.json");

        try {
            FileWriter writer = new FileWriter(outputFile, false);

            writer.write(configFileAsString);
            writer.close();
        } catch(IOException e){
            System.out.println(e.toString());
        }
    }

    public static int ordinalIndexOf(String str, String substring, int n) {
        int pos = str.indexOf(substring);
        while (--n > 0 && pos != -1) {
            pos = str.indexOf(substring, pos + 1);
        }
        return pos;
    }
}

