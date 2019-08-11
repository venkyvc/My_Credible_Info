package com.thesparksfoundation.phase2.task2.mycredibleinfo;

public class UserProfessionalData {

    private String mend_date;
    private String mOrganisation;
    private String mDesignation;
    private String mstart_date;

    public UserProfessionalData(String mend_date, String mOrganisation, String mDesignation, String mstart_date){
        this.mend_date = mend_date;
        this.mOrganisation = mOrganisation;
        this.mDesignation = mDesignation;
        this.mstart_date = mstart_date;
    }

    public String getMend_date() {
        return mend_date;
    }

    public String getmOrganisation() {
        return mOrganisation;
    }

    public String getmDesignation() {
        return mDesignation;
    }

    public String getMstart_date() {
        return mstart_date;
    }
}
