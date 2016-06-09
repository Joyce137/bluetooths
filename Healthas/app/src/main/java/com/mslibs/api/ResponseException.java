package com.mslibs.api;

public class ResponseException extends Exception {
	private static final long serialVersionUID = 1L;
	public int error = 0;
	
    public ResponseException() {
        super("发生错误");
    }
    
    public ResponseException(String message) {
        super(message);
    }

    public ResponseException(String message, int errorcode) {
        super(message);
        error = errorcode;
    }
    
}
