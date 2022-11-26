# Extraction Template Format

The template file that drives the output is an array of JSON objects. Each object in the template has two values:
| Key               | Type   | Description |
|-------------------|--------|-------------|
| tabName           | String | Label applied to tab in the spreadsheet output |
| tabRowDescriptors | Array  | Each object describes one value to extract from the input images |

Each object in the tabRowDescriptors array has these values; all values are Strings.

| Key           |  Description  |
|---------------|---------------|
| label         | User defined label for the data to be extracted. This can be an abstract concept or a specific label such as the name of a DICOM element |
| attributeName | A string that identifies the DICOM element used for data extraction. See notes below for more detail |
| tags          | One or more DICOM tags that specify the DICOM element used for abstraction. A single element is specified as GGGG,EEEE. DICOM Sequence elements use the same notation with : separators. That is, GGGG,EEEE:gggg,eeee |
| type          | The DICOM type of the element (1, 2, 3, 2C, etc.). This is included in the output as entered by the user. It is not compared to the type defined in the DICOM standard. |

**Notes on tabRowDescriptors values**
* The values label, attributeName, and type are defined by the user and used to help annotate the output. The user can place any strings in those values, and they will be copied to the output. The template files included with the software use the value defined by the DICOM Standard for attributeName. When an extracted value is taken from a sequence, the template files follow a pattern of "Element1 > Element2" where Element2 is an element within the sequence Element1. This is convention and not a requirement
* The value entered for ***tags*** needs to follow the syntax understood by the software. A string for this value consists of one or more DICOM tags separated by the : character. A DICOM tag is specified by GGGG,EEEE; the , is mandatory. You can include more than one level of sequence nesting, for example "0054,0016:0054,0302:0008,0100". No spaces are allowed.
* All values including type are encoded as Strings and are therefore enclosed in quotes.

## Example Template
```
[
  {
    "tabName": "Imaging Common",
    "tabRowDescriptors": [
      {
        "label": "Scanner Manufacturer",
        "attributeName": "Manufacturer",
        "tags": "0008,0070",
        "type": "2"
      },
      {
        "label": "Manufacturer's Model Name",
        "attributeName": "Manufacturer's Model Name",
        "tags": "0008,1090",
        "type": "3"
      },
      {
        "label": "Software Versions",
        "attributeName": "Software Versions",
        "tags": "0018,1020",
        "type": "3"
      }
    ]
  },
  {
    "tabName": "PET Imaging",
    "tabRowDescriptors": [
      {
        "label": "Imaging Agent",
        "attributeName": "Radiopharmaceutical Information Sequence > Radiopharmaceutical",
        "tags": "0054,0016:0018,0031",
        "type": "3"
      },
      {
        "label": "Dose",
        "attributeName": "Radiopharmaceutical Information Sequence > Radionuclide Total Dose",
        "tags": "0054,0016:0018,1074",
        "type": "3"
      },
      {
        "label": "Specific Activity",
        "attributeName": "Radiopharmaceutical Information Sequence > Radiopharmaceutical Specific Activity",
        "tags": "0054,0016:0018,1077",
        "type": "3"
      }
    ]
  }
]


```