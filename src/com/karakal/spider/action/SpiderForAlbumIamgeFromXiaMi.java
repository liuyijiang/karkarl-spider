package com.karakal.spider.action;

import java.util.List;

/**
 * ץȡ����ר����Ƭ fromϺ��
 * @author xuting
 *
 */
public class SpiderForAlbumIamgeFromXiaMi implements Runnable{

	private List<String> singers;
	
	public SpiderForAlbumIamgeFromXiaMi(List<String> singers){
		   this.singers = singers;
	}
	
	@Override
	public void run() {
		while(true){
			String singername = null;
			boolean doaction = true;
			try{
				synchronized (singers) {
					if(!singers.isEmpty()){
						System.out.println(Thread.currentThread().getName()+" ��ǰ������"+singers.size());
						singername = singers.get(0);
						singers.remove(0);
					}else{
						singers.notifyAll(); //�����������
						singers.wait();//�ֳɵȴ�
						doaction = false;
					}
				}
				if(doaction){
					int i= (int)(Math.random()*5+1);
					Thread.sleep(i*1000);
					System.out.println(Thread.currentThread().getName()+ singername);
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
