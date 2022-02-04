package it.gesev.exc;

import org.springframework.http.HttpStatus;

public class GesevException extends RuntimeException
{
	private HttpStatus status;
	
	public GesevException(String message)
	{
		super(message);
	}
	
	public GesevException(String message, HttpStatus status)
	{
		super(message);
		this.status = status;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}
}
