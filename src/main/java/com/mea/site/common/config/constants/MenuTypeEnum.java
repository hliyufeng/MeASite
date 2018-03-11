package com.mea.site.common.config.constants;

/**
 * Created by lenovo on 2018/2/27.
 */
public enum MenuTypeEnum {
    CATALOG("目录", "1"),
    MENU("菜单","2"),
    BUTTON("按钮","3"),
    URL("链接", "4");

    private String label;
    private String type;

    MenuTypeEnum() {
    }

    public static String getLabel(String type) {
        for (MenuTypeEnum c : MenuTypeEnum.values()) {
            if (c.getType().equals(type)) {
                return c.getLabel();
            }
        }
        return null;
    }

    MenuTypeEnum(String label, String type) {
        this.label = label;
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
