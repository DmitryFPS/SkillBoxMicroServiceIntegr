package com.test.test.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Office {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Integer number;
    @Column(name = "phone_office")
    Integer phoneOffice;
    String address;
    boolean deleted;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "office_id")
    @ToString.Exclude
    List<Employee> employees;

    public Office(Integer number, Integer phoneOffice, String address, boolean deleted) {
        this.number = number;
        this.phoneOffice = phoneOffice;
        this.address = address;
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Office office = (Office) o;
        return deleted == office.deleted && Objects.equals(number, office.number) && Objects.equals(phoneOffice, office.phoneOffice) && Objects.equals(address, office.address) && Objects.equals(employees, office.employees);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, phoneOffice, address, deleted, employees);
    }
}
