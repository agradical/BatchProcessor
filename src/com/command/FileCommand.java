package com.command;

import org.w3c.dom.Element;

import com.batch.Batch;
import com.exceptions.ProcessException;

public class FileCommand extends Command {

	private String id;
	private String path;
	
	@Override
	public void parse(Element elem) throws ProcessException {
		// TODO Auto-generated method stub
		String id = elem.getAttribute("id");
		if (id == null || id.isEmpty()) {
			throw new ProcessException("Missing ID in File Command");
		}
		System.out.println("ID: " + id);
		
		String path = elem.getAttribute("path");
		if (path == null || path.isEmpty()) {
			throw new ProcessException("Missing PATH in File Command");
		}
		System.out.println("Path: " + path);
		
		setId(id);
		setPath(path);
		
		System.out.println(this.getId());
		System.out.println(this.getPath());
		Batch.addFile(this);
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}


}
