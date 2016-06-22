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
		 String txt = "我爱北京天安门，天安门上彩旗飞,伟大领袖毛主席，指引我们向前进，向前进！！！想起身离开东京法律思考的机会那个上的讲话那伟大的个圣诞sadfsadnfl.sajdfl;aksjdf;lsadfsadfm.asd那是肯定激发了深刻的机会拉萨宽带计费了那个傻大姐华纳公司的机会节贺卡就是对话框那是国天安门际  北京电话卡开始觉啊 北京得人们大会堂  北京！！！！";
		 QueryParser parser = new QueryParser(Version.LUCENE_CURRENT,"content",new StandardAnalyzer(Version.LUCENE_CURRENT));

		 Query query = parser.parse("北京 伟大");

		 QueryScorer queryScorer = new QueryScorer(query);//如果有需要，可以传入评分
	        //设置高亮标签

		 Formatter formatter = new SimpleHTMLFormatter("<span style='color:red;'>","</span>");
	        //高亮分析器

		 Highlighter hl = new Highlighter(formatter,queryScorer);
	        

		 Fragmenter fragmenter = new SimpleSpanFragmenter(queryScorer);

		 hl.setTextFragmenter(fragmenter);
	        //获取返回结果

		 String str = hl.getBestFragment(new StandardAnalyzer(Version.LUCENE_CURRENT),"content",txt);

		 System.out.println(str);
	    }
}
