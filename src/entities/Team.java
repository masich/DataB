package entities;

public class Team {
    private Long idTeam;
    private Client captain;
    private Integer playersCount;
    private Game game;

    public Client getCaptain() {
        return captain;
    }

    public void setCaptain(Client captain) {
        this.captain = captain;
    }

    public Long getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(Long idTeam) {
        this.idTeam = idTeam;
    }

    public void setPlayersCount(Integer playersCount) {
        this.playersCount = playersCount;
    }

    public Integer getPlayersCount() {
        return playersCount;
    }


    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
