package test;

import entities.Client;
import entities.Discount;
import entities.base.DBManager;

import java.sql.Date;
import java.sql.SQLException;

public class Test {
    public static void main(String[] args) throws SQLException {
        Date date = new Date(1, 2, 3);

//        DBManager.getInstance().getConnection().createStatement().execute("INSERT INTO paintball.discounts(id_discount,name,percent,games_count)VALUES(2,'Lol',10,200)");
//        Client client = Client.getById(1, Client.class);
//        System.out.println(client.delete());
        Discount discount = Discount.getById(2, Discount.class);
        System.out.println(discount);
    }
}
