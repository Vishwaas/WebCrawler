import java.util.Map;

public class Application {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Crawler crawler = new Crawler("https://www.mkyong.com/java/jsoup-basic-web-crawler-example/");
		crawler.start().thenAccept((crawledData) -> {
			crawledData.forEach((key, value)-> {
				System.out.print(String.format("Details for link %s at depth %d: ", key, value.getDepth()));
				System.out.println("Title is " + value.getTitle());
			});
		});
		
		
		
	}

}
