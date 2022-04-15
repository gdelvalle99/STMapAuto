## **Release 1.2.0**
- Implemented Weka Segmentation
- 2 new modes of operation: batch and single STMap processing.  Single map mode allows the user to generate classifiers directly. 
- Renamed Width to W-Spatial in Results.xls
- Renamed Height to H-Duration in Results.xls
- Changed the default pixel height and width to 0.0303
- Bug fix: fixed output path name on PC

## **Calcium Imaging Analysis Plugin Fiji/Image J:**

Intracellular Ca<sup>2+</sup> signals occur in nearly all cells, and mediate a myriad of functions such as cell proliferation, secretion, fertilization, and muscle contraction. To investigate these phenomena, Ca<sup>2+</sup> signals are commonly recorded by using either traditional Ca<sup>2+</sup> dyes or genetically encoded Ca<sup>2+</sup> indicators. However, intracellular Ca<sup>2+</sup> signals encode specific responses via complex spatial and temporal patterns including localized oscillations and cell-wide waves, which can be arduous to decipher manually. Therefore, we aim to dramatically enhance current methods of Ca<sup>2+</sup> event detection and analysis. Thus far, we have developed an automated algorithmic solution to the analysis of Ca<sup>2+</sup> signal dynamics and have incorporated a machine learning approach into Ca<sup>2+</sup> Spatio-Temporal Map analysis.

Dr. Sal Baker Lab at the Univerity of Nevada Reno, developed a Spatio-Temporal Map analysis plugin (STMapAuto) that is fully compatible with Fiji/Image J. This plugin allows for automatic extraction of key Ca<sup>2+</sup> event information such as: frequency, propagation velocity, intensity, area, and spatial spread. The developed analysis methods will dramatically reduce opportunities for user error and will provide a fast, standardized and accurate analysis to allow for high throughput analysis of multiple datasets.
Detailes of the plugin generation and testing can be found in our paper in Cell Calcium journal. STMapAuto plugin is based on a publication and  We kindly ask users that utilize the plugin to cite our work
https://www.sciencedirect.com/science/article/pii/S0143416020301020

## **STMapAuto plugin manual:**

First time use:
** DOWNLOAD STMapAuto plugin ** classifiers and example STMap using this link: (on this page-click on Code-select dowanload zip file)

-After downloading the plugin into Fiji/Image J

-Create 2 folders on your computer (i.e. Input and output folders).


Now the software is ready to be used: 

1-Move STMaps to be analyzed into “input folder” 

2-Open STMapAuto 

3-Insert image calibration parameters

4-Select input folder 

5-Select output folder 

6-Select your optimized classifier 

7-All segmented images and excel file are located in the output folder 


-For optimization of new classifiers and training of new set of STMaps, please refer to the machine learning section of the paper (https://www.sciencedirect.com/science/article/pii/S0143416020301020) and/or
contact Dr. Baker's Lab: https://med.unr.edu/directory/sal-baker?v=bio#Biography

-Please note: for single STMap training and analysis. open the STMap FIRST in image J before using the STMAPAUTO plugin.

## **Explanation of results:**
The Excel file will have 10 columns. The information in each column is as follows.
* Col. A: The number corresponding to each ROI.
* Col. B: The area of the corresponding ROI.
* Col. C: The mean gray value of the corresponding ROI.
* Col. D: The x coordinate of the top left corner of the bounding rectangle of the corresponding ROI.
* Col. E: The y coordinate of the top left corner of the bounding rectangle of the corresponding ROI.
* Col. F: The width of the bounding rectangle of the corresponding ROI.
* Col. G: The height of the bounding rectangle of the corresponding ROI.
* Col. H: The major axis of the bounding ellipse of the corresponding ROI.
* Col. I: The minor axis of the bounding ellipse of the corresponding ROI.
* Col. J: The angle of the bounding ellipse of the corresponding ROI.

## **Supplemental Information**
Processes involved in automated analysis of Ca<sup>2+</sup> spatio-temporal maps (STMaps) in Fiji/ImageJ.
1. Upon opening of the .tif image/STMap to be analyzed, the plugin prompts ImageJ to open a Properties window, where the user will be able to calibrate the image. The calibration will allow for both spatial and temporal calibration.
2. The plugin creates and initializes a RoiManager object. This object exists in the plugin and in ImageJ, and is where individual measurements of Ca<sup>2+</sup> events take place.
3. The plugin creates and initializes an ImagePlus object to the currently open image. This is loaded directly into the plugin.
4. The plugin creates and initializes a WekaSegmentation object with the previously created ImagePlus object. 
5. Once the WekaSegmentation is initialized, the plugin creates and initializes an OpenDialog object, which prompts the user to pick a classifier of their choice. 
6. The plugin loads the classifier into the WekaSegmentation object, and it is applied. This will generate a classified image.
7. An ImagePlus object is created and initialized to the classified image generated by the WekaSegmentation object. The image will be then displayed by ImageJ.
8. The image will then be a black image. To fix this, the plugin applies the Enhance Contrast function in ImageJ. The image itself remains unchanged, this is for visibility purposes.
9. The plugin then sends another function to ImageJ, applying the Intermodes threshold. At this point, the image has been segmented.
10. Following this, the plugin will then send a command to ImageJ to measure Area, Bounding Rectangle, Fit Ellipse and Mean Gray Value.
11. The plugin will then send a following command to ImageJ, setting Analyze Particles menu to summarize the different objects and to add the objects to the open ROIManager. The plugin will also send command to close the result image.
12. With the original image, the ROIManager object will apply the mask created by the thresholding and particle analysis, and then measure. This will generate the results.
13. The plugin will send a command to save the results to an excel spreadsheet, and save it to the classifier folder.




## **Citation for STMapAuto plugin:**
Please note that STMapAuto plugin is based on a publication. We kindly ask users that utilize the plugin to cite our work:
* Wesley Leigh, Guillermo Del Valle, Sharif Amit Kamran, Bernard T. Drumm, Alireza Tavakkoli, Kenton M. Sanders, Salah A. Baker (Cell Calcium; July 2020). A High Throughput Machine-Learning Driven Analysis of Ca<sup>2+</sup> Spatio-temporal Maps https://authors.elsevier.com/sd/article/S0143-4160(20)30102-0 or https://www.sciencedirect.com/science/article/pii/S0143416020301020

## **License:**
The code is released under the MIT License, you can read the license file included in the repository for details.


