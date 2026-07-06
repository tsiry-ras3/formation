package hei.school.subscribe.endpoint.rest.controller;

import hei.school.subscribe.mail.Email;
import hei.school.subscribe.mail.Mailer;
import jakarta.mail.internet.InternetAddress;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MailerController {

  private final Mailer mailer;

  @GetMapping("/hello")
  @SneakyThrows
  public String helloWorld(@RequestParam String to) {
    var email =
        new Email(
            new InternetAddress(to), List.of(), List.of(), "Hello world", "... world!", List.of());

    mailer.accept(email);
    return "... world!";
  }
}
