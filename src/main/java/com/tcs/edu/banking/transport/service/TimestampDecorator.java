package com.tcs.edu.banking.transport.service;

import java.time.LocalDateTime;

public class TimestampDecorator implements MessageDecorator {

    /**
     * @param body строка, включающая тело команды для декорирования перед журналированием
     * @return строка по шаблону 'datetime body'
     * @see java.lang.String#format
     * @see java.time.LocalDateTime#now
     */
    public String decorate(String body){
        return LocalDateTime.now() + " %s".formatted(body);
    }
}
