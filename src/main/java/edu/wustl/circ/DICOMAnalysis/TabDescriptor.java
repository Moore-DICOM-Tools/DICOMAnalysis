package edu.wustl.circ.DICOMAnalysis;

import java.util.List;

public class TabDescriptor {
    private String tabName;
    private List<TabRowDescriptor> tabRowDescriptors;

    TabDescriptor() {
        this.tabName = null;
        this.tabRowDescriptors = null;
    }

    TabDescriptor(String tabName, List<TabRowDescriptor> tabRowDescriptors) {
        this.tabName = tabName;
        this.tabRowDescriptors = tabRowDescriptors;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public List<TabRowDescriptor> getTabRowDescriptors() {
        return tabRowDescriptors;
    }

    public void setTabRowDescriptors(List<TabRowDescriptor> tabRowDescriptors) {
        this.tabRowDescriptors = tabRowDescriptors;
    }
}
