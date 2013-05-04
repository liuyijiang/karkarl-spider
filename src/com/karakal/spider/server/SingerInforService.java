package com.karakal.spider.server;

import java.util.List;
/**
 * 查询歌手名称
 * @author xuting
 *
 */
public class SingerInforService implements Runnable{
    
	private boolean runsearch = true;
	private List<String> singers;
	
	private String[] strs1 = {"刘德华","张学友","周杰伦","陶喆","周传雄","萧亚轩","王菲"};
	private String[] strs2 = {"刘德华2","张学友2","周杰伦2","陶喆2","周传雄2","萧亚轩2","王菲2"};
	private String[] strs3 = {"刘德华3","张学友3","周杰伦3","陶喆3","周传雄3","萧亚轩3","王菲3"};
	private String[][] str = {strs1,strs2,strs3};
	
	public SingerInforService(List<String> singers){
	   this.singers = singers;
	}
	
	@Override
	public void run() {
		int i =0;
		while(true){//一直运行 直到查询出没有结果
			String[] strs = null;
			try{
			if(i == 3){
				runsearch = false;
			}else{
				strs = str[i];
			}
			if(runsearch){
				synchronized (singers) {
				
						System.out.println("数据数量："+singers.size()+"  加载一组数据");
						//执行数据库查询
						for(String str: strs){
							singers.add(str);//这里模拟冲数据库中查询出数据
						}
						System.out.println("加载完成! 数据数量："+singers.size());
						i++;
						singers.notifyAll();
						singers.wait();
				}
			}else{
				Thread.sleep(1000);
				System.out.println("查询已完成");
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
