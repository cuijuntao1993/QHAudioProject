package com.gz.audio.entiy;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * Created by boya on 2018/9/15.
 */

public class ECG_Records extends LitePalSupport implements Serializable{

    //app数据库 ECG_Records表
    private int id;
    private int Record_ID;//记录在后台数据库中的ID，由上传请求返回
    private int Archive_ID;//测量人档案号，默认为0
    private String StartTime;//采集开始时间
    private String EndTime;//采集结束时间
    private int DeviceType; //0 手持 1 胸贴
    private int LeadsType;//1
    private String AudioFilePath;//音频文件存储路径
    private String RawEDFFilePath;//原始心电信号EDF文件路径
    private String ProcessedEDFFilePath;//处理后心电信号EDF文件路径

    private int State;//0 未上传 1 已上传 2 已完成
    private String Diagnose_abstract;//诊断结果摘要，默认为空
    private String Diagnose_details;//诊断结果详情，默认为空
    private String Note;// 备注

    private String PhoneNumber;
    private byte[] XinDianByShort;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecord_ID() {
        return Record_ID;
    }

    public void setRecord_ID(int record_ID) {
        Record_ID = record_ID;
    }

    public int getArchive_ID() {
        return Archive_ID;
    }

    public void setArchive_ID(int archive_ID) {
        Archive_ID = archive_ID;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public int getDeviceType() {
        return DeviceType;
    }

    public void setDeviceType(int deviceType) {
        DeviceType = deviceType;
    }

    public int getLeadsType() {
        return LeadsType;
    }

    public void setLeadsType(int leadsType) {
        LeadsType = leadsType;
    }

    public String getAudioFilePath() {
        return AudioFilePath;
    }

    public void setAudioFilePath(String audioFilePath) {
        AudioFilePath = audioFilePath;
    }

    public String getRawEDFFilePath() {
        return RawEDFFilePath;
    }

    public void setRawEDFFilePath(String rawEDFFilePath) {
        RawEDFFilePath = rawEDFFilePath;
    }

    public String getProcessedEDFFilePath() {
        return ProcessedEDFFilePath;
    }

    public void setProcessedEDFFilePath(String processedEDFFilePath) {
        ProcessedEDFFilePath = processedEDFFilePath;
    }

    public int getState() {
        return State;
    }

    public void setState(int state) {
        State = state;
    }

    public String getDiagnose_abstract() {
        return Diagnose_abstract;
    }

    public void setDiagnose_abstract(String diagnose_abstract) {
        Diagnose_abstract = diagnose_abstract;
    }

    public String getDiagnose_details() {
        return Diagnose_details;
    }

    public void setDiagnose_details(String diagnose_details) {
        Diagnose_details = diagnose_details;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }




    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public byte[] getXinDianByShort() {
        return XinDianByShort;
    }

    public void setXinDianByShort(byte[] xinDianByShort) {
        XinDianByShort = xinDianByShort;
    }


}
