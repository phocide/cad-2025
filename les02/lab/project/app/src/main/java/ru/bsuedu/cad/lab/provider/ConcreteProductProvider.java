package ru.bsuedu.cad.lab.provider;

import ru.bsuedu.cad.lab.io.Reader;
import ru.bsuedu.cad.lab.model.Product;
import ru.bsuedu.cad.lab.parser.Parser;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConcreteProductProvider implements ProductProvider {
    // Компонент для чтения данных (например, из файла CSV).
    private final Reader reader;
    // Компонент для парсинга прочитанных данных в объекты Product.
    private final Parser parser;

    public ConcreteProductProvider(Reader reader, Parser parser) {
        this.reader = reader;
        this.parser = parser;
    }

    @Override
    public List<Product> getProducts() {
        // Читаем сырой текст (CSV содержимое) из ресурса.
        String content = reader.read();
        // Парсим текст и возвращаем список объектов Product.
        return parser.parse(content);
    }
} 