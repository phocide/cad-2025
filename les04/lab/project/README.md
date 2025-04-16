### Запуск с помощью Gradle

```
./gradlew clean run --args="[console|html]"
```

Например:
- `./gradlew clean run` - запуск с HTML-рендерером (по умолчанию)
- `./gradlew clean run --args="console"` - запуск с консольным рендерером
- `./gradlew clean run --args="html"` - запуск с HTML-рендерером
