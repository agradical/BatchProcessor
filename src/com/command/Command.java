package com.command;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

public interface Command {
	
	public static List<Element> CommandList = new ArrayList<Element>();
	
	public void parse(Element elem);
	public void execute();
	public String describe();
	
}
