import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Crawler {
	private String sourceLink;
	
	private int MAX_DEPTH = 3;;
	
	private int MAX_LINKS_PER_PAGE = 5;
	
	Map<String, WebPage> crawledData;
	
	public Crawler(String sourceLink) {
		this.sourceLink = sourceLink;
		crawledData = new ConcurrentHashMap<String, WebPage>();
	}
	
	public CompletableFuture<Map<String, WebPage>> start() {
		return crawl(sourceLink, 1).thenApply((result) -> {
			return crawledData;
		}); 
	}
	
	private CompletableFuture<?> crawl(String sourceUrl, int depth)  {
		return CompletableFuture.runAsync(() -> {
			try {
				if (depth > MAX_DEPTH) {
					return;
				}
				Document doc = Jsoup.connect(sourceUrl).get();
				WebPage webpage = new WebPage();
				webpage.setTitle(doc.select("title").get(0).text())
					.setDepth(depth);
				int count = 0;
				List<CompletableFuture<?>> completableFutures = new LinkedList<>();
				for(Element link: doc.select("a[href]")) {
					count++;
					String url = link.attr("abs:href");
					webpage.addLink(url);
					completableFutures.add(crawl(url, depth + 1));
					if (count > MAX_LINKS_PER_PAGE) {
						break;
					}
				}
				CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[completableFutures.size()])).get();
				crawledData.put(sourceUrl, webpage);
			} catch(Exception exception) {
				System.out.println("Error while crawling");
				exception.printStackTrace();
			}
			
		}, Executors.newSingleThreadExecutor());
	}

}
