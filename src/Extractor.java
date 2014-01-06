import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;


public class Extractor{
	private String sourceFolderPath="";
	private String destinationFolderPath="";
	private String root="";
	private String extension="";
	private int buildSetSelected=0;
	private int renameSetSelected=-1;

	/* Build sets */
	public static String buildSet[]={"Arabic", "Asian", "North European", "West European", "South American"};
	
	/* Message strings */	
	private String messageForSource="Enter the source folder (root not defined): ";
	private String messageForDestination="Enter the destination folder (root not defined): ";
	private String messageForBuildSet="Which build set would you like to extract?\n" +
										"1. "+buildSet[0]+"\n" +
										"2. "+buildSet[1]+"\n" +
										"3. "+buildSet[2]+"\n" +
										"4. "+buildSet[3]+"\n" +
										"5. "+buildSet[4]+"";
	private String messageForRenaming="Would you like to rename the files to any of these buildsets?\n" +
										"0. No\n" +
										"1. "+buildSet[0]+"\n" +
										"2. "+buildSet[1]+"\n" +
										"3. "+buildSet[2]+"\n" +
										"4. "+buildSet[3]+"\n" +
										"5. "+buildSet[4]+"";
	
	public static void main (String args[]){
		Extractor extractor=new Extractor();
		try{
			extractor.getInformation();
		}catch(IOException e){
			System.err.println("Could not obtain information. Stopping...");
		}
		try{
			extractor.extract();
		}catch(IOException e){
			System.err.println("Could not extract files. Stopping..."+e.toString());
		}
	}
	public Extractor(){
		Properties properties=new Properties();
		try{
			properties.load(new FileInputStream("configuration.properties"));
		}catch(FileNotFoundException e){
			System.err.println("Have you misplaced the configuration properties file? Stopping...");
		}catch(IOException e){
			System.err.println("Cannot access configuration properties file. Stopping...");
		}
		root=properties.getProperty("root");
		/* Reset root */
		messageForSource="Enter the source folder (root at "+root+"): ";
		messageForDestination="Enter the destination folder (root at "+root+"): ";
		
		extension=properties.getProperty("extension");
	}
	public void getInformation() throws IOException{
		BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
		System.out.println(messageForSource);
		sourceFolderPath=root+File.separator+reader.readLine();
		if(sourceFolderPath.lastIndexOf(File.separator)==sourceFolderPath.length()-File.separator.length())
			sourceFolderPath=sourceFolderPath.substring(0, sourceFolderPath.lastIndexOf(File.separator));
		System.out.println(messageForDestination);
		destinationFolderPath=root+File.separator+reader.readLine();
		if(destinationFolderPath.lastIndexOf(File.separator)==destinationFolderPath.length()-File.separator.length())
			destinationFolderPath=destinationFolderPath.substring(0, destinationFolderPath.length()-2);
		System.out.println(messageForBuildSet);
		buildSetSelected=Integer.parseInt(reader.readLine())-1;
		/* Reset destination */
		destinationFolderPath+=File.separator+buildSet[buildSetSelected];
		System.out.println(messageForRenaming);
		renameSetSelected=Integer.parseInt(reader.readLine())-1;
	}
	public void extract() throws IOException{
		File fileDatabase=new File("buildSet"+buildSetSelected);
		BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(fileDatabase)));
		File currentFile=null;
		String nextFileName="";
		FileInputStream fileInput=null;
		File newFile=null;
		FileOutputStream fileOutput=null;
		byte buffer[]=new byte[1024];
		int renameDifference=0;
		
		System.out.println("Extracting from: "+sourceFolderPath);
		System.out.println("Extracting to: "+destinationFolderPath);
		if(renameSetSelected>=0){
			System.out.println("Renaming build set "+buildSet[buildSetSelected]+" to "+buildSet[renameSetSelected]);
			renameDifference=calculateRenamingDifference();
		}
		
		while((nextFileName=reader.readLine())!=null){
			try{
				currentFile=new File(sourceFolderPath+File.separator+nextFileName+extension);
				fileInput=new FileInputStream(currentFile);
				newFile=new File(destinationFolderPath);
				newFile.mkdirs();
				newFile=new File(destinationFolderPath+File.separator+(Integer.parseInt(nextFileName)+renameDifference)+extension);
				newFile.createNewFile();
				fileOutput=new FileOutputStream(newFile, true);
				while(fileInput.available()>0){
					if(fileInput.read(buffer)!=-1)
						fileOutput.write(buffer);
				}
			}catch(FileNotFoundException e){
				System.out.println("Did not find "+nextFileName+extension);
				continue;
			}
		}
	}
	private int calculateRenamingDifference() throws IOException{
		int difference=0;
		try{
			File originalBuildSetDatabase=new File("buildSet"+buildSetSelected);
			BufferedReader readerFromOriginal=new BufferedReader(new InputStreamReader(new FileInputStream(originalBuildSetDatabase)));
			File renameBuildSetDatabase=new File("buildSet"+renameSetSelected);
			BufferedReader readerFromRename=new BufferedReader(new InputStreamReader(new FileInputStream(renameBuildSetDatabase)));
			int firstFileFromOriginal=Integer.parseInt(readerFromOriginal.readLine());
			int firstFileFromRename=Integer.parseInt(readerFromRename.readLine());
			difference=firstFileFromRename-firstFileFromOriginal;
		}catch(FileNotFoundException e){
			System.out.println("Cannot access database files. Stopping...");
		}
		return difference;
	}
}
