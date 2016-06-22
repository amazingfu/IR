package ir;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.cn.ChineseAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.DocsEnum;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;
import org.lionsoul.jcseg.lucene.JcsegAnalyzer4X;
import org.lionsoul.jcseg.core.*;

public class Index {
	
	 private static String content="";
	 private static Analyzer analyzer = null;
	 private static Directory directory = null;
	 private static IndexWriter indexWriter = null;
	 public final static String INDEX_DIR = "D:\\luceneIndex";
	 //private double f=0.0;
	 
	 private static List<List> allfilename=new ArrayList<List>();
	 static{
		 
		 for(int i=1;i<3;i++)
		 {
		 List<String> filename=MoveTag.getFileName("D:\\TrainingReleased_CS\\STNO-UNICODE\\N0"+i);
		 //getListFiles("D:\\TrainingReleased_CS\\STNO-UNICODE\\N01");
		 allfilename.add(filename);
		 }
		 for(int j=3;j<10;j++)
		 {
		 List<String> filename=MoveTag.getFileName("D:\\TrainingReleased_CS\\FormalRun\\N0"+j);
		 //getListFiles("D:\\TrainingReleased_CS\\STNO-UNICODE\\N01");
		 allfilename.add(filename);
		
		 }
		 for(int k=10;k<17;k++)
		 {
		 List<String> filename=MoveTag.getFileName("D:\\TrainingReleased_CS\\FormalRun\\N"+k);
		 //getListFiles("D:\\TrainingReleased_CS\\STNO-UNICODE\\N01");
		 allfilename.add(filename);
		
		 }
		
		 
	 }
	 
