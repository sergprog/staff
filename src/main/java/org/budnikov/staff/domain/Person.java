package org.budnikov.staff.domain;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "person")
public class Person implements Serializable {


    private Long id;
    private String firstName;
    private String patronymic;
    private String lastName;
    private String description;
    private String department;
    private String position;
    private byte[] photo;
    private Date birthDate;
    private Set<History> histories = new HashSet<History>();

    public Person() {
    }

    public Person(Long id, String firstName, String patronymic,
                  String lastName, Date birthDate) {
        this.id = id;
        this.firstName = firstName;
        this.patronymic = patronymic;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotEmpty(message = "{validation.firstName.NotEmpty.message}")
    @Size(min = 3, max = 20, message = "{validation.firstName.Size.message}")
    @Column(name = "FIRST_NAME")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "PATRONYMIC")
    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    @NotEmpty(message = "{validation.lastName.NotEmpty.message}")
    @Size(min = 3, max = 20, message = "{validation.lastName.Size.message}")
    @Column(name = "LAST_NAME")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "DEPARTMENT")
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Column(name = "POSITION")
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Column(name = "BIRTH_DATE")
    @DateTimeFormat(pattern = "yyyy MM dd")
    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @Basic(fetch = FetchType.LAZY)
    @Lob
    @Column(name = "PHOTO")
    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    public Set<History> getHistories() {
        return this.histories;
    }

    public void setHistories(Set<History> histories) {
        this.histories = histories;
    }

    public void addHistory(History history) {
        history.setPerson(this);
        getHistories().add(history);
    }

    @Transient
    public String getBirthDateString() {
        String birthDateString = "";
        if (birthDate != null)
            birthDateString = birthDate.toString();
        return birthDateString;
    }

    @Override
    public String toString() {

        return "Person - Id: " + id +
                ", Last Name: " + lastName +
                ", First Name: " + firstName +
                ", Patronymic: " + patronymic +
                ", Description: " + description +
                ", Birth date: " + birthDate;
    }

}
