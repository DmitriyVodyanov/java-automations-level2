package com.tcs.edu.banking.account;

import com.tcs.edu.banking.AppController;
import com.tcs.edu.banking.account.domain.Account;
import com.tcs.edu.banking.account.persist.InMemoryAccountRepository;
import com.tcs.edu.banking.account.service.AccountService;
import com.tcs.edu.banking.account.service.ReportingService;
import com.tcs.edu.banking.error.ProcessingException;
import com.tcs.edu.banking.transport.domain.Message;
import com.tcs.edu.banking.transport.domain.RoundFormatMessage;
import com.tcs.edu.banking.transport.domain.Severity;
import com.tcs.edu.banking.transport.persist.FileMessageRepository;
import com.tcs.edu.banking.transport.service.NumberedDecorator;
import com.tcs.edu.banking.transport.service.TimestampDecorator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Системные тесты:
 * для операций используется только публичный API AppController
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AppControllerProcessingTest {

    public final String createAccountDeposit = "CREATE ACCOUNT DEPOSIT";
    public final String createAccountCredit = "CREATE ACCOUNT CREDIT";
    private AppController appController;
    private final Path pathToLogFile = Path.of("target/log.txt");
    private InMemoryAccountRepository inMemoryAccountRepository;
    private Message reportMessage;


    @BeforeEach
    void setUp() throws IOException {
        reportMessage = new RoundFormatMessage("REPORT", Severity.INFO);
        inMemoryAccountRepository = new InMemoryAccountRepository();
        appController = new AppController(
                new NumberedDecorator(),
                new TimestampDecorator(),
                new FileMessageRepository(pathToLogFile),
                new AccountService(inMemoryAccountRepository),
                new ReportingService(inMemoryAccountRepository));
        Files.deleteIfExists(pathToLogFile);
    }

    @Test
    public void shouldGetReport() throws ProcessingException {
        String report = appController.process(reportMessage);
        assertThat(report).containsOnlyOnce("");
    }

    @Test
    public void shouldCreateDepositAccount() throws ProcessingException {
        int id = createAccountWithoutAmount(createAccountDeposit).getId();
        String report = appController.process(reportMessage);
        assertThat(report).containsOnlyOnce(id + ": DEPOSIT 0.0");
    }

    @Test
    public void shouldCreateCreditAccount() throws ProcessingException {
        int id = createAccountWithoutAmount(createAccountCredit).getId();
        String report = appController.process(reportMessage);
        assertThat(report).containsOnlyOnce(id + ": CREDIT 0.0");
    }

    @Test
    public void shouldTransferFromCreditToCreditWhenNotEnoughAmount() throws ProcessingException {
        int fromId = createAccountWithoutAmount(createAccountCredit).getId();
        int toId = createAccountWithoutAmount(createAccountCredit).getId();
        appController.process(createTransferRoundFormatMessage(fromId, toId, 100, Severity.INFO));
        String report = appController.process(new RoundFormatMessage("REPORT", Severity.INFO));
        assertThat(report).containsOnlyOnce(fromId + ": CREDIT -100.0");
        assertThat(report).containsOnlyOnce(toId + ": CREDIT 100.0");
    }

    @Test
    public void shouldTransferFromCreditToDepositWhenNotEnoughAmount() throws ProcessingException {
        int fromIdCredit = createAccountWithoutAmount(createAccountCredit).getId();
        int toIdDeposit = createAccountWithoutAmount(createAccountDeposit).getId();
        appController.process(createTransferRoundFormatMessage(fromIdCredit, toIdDeposit, 100, Severity.INFO));
        String report = appController.process(new RoundFormatMessage("REPORT", Severity.INFO));
        assertThat(report).containsOnlyOnce(fromIdCredit + ": CREDIT -100.0");
        assertThat(report).containsOnlyOnce(toIdDeposit + ": DEPOSIT 100.0");
    }

    @Test
    public void shouldTransferFromCreditToCreditWhenEnoughAmount() throws ProcessingException {
        int fromId = createAccountWithAmount(createAccountCredit, 100).getId();
        int toId = createAccountWithoutAmount(createAccountCredit).getId();
        appController.process(createTransferRoundFormatMessage(fromId, toId, 100, Severity.INFO));
        String report = appController.process(new RoundFormatMessage("REPORT", Severity.INFO));
        assertThat(report).containsOnlyOnce(fromId + ": CREDIT 0.0");
        assertThat(report).containsOnlyOnce(toId + ": CREDIT 100.0");
    }

    @Test
    public void shouldTransferFromDepositToDepositWhenEnoughAmount() throws ProcessingException {
        int fromId = createAccountWithAmount(createAccountDeposit, 100).getId();
        int toId = createAccountWithoutAmount(createAccountDeposit).getId();
        appController.process(createTransferRoundFormatMessage(fromId, toId, 100, Severity.INFO));
        String report = appController.process(new RoundFormatMessage("REPORT", Severity.INFO));
        assertThat(report).containsOnlyOnce(fromId + ": DEPOSIT 0.0");
        assertThat(report).containsOnlyOnce(toId + ": DEPOSIT 100.0");
    }

    @Test
    public void shouldTransferFromDepositToCreditWhenEnoughAmount() throws ProcessingException {
        int fromIdDeposit = createAccountWithAmount(createAccountDeposit, 100).getId();
        int toIdCredit = createAccountWithoutAmount(createAccountCredit).getId();
        appController.process(createTransferRoundFormatMessage(fromIdDeposit, toIdCredit, 100, Severity.INFO));
        String report = appController.process(new RoundFormatMessage("REPORT", Severity.INFO));
        assertThat(report).containsOnlyOnce(fromIdDeposit + ": DEPOSIT 0.0");
        assertThat(report).containsOnlyOnce(toIdCredit + ": CREDIT 100.0");
    }

    private Account createAccountWithoutAmount(String body) throws ProcessingException {
        Message createAccountMessage = new RoundFormatMessage(body, Severity.INFO);
        int idAccount = Integer.parseInt(appController.process(createAccountMessage));
        return inMemoryAccountRepository.findById(idAccount);

    }

    private Account createAccountWithAmount(String body, double amount) throws ProcessingException {
        Message createAccountMessage = new RoundFormatMessage(body, Severity.INFO);
        int idAccount = Integer.parseInt(appController.process(createAccountMessage));
        Account account = inMemoryAccountRepository.findById(idAccount);
        account.deposit(amount);
        return account;
    }

    private Message createTransferRoundFormatMessage(int fromIdAccount, int toIdAccount, int amount, Severity severity) {
        return new RoundFormatMessage(
                new StringBuilder()
                        .append("TRANSFER ")
                        .append(fromIdAccount)
                        .append(" ")
                        .append(toIdAccount)
                        .append(" ")
                        .append(amount)
                        .toString(), severity);
    }
}
