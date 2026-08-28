package com.example;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Connects to a website, downloads its HTML, strips out non-content
 * elements (scripts, styles, navigation, etc.) and returns the
 * remaining readable text.
 */
public class WebsiteTextExtractor {

    // Some websites block requests that don't look like they come
    // from a real browser.
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                    "AppleWebKit/537.36 (KHTML, like Gecko) " +
                    "Chrome/124.0.0.0 Safari/537.36";

    private static final int TIMEOUT_MILLIS = 10_000; // 10 seconds

    /**
     * Downloads the page at the given URL and returns its
     * cleaned, readable text.
     *
     * @param url the full website URL, e.g. https://example.com
     * @return extracted readable text
     * @throws IOException if the connection or download fails
     * @throws IllegalArgumentException if the URL is missing or malformed
     */
    public String extractText(String url) throws IOException {

        // Validate URL
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("URL cannot be empty.");
        }

        // Normalize URL
        String normalizedUrl = normalizeUrl(url.trim());

        Document document;

        try {
            document = Jsoup.connect(normalizedUrl)
                    .userAgent(USER_AGENT)
                    .timeout(TIMEOUT_MILLIS)
                    .followRedirects(true)
                    .ignoreHttpErrors(false)
                    .get();

        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(
                    "The URL is not valid: " + normalizedUrl,
                    e
            );

        } catch (SocketTimeoutException e) {
            throw new IOException(
                    "The website took too long to respond (timeout).",
                    e
            );

        } catch (UnknownHostException e) {
            throw new IOException(
                    "The website could not be found. Check the address.",
                    e
            );

        } catch (HttpStatusException e) {

            int statusCode = e.getStatusCode();

            if (statusCode == 401) {
                throw new IOException(
                        "Access to this website requires authentication (401 Unauthorized).",
                        e
                );
            }

            if (statusCode == 403) {
                throw new IOException(
                        "Access to this website was denied (403 Forbidden).",
                        e
                );
            }

            throw new IOException(
                    "The website returned an HTTP error: " + statusCode,
                    e
            );
        }

        // Remove elements that normally don't contain
        // useful readable page content.
        document.select(
                "script, style, noscript, nav, footer, header, iframe, svg, form"
        ).remove();

        // Extract readable text.
        // Document.body() is available for a parsed HTML document.
        String rawText = document.body().text();

        // Clean whitespace.
        String cleanedText = cleanWhitespace(rawText);

        // Check whether readable text exists.
        if (cleanedText.isBlank()) {
            throw new IOException(
                    "No readable text was found on this page."
            );
        }

        return cleanedText;
    }

    /**
     * Adds "https://" automatically if the user
     * forgot to type a URL scheme.
     */
    private String normalizeUrl(String url) {

        if (!url.startsWith("http://")
                && !url.startsWith("https://")) {

            return "https://" + url;
        }

        return url;
    }

    /**
     * Collapses multiple spaces/newlines into single spaces
     * and trims the result.
     */
    private String cleanWhitespace(String text) {

        return text
                .replaceAll("\\s+", " ")
                .trim();
    }
}