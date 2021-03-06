package ca.corefacility.bioinformatics.irida.repositories.remote.resttemplate;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import ca.corefacility.bioinformatics.irida.exceptions.IridaOAuthException;
import ca.corefacility.bioinformatics.irida.model.RemoteAPI;

/**
 * Error handler for use in {@link OAuthTokenRestTemplate}. Catches HTTP
 * UNAUTHORIZED (401) errors to translate to IridaOAuthExceptions
 * 
 *
 */
public class IridaOAuthErrorHandler extends DefaultResponseErrorHandler {
	private static final Logger logger = LoggerFactory.getLogger(IridaOAuthErrorHandler.class);

	private RemoteAPI remoteAPI;

	/**
	 * Overriding this method to throw a {@link IridaOAuthException} in case of
	 * an HTTP UNAUTHORIZED response.
	 */
	@Override
	public void handleError(ClientHttpResponse response) throws IOException {

		HttpStatus statusCode = response.getStatusCode();
		logger.trace("Checking error type " + statusCode.toString());
		switch (statusCode) {
		case UNAUTHORIZED:
			logger.trace("Throwing new IridaOAuthException for this error");
			throw new IridaOAuthException("User is unauthorized for this service", remoteAPI);
		default:
			logger.trace("Passing error to superclass");
			super.handleError(response);
		}
	}

	/**
	 * Set the {@link RemoteAPI} to return to the caller if an error occurs
	 * 
	 * @param remoteAPI
	 *            the {@link RemoteAPI} to include in the exception.
	 */
	public void setRemoteAPI(RemoteAPI remoteAPI) {
		this.remoteAPI = remoteAPI;
	}
}
