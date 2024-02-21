package org.example;

import java.util.Optional;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    private static Logger log = LoggerFactory.getLogger(App.class);
    public static void main(String[] args) {
        final Optional<String> startingURL = parseStartingURL(args);
        if (startingURL.isPresent()) {
            Crawler crawler = new SingleThreadedCrawler();
            crawler.crawl(startingURL.get().trim());
        }
    }

    private static Optional<String> parseStartingURL(String[] args) {
        try {
            Options options = new Options();
            options.addOption(Option.builder("u")
                                    .longOpt("url")
                                    .hasArg(true)
                                    .required(true)
                                    .desc("Starting url to begin crawling from")
                                    .build());
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = null;
            
            cmd = parser.parse(options, args);
            String startingURL = cmd.getOptionValue("u");
            return Optional.of(startingURL);
        } catch (final ParseException e) {
            log.error("Error parsing arguments: ", e);
            return Optional.empty();
        } catch (final Exception e) {
            log.error("Unhandled error while parsing arguments: ", e);
            return Optional.empty();
        }
    }
}
