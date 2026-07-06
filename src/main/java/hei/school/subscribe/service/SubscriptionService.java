package hei.school.subscribe.service;

import static java.io.File.createTempFile;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import hei.school.subscribe.endpoint.event.EventProducer;
import hei.school.subscribe.endpoint.event.model.SendEmailRequested;
import hei.school.subscribe.entity.ReceiptTemplate;
import hei.school.subscribe.exception.NotFoundException;
import hei.school.subscribe.file.bucket.BucketComponent;
import hei.school.subscribe.repository.CourseRepository;
import hei.school.subscribe.repository.SubscriptionRepository;
import hei.school.subscribe.repository.UserRepository;
import hei.school.subscribe.repository.model.JCourse;
import hei.school.subscribe.repository.model.JSubscription;
import hei.school.subscribe.repository.model.JUser;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SubscriptionService {

  private final SubscriptionRepository subscriptionRepository;
  private final UserRepository userRepository;
  private final CourseRepository courseRepository;
  private final EventProducer<SendEmailRequested> eventProducer;
  private final BucketComponent bucketComponent;

  public JSubscription subscribe(UUID userId, UUID courseId) {
    JUser user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new NotFoundException("User not found: " + userId));
    JCourse course =
        courseRepository
            .findById(courseId)
            .orElseThrow(() -> new NotFoundException("Course not found: " + courseId));

    JSubscription subscription =
        JSubscription.builder().user(user).course(course).subscribedAt(Instant.now()).build();

    JSubscription saved = subscriptionRepository.save(subscription);

    ReceiptTemplate receiptTemplate = new ReceiptTemplate(user.getUserName(), course.getTitle());

    String receiptUrl =
        uploadReceipt(
            user.getFirstName() + " " + user.getLastName(),
            course.getTitle(),
            receiptTemplate.getEmail());

    var event =
        SendEmailRequested.builder()
            .to(saved.getUser().getEmail())
            .course(course.getTitle())
            .username(user.getUserName())
            .receiptUrl(receiptUrl)
            .build();

    eventProducer.accept(List.of(event));
    return saved;
  }

  @SneakyThrows
  public String uploadReceipt(String name, String courseName, String receiptContent) {
    var fileSuffix = ".pdf";
    var filePrefix = "receipt-" + name + "-" + courseName;
    var bucketKey = filePrefix + fileSuffix;
    var fileToUpload = createTempFile(filePrefix, fileSuffix);

    byte[] pdfBytes = generatePdf(receiptContent);
    writeBytesIntoFile(pdfBytes, fileToUpload);

    bucketComponent.upload(fileToUpload, bucketKey);
    return bucketComponent.presign(bucketKey, Duration.ofMinutes(30)).toString();
  }

  private void writeBytesIntoFile(byte[] content, File file) throws IOException {
    try (FileOutputStream fos = new FileOutputStream(file)) {
      fos.write(content);
    }
  }

  private byte[] generatePdf(String htmlContent) {
    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
      PdfRendererBuilder builder = new PdfRendererBuilder();
      builder.useFastMode().withHtmlContent(htmlContent, "").toStream(outputStream);
      builder.run();
      return outputStream.toByteArray();
    } catch (IOException e) {
      throw new RuntimeException("Failed to generate PDF", e);
    }
  }
}
