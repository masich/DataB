package test;

import entities.Client;
import entities.Discount;

import java.sql.Date;
import java.sql.SQLException;

public class Test {
    public static void main(String[] args) throws SQLException {
        Date date = new Date(1, 2, 3);

//        DBManager.getInstance().getConnection().createStatement().execute("INSERT INTO paintball.discounts(id_discount,name,percent,games_count)VALUES(1,'Naeb',5,1)");
        Client client = new Client();
        System.out.println(client.getById(1));
        Discount discount = new Discount();
        System.out.println(discount.getById(1));
    }
}
