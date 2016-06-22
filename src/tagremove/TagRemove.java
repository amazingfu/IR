package tagremove;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TagRemove {
	
	
	 private static String content="";
	    
	 private static String INDEX_DIR = "D:\\luceneIndex";
	 //private static String DATA_DIR = "D:\\luceneData";
	 private static Analyzer analyzer = null;
	 private static Directory directory = null;
	 private static IndexWriter indexWriter = null;
	 private static List<List> filename=new ArrayList<List>();
	 private static List<String> filename_N02=new ArrayList<String>();
	 private static List<String> filename_N03=new ArrayList<String>();
	 private static List<String> filename_N04=new ArrayList<String>();
	 private static List<String> filename_N05=new ArrayList<String>();
	 
	 
	 private static List<String> filename_N01=new ArrayList<String>();
	 static {
		 filename_N01.add("XIN_CMN_20010112_0106.txt");
		 filename_N01.add("XIN_CMN_19990518_0176.txt");
		 filename_N01.add("XIN_CMN_20010111_0138.txt");
		 filename_N01.add("XIN_CMN_20010116_0198.txt");
		 filename_N01.add("za2001_001865.txt");
		 filename_N01.add("za2001_002174.txt");
		 filename_N01.add("za2001_002591.txt");
		 filename_N01.add("za2001_003660.txt");
		 filename_N01.add("za2001_004852.txt");
		 
	 }
	 
	 
	
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
	public void moveFile(String oldpath,String newpath)
	{
		List<File> filelist=getListFiles(oldpath);
		File fnewpath=new File(newpath);
		if(!fnewpath.exists())
		{
			fnewpath.mkdirs();
		}
		for(File file:filelist)
		{
			File fnew=new File(newpath+file.getName());
			file.renameTo(fnew);
		}
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
				System.out.println(content);
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
    
    public static String txt2String(File file){
        String result = "";
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                result = result + "\n" +s;
            }
            br.close();    
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }
    
    public static boolean createIndex(String path){
        Date date1 = new Date();
        List<File> fileList = getListFiles(path);
        for (File file : fileList) {
            content = "";
            //获取文件后缀
            String type = file.getName().substring(file.getName().lastIndexOf(".")+1);
            if("txt".equalsIgnoreCase(type)){
                
                content += txt2String(file);
            
            }
//            }else if("doc".equalsIgnoreCase(type)){
//            
//                content += doc2String(file);
//            
//            }else if("xls".equalsIgnoreCase(type)){
//                
//                content += xls2String(file);
//                
//            }
            
            System.out.println("name :"+file.getName());
            System.out.println("path :"+file.getPath());
//            System.out.println("content :"+content);
            System.out.println();
            
            
            try{
                analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
                directory = FSDirectory.open(new File(INDEX_DIR));
    
                File indexFile = new File(INDEX_DIR);
                if (!indexFile.exists()) {
                    indexFile.mkdirs();
                }
                IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_CURRENT, analyzer);
                indexWriter = new IndexWriter(directory, config);
                
                Document document = new Document();
                document.add(new TextField("filename", file.getName(), Store.YES));
                document.add(new TextField("content", content, Store.YES));
                document.add(new TextField("path", file.getPath(), Store.YES));
                indexWriter.addDocument(document);
                indexWriter.commit();
                closeWriter();
    
                
            }catch(Exception e){
                e.printStackTrace();
            }
            content = "";
        }
        Date date2 = new Date();
        System.out.println("创建索引-----耗时：" + (date2.getTime() - date1.getTime()) + "ms\n");
        return true;
    }
    
    
    public static void searchIndex(String text){
        Date date1 = new Date();
        try{
            directory = FSDirectory.open(new File(INDEX_DIR));
            analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
            DirectoryReader ireader = DirectoryReader.open(directory);
            IndexSearcher isearcher = new IndexSearcher(ireader);
    
            QueryParser parser = new QueryParser(Version.LUCENE_CURRENT, "content", analyzer);
            Query query = parser.parse(text);
            
            ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;
            
            List<String> indexPosition=new ArrayList<String>();
            
            for (int i = 0; i < hits.length; i++) {
                Document hitDoc = isearcher.doc(hits[i].doc);
                String name=hitDoc.get("filename");
                System.out.println("____________________________");
                System.out.println(hitDoc.get("filename"));
                if(filename_N01.contains(name))
                {
                	System.out.println("position"+i);
                	indexPosition.add(i+"");
                }
              //  System.out.println(hitDoc.get("content"));
              //  System.out.println(hitDoc.get("path"));
                System.out.println("____________________________");
            }
            
            System.out.println("The position of the file you searched:");
            for(int i=0;i<indexPosition.size();i++)
            {
            	System.out.print(" "+indexPosition.get(i));
            }
            ireader.close();
            directory.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        Date date2 = new Date();
        System.out.println("查看索引-----耗时：" + (date2.getTime() - date1.getTime()) + "ms\n");
    }
    
    
    public static void closeWriter() throws Exception {
        if (indexWriter != null) {
            indexWriter.close();
        }
    }
    
    
    public static boolean deleteDir(File file){
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for(int i=0; i<files.length; i++){
                deleteDir(files[i]);
            }
        }
        file.delete();
        return true;
    }
    
	public static void main(String[] args)
	{
		
		File fileIndex = new File(INDEX_DIR);
	        if(deleteDir(fileIndex)){
	            fileIndex.mkdir();
	        }else{
	            fileIndex.mkdir();
	        }
		String path_1="D://LuceneTextFileUnicode//";
		String path_2="D://LuceneTextFileUTF-8//";
		String newpath="D://LuceneRefinedTextFile//";
		
		moveTag(path_1, newpath,"Unicode");
		moveTag(path_2, newpath,"UTF-8");
		createIndex(newpath);
		searchIndex("美国对于贫铀炸弹的姿态");
	}

}
