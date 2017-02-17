package org.budnikov.staff.domain;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "history")
public class History implements Serializable {

    private Long id;
    private String rec;
    private Person person;
    private Date whenDate;

    public History() {
    }

    public History(String rec) {
        this.rec = rec;
    }

    public History(Long id, String rec) {
        this.id = id;
        this.rec = rec;
    }

    public History(Long id, String rec, Date whenDate) {
        this.id = id;
        this.rec = rec;
        this.whenDate = whenDate;
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

    @NotEmpty(message = "{validation.rec.NotEmpty.message}")
    @Size(min = 3, max = 2000, message = "{validation.rec.Size.message}")
    @Column(name = "REC")
    public String getRec() {
        return rec;
    }

    public void setRec(String rec) {
        this.rec = rec;
    }

    @Column(name = "WHEN_DATE")
    @DateTimeFormat(pattern = "yyyy MM dd")
    public Date getWhenDate() {
        return whenDate;
    }

    public void setWhenDate(Date whenDate) {
        this.whenDate = whenDate;
    }

    @ManyToOne
    @JoinColumn(name = "PERSON_ID")
    public Person getPerson() {
        return this.person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Transient
    public String getWhenDateString() {
        String whenDateString = "";
        if (whenDate != null)
            whenDateString = whenDate.toString();
        return whenDateString;
    }

    @Override
    public String toString() {
        return "History Id: " + id +
                ", When: " + whenDate +
                ", Record: " + rec +
                ", Person: " + person.getLastName();
    }

}
