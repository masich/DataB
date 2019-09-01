package com.masich.datab.examples.simple;

import com.masich.datab.DBManager;
import com.masich.datab.converter.gson.GsonConverterFactory;
import com.masich.datab.examples.simple.models.Person;
import datab.provider.SQLiteProviderFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {
        initDB();

        String maxTel = "160-332", bobTel = "161-200";
        Person max = new Person("Max", maxTel);
        Person bob = new Person("Bob", bobTel);

        max.save();
        bob.save();
        System.out.println(Person.getById(maxTel, Person.class)); //Person{name='Max', phoneNumber='160-332'}
        System.out.println(Person.getAll(Person.class));          //[Person{name='Max', phoneNumber='160-332'}, Person{name='Bob', phoneNumber='161-200'}]

        max.delete();
        System.out.println(Person.getById(maxTel, Person.class)); //null
        System.out.println(Person.getAll(Person.class));          //[Person{name='Bob', phoneNumber='161-200'}]

        bob.delete();
        System.out.println(Person.getAll(Person.class));          //[]

        List<Person> people = new ArrayList<>();
        people.add(max);
        people.add(bob);
        people.add(new Person("Lara", "163-213"));
        people.add(new Person("Julia", "162-112"));

        Person.saveAll(people);
        System.out.println(Person.getAll(Person.class));          //[Person{name='Max', phoneNumber='160-332'}, Person{name='Bob', phoneNumber='161-200'}, Person{name='Lara', phoneNumber='163-213'}, Person{name='Julia', phoneNumber='162-112'}]

        Person.deleteAll(Person.class);
        System.out.println(Person.getAll(Person.class));          //[]
    }

    private static void initDB() throws SQLException {
        DBManager manager = new DBManager.Builder()
                .addProviderFactory(new SQLiteProviderFactory())    //Provider for your type of database
                .addDatabaseSrc("sample.db")                        //Path to your database. You also can use a full path like "jdbc:sqlite:sample.db"
                .addConverterFactory(new GsonConverterFactory())    //Converter for saving custom classes (which are not DataB Entities) into database
                .build();

        DBManager.setSingleton(manager);
    }
}
