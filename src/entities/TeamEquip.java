package entities;

public class TeamEquip {

    private int idTeamEquip;
    private int refTeam;
    private int refEquip;
    private int equipCount;

    public int getIdTeamEquip() {
        return idTeamEquip;
    }

    public void setIdTeamEquip(int idTeamEquip) {
        this.idTeamEquip = idTeamEquip;
    }

    public int getRefTeam() {
        return refTeam;
    }

    public void setRefTeam(int refTeam) {
        this.refTeam = refTeam;
    }

    public int getRefEquip() {
        return refEquip;
    }

    public void setRefEquip(int refEquip) {
        this.refEquip = refEquip;
    }

    public int getEquipCount() {
        return equipCount;
    }

    public void setEquipCount(int equipCount) {
        this.equipCount = equipCount;
    }
}
