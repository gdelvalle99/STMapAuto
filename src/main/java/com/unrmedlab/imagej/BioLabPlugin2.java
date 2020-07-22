/*
 * To the extent possible under law, the ImageJ developers have waived
 * all copyright and related or neighboring rights to this tutorial code.
 *
 * See the CC0 1.0 Universal license for details:
 *     http://creativecommons.org/publicdomain/zero/1.0/
 */

package com.mycompany.imagej;

import trainableSegmentation.WekaSegmentation;
import trainableSegmentation.utils.Utils;


import org.scijava.command.Command;
import org.scijava.plugin.Plugin;
import ij.IJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.gui.Roi;
import ij.io.OpenDialog;
import ij.plugin.frame.RoiManager;
import ij.process.ImageStatistics;


import java.io.File;
import java.io.FileNotFoundException;

import java.io.IOException;


import ij.plugin.filter.Analyzer;
import ij.plugin.filter.ParticleAnalyzer;
import ij.measure.ResultsTable;

@Plugin(type = Command.class, menuPath = "Plugins>STMapAuto")
public class BioLabPlugin2 implements Command {
		public ImagePlus imp;
		public RoiManager rm;
		public WekaSegmentation weka;
		public ParticleAnalyzer pa;
		public ResultsTable rt;
		public ImageStatistics is;
		public Analyzer a;
		
		public int min = 0;
		public int max = 999999;
		public double width = .0303;
		public double height = .29;
		public int depth = 0;
		public String unit = "um";
		

    @Override
    public void run() {
    	imp = new ImagePlus();
    	weka = new WekaSegmentation();
    	rt = new ResultsTable();
    	is = new ImageStatistics();
    	numberDialog();
    	ParticleAnalyzer.setRoiManager(rm);
    	genDialog("Choose input folder");
    	String input_dir = IJ.getDirectory("Choose input folder");//this is where the images come from
    	if(input_dir == null) {
    		return;
    	}
    	genDialog("Choose output folder");
    	String output_dir = IJ.getDirectory("Choose output folder");//this is where they are stored
    	if(output_dir == null) {
    		return;
    	}
    	genDialog("Choose classifier");
        OpenDialog od = new OpenDialog("Choose a classifier", null);
        String path = od.getPath();
        if(path == null) {
        	return;
        }
        
        final File folder = new File(input_dir);
        processFilesForFolder(folder, output_dir, path);
        
    }

    
    public boolean properties() {
    	IJ.run(imp, "Properties...", "channels=1 slices=1 frames=1 unit=" + unit + " pixel_width="+width+" pixel_height="+height+" voxel_depth="+depth);
    	return true;
    }
    
    
    public void processFilesForFolder(final File folder, String output_folder, String classifier) {
        int counter = 0;
    	for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                processFilesForFolder(fileEntry, output_folder, classifier);
            } else {
                try {
					processImage(fileEntry.getPath(), output_folder, classifier, counter);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                counter++;
            }
        }
    	counter = 0;
    }
    
    
    public void processImage(String img, String output_folder, String classifier, int check) throws FileNotFoundException, IOException {
    	rm = new RoiManager(true);
    	ParticleAnalyzer.setRoiManager(rm);
    	pa = new ParticleAnalyzer(2048, 0, new ResultsTable(), min, max);
        imp = IJ.openImage(img);
        if(imp != null) {
        	imp.show();
        	File dir = new File(output_folder + imp);
        	dir.mkdir();
        	properties();	
        	weka = new WekaSegmentation(imp);
        	if(weka.loadClassifier(classifier) == false) {
        		nullify();
       			return;
       		}
        
       		saveSegmentation(classifier, output_folder);
       		weka.applyClassifier(false);
       		ImagePlus result = new ImagePlus();
       		result = weka.getClassifiedImage();
       		result.show();
       		IJ.run("Enhance Contrast", "saturated=0.3");
       		IJ.setAutoThreshold(result, "Intermodes");
       		IJ.run("Set Measurements...", "area mean bounding fit limit redirect=None decimal=3");	
       		pa.analyze(result);
       		result.close();
        	result = null;
        	Analyzer.setMeasurements(1 + 2048 + 256 + 2 + 512);
        	a = new Analyzer(imp, rt);
        	Roi[] rois = rm.getRoisAsArray();
        	for(int i = 0; i<rois.length;i++) {
        		imp.setRoi(rois[i]);
        		IJ.run(imp, "Add Selection...", "");
        		is = imp.getStatistics(1 + 2048 + 256 + 2 + 512);
        		imp.saveRoi();
        		a.saveResults(is, rois[i]);
        	}
        	ImagePlus imp2 = imp.flatten();
        	IJ.saveAsTiff(imp2, dir + "/Mask");
        	rt.save(dir + "/Results.xls");
       		nullify();
       		return; 
			
       }
    } 
    

    public void saveSegmentation(String classifier, String output_folder) {
    	WekaSegmentation sample = new WekaSegmentation();
    	sample.loadClassifier(classifier);
    	ImagePlus thresholded_sample = sample.applyClassifier(imp);
        thresholded_sample.setLut(Utils.getGoldenAngleLUT());
        IJ.saveAsTiff(thresholded_sample, output_folder + imp + "/Thresholded");

    	
    }
    
  
	

    
    public void nullify() {
    	rt.reset();
    	rm.reset();
    	rm.close();
    	imp.close();
    	a = null;
    	is = null;
    	pa = null;
    	rm = null;
    	imp = null;
    	weka = null;
    	return;
    }
    
    
    public void numberDialog() {
    	GenericDialog gd = new GenericDialog("Calibration");
    	gd.addNumericField("Minimum:", 0, 2);
    	gd.addNumericField("Maximum:", 999999, 0);
    	gd.addStringField("Units:", "um");
    	gd.addNumericField("Pixel width:", 0.0303, 4);
    	gd.addNumericField("Pixel height:", 0.29, 4);
    	gd.addNumericField("Voxel depth", 0, 4);
    	gd.showDialog();
    	
    	if(gd.wasCanceled()) {
    		return;
    	}
    	
    	min = (int)gd.getNextNumber();
    	max = (int)gd.getNextNumber();
    	unit = (String)gd.getNextString();
    	width = (double)gd.getNextNumber();
    	height = (double)gd.getNextNumber();
    	depth = (int)gd.getNextNumber();
    }
   
    public void genDialog(String s) {
    	GenericDialog gd = new GenericDialog(s);
    	gd.showDialog();
    	if(gd.wasCanceled()) {
    		return;
    	}
    }
}
    

