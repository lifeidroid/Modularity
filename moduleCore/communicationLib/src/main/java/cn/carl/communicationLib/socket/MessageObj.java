package cn.carl.communicationLib.socket;

public class MessageObj {
    private String address;
    private String alias;
    private String tag;
    private String datas;
    private boolean isDeal;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDatas() {
        return datas;
    }

    public void setDatas(String datas) {
        this.datas = datas;
    }

    public boolean isDeal() {
        return isDeal;
    }

    public void setDeal(boolean deal) {
        isDeal = deal;
    }
}
