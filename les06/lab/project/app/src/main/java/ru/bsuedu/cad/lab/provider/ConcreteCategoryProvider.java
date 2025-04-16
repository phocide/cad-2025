package ru.bsuedu.cad.lab.provider;

import ru.bsuedu.cad.lab.io.Reader;
import ru.bsuedu.cad.lab.model.Category;
import ru.bsuedu.cad.lab.parser.CategoryCSVParser;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;

public class ConcreteCategoryProvider implements CategoryProvider {
    private final Reader reader;
    private final CategoryCSVParser parser;

    public ConcreteCategoryProvider(@Qualifier("categoryReader") Reader reader, CategoryCSVParser parser) {
        this.reader = reader;
        this.parser = parser;
    }

    @Override
    public List<Category> getCategories() {
        String content = reader.read();
        return parser.parse(content);
    }
} 