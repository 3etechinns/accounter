package com.vimukti.accounter.web.client.core;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {
	private List<Error> errors = new ArrayList<ValidationResult.Error>();
	private List<Warning> warnings = new ArrayList<ValidationResult.Warning>();

	public void addError(Object obj, String msg) {
		this.errors.add(new Error(obj, msg));
	}

	public void add(ValidationResult result) {
		errors.addAll(result.errors);
	}

	public void addWarning(Object obj, String msg) {
		warnings.add(new Warning(obj, msg));
	}

	public static class Error {
		private Object source;
		private String message;

		Error(Object source, String message) {
			this.setSource(source);
			this.setMessage(message);
		}

		public Object getSource() {
			return source;
		}

		public void setSource(Object source) {
			this.source = source;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}

	public static class Warning {
		private Object source;
		private String message;

		/**
		 * Creates new Instance
		 */
		public Warning(Object source, String msg) {
			this.source = source;
			this.message = msg;
		}

		/**
		 * @return the source
		 */
		public Object getSource() {
			return source;
		}

		/**
		 * @return the message
		 */
		public String getMessage() {
			return message;
		}

	}

	public boolean haveErrors() {
		return !this.errors.isEmpty();
	}
}
