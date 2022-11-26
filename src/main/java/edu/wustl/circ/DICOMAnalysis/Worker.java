package edu.wustl.circ.DICOMAnalysis;

import org.apache.xmlbeans.impl.xb.xsdschema.BlockSet;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.ItemPointer;
import org.dcm4che3.data.Sequence;
import org.dcm4che3.data.Tag;
import org.dcm4che3.io.DicomInputStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Worker {
    private ExtractData extractData = null;
    private List<TabDescriptor> tabDescriptors = null;
    private Set<String> tagStrings = null;

    Worker() {
    }

    public Worker(ExtractData extractData) {
        this.extractData = extractData;
    }

    public ExtractData getExtractData() {
        return extractData;
    }

    public void setExtractData(ExtractData extractData) {
        this.extractData = extractData;
    }

    public ExtractData execute(AnalysisTool analysisTool) {
        extractData = new ExtractData();
        try {
            readTemplate(analysisTool);
            processFiles(analysisTool);
            writeOutput(analysisTool);
        } catch (Exception e) {
            System.out.println("Exception encountered during execution");
            e.printStackTrace();
        }
        return extractData;
    }

    private void readTemplate(AnalysisTool analysisTool) throws Exception {
        tabDescriptors = new ArrayList<>();
        tagStrings = new HashSet<>();
        ObjectMapper mapper = new JsonMapper();

        List<String> templateFileList = analysisTool.getTemplateFileList();
        for (String templateFile : templateFileList) {
            TabDescriptor[] descriptors = mapper.readValue(new File(templateFile), TabDescriptor[].class);
            for (TabDescriptor tabDescriptor : descriptors) {
                tabDescriptors.add(tabDescriptor);
                for (TabRowDescriptor tabRowDescriptor : tabDescriptor.getTabRowDescriptors()) {
                    tagStrings.add(tabRowDescriptor.getTags());
                }
            }
        }
    }

    private void processFiles (AnalysisTool analysisTool) throws Exception {
        Iterator<String> iterator = analysisTool.getFileList().iterator();
        while (iterator.hasNext()) {
            String fileName = iterator.next();
            System.out.println("File: " + fileName);
            processFile(fileName);
        }
        iterator = analysisTool.getFolderList().iterator();
        while (iterator.hasNext()) {
            String folderName = iterator.next();
            System.out.println("Folder: " + folderName);
            List<Path> filesInThisFolder = new ArrayList<>();

            Files.walk(Paths.get(folderName))
                    .filter(Files::isRegularFile)
                    .forEach(filesInThisFolder::add);
            Iterator<Path> pathIterator = filesInThisFolder.iterator();
            while (pathIterator.hasNext()) {
                Path path = pathIterator.next();
                processFile(path.toFile());
            }
        }

    }

    private void processFile (String fileName) throws Exception {
        this.processFile(new File(fileName));
    }

    private void processFile (File file) throws Exception {
        DicomInputStream din = null;
        try {
            din = new DicomInputStream(file);
            Attributes metadata = din.readFileMetaInformation();
            Attributes dataSet = din.readDataset(-1, -1);
            String instanceUID = dataSet.getString(Tag.SOPInstanceUID);
//            System.out.println("  " + instanceUID);

//        List<TabRowDescriptor> tabRowDescriptors = tabDescriptor.getTabRowDescriptors();
//        Iterator<TabRowDescriptor> it = tabRowDescriptors.iterator();
            for (String tagString : tagStrings) {
                if (tagString.length() == 0) {
                } else if (tagString.contains(":")) {
                    addExtractedElement(dataSet, tagString, tagString);

                } else {
                    int tagInteger = Integer.parseInt(tagString.replace(",", ""), 16);
                    addExtractedElement(dataSet, tagInteger, tagString);
                }
            }

            din.close();
        } catch (Exception e) {
            System.out.println("Exception when reading DICOM file: " + file.getAbsolutePath());
        } finally {
            if (din != null) {
                din.close();
            }
        }
    }

    private void addExtractedElement (Attributes dataSet, String dicomTags, String label){
        String[] tokens = dicomTags.split(":");
        String extract = "";
        List<ItemPointer> itemPointers = new ArrayList<>();
        for (int i = 0; i < tokens.length - 1; i++) {
            String token = tokens[i];
            int tagInteger = Integer.parseInt(token.replace(",", ""), 16);
            itemPointers.add(new ItemPointer(tagInteger, 0));
        }
        dataSet = dataSet.getNestedDataset(itemPointers);
        if (dataSet == null) {
            return;
        }
        String lastToken = tokens[tokens.length - 1];
        String s = dataSet.getString(Integer.parseInt(lastToken.replace(",", ""), 16));
        extractData.put(label, s);

    }

    private void addExtractedElement (Attributes dataSet, int tag, String label) {
        String delim = "";
        String[] valueArray = dataSet.getStrings(tag);
        String s = "";
        if (valueArray != null) {
            for (int index = 0; index < valueArray.length; index++) {
                s += delim + valueArray[index];
                delim = "\\";
            }
        }


        /*
        String s = dataSet.getString(tag);
        if (tag == 0x00200037) {
            String z = "";
            String y = "";
            String[] b = dataSet.getStrings(tag);
            for (int index = 0; dataSet.getString(tag, index) != null; index++) {
                z += dataSet.getString(tag, index);
                y = z;
            }
            String a = y;
        }

         */
        extractData.put(label, s);
    }

    private void writeOutput (AnalysisTool analysisTool) throws Exception {
        SpreadsheetExport spreadsheetExport = new SpreadsheetExport();
        spreadsheetExport.exportExtract(analysisTool, tabDescriptors, extractData);
    }

    private void xxtest () throws Exception {
        TabRowDescriptor trd1 = new TabRowDescriptor("Patient ID", "Patient Name", "0010,0020", "2", "");
        TabRowDescriptor trd2 = new TabRowDescriptor("Patient Name", "Patient Identifier", "0010,0010", "2", "");
        List<TabRowDescriptor> tabRowDescriptors = new ArrayList<TabRowDescriptor>();
        tabRowDescriptors.add(trd1);
        tabRowDescriptors.add(trd2);
        TabDescriptor td = new TabDescriptor("Study", tabRowDescriptors);

        ObjectMapper mapper = new JsonMapper();

        mapper.writeValue(new File("/tmp/xxx.txt"), td);
    }
}
