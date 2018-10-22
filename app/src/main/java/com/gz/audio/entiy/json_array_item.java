package com.gz.audio.entiy;

/**
 * Created by boya on 2018/10/6.
 */

public class json_array_item {
    private int record_id;
    private int state;
    private String diagnose_abstract;
    private String diagnose_details;

    public int getRecord_id() {
        return record_id;
    }

    public void setRecord_id(int record_id) {
        this.record_id = record_id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getDiagnose_abstract() {
        return diagnose_abstract;
    }

    public void setDiagnose_abstract(String diagnose_abstract) {
        this.diagnose_abstract = diagnose_abstract;
    }

    public String getDiagnose_details() {
        return diagnose_details;
    }

    public void setDiagnose_details(String diagnose_details) {
        this.diagnose_details = diagnose_details;
    }
}
