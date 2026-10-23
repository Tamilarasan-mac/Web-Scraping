# Website Text Extractor

A simple, beginner-friendly **Java 21 + Maven** command-line application that connects
to a website URL, downloads the HTML, strips out non-content elements (scripts,
styles, navigation, headers, footers, etc.), and extracts the main **readable text**.

The extracted text is printed to the console and also saved to a local file:
`extracted-text.txt`.

No Spring Boot. No database. Just plain Java + [Jsoup](https://jsoup.org/).

---

## Features

- Prompts the user for a website URL via the console
- Connects to the site with a proper `User-Agent` and connection timeout
- Parses the HTML using Jsoup
- Removes `script`, `style`, `noscript`, `nav`, `footer`, `header`, `iframe`, `svg`,
  and `form` elements before extracting text
- Collapses excess whitespace into clean, readable output
- Prints the extracted text to the console
- Saves the extracted text to `extracted-text.txt`
- Friendly, human-readable error messages for invalid URLs, timeouts, unreachable
  hosts, HTTP errors (403/404/500), and empty pages — no raw stack traces

---

## Tech Stack

| Component | Version              |
|-----------|-----------------------|
| Java      | 21                    |
| Build tool| Maven                 |
| Parser    | Jsoup 1.23.2           |
| IDE       | IntelliJ IDEA (recommended) |

---

## Project Structure

```text
website-text-extractor/
├── pom.xml
├── README.md
└── src/
    └── main/
        └── java/
            └── com/example/
                ├── Main.java                  # Entry point — handles I/O and user interaction
                └── WebsiteTextExtractor.java   # Core logic — connects, parses, cleans text
```

---

## Prerequisites

- **JDK 21** installed and configured (`java -version` should show 21)
- **Maven 3.8+** installed (`mvn -version`), or use IntelliJ's bundled Maven
- An internet connection (the app needs to reach both Maven Central for the
  dependency, and the target website at runtime)

---

## Getting Started

### 1. Clone or download the project

```bash
git clone <your-repo-url>
cd website-text-extractor
```

### 2. Build the project

```bash
mvn clean compile
```

This downloads the Jsoup dependency and compiles the source code.

### 3. Run the application

**Option A — Maven:**

```bash
mvn exec:java
```

**Option B — IntelliJ IDEA:**

Open `Main.java` and click the green ▶ **Run** icon next to the `main` method.

### 4. Follow the prompt

```text
Enter website URL:
https://example.com
```

Type a URL and press **Enter**.

---

## Example Output

```text
Enter website URL:
https://example.com

Extracting text...

Text extracted successfully!

----- EXTRACTED TEXT -----
Example Domain This domain is for use in illustrative examples in documents.
You may use this domain in literature without prior coordination or asking
for permission. More information...
---------------------------

Output saved to:
/path/to/website-text-extractor/extracted-text.txt
```

The output file `extracted-text.txt` is created in the project's working
directory (the project root when run from IntelliJ or Maven).

---

## Error Handling

The app handles common failure cases with clear messages instead of stack traces:

| Situation                        | Message shown                                         |
|-----------------------------------|--------------------------------------------------------|
| Empty/blank input                 | `Invalid input: URL cannot be empty.`                  |
| Malformed URL                     | `Invalid input: The URL is not valid: ...`              |
| Unknown/unreachable host          | `Reason: The website could not be found. Check the address.` |
| Connection timeout                | `Reason: The website took too long to respond (timeout).` |
| HTTP 403 Forbidden                | `Reason: Access to this website was denied (403 Forbidden).` |
| HTTP 404 / 500 / other errors     | `Reason: The website returned an HTTP error: <code>`   |
| Page has no readable text         | `Reason: No readable text was found on this page.`     |

---

## Notes & Limitations

- This tool only sees the HTML returned directly by the server. It does **not**
  execute JavaScript, so content rendered client-side by JavaScript-heavy sites
  (many single-page apps built with React/Angular/Vue) may not appear in the
  extracted text.
- Some sites actively block automated requests (bot detection, CAPTCHAs, rate
  limiting) regardless of the `User-Agent` header used here.
- Please respect each site's `robots.txt` and terms of service before scraping it.

---

## Possible Future Improvements

- Convert into a Spring Boot REST API (`GET /api/extract?url=...`) by wrapping
  `WebsiteTextExtractor` as a `@Service` and adding a `@RestController` —
  the core extraction logic would not need to change.
- Add support for extracting only the "main article" content (e.g., using
  readability heuristics) instead of the whole page body.
- Add a headless-browser fallback (Selenium/Playwright) for JavaScript-rendered pages.
- Accept a URL as a command-line argument in addition to interactive input.

---

## License

Free to use and modify for learning purposes.
---

## Requirements

- Java 21
- Maven
