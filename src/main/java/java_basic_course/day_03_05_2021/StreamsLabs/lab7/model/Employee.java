package java_basic_course.day_03_05_2021.StreamsLabs.lab7.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    private Gender gender;
    private int salary;
}