# DataB
> Simple and easy to use JDBC based ORM for Java and Kotlin.

DataB is a library for Java and Kotlin programing languages that 
can help you to store application data in database. You will have an
ability to save almost all types of data into database using internal DataB 
functionality.

**Status**: in development.
*Unstable, untested and has a meager functionality.*

## Installation
DataB is provided as a set of Maven repositories containing a lot of modules 
to construct a unique environment for every user. A list of modules and their 
descriptions are shown below.

### Modules
| Module name | Description |
| ----------- | ----------- |
| datab       | Core module. Contains all main components classes, abstract 
classes and interfaces to work with JDBC and databases. |
| datab-converters | Additional module that contains different implementations 
of the DataB [Converter](datab/src/main/java/com/masich/datab/converter/Converter.java) interface. Now this module consists of only one implementation that can convert different objects to JSON. |
| datab-providers | Additional important module that contains the implementations
 of DataB [Provider](datab/src/main/java/com/masich/datab/provider/DBProvider.java) and other components. |
| datab-examples | Module that provides examples of using a DataB library. |


As was mentioned before, the DataB is a set of Maven repositories. 
So, you can install each of them using both Maven or Gradle.

### Maven
#### From sources
1. Download sources.
2. Extract it from zip archive.
3. Go to parent source directory.
4. Open a terminal/console here and execute next command:
```shell
mvn clean install
```
This command will compile and install DataB sources into your local Maven 
repository and you will be able to use them in your projects. 

#### From central Maven repository
TODO

#### Gradle
TODO

## Getting started

### Initialization
Before using databases and any DataB core functionality it is necessary 
to initialize it. The example of a method that initializes the DataB is 
provided below.
```java
private static void initDB() throws SQLException {
    DBManager manager = new DBManager.Builder()
            //Provider for your type of database
            .addProviderFactory(new SQLiteProviderFactory())
            //Path to your database. You also can use a full path like "jdbc:sqlite:sample.db"
            .addDatabaseSrc("sample.db")
            //Converter for saving custom classes (which are not DataB Entities) into database
            .addConverterFactory(new GsonConverterFactory())
            .build();

    DBManager.setSingleton(manager);
}
```
### Data models for DataB
In DataB we define all of the database tables as a specific Java classes. 
To define class as a DataB table (called entity) you need to inherit this 
custom class from a superclass [Entity](datab\src\main\java\com\masich\datab\Entity.java) 
and annotate it using [@Table](/datab/src/main/java/com/masich/datab/annotation/Table.java) annotation. You also need to specify
table name as annotation value. For example, lets define Java class
Person associated with database table "Person" as a DataB Entity.

```java
@Table("Person")
public class Person extends Entity {

}
```

It's also required to provide an empty constructor. This thing is
necessary to create instances of the saved objects and restore
their states.

```java
public Person() {}
```

After that we can add additional fields to the Person class. Lets
define such fields as person's name and phone number. We can also say that every person has unique phone number and it's a good idea
to define it as a primary key for our table.

```java
//Name of the column in a database that will be related to this field
@Field("name")
private String name;

//Unique primary key of the table. It can be almost anything 
//but in this example we are using a phone number
@PrimaryKey("phone_number")
private String phoneNumber;
```

So, now we have defined a simple [Person](datab-examples/src/main/java/com/masich/datab/examples/simple/models/Person.java) class with a set of payload. Lets use DataB functionality to communicate with a database and Person table.  

### Using DataB features

After determing our custom DataB Entity we can start using DataB core
functionality. The first think that we need to do before we start is to invoke
our ```initDB()``` method.

```java
initDB();
```

This method will setup and initialize core DataB instances and database tables.
The SQLite database file ```sample.db``` will also be created after invoking 
this method. Now. it is time to create some Person instances and to play with
our database.

```java
String maxTel = "160-332", bobTel = "161-200";
Person max = new Person("Max", maxTel);
Person bob = new Person("Bob", bobTel);
```
We have Max and Bob as our Person class instances. And we can easily save 
them into our database using ```save()``` method:

```java
max.save();
bob.save();
```
And now our database looks as follows:

| № | name | phone_number (PK) |
|---|------|-------------------|
| 1 | Max  | 160-332           |
| 2 | Bob  | 161-200           |

We can get a particular person in our database by using ```Person.getById()```
method. This method will return a person from database with appropriate
id (Primary Key) or ```null```, if person with such id isn't in the
database table.

```java
System.out.println(Person.getById(maxTel, Person.class));
```

This will print ```Person{name='Max', phoneNumber='160-332'}``` in the console.
We also can use ```Person.getAll()``` method to get all rows in the Person database table. The result of this method is a ```List``` with all of the
table rows.

```java
System.out.println(Person.getAll(Person.class));
```

And this operation will print such information as:

```[Person{name='Max', phoneNumber='160-332'}, Person{name='Bob', phoneNumber='161-200'}]```

Of course it is possible to remove row or rows from the database. To do it
you can use either ```delete()``` for one row or ```Person.deleteAll(Person.class)```
for all rows in the database table:

```java
bob.delete()
System.out.println(Person.getById(maxTel, Person.class));
System.out.println(Person.getAll(Person.class));

Person.deleteAll(Person.class);
System.out.println(Person.getAll(Person.class));
```

And the result will be as follows:

```
null
[Person{name='Bob', phoneNumber='161-200'}]
[]
```

You can also create ```Iterable``` instance and save it in a database table
using ```Person.saveAll()```:

```java
List<Person> people = new ArrayList<>();
people.add(max);
people.add(bob);
people.add(new Person("Lara", "163-213"));
people.add(new Person("Julia", "162-112"));

Person.saveAll(people);
```

And now our Person database table looks like:

| № | name   | phone_number (PK) |
|---|--------|-------------------|
| 1 | Max    | 160-332           |
| 2 | Bob    | 161-200           |
| 3 | Lara   | 163-213           |
| 4 | Julia  | 162-112           |

And of course we can get all these rows:

```java 
System.out.println(Person.getAll(Person.class));
```
With a result such this:

```[Person{name='Max', phoneNumber='160-332'}, Person{name='Bob', phoneNumber='161-200'}, Person{name='Lara', phoneNumber='163-213'}, Person{name='Julia', phoneNumber='162-112'}]```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
