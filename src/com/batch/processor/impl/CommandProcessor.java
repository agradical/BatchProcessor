package com.batch.processor.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.batch.Batch;
import com.batch.processor.BatchProcessor;
import com.command.CmdCommand;
import com.command.FileCommand;
import com.exceptions.ProcessException;

public class CommandProcessor implements BatchProcessor {

	@Override
	public void process() throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		
	}
	
	public void process(CmdCommand cmd) throws IOException, InterruptedException, ProcessException {
		List<String> command = new ArrayList<String>();
		command.add(cmd.getPath());
		if (cmd.getArgs() != null && !cmd.getArgs().isEmpty()) {
			for(String args : cmd.getArgs()){
				command.add(args);
			}
		}
		
		String directory = Batch.getWorkDirectory();
		String outfilepath = "";
		
		if(Batch.getFiles() != null) {
			
			FileCommand fcmd = Batch.getFiles().get(cmd.getOutFile());
			
			if(fcmd != null){
				outfilepath = fcmd.getPath();
			} else {
				System.out.println("Error Processing Batch Unable to locate FileCommand with id " + fcmd);
				throw new ProcessException(
				"Error Processing Batch Unable to locate FileCommand with id: " + fcmd);
			}
		}
		
		ProcessBuilder builder = new ProcessBuilder();
		builder.command(command);
		builder.directory(new File(directory));
		File wd = builder.directory();
		
		File outFile = new File(wd, outfilepath);
		builder.redirectOutput(outFile);

		Process process = builder.start();
		process.waitFor();
		
		System.out.println("Program Terminated! ");
	}
	
}
