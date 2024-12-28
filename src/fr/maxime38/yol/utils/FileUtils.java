package fr.maxime38.yol.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtils {
	
	public String loadAsString(String path) {

		StringBuilder builder = new StringBuilder();
		
		ClassLoader classLoader = getClass().getClassLoader();
		
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(classLoader.getResourceAsStream(path)))){
			String line ="";
			while((line = reader.readLine()) != null) {
				builder.append(line).append("//\n");
			}
			reader.close();
		} catch (IOException e) {
			System.err.println("Couldn't find the file at "+path);
			System.exit(-1);
		}
		
		return builder.toString();
	}
}
