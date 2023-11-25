package com.tcs.edu.banking.error;

import java.io.IOException;

public class ProcessingException extends IOException {

    public ProcessingException(Throwable cause) {
        super(cause);
    }
}
