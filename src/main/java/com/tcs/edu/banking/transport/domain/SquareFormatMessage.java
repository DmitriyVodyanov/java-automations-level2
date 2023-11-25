package com.tcs.edu.banking.transport.domain;

/**
 * Инкапсулирует:
 * - данные команды
 * - поведение форматирования тела команды для журналирования
 */
public class SquareFormatMessage extends FormatMessage {

    public SquareFormatMessage(String body, Severity severity) {
        super(body, severity);
    }

    /**
     * @return строковое представление команды по шаблону '[severity] body'
     * @see java.lang.String#format
     */
    protected String getDecorator() {
        return "[%s]".formatted(getSeverity().toString());
    }
}
