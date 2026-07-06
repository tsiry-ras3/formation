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

    String mailMessage = """
        <!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Course Registration</title>
</head>
<body style="margin:0; padding:20px; font-family:Arial, Helvetica, sans-serif;">

  <p>Hi {{studentName}},</p>

  <p>You're successfully registered for <strong>{{courseName}}</strong>.</p>

  <table cellpadding="0" cellspacing="0" border="0" style="margin:20px 0;">
    <tr><td style="padding:4px 0;"><strong>Start Date:</strong> {{startDate}}</td></tr>
    <tr><td style="padding:4px 0;"><strong>Duration:</strong> {{duration}}</td></tr>
    <tr><td style="padding:4px 0;"><strong>Instructor:</strong> {{instructorName}}</td></tr>
  </table>

  <p>Next steps:</p>
  <ol>
    <li>Check your materials in the student portal</li>
    <li>Join the community discussion group</li>
    <li>Mark your calendar for the first session</li>
  </ol>

  <p style="margin:30px 0;">
    <a href="{{portalUrl}}" style="text-decoration:underline;">Access Student Portal</a>
  </p>

  <p>Questions? Contact us at <a href="mailto:support@yourdomain.com">support@yourdomain.com</a>.</p>

  <hr style="border:none; border-top:1px solid #cccccc; margin:30px 0;">

  <p style="font-size:12px; color:#666666;">
    {{companyName}} | {{companyAddress}}<br>
    <a href="{{unsubscribeUrl}}" style="color:#666666;">Unsubscribe</a>
  </p>

</body>
</html>
        """;

    mailer.accept(
        new Email(
            recipientAddress, List.of(), List.of(), "", "SUCCESSFULL SUBSCRIPTION", List.of()));
  }
}
