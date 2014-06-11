/*******************************************************************************
 * © 2014 Copyright Agnie Technologies
 *   
 * NOTICE: All information contained herein is, and remains the property of Agnie Technologies and its suppliers, if
 * any. The intellectual and technical concepts contained herein are proprietary to Agnie Technologies and its suppliers
 * and may be covered by Indian and Foreign Patents, patents in process, and are protected by trade secret or copyright
 * law. Dissemination of this information or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Agnie Technologies.
 ******************************************************************************/
package com.agnie.common.tools;

import java.util.HashMap;
import java.util.Map;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.MissingCommandException;
import com.beust.jcommander.ParameterException;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

@Singleton
public class Commander extends JCommander {

	private Map<String, CommandProcessor>	cmdProcessors	= new HashMap<String, CommandProcessor>();

	private JCommander						commander;

	private MainArgs						mainArgs;
	@Inject
	@Named("version")
	private String							version;

	@Inject
	public Commander(MainArgs mainArgs) {
		this.mainArgs = mainArgs;
		commander = new JCommander(mainArgs);
	}

	public void addCommand(String command, CommandProcessor processor) {
		processor.setCommand(command);
		processor.setCommander(commander);
		commander.addCommand(command, processor);
		cmdProcessors.put(command, processor);
	}

	public void processArguments(String... args) throws Exception {
		String command = null;
		try {
			commander.parse(args);
			if (mainArgs.isHelp()) {
				commander.usage();
				return;
			} else if (mainArgs.isVersion()) {
				System.out.println("Version  - '" + version + "'");
				return;
			}
			command = commander.getParsedCommand();
		} catch (MissingCommandException e) {
			System.out.println("Invalid option -'" + e.getMessage() + "'");
		} catch (ParameterException ex) {
			System.out.println(ex.getMessage());
		}

		if (command == null || command.isEmpty())
			commander.usage();
		else {
			CommandProcessor processor = cmdProcessors.get(command);
			processor.processCommand();
		}
	}

}
