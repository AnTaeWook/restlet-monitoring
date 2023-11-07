package org.example.metric.utils.systemCommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.example.PathVariables;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OsTemplateCommandUtil {
	protected final int START_TOKEN = 1;
	protected List<String> executeCommand(String[] command, String separator){
		Process process = null;
		BufferedReader reader = null;
		ProcessBuilder processBuilder = new ProcessBuilder(command);
		processBuilder.environment().put("LANG", PathVariables.getInstance().getString("lang.encoding"));
		List<String> tokens = new LinkedList<>();
		try
		{
			process = processBuilder.start();
			reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null){
				if (separator == null) {
					tokens.add(line);
					continue;
				}
				String[] strings = line.trim().split(separator);
				tokens.addAll(Arrays.asList(strings));
			}
		}
		catch (IOException e)
		{
			log.error(e.getMessage(), e);
		}finally {
			try {
				if (process != null) {
					process.destroy();
				}
				if (reader != null){
					reader.close();
				}
			}catch (IOException e){
				log.error(e.getMessage(), e);
			}

		}
		return tokens;
	}
}
