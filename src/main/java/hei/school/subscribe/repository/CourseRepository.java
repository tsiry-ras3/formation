package hei.school.subscribe.repository;

import hei.school.subscribe.repository.model.JCourse;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<JCourse, UUID> {}
