package com.tcs.edu.banking.transport.service;

/**
 * Инкапсулирует поведение обогащения тела сообщения при журналировании.
 */
public interface MessageDecorator {

    String decorate(String body);
}
