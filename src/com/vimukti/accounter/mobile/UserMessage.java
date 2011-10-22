/**
 * 
 */
package com.vimukti.accounter.mobile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Prasanna Kumar G
 * 
 */
public class UserMessage {

	public enum Type {
		COMMAND, HELP, NUMBER, NAME, UNKNOWN;
	}

	private Type type = Type.UNKNOWN;
	private Result result;
	private Command command;
	private List<String> inputs = new ArrayList<String>();
	private String originalMsg;
	private String userId;
	private String networkId;
	private int networkType;

	public UserMessage(String message, String userId, String networkId,
			int networkType) {
		this.originalMsg = message;
		this.userId = userId;
		this.networkId = networkId;
		this.networkType = networkType;
	}

	public String getUserId() {
		return userId;
	}

	public String getNetworkId() {
		return networkId;
	}

	public int getNetworkType() {
		return networkType;
	}

	/**
	 * Returns the Type of the Message
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @param command2
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * Return Command if type is COMMAND
	 */
	public Command getCommand() {
		return command;
	}

	/**
	 * 
	 */
	public List<String> getInputs() {
		return this.inputs;
	}

	/**
	 * @param currentCommand
	 */
	public void setCommand(Command command) {
		this.command = command;
	}

	/**
	 * @param reply
	 */
	public void setResult(Result result) {
		this.result = result;
	}

	/**
	 * Returns the Result for this UserMessage
	 */
	public Result getResult() {
		return this.result;
	}

	/**
	 * @param split
	 */
	public void setInputs(String[] split) {
		this.inputs = Arrays.asList(split);
	}

	public String getOriginalMsg() {
		return this.originalMsg;
	}

	public void setOriginalMsg(String originalMsg) {
		this.originalMsg = originalMsg;
	}
}
