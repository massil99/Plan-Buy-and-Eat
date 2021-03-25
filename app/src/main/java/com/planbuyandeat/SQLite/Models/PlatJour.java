package com.planbuyandeat.SQLite.Models;

public class PlatJour {
    /**
     * Date d'un jour du planning
     */
    private long dateid;

    /**
     * L'id du plat associé a cette date
     */
    private long platid;

    public PlatJour() {
    }

    /** Getters et Setter */

    public long getDateid() {
        return dateid;
    }

    public void setDateid(long dateid) {
        this.dateid = dateid;
    }

    public long getPlatid() {
        return platid;
    }

    public void setPlatid(long platid) {
        this.platid = platid;
    }
}
