package entities;

import entities.base.Entity;
import entities.base.annotations.Field;
import entities.base.annotations.PrimaryKey;
import entities.base.annotations.Table;

@Table("DISCOUNTS")
public class Discount extends Entity<Discount> {
    @PrimaryKey("id_discount")
    private Long idDiscount;
    @Field("name")
    private String name;
    @Field("percent")
    private Integer percent;
    @Field("games_count")
    private Integer gamesCount;

    public Long getIdDiscount() {
        return idDiscount;
    }

    public void setIdDiscount(Long idDiscount) {
        this.idDiscount = idDiscount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
    }

    public Integer getGamesCount() {
        return gamesCount;
    }

    public void setGamesCount(Integer gamesCount) {
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
