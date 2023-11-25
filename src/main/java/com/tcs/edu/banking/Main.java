package com.tcs.edu.banking;

import com.tcs.edu.banking.account.domain.Account;
import com.tcs.edu.banking.account.domain.CreditAccount;
import com.tcs.edu.banking.account.domain.DepositAccount;
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
import org.mockito.MockedStatic;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;

import static org.mockito.Mockito.mockStatic;

public class Main {

    public static void main(String[] args) throws IOException, ProcessingException {

//        LocalDateTime now = LocalDateTime.now();
//
//        System.out.println("Before : " + now);
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//        String formatDateTime = now.format(formatter);
//
//        System.out.println("After : " + formatDateTime);
//
//        List<Severity> listSeverity = new ArrayList<>(List.of(Severity.ERROR, Severity.DEBUG, Severity.INFO));
//        List<String> listWord = new ArrayList<>(List.of("народ", "проектный", "встречный", "послать", "зеркало", "описать", "выявить", "собираться", "блестящий", "обеспечение", "зенитный", "вкусный", "любовный", "разрушить", "летать"));
//
//        SquareFormatMessage squareFormatMessage;
//        AppController appController = new AppController();
//
//
////         for (Severity severity : listSeverity) {
////             squareFormatMessage = new SquareFormatMessage("message", severity);
////             System.out.println(appController.process(squareFormatMessage));
////         }
//        for (int i = 0; i < 10; i++) {
//            for (Severity severity : listSeverity) {
//                int randomNumber = 0 + (int) (Math.random() * 5);
////                int randomNumber1 = 6 + (int) (Math.random() * 10);
//                String word = listWord.get(randomNumber);
////                String word2 = listWord.get(randomNumber1);
//                squareFormatMessage = new SquareFormatMessage("message %s".formatted(word), severity);
//                System.out.println(appController.process(squareFormatMessage));
//            }
//        }


        LocalDateTime fixedDateTime = LocalDateTime.of(2023, 1, 1, 23, 59, 59, 403790000);
        try (MockedStatic<LocalDateTime> mockedStatic = mockStatic(LocalDateTime.class)) {
            mockedStatic.when(LocalDateTime::now).thenReturn(fixedDateTime);
        }

//        System.out.println(fixedDateTime);
//        var body = "Create account DEPOSIT";
//        var body1 = "Create account";
//
//        System.out.println("Create account DEPOSIT".startsWith("Create account"));
//        String[] params = body.substring("Create account".length()).split(" ");
//        String[] params1 = body1.substring("Create account".length()).split(" ");
//        if (params1.length != 2) new IllegalArgumentException("Invalid command");
//        System.out.println(params[1]);
//        System.out.println(params1[1]);

        Account accountC = new CreditAccount();
        Account accountD = new DepositAccount();
        Message message = new RoundFormatMessage("test", Severity.INFO);
        AppController appController = new AppController(
                new NumberedDecorator(),
                new TimestampDecorator(),
                new FileMessageRepository(Path.of("target/log.txt")),
                new AccountService(new InMemoryAccountRepository()),
                new ReportingService(new InMemoryAccountRepository()));
        int id = Integer.parseInt(appController.process(message));

           print(accountC);
           print(accountD);


    }

    /**
     Абстрактный класс AbstractAccount является базовым классом для работы с аккаунтами.
     Реализует общую функциональность и содержит методы для взаимодействия с аккаунтами.

     Метод hash используется для генерации хеш-представления объекта аккаунта.
     Хеш-представление используется для уникальной идентификации аккаунтов,
     быстрого поиска и проверки целостности данных.
     @return Хеш-представление объекта аккаунта
     Метод equals используется для сравнения двух объектов аккаунтов по их значениям.
     Возвращает true, если переданный объект равен текущему объекту аккаунта, иначе false.
     @param obj Объект, с которым происходит сравнение
     @return Результат сравнения двух объектов аккаунтов
     **/
    static void print(Account account) {
        System.out.println(account.toString());

    }
}