    public String txt2String(File file){
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
    

	public boolean createIndex(String path){
        Date date1 = new Date();
        System.out.println("正在建立索引....");
        List<File> fileList = MoveTag.getListFiles(path);
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
            
            //System.out.println("name :"+file.getName());
           // System.out.println("path :"+file.getPath());
//            System.out.println("content :"+content);
            //System.out.println();
            
            
            try{
                analyzer =new CJKAnalyzer(Version.LUCENE_CURRENT);
                		//new JcsegAnalyzer4X(JcsegTaskConfig.SIMPLE_MODE);
                		//new SimpleAnalyzer(Version.LUCENE_CURRENT);
                		//new CJKAnalyzer(Version.LUCENE_CURRENT);
                		//
                		// new AnsjAnalyzer();
                		//new IKAnalyzer(true);
                		
                		//new IKAnalyzer(true);
                //new StandardAnalyzer(Version.LUCENE_CURRENT);
  
                		
                		
                		
                directory = FSDirectory.open(new File(INDEX_DIR));
    
                File indexFile = new File(INDEX_DIR);
                if (!indexFile.exists()) {
                    indexFile.mkdirs();
                }
                IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_CURRENT, analyzer);
                indexWriter = new IndexWriter(directory, config);
                
                FieldType ft = new FieldType();
                ft.setIndexed(true);// 存储
                ft.setStored(true);// 索引
                ft.setStoreTermVectors(true);
                ft.setTokenized(true);
                ft.setStoreTermVectorPositions(true);// 存储位置
                ft.setStoreTermVectorOffsets(true);// 存储偏移量
                
                Document document = new Document();
                document.add(new TextField("filename", file.getName(), Store.YES));
                document.add(new TextField("content", content, Store.YES));
                document.add(new TextField("path", file.getPath(), Store.YES));
                document.add(new Field("text", content, ft));
                indexWriter.addDocument(document);
                indexWriter.forceMerge(1);
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
	
	 public void closeWriter() throws Exception {
	        if (indexWriter != null) {
	            indexWriter.close();
	        }
	    }
	 
	   public double searchIndex(String text,int filegroup,double limit){
	        Date date1 = new Date();
	        double f=0.0;
	        try{
	            directory = FSDirectory.open(new File(INDEX_DIR));
	            analyzer = new CJKAnalyzer(Version.LUCENE_CURRENT);
	            		//new JcsegAnalyzer4X(JcsegTaskConfig.SIMPLE_MODE);
	            		//new SimpleAnalyzer(Version.LUCENE_CURRENT);
 
	            		//
	            		//new IKAnalyzer(true);
	            //analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
	            DirectoryReader ireader = DirectoryReader.open(directory);
	            IndexSearcher isearcher = new IndexSearcher(ireader);
	    
	            QueryParser parser = new QueryParser(Version.LUCENE_CURRENT, "content", analyzer);
	            Query query = parser.parse(text);
	            
	            ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;
	            
	            List<String> indexPosition=new ArrayList<String>();
	            //System.out.println("目标文件总数"+allfilename.get(filegroup).size());
	            
	            for (int i = 0; i < hits.length; i++) {
	                Document hitDoc = isearcher.doc(hits[i].doc);
	                String name=hitDoc.get("filename");
	                
	                
	                if(hits[i].score>limit)
	                {
	                	
	                //System.out.println(hits.length);
	                //System.out.println("____________________________");
	                //System.out.println(hitDoc.get("filename"));
	                //System.out.println(isearcher.explain(query, hits[i].doc));
	                
	                if(allfilename.get(filegroup).contains(name))
	                {
	                	//System.out.println("position"+i);
	                	indexPosition.add(i+"");
	                	
	                	
	                }
	              //  System.out.println(hitDoc.get("content"));
	              //  System.out.println(hitDoc.get("path"));
	                //System.out.println("____________________________");
	                }
	                else{
	           // System.out.println("总共返回"+i+"个文件");
	           // System.out.println("其中目标文件"+indexPosition.size()+"个");
	           // System.out.println("目标文件的位置是");
	            for(int t=0;t<indexPosition.size();t++)
	            {
	            	//System.out.print(" "+indexPosition.get(t));
	            }
	            //f+=
	            f=(float)indexPosition.size()/(float)allfilename.get(filegroup).size()*(float)indexPosition.size()/(float)i*2/((float)indexPosition.size()/(float)i+(float)indexPosition.size()/(float)allfilename.get(filegroup).size());
	           // System.out.println();
	           // System.out.println("召回率："+(float)indexPosition.size()/(float)allfilename.get(filegroup).size());
	            //System.out.println("准确率："+(float)indexPosition.size()/(float)i);
	           // System.out.println("f值："+(float)indexPosition.size()/(float)allfilename.get(filegroup).size()*(float)indexPosition.size()/(float)i*2/((float)indexPosition.size()/(float)i+(float)indexPosition.size()/(float)allfilename.get(filegroup).size()));
	            
	            break;
	            }
	            
	                
	                
	            }
	            //System.out.println(f);
	            ireader.close();
	            directory.close();
	        }catch(Exception e){
	            e.printStackTrace();
	        }
	        Date date2 = new Date();
	       // System.out.println("查看索引-----耗时：" + (date2.getTime() - date1.getTime()) + "ms\n");
	        return f;
	    }
	   
	   
	   
	  /* public void getTF(String query) {
	        try {
	            Directory directroy = FSDirectory.open(new File(
	            		INDEX_DIR));
	            IndexReader reader = DirectoryReader.open(directroy);
	            for (int i = 0; i < reader.numDocs(); i++) {
	                int docId = i;
	                System.out.println("第" + (i + 1) + "篇文档：");
	                Terms terms = reader.getTermVector(docId, "text");
	                if (terms == null)
	                    continue;
	                TermsEnum termsEnum = terms.iterator(null);
	                BytesRef thisTerm = null;
	                while ((thisTerm = termsEnum.next()) != null) {
	                    String termText = thisTerm.utf8ToString();
	                    DocsEnum docsEnum = termsEnum.docs(null, null);
	                   // (termText.equals("indexed"))&&
	                   // query.contains(termText)
	                    while ((docsEnum.nextDoc()) != DocIdSetIterator.NO_MORE_DOCS) {
	                    	if(query.contains(termText))
	                    	{
	                        System.out.println("termText:" + termText + " TF:  "
	                                + 1.0 * docsEnum.freq() / terms.size());
	                    	}
	                    }
	                }
	            }
	            reader.close();
	            directroy.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
*/
	    /*
	     * 计算IDF
	     * 
	     * *
	     */
/*	    public void getIDF() {
	        try {
	            Directory directroy = FSDirectory.open(new File(INDEX_DIR));
	            IndexReader reader = DirectoryReader.open(directroy);
	            List<AtomicReaderContext> list = reader.leaves();
	            System.out.println("文档总数 : " + reader.maxDoc());
	            for (AtomicReaderContext ar : list) {
	                String field = "text";
	                AtomicReader areader = ar.reader();
	                Terms terms = areader.terms(field);
	                TermsEnum tn = terms.iterator(null);
	                BytesRef text;
	                
	                while (((text = tn.next()) != null)) {
	                //	if(text.utf8ToString().equals("indexed"))
	                	{
	                		  System.out.println("field=" + field + "; text="
	                            + text.utf8ToString() + "   IDF : "
	                            + Math.log10(reader.maxDoc() * 1.0 / tn.docFreq())
	                    // + " 全局词频 :  " + tn.totalTermFreq()
	                            );
	                	}
	                  
	                }
	            }
	            reader.close();
	            directroy.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }*/
    
	
}
