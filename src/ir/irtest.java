package ir;

import java.io.File;

public class irtest {
	
	private static String[] topic=new String[]{"再生医学","美国对于贫铀炸弹的姿态","911恐怖袭击后美国经济发生了什么变化？"
			,"各国对洛克比空难的反应","关于科索沃战争中的暴行、军事干涉、独立申明以及其他任何与之有关的事情"
			,"尼泊尔统治家族（王室）事变的背景和详情以及后继的来自各方的反应","针对印尼华人的暴力事件。"
			,"美国政府起诉微软垄断行为，需知道该诉讼的内容、该案例的事实、和解过程以及最后的判决。"
			,"各国已经进行的核武器试验。","叙利亚对中东和平进程的立场。","美国在线（AOL）和网景（Netscape）的关系是什么？"
			,"什么是厄尔尼诺？","中俄之间发生的事情。"," 什么是温室气体？","北约和波兰是什么关系？"
			,"泰国在亚洲经济危机中的角色及其对世界经济的影响，同时各国针对这次危机所采取的步骤。"
	};
			 
		
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
		Index index=new Index();
		MoveTag move=new MoveTag();
//		File fileIndex = new File(Index.INDEX_DIR);
//        if(deleteDir(fileIndex)){
//            fileIndex.mkdir();
//        }else{
//            fileIndex.mkdir();
//        }
	String path_1="D://LuceneTextFileUnicode//";
	String path_2="D://LuceneTextFileUTF-8//";
	String newpath="D://LuceneRefinedTextFile//";
	
//	move.moveTag(path_1, newpath,"Unicode");
//	move.moveTag(path_2, newpath,"UTF-8");
//	index.createIndex(newpath);
//	//index.getTF("再生医学");
	//index.getIDF();
	for(double limit=0.001;limit<0.1;limit+=0.001)
	{double sum=0.0;
	for(int i=0;i<topic.length;i++)
	{
		//System.out.println("搜索话题为："+topic[i]);
		double f=index.searchIndex(topic[i],i,limit);
		sum+=f;
	}
	System.out.println("阈值"+limit+"时,f值总和为"+sum);
	}
//	System.out.println("搜索话题为："+topic[0]);
//	index.searchIndex(topic[0],0);
	
	
	}
	
	
}
