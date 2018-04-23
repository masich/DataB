package test;

import entities.Client;
import entities.Discount;
import entities.base.Entity;

import java.sql.Date;
import java.sql.SQLException;

public class Test {
    public static void main(String[] args) throws SQLException {
        Date date = new Date(1, 2, 3);

//        DBManager.getInstance().getConnection().createStatement().execute("INSERT INTO paintball.discounts(id_discount,name,percent,games_count)VALUES(1,'Naeb',5,1)");
        Client client = new Client();
        System.out.println(Client.getById(1, Client.class));
        Discount discount = Discount.getById(1, Discount.class);
        System.out.println(Discount.getById(1, Discount.class));
    }
}
