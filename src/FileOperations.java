

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;

class FileOperations {
  public static void write(String path, String content)
  {
    try
    {
      FileWriter fstream = new FileWriter(path);
      BufferedWriter out = new BufferedWriter(fstream);
      out.write(content);
      
      out.close();
    }
    catch (Exception e)
    {
    	TestSuitRunner.logger.error("FileOperations|write|Unable to write to file.",e);
    }
  }
  
  public static String read(String path)
  {
    File file = new File(path);
    
    StringBuffer strContent = new StringBuffer("");
    FileInputStream fin = null;
    try
    {
      fin = new FileInputStream(file);
      int ch;
      while ((ch = fin.read()) != -1)
      {
        //int ch;
        strContent.append((char)ch);
      }
      fin.close();
    }
    catch (Exception e)
    {
    	TestSuitRunner.logger.error("FileOperations|write|Unable to read file.",e);
    }
    return strContent.toString();
  }
  
  public static String getTemplateProjectFolder() throws IOException
  {
    boolean folderFound = false;
    //boolean systemFolder = false;
    File directory = new File(".");
    String currPath = directory.getCanonicalPath();
    String[] subDirs = currPath.split(Pattern.quote(File.separator));
    String varPath = "";
    for (int i = 0; i < subDirs.length; i++)
    {
      varPath = varPath + subDirs[i] + "//";
      if (subDirs[i].equals("Template_Project"))
      {
        folderFound = true;
        break;
      }
      if (subDirs[i].equals("System32"))
      {
        //systemFolder = true;
        break;
      }
    }
    if (!folderFound) {
      varPath = currPath;
    }
    //System.out.println(varPath);
    return varPath;
  }
  public static String getTestcaseFiles() throws IOException
  {
    boolean folderFound = false;
    //boolean systemFolder = false;
    File directory = new File(".");
    String currPath = directory.getCanonicalPath();
    File tcFolder = new File(currPath+"//TestCases");
    File[] listOfTC=tcFolder.listFiles();
    String[] subDirs = currPath.split(Pattern.quote(File.separator));
    String tcPath = "";
    
    for (int i = 0; i < subDirs.length; i++)
    {
      tcPath = tcPath + subDirs[i] + "//";
      if (subDirs[i].equals("TestCases"))
      {
        folderFound = true;
        break;
      }
      if (subDirs[i].equals("System32"))
      {
        //systemFolder = true;
        break;
      }
    }
    if (!folderFound) {
      tcPath = currPath+"//TestCases";
    }

    for (int i = 0; i < listOfTC.length; i++) {
        if (listOfTC[i].isFile()) {
          System.out.println("File " + listOfTC[i].getName());
        } else if (listOfTC[i].isDirectory()) {
          System.out.println("Directory " + listOfTC[i].getName());
        }
      }
    //System.out.println(varPath);
    return tcPath;
  }
}