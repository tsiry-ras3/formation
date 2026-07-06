package hei.school.subscribe.service.event;

import hei.school.subscribe.endpoint.event.model.SendEmailRequested;
import hei.school.subscribe.mail.Email;
import hei.school.subscribe.mail.Mailer;
import jakarta.mail.internet.InternetAddress;
import java.util.List;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SendEmailRequestedService implements Consumer<SendEmailRequested> {
  private final Mailer mailer;

  @SneakyThrows
  @Override
  public void accept(SendEmailRequested sendEmailRequested) {

    InternetAddress recipientAddress = new InternetAddress(sendEmailRequested.getTo());

    String mailMessage =
        """
            <!DOCTYPE html>
              <html>
              <head>
                <meta charset="UTF-8">
                <title>Course Registration</title>
              </head>
              <body style="margin:0; padding:20px; font-family:Arial, Helvetica, sans-serif;">

                <p>Hi %s,</p>

                <p>You're successfully registered for <strong>%s</strong>.</p>

                <p>Please find below the receipt as a confirmation of your registration</p>

                <p>[< a href="%s">Receipt link </a>]</p>

              </body>
              </html>
        """;

    String formatedEmail =
        String.format(
            mailMessage,
            sendEmailRequested.getUsername(),
            sendEmailRequested.getCourse(),
            sendEmailRequested.getReceiptUrl());

    mailer.accept(
        new Email(
            recipientAddress,
            List.of(),
            List.of(),
            "Course regisration",
            formatedEmail,
            List.of()));
  }
}
