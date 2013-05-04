package com.karakal.spider.main;

import java.util.ArrayList;
import java.util.List;

import com.karakal.spider.action.SpiderForAlbumIamgeFromXiaMi;
import com.karakal.spider.server.SingerInforService;

public class SpiderRun {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<String> singers = new ArrayList<String>();
		Thread t1 = new Thread(new SingerInforService(singers));
		Thread t2 = new Thread(new SpiderForAlbumIamgeFromXiaMi(singers));
		Thread t3 = new Thread(new SpiderForAlbumIamgeFromXiaMi(singers));
		Thread t4 = new Thread(new SpiderForAlbumIamgeFromXiaMi(singers));
         t1.start();
         t2.start();
         t3.start();
         t4.start();
	}

}
