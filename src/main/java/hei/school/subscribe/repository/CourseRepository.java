package hei.school.subscribe.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import hei.school.subscribe.repository.model.JCourse;

public interface CourseRepository extends JpaRepository<JCourse, UUID> {
}
