package ru.bsuedu.cad.demo.service;


import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import ru.bsuedu.cad.demo.entity.Student;
import ru.bsuedu.cad.demo.repository.GroupRepository;
import ru.bsuedu.cad.demo.repository.StudentRepository;

@Service

public class StudentService {
    final private StudentRepository studentRepository;
    final private GroupRepository groupRepository;

    public StudentService(StudentRepository studentRepository,  GroupRepository groupRepository) {
        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
    }

    
    @Transactional
    public void createStudent(String name, int groupNumber) {
        var group = groupRepository.findByNumber(groupNumber).get(0);
        var student = new Student();
        student.setName(name);
        student.setGroup(group);
        studentRepository.save(student);
    }

}
