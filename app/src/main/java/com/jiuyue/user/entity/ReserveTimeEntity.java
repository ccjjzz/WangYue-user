package com.jiuyue.user.entity;

import java.util.List;

public class ReserveTimeEntity {
    private String date;
    private String dateTitle;
    private String weekDay;
    private List<TimesDTO> times;
    private boolean checked;

    public String getDate() {
        return date == null ? "" : date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDateTitle() {
        return dateTitle == null ? "" : dateTitle;
    }

    public void setDateTitle(String dateTitle) {
        this.dateTitle = dateTitle;
    }

    public String getWeekDay() {
        return weekDay == null ? "" : weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public List<TimesDTO> getTimes() {
        return times;
    }

    public void setTimes(List<TimesDTO> times) {
        this.times = times;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public static class TimesDTO {
        private String time;
        private int status;
        private boolean checked;

        public String getTime() {
            return time == null ? "" : time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }
    }
}
