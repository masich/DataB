package entities;

import entities.base.Entity;
import entities.base.annotations.Field;
import entities.base.annotations.PrimaryKey;
import entities.base.annotations.Table;

@Table("DISCOUNTS")
public class Discount extends Entity<Discount> {
    @PrimaryKey("id_discount")
    private long idDiscount;
    @Field("name")
    private String name;
    @Field("percent")
    private long percent;
    @Field("games_count")
    private long gamesCount;

    public long getIdDiscount() {
        return idDiscount;
    }

    public void setIdDiscount(long idDiscount) {
        this.idDiscount = idDiscount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public long getGamesCount() {
        return gamesCount;
    }

    public void setGamesCount(int gamesCount) {
        this.gamesCount = gamesCount;
    }

    @Override
    public String toString() {
        return "Discount{" +
                "idDiscount=" + idDiscount +
                ", name='" + name + '\'' +
                ", percent=" + percent +
                ", gamesCount=" + gamesCount +
                '}';
    }
}
