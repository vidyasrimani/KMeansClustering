# KMeansClustering
/***
 * CS6375.001 Machine Learning
 * Machine Learning
 * Vidya Sri Mani (vxm163230)
 * Implementaion of KMeans Compression
 */

	File Name: KMeans.java
	Input to the program : <input-image absolute path>  <k-value>  <output-image absolute path>
Example: 
C:/Users/Vidya/Desktop/Assignment3/Penguins.jpg 2 C:/Users/Vidya/Desktop/Assignment3/Penguins1.jpg
	Output : Compressed .jpg files for specified k value and k = 2,5,10,15,20
o	Average, Variance of compression Ratios
-------------------------------------------------------------------------------------------------
	The input to the program includes 3 parameters sent as command line input.
The 1st : the image file that needs to be compressed
	Eg. C:/Users/Vidya/Desktop/Assignment3/Penguins.jpg
The 2nd : The desired value of K
	Eg. 2
The 3rd : the image file that is clustered after KMeans clustering in the specified path
	Eg. C:/Users/Vidya/Desktop/Assignment3/Penguins1.jpg
	The program implements KMeans clustering for k values = 2,5,10,15,20
	The output file for each k value is present in the same path as the original input file prefixed with the k value.
Eg. Input file : Penguins.jpg
For k = 2, output : 2-Penguins.jpg
For k = 5, output : 5-Penguins.jpg
For k = 10, output : 10-Penguins.jpg
For k = 15, output : 15-Penguins.jpg
For k = 20, output : 20-Penguins.jpg
Present in the same folder as input image file.
