package edu.wustl.circ.DICOMAnalysis;

import java.util.List;
import java.util.ArrayList;

public class AnalysisTool {
    private List<String> fileList = new ArrayList<>();
    private List<String> folderList = new ArrayList<>();
    private List<String> templateFileList = new ArrayList<>();
    private String outputFile = null;
    private boolean haltOnFailure = false;
    private int maximumElementCount = 1000000;

    AnalysisTool() {
    }
    public static void main(String[] theArgs) {
        System.out.println("Argument Count: " + theArgs.length);
        AnalysisTool tool = new AnalysisTool();

        int index = 0;
        while (index < theArgs.length) {
            String argument = theArgs[index++];
            switch (argument) {
                case "-h":
                    tool.setHaltOnFailure(true);
                    break;
                case "-F":
                    tool.addFolder(theArgs[index++]);
                    break;
                case "-f":
                    tool.addFile(theArgs[index++]);
                    break;
                case "-m":
                    int maxCount = Integer.parseInt(theArgs[index++]);
                    if (maxCount > 0) {
                        tool.setMaximumElementCount(maxCount);
                    }
                    break;
                case "-o":
                    tool.setOutputFile(theArgs[index++]);
                    break;
                case "-t":
                    tool.addTemplateFile(theArgs[index++]);
                    break;
                default:
                    System.out.println("Unrecognized argument: " + argument);
                    System.exit(1);
                    break;
            }
        }
        tool.execute();
    }

    public void execute() {
        Worker worker = new Worker();
        worker.execute(this);

    }

    public List<String> getFileList() {
        return fileList;
    }

    public void setFileList(List<String> fileList) {
        this.fileList = fileList;
    }

    public List<String> getFolderList() {
        return folderList;
    }

    public void setFolderList(List<String> folderList) {
        this.folderList = folderList;
    }

    public boolean isHaltOnFailure() {
        return haltOnFailure;
    }

    public void setHaltOnFailure(boolean haltOnFailure) {
        this.haltOnFailure = haltOnFailure;
    }

    public List<String> getTemplateFileList() {
        return templateFileList;
    }

    public void setTemplateFileListTemplateFile(List<String> templateFileList) {

        this.templateFileList = templateFileList;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public void addFile(String file) {
        fileList.add(file);
    }

    public void addFolder(String folder) {
        folderList.add(folder);
    }

    public void addTemplateFile(String file) {
        templateFileList.add(file);
    }

    public int getMaximumElementCount() {
        return maximumElementCount;
    }

    public void setMaximumElementCount(int maximumElementCount) {
        this.maximumElementCount = maximumElementCount;
    }
}
