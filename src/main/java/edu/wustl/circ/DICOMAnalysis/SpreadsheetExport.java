package edu.wustl.circ.DICOMAnalysis;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.SheetBuilder;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SpreadsheetExport {
    private CellStyle yellowCellStyle = null;
    private CellStyle greenCellStyle  = null;
    private CellStyle blueCellStyle   = null;
    private CellStyle greyCellStyle   = null;
    private CellStyle whiteCellStyle  = null;
    private CellStyle orangeCellStyle = null;
    public void exportExtract(AnalysisTool analysisTool, List<TabDescriptor> tabDescriptors, ExtractData extractData) throws Exception {
        Workbook w = new XSSFWorkbook();
        int maxCount = analysisTool.getMaximumElementCount();
        setCellStyles(w);
        for (TabDescriptor tabDescriptor: tabDescriptors) {
            Sheet sheet = w.createSheet(tabDescriptor.getTabName());

            List<TabRowDescriptor> tabRowDescriptors = tabDescriptor.getTabRowDescriptors();
            int rowIndex = 0;
            setTableHeader(sheet, rowIndex++);
            for (TabRowDescriptor row: tabRowDescriptors) {
                CellStyle rowCellStyle = deriveCellStyle(row.getColor());

                Set<String> dataValues = extractData.getValues(row.getTags());
                if (dataValues == null) {
                    dataValues = new HashSet<>();
                    dataValues.add("Attribute not included in DICOM data");
                }

                int localValueCount = 0;
                for (String value: dataValues) {
                    Row tableRow = sheet.createRow(rowIndex++);
                    int cellIndex = 0;
                    Cell cellLabel = tableRow.createCell(cellIndex++);
                    Cell attributeName = tableRow.createCell(cellIndex++);
                    Cell cellTags  = tableRow.createCell(cellIndex++);
                    Cell cellType = tableRow.createCell(cellIndex++);
                    Cell cellValue = tableRow.createCell(cellIndex++);

                    cellLabel.setCellStyle(rowCellStyle);
                    attributeName.setCellStyle(rowCellStyle);
                    cellTags.setCellStyle(rowCellStyle);
                    cellType.setCellStyle(rowCellStyle);
                    cellValue.setCellStyle(rowCellStyle);

                    cellLabel.setCellValue(row.getLabel());
                    attributeName.setCellValue(row.getAttributeName());
                    cellTags.setCellValue(row.getTags());
                    cellType.setCellValue(row.getType());
                    cellValue.setCellValue(value);

                    ++localValueCount;

                    if (localValueCount >= maxCount && dataValues.size() > localValueCount) {
                        int deltaValues = dataValues.size() - localValueCount;
                        Row truncationRow = sheet.createRow(rowIndex++);
                        populateTruncationRow(truncationRow, row, deltaValues);
                        break;
                    }
                }
            }
        }

        FileOutputStream fos = new FileOutputStream(new File(analysisTool.getOutputFile()));
        w.write(fos);
        fos.close();
    }

    private void populateTruncationRow(Row truncationRow, TabRowDescriptor row, int delta) {
        int cellIndex = 0;
        Cell cellLabel = truncationRow.createCell(cellIndex++);
        Cell attributeName = truncationRow.createCell(cellIndex++);
        Cell cellTags  = truncationRow.createCell(cellIndex++);
        Cell cellType = truncationRow.createCell(cellIndex++);
        Cell cellValue = truncationRow.createCell(cellIndex++);

        cellLabel.setCellStyle(orangeCellStyle);
        attributeName.setCellStyle(orangeCellStyle);
        cellTags.setCellStyle(orangeCellStyle);
        cellType.setCellStyle(orangeCellStyle);
        cellValue.setCellStyle(orangeCellStyle);

        cellLabel.setCellValue(row.getLabel());
        attributeName.setCellValue(row.getAttributeName());
        cellTags.setCellValue(row.getTags());
        cellType.setCellValue(row.getType());
        cellValue.setCellValue("Truncated " + Integer.toString(delta) + " remaining values");
    }

    private CellStyle deriveCellStyle(String color) {
        CellStyle rtn = whiteCellStyle;
        if (color != null) {
            switch (color) {
                case "yellow":
                    rtn = yellowCellStyle;
                    break;
                case "green":
                    rtn = greenCellStyle;
                    break;
                case "blue":
                    rtn = blueCellStyle;
                    break;
                case "grey":
                    rtn = greyCellStyle;
                    break;
                case "white":
                    rtn = whiteCellStyle;
                    break;
                case "orange":
                    rtn = orangeCellStyle;
                    break;
                default:
                    break;
            }
        }
        return rtn;
    }

    private void setCellStyles(Workbook w) {
        yellowCellStyle = createOneCellStyle(w, IndexedColors.YELLOW.getIndex());
        greenCellStyle  = createOneCellStyle(w, IndexedColors.BRIGHT_GREEN.getIndex());
        blueCellStyle   = createOneCellStyle(w, IndexedColors.CORNFLOWER_BLUE.getIndex());
        greyCellStyle   = createOneCellStyle(w, IndexedColors.GREY_25_PERCENT.getIndex());
        whiteCellStyle  = createOneCellStyle(w, IndexedColors.WHITE.getIndex());
        orangeCellStyle = createOneCellStyle(w, IndexedColors.ORANGE.getIndex());
        /*
        yellowCellStyle = w.createCellStyle();
        yellowCellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        yellowCellStyle.setFillBackgroundColor(IndexedColors.YELLOW.getIndex());
        yellowCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        greenCellStyle = w.createCellStyle();
        greenCellStyle.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
        greenCellStyle.setFillBackgroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
        greenCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        blueCellStyle = w.createCellStyle();
        blueCellStyle.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.getIndex());
        blueCellStyle.setFillBackgroundColor(IndexedColors.CORNFLOWER_BLUE.getIndex());
        blueCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        greyCellStyle = w.createCellStyle();
        greyCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        greyCellStyle.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        greyCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        whiteCellStyle = w.createCellStyle();
        whiteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        whiteCellStyle.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
        whiteCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

         */
    }

    private CellStyle createOneCellStyle(Workbook w, short colorIndex) {
        CellStyle cellStyle = w.createCellStyle();
        cellStyle.setFillForegroundColor(colorIndex);
        cellStyle.setFillBackgroundColor(colorIndex);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        return cellStyle;
    }
    private void setTableHeader(Sheet sheet, int rowIndex) {
        Row tableRow = sheet.createRow(rowIndex);
        int cellIndex = 0;
        Cell cellLabel = tableRow.createCell(cellIndex++);
        Cell attributeName = tableRow.createCell(cellIndex++);
        Cell cellTags  = tableRow.createCell(cellIndex++);
        Cell cellType = tableRow.createCell(cellIndex++);
        Cell cellValue = tableRow.createCell(cellIndex++);

        cellLabel.setCellStyle(whiteCellStyle);
        attributeName.setCellStyle(whiteCellStyle);
        cellTags.setCellStyle(whiteCellStyle);
        cellType.setCellStyle(whiteCellStyle);
        cellValue.setCellStyle(whiteCellStyle);

        cellLabel.setCellValue("Label");
        attributeName.setCellValue("DICOM Element");
        cellTags.setCellValue("DICOM Tag(s)");
        cellType.setCellValue("Type (1, 2, 3, etc.)");
        cellValue.setCellValue("Unique Value Identified in Data");
    }
}
