package ir;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MoveTag {
	
	
	   public static ArrayList<File> getListFiles(Object obj) {  
	        File directory = null;  
	        if (obj instanceof File) {  
	            directory = (File) obj;  
	        } else {  
	            directory = new File(obj.toString());  
	        }  
	        ArrayList<File> files = new ArrayList<File>();  
	        if (directory.isFile()) {  
	            files.add(directory);  
	            return files;  
	        } else if (directory.isDirectory()) {  
	            File[] fileArr = directory.listFiles();  
	            for (int i = 0; i < fileArr.length; i++) {  
	                File fileOne = fileArr[i];  
	                files.addAll(getListFiles(fileOne));  
	            }  
	        }  
	        return files;  
	    }  
	
	public static void moveTag(String path,String newpath,String encoding)
    {
    	//
		//String encoding = "Unicode";
		BufferedReader reader=null;
		List<File> filelist=getListFiles(path);
		for(File file:filelist){
			
			 String   filename=file.getAbsolutePath();     
	         if(filename.indexOf(".")>=0)     
	          {     
	              filename   =   filename.substring(0,filename.lastIndexOf("."));
	              //System.out.println(filename);
	          } 
	          file.renameTo(new File(filename+".txt"));
	         // System.out.println(file.getName()+"222221111");
		}
		List<File> newfilelist=getListFiles(path);
		System.out.println(newfilelist.size());
		for(File file:newfilelist){
			
			try {
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
				reader=new BufferedReader(read);
				String tempString=null;
				String content=null;
				String newcontent=null;
				//String newfilename=null;
				//while(reader.ready())
				while((tempString=reader.readLine())!=null)
				{
					content=content+tempString+"\r\n";
					//System.out.println("content");
				}
				//System.out.println(content);
				org.jsoup.nodes.Document doc=Jsoup.parse(content);
				Elements headline=doc.getElementsByTag("HEADLINE");
				//System.out.println(headline.text());
				
				Elements dateline=doc.getElementsByTag("DATELINE");
				//System.out.println(dateline.text());
				newcontent+=headline.text()+dateline.text();
				if(encoding=="UTF-8"&&file.getName().startsWith("XIN")){
					Elements ps=doc.getElementsByTag("P");
					for(Element p:ps)
					{
						newcontent+=p.ownText();
					}
				}
				else{
				Elements text=doc.getElementsByTag("TEXT");
				//System.out.println(file.getName());
				
				//System.out.println(newcontent);
				for(Element fragment:text)
				{
					newcontent+=fragment.ownText();
					//System.out.println(fragment.ownText());
				}
				}
				//System.out.println(newpath+newfilename);
				//System.out.println(newcontent);
				File newfile=new File(newpath+file.getName());
				if(!newfile.exists())
				{
					newfile.createNewFile();
				}
				FileWriter fileWritter = new FileWriter(newfile.getAbsolutePath(),true);
		        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
		        bufferWritter.write(newcontent.substring(4));
		        bufferWritter.close();
				//System.out.println(text);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println("no file");
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("no content");
				e.printStackTrace();
			}
		}

}
	
	 public static List<String> getFileName(String path)
	 {
		 ArrayList<File> file=getListFiles(path);
		 String[] arry=new String[2];
		 String name;
		 List<String> filename=new ArrayList<String>();
		 for(File fis:file)
		 {
			// System.out.println(fis.getName());
			 arry=fis.getName().split("\\.");
			// System.out.println(arry[1]+arry[0]);
			 name=arry[0]+".txt";
			 filename.add(name);
		 }
		 return filename;
		 
	 }
	 
	 
	
	 
	
}
