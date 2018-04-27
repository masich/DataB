package test;

import entities.Client;

import java.sql.*;
import java.util.Collections;

public class Main {
    public static void main(String[] args) throws SQLException {

//        Client client = new Client(1, );
//        client.setFullName("LOXssbf");
//        System.out.println(Client.save(client));
       /* ResultSet rs = null;
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/" + KeyStore.DB_NAME + "?" +
                    "user=" + KeyStore.LOGIN + "&password=" + KeyStore.PASSWORD);

            Field[] fields = Client.class.getDeclaredFields();
            Table table = Client.class.getAnnotation(Table.class);

            Statement select = conn.createStatement();
            rs = select.executeQuery("SELECT * FROM " + table.value() + " WHERE " + table.value() + ".id_client = 1");
//            insert.execute("INSERT INTO paintball.clients(id_client,full_name,birthday,games_count)VALUES(1,'LOX'," + "'" + date + "'" + ",1)");

            rs.next();


            System.out.println(fields.length);
//            for (Field field : fields) {
//                field.setAccessible(true);
//                for (Annotation annotation : field.getDeclaredAnnotations()) {
//                    if (annotation instanceof EntityAnnotations.PrimaryKey) {
//                        field.set(client, rs.getObject(((EntityAnnotations.PrimaryKey) annotation).value()));
//                    } else if (annotation instanceof EntityAnnotations.ForeignKey) {
//                        field.set(client, rs.getObject(((EntityAnnotations.ForeignKey) annotation).value()));
//                    } else if (annotation instanceof EntityAnnotations.Field) {
//                        field.set(client, rs.getObject(((EntityAnnotations.Field) annotation).value()));
//                    }
//                }
//            }
            conn.close();
            System.out.println(client);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}
