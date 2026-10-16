package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NoSuchElementException;

@Service
public class AvatarService {

    private static final Logger logger =
            LoggerFactory.getLogger(AvatarService.class);
    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public AvatarService(AvatarRepository avatarRepository,
                         StudentRepository studentRepository) {
        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
    }

    public Avatar upload(Long studentId, MultipartFile file) throws IOException {
        logger.info("Was invoked method for upload avatar for student with ID: {}", studentId);
        Student student = studentRepository.findById(studentId).orElseThrow(() -> {
            logger.error("No student with ID: {}", studentId);
            return new NoSuchElementException("No student with ID:" + studentId);
        });

        Path directory = Path.of("avatars");
        Files.createDirectories(directory);
        Path filePath = directory.resolve(file.getOriginalFilename());
        Files.write(filePath, file.getBytes());

        Avatar avatar = new Avatar();
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(file.getBytes());
        avatar.setStudent(student);

        return avatarRepository.save(avatar);
    }

    public Avatar get(Long id) {
        logger.info("Was invoked method for get avatar with ID: {}", id);
        return avatarRepository.findById(id).orElseThrow(() -> {
            logger.error("No any avatar with ID: {}", id);
            return new NoSuchElementException("Not found" + id);
        });
    }

    public Page<Avatar> getAll(Pageable pageable) {
        logger.info("Was invoked method for get all avatars");
        return avatarRepository.findAll(pageable);
    }
}