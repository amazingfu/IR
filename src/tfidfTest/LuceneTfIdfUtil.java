package tfidfTest;

import java.io.File;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.DocsEnum;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

public class LuceneTfIdfUtil {
	
	public static final String INDEX_PATH = "d:\\testindex";

    public void index() {
        try {
            Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
            Directory directory = FSDirectory.open(new File(INDEX_PATH));
            IndexWriterConfig config = new IndexWriterConfig(
                    Version.LUCENE_CURRENT, analyzer);
            IndexWriter iwriter = new IndexWriter(directory, config);
            FieldType ft = new FieldType();
            ft.setIndexed(true);// �洢
            ft.setStored(true);// ����
            ft.setStoreTermVectors(true);
            ft.setTokenized(true);
            ft.setStoreTermVectorPositions(true);// �洢λ��
            ft.setStoreTermVectorOffsets(true);// �洢ƫ����
            Document doc = new Document();
            String text = "This is the text to be indexed.";
            doc.add(new Field("text", text, ft));
            iwriter.addDocument(doc);
            doc = new Document();
            text = "I am the text to be stored.";
            doc.add(new Field("text", text, ft));
            iwriter.addDocument(doc);
            iwriter.forceMerge(1);// ���һ��Ҫ�ϲ�Ϊһ��segment����Ȼ�޷�����idf
            iwriter.close();
        } catch (Exception e) {

        }
    }

    /**
     * ��ȡ��������ʾ��Ƶ
     * 
     * **/
    public void getTF() {
        try {
            Directory directroy = FSDirectory.open(new File(
                    INDEX_PATH));
            IndexReader reader = DirectoryReader.open(directroy);
            for (int i = 0; i < reader.numDocs(); i++) {
                int docId = i;
                
                System.out.println("��" + (i + 1) + "ƪ�ĵ���");
                Terms terms = reader.getTermVector(docId, "text");
                if (terms == null)
                    continue;
                TermsEnum termsEnum = terms.iterator(null);
                BytesRef thisTerm = null;
                while ((thisTerm = termsEnum.next()) != null) {
                    String termText = thisTerm.utf8ToString();
                    DocsEnum docsEnum = termsEnum.docs(null, null);
                    while ((termText.equals("indexed"))&&(docsEnum.nextDoc()) != DocIdSetIterator.NO_MORE_DOCS) {
                        System.out.println("termText:" + termText + " TF:  "
                                + 1.0 * docsEnum.freq() / terms.size());
                    }
                }
            }
            reader.close();
            directroy.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * ����IDF
     * 
     * *
     */
    public void getIDF() {
        try {
            Directory directroy = FSDirectory.open(new File(INDEX_PATH));
            IndexReader reader = DirectoryReader.open(directroy);
            List<AtomicReaderContext> list = reader.leaves();
            System.out.println("�ĵ����� : " + reader.maxDoc());
            for (AtomicReaderContext ar : list) {
                String field = "text";
                AtomicReader areader = ar.reader();
                Terms terms = areader.terms(field);
                TermsEnum tn = terms.iterator(null);
                BytesRef text;
                
                while (((text = tn.next()) != null)) {
                	if(text.utf8ToString().equals("indexed"))
                	{
                		  System.out.println("field=" + field + "; text="
                            + text.utf8ToString() + "   IDF : "
                            + Math.log10(reader.maxDoc() * 1.0 / tn.docFreq())
                    // + " ȫ�ִ�Ƶ :  " + tn.totalTermFreq()
                            );
                	}
                  
                }
            }
            reader.close();
            directroy.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        LuceneTfIdfUtil luceneTfIdfUtil = new LuceneTfIdfUtil();
        //luceneTfIdfUtil.index();
        luceneTfIdfUtil.getTF();
        luceneTfIdfUtil.getIDF();
    }

}
