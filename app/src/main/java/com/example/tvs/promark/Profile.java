package com.example.tvs.promark;

public class Profile {

    int id, active;
    String name;
    Trigger trigger;
    Action action;

    public Profile() {
        this.trigger = new Trigger();
        this.action = new Action();
    }

    public Profile(String name, int active, Trigger trigger, Action action) {
        this.name = name;
        this.active = active;
        this.trigger = trigger;
        this.action = action;
    }

    public static class Trigger {
        public int wiFi, cellNetwork;
        public String fromTime, toTime, wifiId, cellNetworkId;
        public int minCharge, maxCharge;

        public Trigger() {
        }

        public Trigger(int wiFi, int cellNetwork, String fromTime, String toTime, String wifiId, String cellNetworkId, int minCharge, int maxCharge) {
            this.wiFi = wiFi;
            this.cellNetwork = cellNetwork;
            this.fromTime = fromTime;
            this.toTime = toTime;
            this.wifiId = wifiId;
            this.cellNetworkId = cellNetworkId;
            this.minCharge = minCharge;
            this.maxCharge = maxCharge;
        }
    }

    public static class Action {
        public int wiFi, bluetooth, autoBrightness;
        public int ringingVolume;

        public Action() {
        }

        public Action(int wiFi, int bluetooth, int autoBrightness, int ringingVolume) {
            this.wiFi = wiFi;
            this.bluetooth = bluetooth;
            this.autoBrightness = autoBrightness;
            this.ringingVolume = ringingVolume;
        }
    }
}
