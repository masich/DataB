package entities;

import entities.base.Entity;
import entities.base.annotations.Field;
import entities.base.annotations.PrimaryKey;
import entities.base.annotations.Table;

import java.sql.Timestamp;

@Table("CLIENTS")
public class Client extends Entity<Client> {
    @PrimaryKey("id_client")
    private long idClient;
    @Field("full_name")
    private String fullName;
    @Field("birthday")
    private Timestamp birthday;
    @Field("games_count")
    private long gamesCount;

    public long getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public void setGamesCount(int gamesCount) {
        this.gamesCount = gamesCount;
    }

    @Override
    public String toString() {
        return "Client{" +
                "idClient=" + idClient +
                ", fullName='" + fullName + '\'' +
                ", birthday=" + birthday +
                ", gamesCount=" + gamesCount +
                '}';
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Timestamp getBirthday() {
        return birthday;
    }

    public void setBirthday(Timestamp birthday) {
        this.birthday = birthday;
    }

    public long getGamesCount() {
        return gamesCount;
    }

    public void setGamesCount(long gamesCount) {
        this.gamesCount = gamesCount;
    }
}
