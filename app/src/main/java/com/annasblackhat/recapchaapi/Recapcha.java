
package com.annasblackhat.recapchaapi;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class Recapcha {

    @SerializedName("apk_package_name")
    private String mApkPackageName;
    @SerializedName("challenge_ts")
    private String mChallengeTs;
    @SerializedName("success")
    private Boolean mSuccess;

    public String getApkPackageName() {
        return mApkPackageName;
    }

    public void setApkPackageName(String apkPackageName) {
        mApkPackageName = apkPackageName;
    }

    public String getChallengeTs() {
        return mChallengeTs;
    }

    public void setChallengeTs(String challengeTs) {
        mChallengeTs = challengeTs;
    }

    public Boolean getSuccess() {
        return mSuccess;
    }

    public void setSuccess(Boolean success) {
        mSuccess = success;
    }

}
