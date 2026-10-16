package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;

@Service
public class StudentService {

    private static final Logger logger =
            LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student create(Student student) {
        logger.info("Was invoked method for create student");
        return studentRepository.save(student);
    }

    public Student get(Long id) {
        logger.info("Was invoked method for get student with ID: {}", id);
        return studentRepository.findById(id).orElse(null);
    }

    public Student update(Long id, Student student) {
        logger.info("Was invoked method for update student with ID: {}", id);
        student.setId(id);
        return studentRepository.save(student);
    }

    public void delete(Long id) {
        logger.info("Was invoked method for delete student with ID: {}", id);
        studentRepository.deleteById(id);
    }

    public List<Student> getByAge(Integer min, Integer max) {
        logger.info("Was invoked method for get students by age");
        logger.info("Age limitations: min = {}, max = {}", min, max);

        if (min != null && max != null) {
            return studentRepository.findByAgeBetween(min, max);
        }
        if (min != null) {
            return studentRepository.findByAgeGreaterThanEqual(min);
        }
        if (max != null) {
            return studentRepository.findByAgeLessThanEqual(max);
        }
        return studentRepository.findAll();
    }

    public Faculty getFaculty(Long id) {
        logger.info("Was invoked method for get faculty of student with ID: {}", id);
        Student student = studentRepository.findById(id).orElse(null);
        if (student == null) {
            logger.warn("There is no student with ID: {}", id);
            return null;
        }
        return student.getFaculty();
    }

    public long countAllStudents() {
        logger.info("Was invoked method for count all students");
        return studentRepository.countAllStudents();
    }

    public Double getAverageAge() {
        logger.info("Was invoked method for get average age of students");
        Double averageAge = studentRepository.getAverageAge();
        return averageAge != null ? averageAge : 0.0;
    }

    public List<Student> getLastFiveStudents() {
        logger.info("Was invoked method for get last five students");
        return studentRepository.getLastFiveStudents();
    }

    public List<String> getStudentsNamesWithA() {
        return studentRepository.findAll()
                .parallelStream()
                .map(Student::getName)
                .filter(name -> name.toUpperCase().startsWith("А"))
                .map(String::toUpperCase)
                .sorted()
                .toList();
    }
}
