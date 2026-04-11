package com.devops.tpjenkins.service;

import com.devops.tpjenkins.entity.Student;
import com.devops.tpjenkins.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student;

    @BeforeEach
    void setUp() {
        student = Student.builder()
                .id(1L)
                .nom("Dupont")
                .prenom("Jean")
                .age(22)
                .email("jean.dupont@email.com")
                .build();
    }

    @Test
    void getAllStudents_shouldReturnList() {
        when(studentRepository.findAll()).thenReturn(Arrays.asList(student));

        List<Student> result = studentService.getAllStudents();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNom()).isEqualTo("Dupont");
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void getStudentById_shouldReturnStudent() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Student result = studentService.getStudentById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void getStudentById_shouldThrowWhenNotFound() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.getStudentById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    @Test
    void createStudent_shouldSaveAndReturn() {
        when(studentRepository.existsByEmail(student.getEmail())).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        Student result = studentService.createStudent(student);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("jean.dupont@email.com");
        verify(studentRepository).save(student);
    }

    @Test
    void createStudent_shouldThrowWhenEmailExists() {
        when(studentRepository.existsByEmail(student.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> studentService.createStudent(student))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining(student.getEmail());

        verify(studentRepository, never()).save(any());
    }

    @Test
    void deleteStudent_shouldCallDelete() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        studentService.deleteStudent(1L);

        verify(studentRepository).delete(student);
    }
}
