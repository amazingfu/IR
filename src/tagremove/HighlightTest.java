package tagremove;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.util.Version;

  public class HighlightTest 
    {
	 public static void main(String args[]) throws Exception{
		 String txt = "�Ұ������찲�ţ��찲���ϲ����,ΰ������ë��ϯ��ָ��������ǰ������ǰ���������������뿪��������˼���Ļ����Ǹ��ϵĽ�����ΰ��ĸ�ʥ��sadfsadnfl.sajdfl;aksjdf;lsadfsadfm.asd���ǿ϶���������̵Ļ�����������Ʒ����Ǹ�ɵ��㻪�ɹ�˾�Ļ���ںؿ����ǶԻ������ǹ��찲�ż�  �����绰����ʼ���� ���������Ǵ����  ������������";
		 QueryParser parser = new QueryParser(Version.LUCENE_CURRENT,"content",new StandardAnalyzer(Version.LUCENE_CURRENT));

		 Query query = parser.parse("���� ΰ��");

		 QueryScorer queryScorer = new QueryScorer(query);//�������Ҫ�����Դ�������
	        //���ø�����ǩ

		 Formatter formatter = new SimpleHTMLFormatter("<span style='color:red;'>","</span>");
	        //����������

		 Highlighter hl = new Highlighter(formatter,queryScorer);
	        

		 Fragmenter fragmenter = new SimpleSpanFragmenter(queryScorer);

		 hl.setTextFragmenter(fragmenter);
	        //��ȡ���ؽ��

		 String str = hl.getBestFragment(new StandardAnalyzer(Version.LUCENE_CURRENT),"content",txt);

		 System.out.println(str);
	    }
}
