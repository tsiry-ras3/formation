package hei.school.subscribe.entity;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User {
  private UUID id;
  private String firstName;
  private String lastName;
  private String userName;
  private String email;
}
