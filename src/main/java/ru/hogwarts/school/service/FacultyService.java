package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.List;

@Service
public class FacultyService {

    private static final Logger logger =
            LoggerFactory.getLogger(FacultyService.class);
    private final FacultyRepository facultyRepository;

    @Autowired
    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty create(Faculty faculty) {
        logger.info("Was invoked method for create faculty");
        return facultyRepository.save(faculty);
    }

    public Faculty get(Long id) {
        logger.info("Was invoked method for get faculty with ID: {}", id);
        return facultyRepository.findById(id).orElse(null);
    }

    public Faculty update(Long id, Faculty faculty) {
        logger.info("Was invoked method for update faculty with ID: {}", id);
        faculty.setId(id);
        return facultyRepository.save(faculty);
    }

    public void delete(Long id) {
        logger.info("Was invoked method for delete faculty with ID: {}", id);
        facultyRepository.deleteById(id);
    }

    public List<Faculty> getByNameOrColor(String name, String color) {
        logger.info("Was invoked method for get faculties by name or color");
        logger.info("Search by parameters: name = {}, color = {}", name, color);
        if (name != null && color != null) {
            return facultyRepository.findByNameIgnoreCaseAndColorIgnoreCase(name, color);
        }
        if (name != null) {
            return facultyRepository.findByNameIgnoreCase(name);
        }
        if (color != null) {
            return facultyRepository.findByColorIgnoreCase(color);
        }
        return facultyRepository.findAll();
    }

    public List<Faculty> getByNameOrColor(String name) {
        logger.info("Was invoked method for search faculties by name or color");
        logger.info("Search by parameter: name = {}", name);
        if (name != null) {
            return facultyRepository
                    .findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(name, name);
        }
        return facultyRepository.findAll();
    }

    public List<Student> getStudents(Long facultyId) {
        logger.info("Was invoked method for get students of faculty with ID: {}", facultyId);
        Faculty faculty = facultyRepository.findById(facultyId).orElse(null);
        if (faculty == null) {
            logger.warn("There is no faculty with ID: {}", facultyId);
            return null;
        }
        return faculty.getStudents();
    }
}