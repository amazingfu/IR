package ir;

import java.io.File;

public class irtest {
	
	private static String[] topic=new String[]{"����ҽѧ","��������ƶ��ը������̬","911�ֲ�Ϯ�����������÷�����ʲô�仯��"
			,"��������˱ȿ��ѵķ�Ӧ","���ڿ�����ս���еı��С����¸��桢���������Լ������κ���֮�йص�����"
			,"�Ჴ��ͳ�μ��壨���ң��±�ı����������Լ���̵����Ը����ķ�Ӧ","���ӡ�Ừ�˵ı����¼���"
			,"������������΢��¢����Ϊ����֪�������ϵ����ݡ��ð�������ʵ���ͽ�����Լ������о���"
			,"�����Ѿ����еĺ��������顣","�����Ƕ��ж���ƽ���̵�������","�������ߣ�AOL����������Netscape���Ĺ�ϵ��ʲô��"
			,"ʲô�Ƕ����ŵ��","�ж�֮�䷢�������顣"," ʲô���������壿","��Լ�Ͳ�����ʲô��ϵ��"
			,"̩�������޾���Σ���еĽ�ɫ��������羭�õ�Ӱ�죬ͬʱ����������Σ������ȡ�Ĳ��衣"
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
//	//index.getTF("����ҽѧ");
	//index.getIDF();
	for(double limit=0.001;limit<0.1;limit+=0.001)
	{double sum=0.0;
	for(int i=0;i<topic.length;i++)
	{
		//System.out.println("��������Ϊ��"+topic[i]);
		double f=index.searchIndex(topic[i],i,limit);
		sum+=f;
	}
	System.out.println("��ֵ"+limit+"ʱ,fֵ�ܺ�Ϊ"+sum);
	}
//	System.out.println("��������Ϊ��"+topic[0]);
//	index.searchIndex(topic[0],0);
	
	
	}
	
	
}
