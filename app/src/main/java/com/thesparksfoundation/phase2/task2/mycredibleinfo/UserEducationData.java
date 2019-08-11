package com.thesparksfoundation.phase2.task2.mycredibleinfo;

public class UserEducationData {

    private String mUniversityName;
    private String mStreamName;
    private String mCityName;
    private String mStartYear;
    private String mEndYear;

    public UserEducationData(String mUniversityName, String mDegreeName, String mCityName, String mStartYear, String mEndYear){
        this.mUniversityName = mUniversityName;
        this.mStreamName = mDegreeName;
        this.mCityName = mCityName;
        this.mStartYear = mStartYear;
        this.mEndYear = mEndYear;
    }

    public String getmUniversityName() {
        return mUniversityName;
    }

    public String getmStreamName() {
        return mStreamName;
    }

    public String getmCityName() {
        return mCityName;
    }

    public String getmStartYear() {
        return mStartYear;
    }

    public String getmEndYear() {
        return mEndYear;
    }
}
