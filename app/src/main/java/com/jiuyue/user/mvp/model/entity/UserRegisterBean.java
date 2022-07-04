package com.jiuyue.user.mvp.model.entity;

public class UserRegisterBean {

    /**
     * info : {"uuid":"f37efd6d-b790-4bca-ad51-6cc72f0e7a68","headPic":"https://dss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1305353222,2352820043&fm=26&gp=0.jpg","userName":"5v3pt4yk","id":5079504,"state":0,"vipLevel":0,"vipName":"体验用户","icon":"ty","nextLevelName":"黑铁会员","nextLevel":1,"nextIcon":"ht","gold":0,"password":"123456","vipEndTime":"","vipExitDisplayNum":0,"upgradeUrl":"http://zl.i60yyb.cn/app/pkg/1/a100/io","upgradeTimes":"60,180,300,540","do_mode":1}
     */

    private InfoBean info;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {
        /**
         * icon : ty
         * nextLevelName : 黑铁会员
         * nextLevel : 1
         * userName : 6a3ecf71
         * headPic : https://dss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1305353222,2352820043&fm=26&gp=0.jpg
         * nextIcon : ht
         * uuid : 728a8a7b4a7d46da8f538548385207dc
         * gold : 0
         * vipLevel : 0
         * password : 123456
         * vipName : 体验用户
         * id : 29966873
         * state : 0
         */

        private String icon;
        private String nextLevelName;
        private int nextLevel;
        private String userName;
        private String headPic;
        private String nextIcon;
        private String uuid;
        private int gold;
        private int vipLevel;
        private String password;
        private String vipName;
        private int id;
        private int state;
        private String vipEndTime;
        private int vipExitDisplayNum;
        private int do_mode;
        private String upgradeUrl;
        private String upgradeTimes;

        public String getUpgradeUrl() {
            return upgradeUrl;
        }

        public void setUpgradeUrl(String upgradeUrl) {
            this.upgradeUrl = upgradeUrl;
        }

        public String getUpgradeTimes() {
            return upgradeTimes;
        }

        public void setUpgradeTimes(String upgradeTimes) {
            this.upgradeTimes = upgradeTimes;
        }

        public int getDo_mode() {
            return do_mode;
        }

        public void setDo_mode(int do_mode) {
            this.do_mode = do_mode;
        }

        public String getVipEndTime() {
            return vipEndTime;
        }

        public void setVipEndTime(String vipEndTime) {
            this.vipEndTime = vipEndTime;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getNextLevelName() {
            return nextLevelName;
        }

        public void setNextLevelName(String nextLevelName) {
            this.nextLevelName = nextLevelName;
        }

        public int getNextLevel() {
            return nextLevel;
        }

        public void setNextLevel(int nextLevel) {
            this.nextLevel = nextLevel;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getHeadPic() {
            return headPic;
        }

        public void setHeadPic(String headPic) {
            this.headPic = headPic;
        }

        public String getNextIcon() {
            return nextIcon;
        }

        public void setNextIcon(String nextIcon) {
            this.nextIcon = nextIcon;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public int getGold() {
            return gold;
        }

        public void setGold(int gold) {
            this.gold = gold;
        }

        public int getVipLevel() {
            return vipLevel;
        }

        public void setVipLevel(int vipLevel) {
            this.vipLevel = vipLevel;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getVipName() {
            return vipName;
        }

        public void setVipName(String vipName) {
            this.vipName = vipName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getVipExitDisplayNum() {
            return vipExitDisplayNum;
        }

        public void setVipExitDisplayNum(int vipExitDisplayNum) {
            this.vipExitDisplayNum = vipExitDisplayNum;
        }
    }
}
