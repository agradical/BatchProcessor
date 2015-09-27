package com.command;

import java.io.IOException;

import org.w3c.dom.Element;

import com.exceptions.ProcessException;

public abstract class Command {
	
	public abstract void parse(Element elem) throws ProcessException;
	public abstract void execute() throws IOException, InterruptedException, ProcessException;
	public abstract String describe();
	
}
