package ie.ucd.engac.fileutilities;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtilities{
	public static String GetEntireContentsAsString(String filepath) {
		byte[] encodedFileContent = new byte[0];
		
		try {
			encodedFileContent = Files.readAllBytes(Paths.get(filepath));
		} catch (Exception e) {
			System.out.println("Exception in FileUtilities.GetEntireContentsAsString() : \n" + e.toString());
		}
		
		Charset charset = Charset.defaultCharset();		
		String fileContentsAsString = new String(encodedFileContent, charset);
		
		return fileContentsAsString;
	}
}
