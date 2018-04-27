package test;

import entities.Client;
import entities.Discount;
import entities.base.DBManager;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class Test {
    public static void main(String[] args) throws SQLException {
        Date date = new Date(1, 2, 3);

//        for (int i = 5; i < 10; i++) {
//            DBManager.getInstance().getConnection()
//                    .createStatement().execute("INSERT INTO paintball.discounts(id_discount,name,percent,games_count)VALUES(" + i + ",'Lol',10,20" + i + ")");
//        }
//        Client client = Client.getById(1, Client.class);
//        System.out.println(client.delete());
        Client client = new Client(1, "Max", new Timestamp(date.getTime()),12 );
        client.save();


        List<Client> clients = Client.getAll(Client.class);
        clients.get(0).delete();
    }
}
