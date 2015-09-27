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

public class CommandPrcessorWithFiles implements BatchProcessor {

	@Override
	public void process() {
		// TODO Auto-generated method stub
		
	}
	
	public void process(CmdCommand cmd) throws IOException, InterruptedException, ProcessException{
		List<String> command = new ArrayList<String>();
		command.add(cmd.getPath());
		if (cmd.getArgs() != null && !cmd.getArgs().isEmpty()) {
			for(String args : cmd.getArgs()){
				command.add(args);
			}
		}
		
		String directory = Batch.getWorkDirectory();
		String outfilepath = "";
		String infilepath = "";
		
		if(Batch.getFiles() != null) {			
			FileCommand ofcmd = Batch.getFiles().get(cmd.getOutFile());
			FileCommand ifcmd = Batch.getFiles().get(cmd.getInFile());
			
			if(ofcmd != null){
				outfilepath = ofcmd.getPath();
			} else {
				System.out.println("Error Processing Batch Unable to locate FileCommand with id " + cmd.getOutFile());
				throw new ProcessException(
				"Error Processing Batch Unable to locate FileCommand with id: " + cmd.getOutFile());
			}
			
			if(ifcmd != null){
				infilepath = ifcmd.getPath();
			} else {
				System.out.println("Error Processing Batch: Unable to locate FileCommand with id: " + cmd.getInFile());
				throw new ProcessException(
				"Error Processing Batch: Unable to locate FileCommand with id: " + cmd.getInFile());
			}
			
			if (infilepath == outfilepath) {
				System.out.println("Error Processing Batch: Input and Output File cannot be same ");
				throw new ProcessException(
				"Error Processing Batch: Input and Output File cannot be same " );
			}
		} else{
			System.out.println("Error Processing Batch: No Files found to process");
			throw new ProcessException(
			"Error Processing Batch: No Files found to process");
		}
		
		ProcessBuilder builder = new ProcessBuilder();
		builder.command(command);
		builder.directory(new File(directory));
		File wd = builder.directory();
		
		File infile = new File(wd, infilepath);
		builder.redirectInput(infile);
		File outFile = new File(wd, outfilepath);
		builder.redirectOutput(outFile);

		Process process = builder.start();
		process.waitFor();
		
		System.out.println("Command Terminated! ");
	}
	
}
