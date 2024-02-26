package com.somil.student.controller;

import com.somil.student.model.Student;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final Map<Long, Student> studentsById = new HashMap<>();
    private final Map<String, List<Student>> studentsByUniversity = new HashMap<>();

    @PostMapping("/add")
    public ResponseEntity<Long> addStudent(@RequestBody Student student) {
        Long id = studentsById.size() + 1L; // Generate ID
        student.setId(id);
        studentsById.put(id, student);

        studentsByUniversity.computeIfAbsent(student.getUniversity(), k -> new ArrayList<>()).add(student);

        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @GetMapping("/university/{university}")
    public ResponseEntity<List<Student>> getAllStudentsByUniversity(@PathVariable String university) {
        List<Student> students = studentsByUniversity.getOrDefault(university, new ArrayList<>());
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Student student = studentsById.get(id);
        if (student != null) {
            return ResponseEntity.ok(student);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
