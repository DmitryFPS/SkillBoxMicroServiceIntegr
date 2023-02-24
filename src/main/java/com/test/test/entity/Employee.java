package com.test.test.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE employee SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "last_name")
    String lastName;
    @Column(name = "first_name")
    String firstName;
    @Column(name = "sur_name")
    String surName;
    Timestamp birthday;
    String position;
    @Column(name = "extension_number")
    Integer extensionNumber;
    boolean deleted;
    @Column(name = "office_id")
    Long officeId;

    public Employee(String lastName, String firstName, String surName, Timestamp birthday,
                    String position, Integer extensionNumber, boolean deleted, Long officeId) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.surName = surName;
        this.birthday = birthday;
        this.position = position;
        this.extensionNumber = extensionNumber;
        this.deleted = deleted;
        this.officeId = officeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return deleted == employee.deleted && Objects.equals(lastName, employee.lastName) && Objects.equals(firstName, employee.firstName) && Objects.equals(surName, employee.surName) && Objects.equals(birthday, employee.birthday) && Objects.equals(position, employee.position) && Objects.equals(extensionNumber, employee.extensionNumber) && Objects.equals(officeId, employee.officeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lastName, firstName, surName, birthday, position, extensionNumber, deleted, officeId);
    }
}
