package com.yuyin.conf;

public class QuestionInfo {
    private String tiMu;
    private String helpTip;

    public QuestionInfo() {
    }

    public QuestionInfo(String tiMu, String helpTip) {
        this.tiMu = tiMu;
        this.helpTip = helpTip;
    }

    public String getTiMu() {
        return tiMu;
    }

    public void setTiMu(String tiMu) {
        this.tiMu = tiMu;
    }

    public String getHelpTip() {
        return helpTip;
    }

    public void setHelpTip(String helpTip) {
        this.helpTip = helpTip;
    }
}
