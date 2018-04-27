package test;

import entities.Client;
import entities.Discount;
import entities.base.DBManager;
import entities.base.utils.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class Test {
    public static void main(String[] args) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Date date = new Date(1, 2, 3);

        System.out.println(ReflectionUtils.getNewInstance(Client.class));
//        for (int i = 5; i < 10; i++) {
//            DBManager.getInstance().getConnection()
//                    .createStatement().execute("INSERT INTO paintball.discounts(id_discount,name,percent,games_count)VALUES(" + i + ",'Lol',10,20" + i + ")");
//        }
//        Client client = Client.getById(1, Client.class);
//        System.out.println(client.delete());
//        List<Discount> discounts = Discount.getAll(Discount.class);
//        System.out.println(discounts);
//        Discount discount = Discount.getById(5,Discount.class);
//        System.out.println(discount);
    }
}
