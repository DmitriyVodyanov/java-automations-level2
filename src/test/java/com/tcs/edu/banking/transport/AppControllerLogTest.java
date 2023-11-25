package com.tcs.edu.banking.transport;

import com.tcs.edu.banking.AppController;
import com.tcs.edu.banking.account.persist.InMemoryAccountRepository;
import com.tcs.edu.banking.account.service.AccountService;
import com.tcs.edu.banking.account.service.ReportingService;
import com.tcs.edu.banking.transport.domain.Message;
import com.tcs.edu.banking.transport.domain.RoundFormatMessage;
import com.tcs.edu.banking.transport.domain.Severity;
import com.tcs.edu.banking.transport.domain.SquareFormatMessage;
import com.tcs.edu.banking.transport.persist.FileMessageRepository;
import com.tcs.edu.banking.transport.service.NumberedDecorator;
import com.tcs.edu.banking.transport.service.TimestampDecorator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;

/**
 * @see java.nio.file.Paths#get
 * @see org.junit.jupiter.api.BeforeEach
 * @see java.nio.file.Files#deleteIfExists
 * @see java.nio.file.Files#readString
 */

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AppControllerLogTest {
    private final String newLine = System.lineSeparator();
    private AppController appController;
    private Path pathToLogFile = Path.of("target/log.txt");
    private final String testMessage = "REPORTING";

    @BeforeEach
    void setUp() throws IOException {
        InMemoryAccountRepository inMemoryAccountRepository = new InMemoryAccountRepository();
        appController = new AppController(
                new NumberedDecorator(),
                new TimestampDecorator(),
                new FileMessageRepository(pathToLogFile),
                new AccountService(inMemoryAccountRepository),
                new ReportingService(inMemoryAccountRepository));
        Files.deleteIfExists(pathToLogFile);
    }
    @Test
    public void shouldLogDebugMessageWhenSquareDecoration() throws IOException {
        Message squareFormatMessage = new SquareFormatMessage(testMessage, Severity.DEBUG);
        checkMessage(" [DEBUG] REPORTING", squareFormatMessage);
    }

    @Test
    public void shouldLogInfoMessageWhenSquareDecoration() throws IOException {
        Message squareFormatMessage = new SquareFormatMessage(testMessage, Severity.INFO);
        checkMessage(" [INFO] REPORTING", squareFormatMessage);
    }

    @Test
    public void shouldLogErrorMessageWhenSquareDecoration() throws IOException {
        Message squareFormatMessage = new SquareFormatMessage(testMessage, Severity.ERROR);
        checkMessage(" [ERROR] REPORTING", squareFormatMessage);
    }

    @Test
    public void shouldLogDebugMessageWhenRoundDecoration() throws IOException {
        Message roundFormatMessage = new RoundFormatMessage(testMessage, Severity.DEBUG);
        checkMessage(" (DEBUG) REPORTING", roundFormatMessage);
    }

    @Test
    public void shouldLogInfoMessageWhenRoundDecoration() throws IOException {
        Message roundFormatMessage = new RoundFormatMessage(testMessage, Severity.INFO);
        checkMessage(" (INFO) REPORTING", roundFormatMessage);
    }
    @Test
    public void shouldLogErrorMessageWhenRoundDecoration() throws IOException {
        Message roundFormatMessage = new RoundFormatMessage(testMessage, Severity.ERROR);
        checkMessage(" (ERROR) REPORTING", roundFormatMessage);
    }

    @Test
    public void shouldNotLogWhenNullMessage() {
        Message squareFormatNullMessage = new SquareFormatMessage(null, Severity.ERROR);
        assertThrows(IllegalArgumentException.class, () ->
            appController.process(squareFormatNullMessage));
    }

    @Test
    public void shouldNotLogWhenNullSeverity() {
        Message squareFormatNullSeverityMessage = new SquareFormatMessage(testMessage, null);
        assertThrows(IllegalArgumentException.class, () ->
            appController.process(squareFormatNullSeverityMessage));
    }

    private void checkMessage(String expectedSeverityAndBodyMessage, Message message) throws IOException {
        try (MockedStatic<LocalDateTime> mockedStatic = mockStatic(LocalDateTime.class)) {
            LocalDateTime fixedDateTime = LocalDateTime.of(2023, 1, 1, 23, 59, 59, 403790000);
            mockedStatic.when(LocalDateTime::now).thenReturn(fixedDateTime);
            String expectedCountAndDateTime = 1 + " " + fixedDateTime;
            String expectedMessageError = expectedCountAndDateTime + expectedSeverityAndBodyMessage + newLine;
            appController.process(message);
            String actualMessage = Files.readString(pathToLogFile);
            assertEquals(expectedMessageError, actualMessage);
        }
    }
}
