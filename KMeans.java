/*** Author :Vibhav Gogate
The University of Texas at Dallas
*****/

/***
 * CS6375.001 Machine Learning
 * Machine Learning
 * Vidya Sri Mani
 * Implementaion of KMeans Compression
 */


import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Random;
import java.awt.Color; 
import java.net.URL;

public class KMeans {
    public static void main(String [] args){
	if (args.length < 3){ 
	    System.out.println("Usage: Kmeans <input-image absoulute path> <k-value> <output-image absolute path>");
	    return;
	}
	try{
	    BufferedImage originalImage = ImageIO.read(new File(args[0]));
	    int k=Integer.parseInt(args[1]);//inputs the value of K
	    System.out.println("K:"+k);
	    File newImg = new File(args[0]);
	    
	    BufferedImage kmeansJpg1 = kmeans_helper(originalImage,k);
	    ImageIO.write(kmeansJpg1, "jpg", new File(args[2]));
	    double cr=getCR(args[0],args[2]);
	    System.out.println("---------------------------------------------");
	    
	    System.out.println("Clustering with k values : 2,5,10,15,20 ");
	    int[] kValues = {2,5,10,15,20};
	    int n=5;//number of k values
	    double[] CRatios=new double[5];
	    double avg = 0;
	    double variance = 0;
	    
	    for (int i=0;i<n;i++){
	    	//System.out.println("path :" + newImg.getParent());
	    	
	    	System.out.println("K :"+kValues[i]);
	    	String filename = newImg.getParent()+ "\\"+ kValues[i]+"-" +newImg.getName();
	    	System.out.println("Compressed file :" +filename);
	    	
	    	BufferedImage kmeansJpg = kmeans_helper(originalImage,kValues[i]);
		    ImageIO.write(kmeansJpg, "jpg", new File(filename));
		    CRatios[i]=getCR(args[0],filename);
		    avg+=CRatios[i];
		    variance+=Math.pow(CRatios[i],2);
		    System.out.println("---------------------------------------------");
	    }
	    
	    variance=variance/n;
	    avg = avg/n;
	    variance = variance - Math.pow(avg, 2);
	    System.out.println("Average Compression Ratio :" +avg);
	    System.out.println("Variance Compression Ratio :" +variance);
	    
	    System.out.println("Completed");
	    
	    
	}catch(IOException e){
	    System.out.println(e.getMessage());
	}	
    }
    
