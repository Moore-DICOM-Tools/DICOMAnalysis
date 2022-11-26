package edu.wustl.circ.DICOMAnalysis;

import java.util.List;

public class TabRowDescriptor {
    private String label;
    private String attributeName;
    private String tags;
    private String type;
    private String color;

    TabRowDescriptor() {
        this.label = null;
        this.attributeName = null;
        this.tags = null;
        this.type = null;
        this.color = null;
    }

    TabRowDescriptor(String label, String attributeName, String tags, String type, String color) {
        this.label = label;
        this.attributeName = attributeName;
        this.tags = tags;
        this.type = type;
        this.color = color;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
