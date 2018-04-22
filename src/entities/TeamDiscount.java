package entities;

public class TeamDiscount {
    private int idTeamDiscount;
    private int refTeam;
    private int refDiscount;

    public int getIdTeamDiscount() {
        return idTeamDiscount;
    }

    public void setIdTeamDiscount(int idTeamDiscount) {
        this.idTeamDiscount = idTeamDiscount;
    }

    public int getRefTeam() {
        return refTeam;
    }

    public void setRefTeam(int refTeam) {
        this.refTeam = refTeam;
    }

    public int getRefDiscount() {
        return refDiscount;
    }

    public void setRefDiscount(int refDiscount) {
        this.refDiscount = refDiscount;
    }
}
