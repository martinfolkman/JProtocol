package org.jprotocol.framework.test;

import org.jprotocol.quantity.Quantity;

/**
 * 
 * @author eliasa01
 *
 */
public interface IProtocolLogger {
	/**
	 * An info message
	 * @param infoMessage
	 */
	void writeInfo(String infoMessage);

	/**
	 * A test message, for example: Test A has started
	 * @param message
	 */
	void writeTest(String message);
	
	/**
	 * Successful expect has been mad
	 * @param expectStr the expect string
	 */
	void writeSuccessfulExpect(String expectStr);
	/**
	 * Failed expect has been made
	 * @param expectStr the expect string
	 * @param failureMessage the failure message
	 */
	void writeFailedExpect(String expectStr, String failureMessage);

	/**
	 * Successful allow has been made
	 * @param allowStr the expect string
	 */
	void writeSuccessfulAllow(String allowStr);
	/**
	 * Failed allow has been made
	 * @param allowStr the expect string
	 * @param failureMessage the failure message
	 */
	void writeFailedAllow(String allowStr, String failureMessage);

	
	/**
	 * A pending verify has been called
	 * @param timeout 
	 */
	void writePendingVerify(Quantity timeout);
	
	/**
	 * Successful verify has been made
	 */
	void writeSuccessfulVerify();
	
	/**
	 * Failed verify has been made
	 * @param errorMessage
	 */
	void writeFailedVerify(String errorMessage);
	/**
	 * 
	 * @param request
	 * @param response
	 */
	void writeAddResponse(String request, String response);
	/**
	 * 
	 * @param response
	 */
	void writeInject(String response);
	/**
	 * Expectation mode set to Allow Request
	 */
	void writeAllowRequests();
	/**
	 * Expectation mode set to Specify Request
	 */
	void writeSpecifyRequests();

	public static class NullProtocolLogger implements IProtocolLogger {
		@Override
		public void writeSuccessfulExpect(String expectStr) {
			//Do nothing
		}

		@Override
		public void writeFailedVerify(String errorMessage) {
			//Do nothing
		}
		

		@Override
		public void writeSuccessfulVerify() {
			//Do nothing
		}

		@Override
		public void writeTest(String message) {
			//Do nothing
		}

		@Override
		public void writePendingVerify(Quantity timeout) {
			//Do nothing
		}

		@Override
		public void writeFailedExpect(String expectStr, String failureMessage) {
			//Do nothing
		}

		@Override
		public void writeAddResponse(String request, String response) {
			//Do nothing
		}

		@Override
		public void writeInject(String response) {
			//Do nothing
		}

		@Override
		public void writeInfo(String infoMessage) {
			//Do nothing
		}

		@Override
		public void writeSuccessfulAllow(String allowStr) {
			//Do nothing
		}

		@Override
		public void writeFailedAllow(String allowStr, String failureMessage) {
			//Do nothing
		}

		@Override
		public void writeAllowRequests() {
			//Do nothing
		}

		@Override
		public void writeSpecifyRequests() {
			//Do nothing
		}

		
	}


}
