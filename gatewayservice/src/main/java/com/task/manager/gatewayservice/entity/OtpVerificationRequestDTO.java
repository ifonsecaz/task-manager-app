package com.task.manager.gatewayservice.entity;

public class OtpVerificationRequestDTO {
    private String username;
    private String mfaOtp;

    public OtpVerificationRequestDTO(String username, String mfaOtp){
        this.username=username;
        this.mfaOtp=mfaOtp;
    }

    public String getUsername(){
        return username;
    }

    public String getMfaOtp(){
        return mfaOtp;
    }
}
