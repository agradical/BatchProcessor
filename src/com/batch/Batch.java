package com.batch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.command.CmdCommand;
import com.command.FileCommand;
import com.command.PipeCommand;
import com.command.WdCommand;

public class Batch {

	private static String workDirectory;
	private static Map<String, FileCommand> files;
	private static Map<String, CmdCommand> commands;
	private static List<PipeCommand> pipes;
	
	public static String getWorkDirectory() {
		return workDirectory;
	}
	public static void setWorkDirectory(WdCommand workDirectory) {
		Batch.workDirectory = workDirectory.getPath();
	}
	public static Map<String, FileCommand> getFiles() {
		return files;
	}
	public static void setFiles(Map<String, FileCommand> files) {
		Batch.files = files;
	}
	public static void addFile(FileCommand file) {
		Map<String, FileCommand> files;
		if (getFiles() == null || getFiles().isEmpty()) {
			files = new HashMap<String, FileCommand>();
		} else {
			files = getFiles();
		}
		files.put(file.getId(), file);
		setFiles(files);
	}
	public static Map<String, CmdCommand> getCommands() {
		return commands;
	}
	public static void setCommands(Map<String, CmdCommand> commands) {
		Batch.commands = commands;
	}
	public static void addCommand(CmdCommand command) {
		Map<String, CmdCommand> commands;
		if(getCommands() == null || getCommands().isEmpty()) {
			commands =  new LinkedHashMap<String, CmdCommand>();
		} else {
			commands = getCommands();
		}
		commands.put(command.getId(), command);
		setCommands(commands);
	}
	public static List<PipeCommand> getPipes() {
		return pipes;
	}
	public static void setPipes(List<PipeCommand> pipes) {
		Batch.pipes = pipes;
		
	}
	public static void addPipe(PipeCommand pipe) {
		List<PipeCommand> pipes;
		if(getPipes() == null || getPipes().isEmpty()) {
			pipes = new ArrayList<PipeCommand>();
		} else {
			pipes = getPipes();
		}
		pipes.add(pipe);
		setPipes(pipes);
	}
	
	public void executeBatch() {
		
		try {
			//Executing Commands
			if (Batch.getCommands() != null && !Batch.getCommands().isEmpty()) {
				for(Map.Entry<String, CmdCommand> cmdCommand: Batch.getCommands().entrySet()) {
					cmdCommand.getValue().execute();
				}
			}
			//Executing Pipes
			if (Batch.getPipes() != null && !Batch.getPipes().isEmpty()) {
				for(int i=0; i < Batch.getPipes().size(); i++) {
					Batch.getPipes().get(i).execute();
				}
			}
			
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
}
