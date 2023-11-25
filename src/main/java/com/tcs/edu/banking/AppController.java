package com.tcs.edu.banking;

import com.tcs.edu.banking.account.domain.CommandType;
import com.tcs.edu.banking.account.domain.CreditAccount;
import com.tcs.edu.banking.account.domain.DepositAccount;
import com.tcs.edu.banking.account.service.AccountService;
import com.tcs.edu.banking.account.service.ReportingService;
import com.tcs.edu.banking.error.ProcessingException;
import com.tcs.edu.banking.transport.domain.Message;
import com.tcs.edu.banking.transport.persist.FileMessageRepository;
import com.tcs.edu.banking.transport.service.MessageDecorator;

import java.io.IOException;
import java.util.Objects;

/**
 * Ответственности:
 * - [x] единая точка входа приложения: афиширует методы публичного API всей системы
 * - [x] инкапсулирует общий алгоритм обработки входящих команд
 * - [x] валидация данных входящих команд
 * - [x] журналирование входящих команд
 * - [x] рефакторинг метода публичного API всей системы
 * в этом релизе:
 * - [ ] парсинг входящих команд и их параметров
 * - [ ] диспетчеризация: вызов нужной логики обработки команды
 */
public class AppController {
    private final MessageDecorator numberedDecorator;
    private final MessageDecorator timestampDecorator;
    private final FileMessageRepository fileMessageRepository;
    private final AccountService accountService;
    private final ReportingService reportingService;


    public AppController(MessageDecorator numberedDecorator, MessageDecorator timestampDecorator,
                         FileMessageRepository fileMessageRepository, AccountService accountService,
                         ReportingService reportingService) {
        this.numberedDecorator = numberedDecorator;
        this.timestampDecorator = timestampDecorator;
        this.fileMessageRepository = fileMessageRepository;
        this.accountService = accountService;
        this.reportingService = reportingService;
    }

    /**
     * Метод обработки входящей команды.
     * @param message Входящая команда типа {@link com.tcs.edu.banking.transport.domain.Message}.
     * @return Строковый результат обработки входящей команды.
     * @throws com.tcs.edu.banking.error.ProcessingException в случае ошибки обработки команды. Инкапсулирует ошибку-причину.
     * @throws java.lang.IllegalArgumentException в случае невалидной входящей команды: команда == null, тело == null, значимость == null, команда не соответствует ни одной допустимой структуре
     * @see java.util.Objects#isNull
     * @see java.lang.String#startsWith
     * @see java.lang.String#substring
     * @see java.lang.String#length
     * @see java.lang.String#split
     * @see java.lang.String#valueOf
     */
    public String process(Message message) throws ProcessingException {
        if (Objects.isNull(message)) {
            throw new IllegalArgumentException("Message cannot be null");
        }
        if (Objects.isNull(message.getBody()) || Objects.isNull(message.getSeverity())) {
            throw new IllegalArgumentException("Illegal argument");
        }
        try {
            fileMessageRepository.save(
                    numberedDecorator.decorate(
                            timestampDecorator.decorate(
                                    message.toString())));
        } catch (IOException e) {
            throw new ProcessingException(e);
        }
        CommandType command = getCommand(message.getBody());
        switch (command) {
            case CREATE_ACCOUNT:
                return processCreateAccount(message.getBody());
            case REPORT:
                return reportingService.generate();
            case TRANSFER:
                return processTransfer(message.getBody());
            default:
                throw new IllegalArgumentException("Unknown command");
        }
    }
    private String processCreateAccount(String body) {
        final String[] paramsCommand = body.substring("CREATE ACCOUNT".length()).split(" ");
        if (paramsCommand.length != 2) {
            throw new IllegalArgumentException("Invalid command");
        }
        final String accountType = paramsCommand[1];
        switch (accountType) {
            case "DEPOSIT":
                return String.valueOf(accountService.create(new DepositAccount()));
            case "CREDIT":
                return String.valueOf(accountService.create(new CreditAccount()));
            default:
                throw new IllegalArgumentException("Unknown account type");
        }
    }
    private String processTransfer(String body) {
        final String[] paramsCommand = body.substring("TRANSFER".length()).split(" ");
        if (paramsCommand.length != 4) {
            throw new IllegalArgumentException("Invalid command");
        }
        final int from = Integer.parseInt(paramsCommand[1]);
        final int to = Integer.parseInt(paramsCommand[2]);
        final double amount = Integer.parseInt(paramsCommand[3]);
        accountService.transfer(from, to, amount);
        return "Transfer success";
    }

    private CommandType getCommand(String body) {
        if (body.startsWith(CommandType.CREATE_ACCOUNT.getCommand())) {
            return CommandType.CREATE_ACCOUNT;
        }
        if (body.startsWith(CommandType.REPORT.getCommand())) {
            return CommandType.REPORT;
        }
        if (body.startsWith(CommandType.TRANSFER.getCommand())) {
            return CommandType.TRANSFER;
        } else {
              throw new IllegalArgumentException("Unknown command");
        }
    }
}
