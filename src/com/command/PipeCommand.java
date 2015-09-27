package com.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.batch.Batch;
import com.batch.processor.impl.CommandProcessorWithStreamsThreaded;
import com.exceptions.ProcessException;

public class PipeCommand extends Command {

	private String id;
	private List<CmdCommand> commandSet;
	
	@Override
	public void parse(Element elem) throws ProcessException {
		// TODO Auto-generated method stub
		String id = elem.getAttribute("id");
		if (id != null && !id.isEmpty()) {
			System.out.println("ID: " + id);
		}
		
		setId(id);
		
		NodeList nodes = elem.getChildNodes();
		
		for (int _node = 0; _node < nodes.getLength(); _node++) {
			Node node = nodes.item(_node);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element _elem = (Element) node;
				CmdCommand cmd = new CmdCommand();
				if(_elem.getNodeName().equalsIgnoreCase("cmd")){
					cmd.parse(_elem);
					Map<String, CmdCommand> commands = Batch.getCommands();
					commands.remove(_elem.getAttribute("id"));
					Batch.setCommands(commands);
				}
				addCommandSet(cmd);
			}
		}
		
		Batch.addPipe(this);
	}

	@Override
	public void execute() throws IOException, InterruptedException, ProcessException {
		// TODO Auto-generated method stub
		CommandProcessorWithStreamsThreaded batch = new CommandProcessorWithStreamsThreaded();
		batch.process(this);
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

	public List<CmdCommand> getCommandSet() {
		return commandSet;
	}
	
	public void setCommandSet(List<CmdCommand> commands) {
		this.commandSet = commands;
	}
	
	public void addCommandSet(CmdCommand command) {
		List<CmdCommand> commands;
		if (this.getCommandSet() == null || this.getCommandSet().isEmpty()) {
			 commands = new ArrayList<CmdCommand>(); 
		}
		else {
			commands = this.getCommandSet();
		}
		commands.add(command);
		setCommandSet(commands);
	}



}
