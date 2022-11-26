# DICOMAnalysis
This tool is designed to extract elements from DICOM files and present those elements/data in a format that simplifies analysis tasks. Traditional tools dump DICOM headers in a linear format following the elements as they appear in a file and then possibly apply post-processing to extract/group the elements of interest. The post-processing operations are critical when trying to examine elements over a large number of files and/or trying to examine a limited number of element while ignoring other elements found in DICOM headers.

# Modes of Operation
## Template Based Extraction
The user specifies a template file that defines the output written as an Excel spreadsheet. Full details are found here: [Extraction_Template_Format.md](Extraction_Template_Format.md). In general,
1. The user specifies one or more sheets to be included in the output file. Each sheet is identified by a human readable label.
1. For each sheet, the user specifies the list of elements to be included in the output. The list can include elements in DICOM sequences.
1. The tool reads all files specified by the user and collects the set of **unique values** for each element identified in the template file.
1. Rows in the spreadsheet output consist of the DICOM element tag(s), a label specified by the user and the unique values found in the DICOM files. One row is generated for each unqique value.

