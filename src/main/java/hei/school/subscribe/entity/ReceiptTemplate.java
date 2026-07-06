package hei.school.subscribe.entity;

import lombok.Getter;

@Getter
public class ReceiptTemplate {

  private String email;

  public ReceiptTemplate(String username, String courseName) {
    String baseEmail =
        """
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0"/>
<title>Course Registration Receipt</title>
</head>

<body style="margin: 0; padding: 0; background-color: #f4f7f6; font-family: Arial, Helvetica, sans-serif; color: #333333; -webkit-font-smoothing: antialiased;">

<table role="presentation" width="100%%" cellspacing="0" cellpadding="0" border="0" style="background-color: #f4f7f6; padding: 40px 20px;">
    <tr>
    <td align="center">

        <table role="presentation" width="600" cellspacing="0" cellpadding="0" border="0" style="background-color: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 6px rgba(0,0,0,0.05);">

        <tr>
            <td style="background-color: #2563eb; padding: 30px; text-align: center; color: #ffffff;">
            <h1 style="margin: 0; font-size: 24px; font-weight: bold; letter-spacing: 1px;">YOUR COMPANY</h1>
            <p style="margin: 5px 0 0; font-size: 14px; opacity: 0.9;">Course Registration Receipt</p>
            </td>
        </tr>

        <tr>
            <td style="padding: 40px 40px 20px;">
            <div style="display: inline-block; background-color: #dcfce7; color: #166534; padding: 6px 14px; border-radius: 20px; font-size: 13px; font-weight: bold; margin-bottom: 20px;">
                ✓ Registration Successful
            </div>

            <h2 style="margin: 0 0 15px; font-size: 22px; color: #111827;">Hi %s,</h2>

            <p style="margin: 0; font-size: 16px; line-height: 1.6; color: #4b5563;">
                You're successfully registered for <strong style="color: #111827;">
            %s</strong>.
                Please find the details of your registration below.
            </p>
            </td>
        </tr>

        <tr>
            <td style="padding: 0 40px 30px;">
            <table width="100%%" cellspacing="0" cellpadding="0" border="0" style="border: 1px solid #e5e7eb; border-radius: 8px; overflow: hidden;">
                <tr>
                <td colspan="2" style="background-color: #f9fafb; padding: 15px 20px; font-size: 16px; font-weight: bold; color: #374151; border-bottom: 1px solid #e5e7eb;">
                    Registration Details
                </td>
                </tr>
                <tr>
                <td style="padding: 15px 20px; font-size: 14px; color: #6b7280; border-bottom: 1px solid #f3f4f6; width: 40%%;">Course</td>
                <td style="padding: 15px 20px; font-size: 14px; color: #111827; font-weight: bold; border-bottom: 1px solid #f3f4f6;">%s</td>
                </tr>
                <tr>
                <td style="padding: 15px 20px; font-size: 14px; color: #6b7280; border-bottom: 1px solid #f3f4f6;">Status</td>
                <td style="padding: 15px 20px; font-size: 14px; color: #166534; font-weight: bold;">Confirmed</td>
                </tr>
                <tr>
                <td style="padding: 15px 20px; font-size: 14px; color: #6b7280;">Registration Date</td>
                <td style="padding: 15px 20px; font-size: 14px; color: #111827;">July 06, 2026</td>
                </tr>
            </table>
            </td>
        </tr>

        <tr>
            <td style="background-color: #f9fafb; padding: 30px 40px; text-align: center; border-top: 1px solid #e5e7eb;">
            <p style="margin: 0 0 10px; font-size: 13px; color: #6b7280;">
                If you have any questions, reply to this email or contact us at <a href="mailto:support@please.dont.email.us.back.com" style="color: #2563eb; text-decoration: none;">support@yourcompany.com</a>
            </p>
            <p style="margin: 0; font-size: 12px; color: #9ca3af;">
                © 2026 Your Company. All rights reserved.
            </p>
            </td>
        </tr>

        </table>
    </td>
    </tr>
</table>

</body>
</html>
""";

    this.email = String.format(baseEmail, username, courseName, courseName);
  }
}
