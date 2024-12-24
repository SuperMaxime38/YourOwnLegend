package fr.maxime38.yol.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import fr.maxime38.yol.MainGame;

public class FileUtils {
	
	public static String loadAsString(String path) {

		StringBuilder builder = new StringBuilder();
		
		ClassLoader classLoader = MainGame.class.getClassLoader();
        URL resource = classLoader.getResource(path);
        
        try {
			File f = new File(resource.toURI());
			
			@SuppressWarnings("resource")
			BufferedReader reader = new BufferedReader(new FileReader(f));
			
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line).append("//\n");
			}
			
        } catch (URISyntaxException e) {
        	System.err.println("Couldn't find the file at "+path);
			System.exit(-1);
		} catch (FileNotFoundException e) {
			System.err.println("Couldn't find the file at "+path);
			System.exit(-1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
//		try (BufferedReader reader = new BufferedReader(new InputStreamReader(Class.class.getResourceAsStream(path)))){
//			String line ="";
//			while((line = reader.readLine()) != null) {
//				builder.append(line).append("//\n");
//			}
//			reader.close();
//		} catch (IOException e) {
//			System.err.println("Couldn't find the file at "+path);
//			System.exit(-1);
//		}
		
		return builder.toString();
	}
}
