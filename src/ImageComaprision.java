

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;

public class ImageComaprision {
	public static String tempFlag;
	/*
	public static void main(String args[]){
		String s1="http://spatial.vodacom.co.za/gmlc/index.jsp?width=400&height=300&centerX=28.213521&centerY=-25.742204&scale=20000&maptype=1";
		processImg(s1);
	}
	*/
	public static boolean processImg(String s1,String s2)//pass this method the image of your chart from link
	{
	boolean ret = true;
	
	try {
		BufferedImage original = ImageIO.read(new File(s2));//path of your image stored in your machine
		URL url = new URL(s1);
		SocketAddress address = new InetSocketAddress("ptproxy.persistent.co.in", 8080);
		Proxy proxy = new Proxy(Proxy.Type.HTTP, address);
		URLConnection conn = url.openConnection(proxy);
		InputStream inStream = conn.getInputStream();
		BufferedImage copy = ImageIO.read(inStream);

		Raster ras1 = original.getData();
		Raster ras2 = copy.getData();
	//Comparing the the two images for number of bands,width & height.
		if (ras1.getNumBands() != ras2.getNumBands()
				|| ras1.getWidth() != ras2.getWidth()
				|| ras1.getHeight() != ras2.getHeight()) {
			ret=false;
		}
		else{
			// Once the band ,width & height matches, comparing the images.
		
			search: for (int i = 0; i < ras1.getNumBands(); ++i) {
				for (int x = 0; x < ras1.getWidth(); ++x) {
					for (int y = 0; y < ras1.getHeight(); ++y) {
						if (ras1.getSample(x, y, i) != ras2.getSample(x, y, i)) {
						// If one of the result is false setting the result as false and breaking the loop.
						ret = false;
						break search;
						}
					}
				}
			}
		}
		if (ret == true) {
			//System.out.println("Image matches");
			tempFlag="true";
		}
		else {
			//System.out.println("Image does not matches");
			tempFlag="false";
		}

	}
	catch (IOException e)
	{
		TestSuitRunner.logger.error("ImageComaprision|processImg|Unable to compare images.",e);
	}
	if (tempFlag=="true")
		return true;
	else
		return false;
	}
}