    private static double getCR(String oldImage, String NewImage){
    	double cr=0;
    	File Oldimg = new File(oldImage);
    	
    	double OldimgLength = (int) Oldimg.length();
    	File Newimg = new File(NewImage);
    	double NewimgLength = (int) Newimg.length();
    	cr=OldimgLength/NewimgLength;
    	System.out.println("Compression Ratio :" + cr);
    	return cr;
    }
    private static BufferedImage kmeans_helper(BufferedImage originalImage, int k){
	int w=originalImage.getWidth();//original width
	int h=originalImage.getHeight();//original height
	BufferedImage kmeansImage = new BufferedImage(w,h,originalImage.getType());
	Graphics2D g = kmeansImage.createGraphics();
	g.drawImage(originalImage, 0, 0, w,h , null);
	// Read rgb values from the image
	int[] rgb=new int[w*h];
	int count=0;
	for(int i=0;i<w;i++){
	    for(int j=0;j<h;j++){
		rgb[count++]=kmeansImage.getRGB(i,j);
	    }
	}
	// Call kmeans algorithm: update the rgb values
	kmeans(rgb,k);

	// Write the new rgb values to the image
	count=0;
	for(int i=0;i<w;i++){
	    for(int j=0;j<h;j++){
		kmeansImage.setRGB(i,j,rgb[count++]);
	    }
	}
	return kmeansImage;
    }

    
    // Your k-means code goes here
    // Update the array rgb by assigning each entry in the rgb array to its cluster center
    private static void kmeans(int[] rgb, int k){
    	//Set appropriate rgb value by clustering and finding the center
    	int len = rgb.length;
    	double [][] rgba = new double[k][4];
    	double [][] rgba2 = new double[4][len];
    	
    	long[] sum = new long[k];//
    	long[] count = new long[k];//
    	int[] current = new int[len];
    	
    	rgba=initializeRGBA(rgba,k);
    	rgba2=setRGBA(rgba2,rgb);
    	
    	boolean change = true;
    	///////////////////
    	
    	while(change){
    		change = false;
    		int[] temp=new int[len];
	    	for(int i = 0; i < len; i++)
	    	{
	    		double dist = Double.MAX_VALUE;
	    		for(int m = 0; m < k; m++)
	    		{	
	    			double edist = ( Math.pow((double)(rgba2[0][i] - rgba[m][0]), 2) + Math.pow((double)(rgba2[1][i] - rgba[m][1]), 2) + Math.pow((double)(rgba2[2][i] - rgba[m][2]), 2)+ Math.pow((double)(rgba2[3][i] - rgba[m][3]), 2));
	    			if (edist < dist){
	    				temp[i]=m;
	    				dist = edist;
		    			}
	    		}		    			
	     	}
	    	
	    	double changedCount=0;
	    	
	    	for(int i=0;i<len;i++)
	    	{
	    		if(current[i]!=temp[i])
	    			{
	    				changedCount++;
	    			}
	    	}
	    	
	    	if(changedCount>0)
	    	{
	    		change=true;
	    		for(int i=0;i<len;i++)
	    		{
		    		current[i]=temp[i];
		    	}
	    		
		    	for(int i=0; i< len; i++)
		    	{
		    			int meanInd = temp[i];
		    			count[meanInd]++;
		    			if(count[meanInd]==1){
		    				rgba[meanInd][0]=rgba2[0][i];
		    				rgba[meanInd][1]=rgba2[1][i];
		    				rgba[meanInd][2]=rgba2[2][i];
		    				rgba[meanInd][3]=rgba2[3][i];
		    			}
		    			else{
		    				double prevC=(double)count[meanInd]-1.0;
			    			rgba[meanInd][0]=(prevC/(prevC+1.0))*(rgba[meanInd][0]+(rgba2[0][i]/prevC));
			    			rgba[meanInd][1]=(prevC/(prevC+1.0))*(rgba[meanInd][1]+(rgba2[1][i]/prevC));
			    			rgba[meanInd][2]=(prevC/(prevC+1.0))*(rgba[meanInd][2]+(rgba2[2][i]/prevC));
			    			rgba[meanInd][3]=(prevC/(prevC+1.0))*(rgba[meanInd][3]+(rgba2[3][i]/prevC));
		    			}
		    	}
		    	
	    	}
				
		}

    	for(int i=0; i<len; i++)
    	{    		
    		rgb[i]=(int)rgba[current[i]][3]<<24|(int)rgba[current[i]][0]<<16|(int)rgba[current[i]][2]<<8|(int)rgba[current[i]][1]<<0;
    	}
    	
    }
    
  //Initialize the 2D array, set default values
    private static double[][] initializeRGBA(double[][] rgba,int k){
    	Random rand = new Random();
    	for(int i=0;i<k;i++){
    		rgba[i][0] = rand.nextDouble()*255;
    		rgba[i][1] = rand.nextDouble()*255;
    		rgba[i][2] = rand.nextDouble()*255;
    		rgba[i][3] = rand.nextDouble()*255;
    	}
    	return rgba;
    }
    
    //Initialize colour values
    private static double[][] setRGBA(double[][] rgba2,int[] rgb){
    	for(int i=0;i<rgb.length;i++){
    		Color c = new Color(rgb[i]);
    		rgba2[0][i] = c.getRed();
    		rgba2[1][i] = c.getBlue();
    		rgba2[2][i] = c.getGreen();
    		rgba2[3][i] = c.getAlpha();
    	}
    	return rgba2;
    }
}