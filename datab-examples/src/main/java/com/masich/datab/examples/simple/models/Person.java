package com.masich.datab.examples.simple.models;


import com.masich.datab.Entity;
import com.masich.datab.annotation.Field;
import com.masich.datab.annotation.PrimaryKey;
import com.masich.datab.annotation.Table;

@Table("Person") //Name of the table that will be related to this class
public class Person extends Entity {
    @Field("name") //Name of the column in a database that will be related to this field
    private String name;

    @PrimaryKey("phone_number") //Unique primary key of the table. It can be almost anything. In this example we are using a phone number
    private String phoneNumber;

    public Person(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    //The mandatory empty constructor
    public Person() {
    }
}
