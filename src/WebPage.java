import java.util.HashSet;
import java.util.Set;

public class WebPage {
	private Set<String> links;
	
	private String title;
	
	private int depth;
	
	public int getDepth() {
		return depth;
	}

	public WebPage setDepth(int depth) {
		this.depth = depth;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public WebPage setTitle(String title) {
		this.title = title;
		return this;
	}

	public WebPage() {
		links = new HashSet<>();
	}
	
	public WebPage addLink(String link) {
		links.add(link);
		return this;
	}
	
	public String[] getLinks() {
		return links.toArray(new String[links.size()]);
	}

}
