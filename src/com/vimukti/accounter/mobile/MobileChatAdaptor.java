/**
 * 
 */
package com.vimukti.accounter.mobile;

import java.util.List;

import com.vimukti.accounter.mobile.UserMessage.Type;
import com.vimukti.accounter.mobile.commands.AuthenticationCommand;
import com.vimukti.accounter.mobile.commands.SelectCompanyCommand;
import com.vimukti.accounter.mobile.store.CommandsFactory;
import com.vimukti.accounter.mobile.store.PatternStore;

/**
 * @author Prasanna Kumar G
 * 
 */
public class MobileChatAdaptor implements MobileAdaptor {

	public static MobileAdaptor INSTANCE = new MobileChatAdaptor();

	/**
	 * PreProcess the UserMessage
	 * 
	 * @param userMessage
	 * @return
	 * @throws AccounterMobileException
	 */
	public UserMessage preProcess(MobileSession session, String message,
			String networkId, int networkType) throws AccounterMobileException {
		UserMessage userMessage = new UserMessage(message, networkId,
				networkType);

		if (message == null || message.isEmpty()) {
			userMessage = session.getLastMessage();
			message = userMessage.getOriginalMsg();
		}
		Command command = null;

		if (command == null) {
			command = session.getCurrentCommand();
		}

		if (command == null && !session.isAuthenticated()) {
			command = new AuthenticationCommand();
			userMessage.setOriginalMsg("");// To know it is first
			message = "";
			userMessage.setCommandString("");
		}
		if (command == null) {
			long companyId = session.getCompanyID();
			if (companyId == 0) {
				command = new SelectCompanyCommand();
				userMessage.setCommandString("");
			}
		}

		if (session.isAuthenticated()) {
			String commandString = "";
			Command matchedCommand = null;
			for (String str : message.split(" ")) {
				commandString += str;
				matchedCommand = CommandsFactory.INSTANCE
						.getCommand(commandString);
				if (matchedCommand != null) {
					break;
				}
				commandString += ' ';
			}

			if (matchedCommand != null) {
				message = message.replaceAll(commandString.trim(), "").trim();
				userMessage.setOriginalMsg(message);
				command = matchedCommand;
				userMessage.setCommandString(commandString.trim());
			}

			Result result = PatternStore.INSTANCE.find(message);
			if (result != null) {
				userMessage.setType(Type.HELP);
				userMessage.setResult(result);
				return userMessage;
			}
		}

		UserMessage lastMessage = session.getLastMessage();
		Result lastResult = lastMessage == null ? null : lastMessage
				.getResult();
		if (lastResult instanceof PatternResult) {
			PatternResult patternResult = (PatternResult) lastResult;
			Result result = getPatternResult(patternResult.getCommands(),
					message);
			if (result != null) {
				userMessage.setType(Type.HELP);
				userMessage.setResult(result);
				return userMessage;
			}
		}

		if (lastResult instanceof PatternResult) {
			PatternResult patternResult = (PatternResult) lastResult;
			Command selectCommand = getCommand(patternResult.getCommands(),
					message, userMessage);
			if (selectCommand != null) {
				command = selectCommand;
				UserMessage lastMessage2 = session.getLastMessage();
				if (lastMessage2 != null) {
					lastMessage2.setOriginalMsg("");
				}
			}
		}

		userMessage.setLastResult(lastResult);

		if (command != null && !command.isDone()) {
			session.addCommand(command);
		}

		if (command != null) {
			userMessage.setType(Type.COMMAND);
			userMessage.setCommand(command);
			userMessage.setInputs(message.split(" "));
			return userMessage;
		}

		if (message.startsWith("#")) {
			userMessage.setType(Type.NUMBER);
			userMessage.setInputs(message.replaceAll("#", "").split(" "));
			return userMessage;
		}

		// TODO Check for isName
		if (!message.contains(" ")) {
			userMessage.setType(Type.NAME);
			userMessage.setInputs(message.split(" "));
		}

		userMessage.setInputs(new String[] { message });

		return userMessage;
	}

	private Result getPatternResult(CommandList commands, String input) {
		// Getting the First Character of the Input
		if (input == null || input.isEmpty() || input.length() > 1) {
			return null;
		}
		char ch = input.charAt(0);
		// Finding the Command for Input
		int index = ch - 97;// If ch is number
		if (index < 0) {
			return null;
		}
		UserCommand userCommand = commands.get(index);
		String commandString = userCommand.getCommandName();
		if (commandString == null) {
			return null;
		}
		return PatternStore.INSTANCE.find(commandString);
	}

	private Command getCommand(CommandList commands, String input,
			UserMessage userMessage) {
		// Getting the First Character of the Input
		if (input == null || input.isEmpty() || input.length() > 1) {
			return null;
		}
		char ch = input.charAt(0);
		// Finding the Command for Input
		int index = ch - 97;// If ch is number
		if (index < 0) {
			return null;
		}
		UserCommand userCommand = commands.get(index);
		String commandString = userCommand.getCommandName();
		if (commandString == null) {
			return null;
		}
		Command command = null;
		String str = "";
		for (String s : commandString.split(" ")) {
			str += s;
			command = CommandsFactory.INSTANCE.getCommand(str);
			if (command != null) {
				break;
			}
			str += " ";
		}
		commandString = commandString.replaceAll(str.trim(), "").trim();
		userMessage.setOriginalMsg(commandString);
		userMessage.setCommandString(str);
		return command;
	}

	/**
	 * PostProcess the Result
	 * 
	 * @param result
	 * @return
	 */
	public String postProcess(Result result) {
		if (result == null) {
			return null;
		}
		List<Object> resultParts = result.getResultParts();
		StringBuffer reply = new StringBuffer();
		int recordsCount = 1;
		int commandIndex = 97;
		for (Object part : resultParts) {
			if (part instanceof ResultList) {
				ResultList resultList = (ResultList) part;
				for (int x = 0; x < resultList.size(); x++, recordsCount++) {
					Record record = resultList.get(x);
					record.setCode(recordsCount);
					reply.append(record.toString());
				}
			} else if (part instanceof CommandList) {
				CommandList commandList = (CommandList) part;
				for (int x = 0; x < commandList.size(); x++, commandIndex++) {
					reply.append('(');
					reply.append((char) commandIndex);
					reply.append(") ");
					reply.append(commandList.get(x));
					reply.append('\n');
				}
			} else {
				reply.append((String) part);
			}
			reply.append('\n');
		}
		return reply.toString();
	}
}
