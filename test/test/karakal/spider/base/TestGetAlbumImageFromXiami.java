package test.karakal.spider.base;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 测试获得专辑照片 虾米
 * 
 * @author xuting
 * 
 */
public class TestGetAlbumImageFromXiami {

	private static final Log log = LogFactory
			.getLog(TestGetAlbumImageFromXiami.class);

	/**
	 * 抓取图片存放目录
	 */
	private static final String PIC_DIR = "D://image" + File.separator;

	/**
	 * 链接超时
	 */
	private static final int TIME_OUT = 5000;

	private static final String SEARCHURL = "http://www.xiami.com/search/album/page/";

	private static final String SEARCHPARM = "?spm=0.0.0.0.0kjlAe&key=";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String url = SEARCHURL + 2 + SEARCHPARM + "%E5%BC%A0%E5%AD%A6%E5%8F%8B";
		TestGetAlbumImageFromXiami t = new TestGetAlbumImageFromXiami();
		t.filterElementResources(url);
	}

	// 获得这个明星的专辑可以分多少页
	private int findAllPage(Elements search) throws Exception {
		int allpage = 0;
		if (search != null) {
			Element searchTag = search.get(0);// 解析页面上的查询记录数Html
			Elements infoTag = searchTag.children();
			Element allTag = infoTag.get(0);
			String all = allTag.html();// 获得页面上总记录数
			int allrecord = Integer.parseInt(all);
			allpage = ((allrecord - 1) / 12) + 1;// 虾米网每页显示12条记录
		}
		return allpage;
	}

	public void filterMoreElementResources(String url) throws Exception {
		Connection conn = Jsoup.connect(url);
		Document doc = conn.post();
		Elements aTags = doc.select("a[class=CDcover100]");
		for (int i = 0; i < aTags.size(); i++) {
			String[] parms = findImageResources(aTags.get(i));
			save(parms);
		}
	}

	public void filterElementResources(String url) {
		try {
			System.out.println("--start--");
			Connection conn = Jsoup.connect(url);
			Document doc = conn.post();
			// step 找打总页数
			int page = findAllPage(doc.select("p[class=seek_counts ok]"));// 虾米网分页显示信息是属性石seek_counts
																			// ok
			Elements aTags = doc.select("a[class=CDcover100]");
			for (int i = 0; i < aTags.size(); i++) {
				String[] parms = findImageResources(aTags.get(i));
				save(parms);
			}
			if (page > 1) {
				for (int i = 2; i <= page; i++) {
					System.out.println("################"+i);
					String nexturl = SEARCHURL + i + SEARCHPARM
							+ "%E5%BC%A0%E5%AD%A6%E5%8F%8B";
					filterMoreElementResources(nexturl);
				}
			}
			System.out.println("--over--");
		} catch (Exception e) {
              e.printStackTrace();
		} finally {

		}
	}

	private String[] findImageResources(Element aTag) {
		String[] parms = new String[2];
		String title = aTag.attr("title");// 获得专辑名
		if(title.contains("/")){
			title = title.replaceAll("/", "_");
		}
		parms[0] = title;
		Elements aimgs = aTag.children();
		if (aimgs != null && aimgs.size() > 0) {
			Element img = aimgs.get(0);
			String url = img.attr("src");
			String type = url.substring(url.lastIndexOf("."),url.length());
			url = url.substring(0,url.lastIndexOf("_"));
			parms[1] = url + type;
			System.out.println(parms[1]);
			System.out.println("");
		}
		return parms;
	}

	private void save(String[] parms) throws Exception {
		String fileName = parms[0]+".jpg";
		String imageFolder = PIC_DIR + File.separator + "张学友" + File.separator + "专辑" + File.separator;
		File folder = new File(imageFolder);
		if(!folder.exists()){
			folder.mkdirs();
		}
		String filePath = imageFolder + fileName;
		BufferedOutputStream out = null;
		byte[] bit = getByte(parms[1]);
		if (bit.length > 0) {
			try {
				out = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
				out.write(bit);
				out.flush();
			}catch(Exception e){
				e.printStackTrace();
			} finally {
				if (out != null)
					out.close();
			}
		}
	}
	
	
	private byte[] getByte(String uri) throws Exception {
		HttpClient client = new DefaultHttpClient();
		client.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, TIME_OUT);
		HttpGet get = new HttpGet(uri);
		get.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
				TIME_OUT);
		try {
			HttpResponse resonse = client.execute(get);
			if (resonse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = resonse.getEntity();
				if (entity != null) {
					return EntityUtils.toByteArray(entity);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.getConnectionManager().shutdown();
		}
		return new byte[0];
	}

}
