# Simple WebCrawler
This is a simple implementation of a WebCrawler. The crawler will only crawl links within a given domain (any sub-pages from the provided starting URL). For each page it visits it prints the URL visited and a list of URLs found on that page.

## Running the webcrawler
The project was built using gradle. It can be run as follows:
```
./gradlew run --args='-u https://STARTING_URL.com'
```

## Considerations
These are the major areas that were considered when building the web crawler.
### Starting URL
The crawler requires a URL to start crawling from. I chose to supply this as a command-line argument. Some assumptions were made here to simplify the implementation: 
1. The starting URL is a valid URL. If it isn't the program will simply fail, rather than gracefully informing the user what happened.
2. The starting URL is the root URL for a domain. This was to simplify the process of fetching the `robots.txt` file for processing.
### DFS vs BFS
I chose to implement this using Depth-First Search, and keep track of the URLs already visited to avoid loops. 
### Throttling
Since the crawler is being limited to a single domain, it's important to implement throttling to avoid over-loading any particular website with requests.
### Multi-threading
Multi-threading would allow for faster and more efficient crawling. I decided to start with a single-threaded implementation since the crawl speed would be limited with throttling to avoid over-loading any websites anyway. For larger sites which can handle higher traffic multi-threaded would be desirable, so I started an outline of what a multi-threaded crawler would look like.


## Simplifications
There were a number of simplifications made to cut down on implementation time.
### Handling URLs
I used a simplified approach of checking whether a page was on the same domain with checking it `startsWith` the starting URL. There may be a more robust way of checking this. Additionally I did some simple URL manipulation to trim anchor tags (e.g `www.example.com/page#ANCHOR_TAG) to avoid visiting the same page multiple times. There are further improvements that could be made here, such as avoiding any URLs with particular extensions (.jpg/.png/.pdf etc).
### Unit tests
I implemented some simple unit tests to show the general idea, but ran into issues with mocking classes. I didn't want to spend too much time fixing this so the unit tests aren't passing currently.
### Handling robots.txt 
I didn't want to spend too much time implementing the handling of the robots.txt file, so I just added a skeleton framework for how the rest of the program would handle this file. 
### Efficiency tests
It would be really nice to be able to compare the performance of the single-threaded vs multi-threaded implementations. This could be done by setting up a dummy website.