package com.batchparser;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.command.CmdCommand;
import com.command.Command;
import com.command.FileCommand;
import com.command.PipeCommand;
import com.command.WdCommand;
import com.exceptions.ProcessException;

public class XMLParser {
	
	public static void main (String args[]) {
		
		String OS = System.getProperty("os.name").toLowerCase();		
		String filename = null;
		if (args.length > 0) {
			filename = OS+"_"+args[0];
		} else {
			if (OS.indexOf("windows") >= 0) {
				filename = "input/win_batch1.xml";
			} else if (OS.indexOf("linux") >= 0) {
				filename = "input/linux_batch1.xml";
			} else {
				System.out.println("Your OS type input not supported");
				return;
			}
		}
		
		System.out.println("Opening " + filename);
		File f = new File(filename);
		
		try {
			FileInputStream fis = new FileInputStream(f);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fis);

			Element pnode = doc.getDocumentElement();
			NodeList nodes = pnode.getChildNodes();
			
			for (int _node = 0; _node < nodes.getLength(); _node++) {
				Node node = nodes.item(_node);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element elem = (Element) node;
					parseCommand(elem);
				}
			}
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
	}

	public static void parseCommand(Element elem) throws ProcessException{
		String cmdName = elem.getNodeName();
		
		if (cmdName == null) {
			throw new ProcessException("unable to parse command from " + elem.getTextContent());
		}
		else if ("wd".equalsIgnoreCase(cmdName)) {
			System.out.println("Parsing wd");
			Command cmd = new WdCommand();
			cmd.parse(elem);
		}
		else if ("file".equalsIgnoreCase(cmdName)) {
			System.out.println("Parsing file");
			Command cmd = new FileCommand();
			cmd.parse(elem);
		}
		else if ("cmd".equalsIgnoreCase(cmdName)) {
			System.out.println("Parsing cmd");
			Command cmd = new CmdCommand();
			cmd.parse(elem);
		}
		else if ("pipe".equalsIgnoreCase(cmdName)) {
			System.out.println("Parsing pipe");
			Command cmd = new PipeCommand();
			cmd.parse(elem);
		}
		else {
			throw new ProcessException("Unknown command " + cmdName + " from: " + elem.getBaseURI());
		}
	}
	

}

