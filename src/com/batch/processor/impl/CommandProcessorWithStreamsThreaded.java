package com.batch.processor.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.batch.Batch;
import com.batch.processor.BatchProcessor;
import com.command.CmdCommand;
import com.command.FileCommand;
import com.command.PipeCommand;
import com.exceptions.ProcessException;

public class CommandProcessorWithStreamsThreaded implements BatchProcessor {
	
	@Override
	public void process() {
		// TODO Auto-generated method stub
		
	}
	
	public void process(PipeCommand pcmd) throws ProcessException, IOException, InterruptedException {
	
		List<CmdCommand> cmd = pcmd.getCommandSet();
		String directory = Batch.getWorkDirectory();

		List<String> inCommand = new ArrayList<String>();
		List<String> outCommand = new ArrayList<String>();
		
		CmdCommand inCmd = null;
		CmdCommand outCmd = null;
		
		for(CmdCommand _cmd : cmd) {	

			List<String> command = new ArrayList<String>();
			command.add(_cmd.getPath());		
			if (_cmd.getArgs() != null && !_cmd.getArgs().isEmpty()) {
				for(String args : _cmd.getArgs()){
					command.add(args);
				}
			}
			
			if(_cmd.getInFile() != null && !_cmd.getInFile().isEmpty()){
				inCommand = command;
				inCmd = _cmd;
			}
			if(_cmd.getOutFile() != null && !_cmd.getOutFile().isEmpty()){
				outCommand = command;
				outCmd = _cmd;
			}

		}
		
		if(inCmd == null || outCmd == null) {
			throw new ProcessException("Improper Pipe Configuration");
		}
		
		ProcessBuilder builder1 = new ProcessBuilder();
		builder1.directory(new File(directory));
		File wd = builder1.directory();
		builder1.command(inCommand);

		
		String outfilepath = "";
		String infilepath = "";
		
		if(Batch.getFiles() != null) {			
			FileCommand ofcmd = Batch.getFiles().get(outCmd.getOutFile());
			FileCommand ifcmd = Batch.getFiles().get(inCmd.getInFile());
			
			if(ofcmd != null){
				outfilepath = ofcmd.getPath();
			} else {
				System.out.println("Error Processing Batch Unable to locate FileCommand with id " + outCmd.getOutFile());
				throw new ProcessException(
				"Error Processing Batch Unable to locate FileCommand with id: " + outCmd.getOutFile());
			}
			
			if(ifcmd != null){
				infilepath = ifcmd.getPath();
			} else {
				System.out.println("Error Processing Batch: Unable to locate FileCommand with id: " + inCmd.getInFile());
				throw new ProcessException(
				"Error Processing Batch: Unable to locate FileCommand with id: " + inCmd.getInFile());
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
		
		File infile = new File(wd, infilepath);
	
		builder1.redirectInput(infile);
		final Process process1 = builder1.start();		

		InputStream is1 = process1.getInputStream();
		OutputStream os1 = process1.getOutputStream();
		
		os1.close();

		ProcessBuilder builder2 = new ProcessBuilder();
		builder2.directory(new File(directory));
		builder2.command(outCommand);
		
		File outfile = new File(wd,outfilepath);
		FileOutputStream fos = new FileOutputStream(outfile);
		
		builder2.redirectError();
		
		final Process process2 = builder2.start();
		
		InputStream is2 = process2.getInputStream();
		OutputStream os2 = process2.getOutputStream();
		
		copyStreams(is1, os2);
		copyStreams(is2, fos);
		
		process1.waitFor();
		process2.waitFor();
		
		fos.close();
		
		System.out.println("Command Terminated! -- ");
	}
	

	static void copyStreams(final InputStream is, final OutputStream os) {
		Runnable copyThread = (new Runnable() {
			@Override
			public void run()
			{
				try {
					int achar;
					while ((achar = is.read()) != -1) {
						os.write(achar);
					}
					os.close();
				}
				catch (IOException ex) {
					throw new RuntimeException(ex.getMessage(), ex);
				}
			}
		});
		new Thread(copyThread).start();
	}
}
