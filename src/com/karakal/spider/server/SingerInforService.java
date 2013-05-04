package com.karakal.spider.server;

import java.util.List;
/**
 * ��ѯ��������
 * @author xuting
 *
 */
public class SingerInforService implements Runnable{
    
	private boolean runsearch = true;
	private List<String> singers;
	
	private String[] strs1 = {"���»�","��ѧ��","�ܽ���","�Ն�","�ܴ���","������","����"};
	private String[] strs2 = {"���»�2","��ѧ��2","�ܽ���2","�Ն�2","�ܴ���2","������2","����2"};
	private String[] strs3 = {"���»�3","��ѧ��3","�ܽ���3","�Ն�3","�ܴ���3","������3","����3"};
	private String[][] str = {strs1,strs2,strs3};
	
	public SingerInforService(List<String> singers){
	   this.singers = singers;
	}
	
	@Override
	public void run() {
		int i =0;
		while(true){//һֱ���� ֱ����ѯ��û�н��
			String[] strs = null;
			try{
			if(i == 3){
				runsearch = false;
			}else{
				strs = str[i];
			}
			if(runsearch){
				synchronized (singers) {
				
						System.out.println("����������"+singers.size()+"  ����һ������");
						//ִ�����ݿ��ѯ
						for(String str: strs){
							singers.add(str);//����ģ������ݿ��в�ѯ������
						}
						System.out.println("�������! ����������"+singers.size());
						i++;
						singers.notifyAll();
						singers.wait();
				}
			}else{
				Thread.sleep(1000);
				System.out.println("��ѯ�����");
			}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}

	public List<String> getSingers() {
		return singers;
	}

	public void setSingers(List<String> singers) {
		this.singers = singers;
	}
	
	
	
}
