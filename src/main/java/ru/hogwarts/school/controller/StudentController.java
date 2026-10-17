package ru.hogwarts.school.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    private static final Logger logger =
            LoggerFactory.getLogger(StudentController.class);

    private synchronized void printName(String name) {
        System.out.println(name);
    }

    @PostMapping
    public Student create(@RequestBody Student student) {
        return studentService.create(student);
    }

    @GetMapping("/{id}")
    public Student get(@PathVariable Long id) {
        Student student = studentService.get(id);
        if (student == null) {
            logger.error("No student with ID: {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return student;
    }

    @GetMapping("/age")
    public List<Student> getStudents(
            @RequestParam Integer min, @RequestParam Integer max) {
        return studentService.getByAge(min, max);
    }

    @GetMapping("/{id}/faculty")
    public Faculty getFaculty(@PathVariable Long id) {
        return studentService.getFaculty(id);
    }

    @GetMapping("/count")
    public long countAllStudents() {
        return studentService.countAllStudents();
    }

    @GetMapping("/age/average")
    public Double getAverageAge() {
        return studentService.getAverageAge();
    }

    @GetMapping("/last-five")
    public List<Student> getLastFiveStudents() {
        return studentService.getLastFiveStudents();
    }

    @GetMapping("/names-A")
    public List<String> getStudentsNamesWithA() {
        return studentService.getStudentsNamesWithA();
    }

    @GetMapping("/totalSumCalculator")
    public long getBackIntegerValue() {
        return studentService.getBackIntegerValue();
    }

    @GetMapping("/print-parallel")
    public void printAllStudentsParallel() {
        List<Student> students = studentService.getByAge(null, null);

        if (students.size() < 6) {
            throw new IllegalStateException("No more than 6 students");
        }
        System.out.println("main-thread: " + students.get(0).getName());
        System.out.println("main-thread: " + students.get(1).getName());

        Thread thread1 = new Thread(() -> {
            System.out.println("thread1: " + students.get(2).getName());
            System.out.println("thread1: " + students.get(3).getName());
        });

        Thread thread2 = new Thread(() -> {
            System.out.println("thread2: " + students.get(4).getName());
            System.out.println("thread2: " + students.get(5).getName());
        });

        thread1.start();
        thread2.start();
    }

    @GetMapping("/print-synchronized")
    public void printAllStudentsSynchronized() {
        List<Student> students = studentService.getByAge(null, null);

        if (students.size() < 6) {
            throw new IllegalStateException("No more than 6 students");
        }
        printName(students.get(0).getName());
        printName(students.get(1).getName());

        Thread thread1 = new Thread(() -> {
            printName(students.get(2).getName());
            printName(students.get(3).getName());
        });

        Thread thread2 = new Thread(() -> {
            printName(students.get(4).getName());
            printName(students.get(5).getName());
        });

        thread1.start();
        thread2.start();
    }

    @PutMapping("/{id}")
    public Student update(@PathVariable Long id, @RequestBody Student student) {
        return studentService.update(id, student);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        studentService.delete(id);
    }
}
