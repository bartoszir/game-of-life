package org.example.exceptions;

public class FileOperationException extends BasicException {
  public FileOperationException(String messageKey, Throwable cause) {
    super(messageKey, cause);
  }
}
