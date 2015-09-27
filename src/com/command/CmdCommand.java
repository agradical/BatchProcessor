package com.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.w3c.dom.Element;

import com.batch.Batch;
import com.batch.processor.impl.CommandPrcessorWithFiles;
import com.batch.processor.impl.CommandProcessor;
import com.exceptions.ProcessException;

public class CmdCommand extends Command {

	private String id;
	private String path;
	private List<String> args;
	private String inFile;
	private String outFile;
	
	@Override
	public void parse(Element elem) throws ProcessException {
		// TODO Auto-generated method stub
		String id = elem.getAttribute("id");
		String path = elem.getAttribute("path");
		
		if (id == null || id.isEmpty()) {
			throw new ProcessException("Missing ID in CMD Command");
		}
		if (path == null || path.isEmpty()) {
			throw new ProcessException("Missing PATH in CMD Command");
		}
		
		System.out.println("ID: " + id);
		System.out.println("Path: " + path);
 
		List<String> cmdArgs = new ArrayList<String>();
		String arg = elem.getAttribute("args");
		if (!(arg == null || arg.isEmpty())) {
			StringTokenizer st = new StringTokenizer(arg);
			while (st.hasMoreTokens()) {
				String tok = st.nextToken();
				cmdArgs.add(tok);
			}
		}
		
		String inID = elem.getAttribute("in");
		if (!(inID == null || inID.isEmpty())) {
			System.out.println("inID: " + inID);
		}

		String outID = elem.getAttribute("out");
		if (!(outID == null || outID.isEmpty())) {
			System.out.println("outID: " + outID);
		}

		setId(id);
		setPath(path);
		setArgs(cmdArgs);
		setInFile(inID);
		setOutFile(outID);
		
		Batch.addCommand(this);
	}

	@Override
	public void execute() throws IOException, InterruptedException, ProcessException {
		// TODO Auto-generated method stub
		if(this.getInFile() == null || this.getInFile().isEmpty()) {
			CommandProcessor batch = new CommandProcessor();
			batch.process(this);
		} else {
			CommandPrcessorWithFiles batch = new CommandPrcessorWithFiles();
			batch.process(this);
		}
	}

	@Override
	public String describe() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getArgs() {
		return args;
	}

	public void setArgs(List<String> args) {
		this.args = args;
	}

	public String getInFile() {
		return inFile;
	}

	public void setInFile(String inFile) {
		this.inFile = inFile;
	}

	public String getOutFile() {
		return outFile;
	}

	public void setOutFile(String outFile) {
		this.outFile = outFile;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void validate(Element elem) throws ProcessException {
		// TODO Auto-generated method stub
		
		String id = elem.getAttribute("id");
		
		if (Batch.getCommands().get(id) != null){
			throw new ProcessException("Command with id <"+id+"> already exists");
		}
		
	}

}
