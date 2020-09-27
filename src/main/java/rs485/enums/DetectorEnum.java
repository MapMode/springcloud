package rs485.enums;

public enum DetectorEnum {

    preheat("预热", "preheat", 0),

    normal("正常", "normal", 1),

    fault("故障", "fault",2),

    underreport("低报", "underreport", 3),

    higher("高报", "higher", 4);

    // 成员变量
    private String name;
    private String engName;
    private int index;

    // 构造方法
    DetectorEnum(String name, String engName, int index) {
        this.name = name;
        this.engName = engName;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (DetectorEnum c : DetectorEnum.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    // 普通方法
    public static String getNameToEngName(String engName) {
        for (DetectorEnum c : DetectorEnum.values()) {
            if (c.getEngName().equals(engName)) {
                return c.name;
            }
        }
        return null;
    }


    public static int getIndex(String name){
        for (DetectorEnum c : DetectorEnum.values()) {
            if(c.getName().equals(name)){
                return c.index;
            }
        }
        return 0;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getEngName() {
        return engName;
    }

    public void setEngName(String engName) {
        this.engName = engName;
    }



}
