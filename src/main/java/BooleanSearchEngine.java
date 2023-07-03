import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {
    private final Map<String, List<PageEntry>> index;

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        index = new HashMap<>();
        for (File file : pdfsDir.listFiles()) {
            if (file.getName().endsWith(".pdf")) {
                PdfDocument doc = new PdfDocument(new PdfReader(file));
                int numPages = doc.getNumberOfPages();
                for (int pageNum = 1; pageNum <= numPages; pageNum++) {
                    PdfPage page = doc.getPage(pageNum);
                    String text = PdfTextExtractor.getTextFromPage(page);
                    String[] words = text.split("\\P{IsAlphabetic}+");
                    Map<String, Integer> freqs = new HashMap<>();
                    for (String word : words) {
                        if (word.isEmpty()) {
                            continue;
                        }
                        word = word.toLowerCase();
                        freqs.put(word, freqs.getOrDefault(word, 0) + 1);
                    }
                    for (String word : freqs.keySet()) {
                        List<PageEntry> entries = index.getOrDefault(word, new ArrayList<>());
                        entries.add(new PageEntry(file.getName(), pageNum, freqs.get(word)));
                        index.put(word, entries);
                    }
                }
                doc.close();
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        return index.getOrDefault(word.toLowerCase(), Collections.emptyList());
    }
}
