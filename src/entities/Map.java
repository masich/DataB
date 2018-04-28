package entities;

import entities.base.Entity;
import entities.base.annotations.Field;
import entities.base.annotations.PrimaryKey;
import entities.base.annotations.Table;

@Table("MAPS")
public class Map extends Entity {
    @PrimaryKey("id_map")
    private Long idMap;
    @Field("name")
    private String name;
    @Field("capacity")
    private Integer capacity;
    @Field("price_hour")
    private Double pricePerHour;

    private Map() {
    }

    public Map(Long idMap, String name, Integer capacity, Double pricePerHour) {
        this.idMap = idMap;
        this.name = name;
        this.capacity = capacity;
        this.pricePerHour = pricePerHour;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Long getIdMap() {
        return idMap;
    }

    public void setIdMap(Long idMap) {
        this.idMap = idMap;
    }

    public Double getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(Double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
