package com.personalplatform.services;

import com.personalplatform.model.StoreData;
import com.personalplatform.repos.StoreDataRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class CsvImportService {

    private final StoreDataRepository storeDataRepository;

    public CsvImportService(StoreDataRepository storeDataRepository) {
        this.storeDataRepository = storeDataRepository;
    }

    @Transactional
    public int importCsv(MultipartFile file) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("CSV file is empty");
        }

        /*
         * Ключ:
         *
         * storeNumber
         * +
         * categoryNormalized
         * +
         * format
         *
         * Наприклад:
         *
         * 1026|вино|7
         *
         */

        Map<String, StoreData> aggregatedData = new LinkedHashMap<>();

        try (
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                file.getInputStream(),
                                StandardCharsets.UTF_8
                        )
                )
        ) {

            String line;

            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {

                lineNumber++;

                line = line.trim();

                // Пропускаємо пусті рядки
                if (line.isEmpty()) {
                    continue;
                }

                try {

                    StoreData row = parseLine(line);

                    String key = createKey(row);

                    StoreData existing = aggregatedData.get(key);

                    if (existing == null) {

                        aggregatedData.put(key, row);

                    } else {

                        // Сумуємо продажі
                        existing.setSales(
                                existing.getSales() + row.getSales()
                        );
                    }

                } catch (Exception e) {

                    throw new IllegalArgumentException(
                            "Error parsing CSV line "
                                    + lineNumber
                                    + ": "
                                    + line,
                            e
                    );
                }
            }
        }

        /*
         * Якщо потрібно кожен новий CSV додавати до існуючих даних,
         * цей блок можна змінити.
         *
         * Зараз перед імпортом очищаємо таблицю.
         */

        storeDataRepository.deleteAll();

        storeDataRepository.saveAll(aggregatedData.values());

        return aggregatedData.size();
    }


    /**
     * Розбір одного рядка:
     *
     * 1026~Тернопіль, Перля, 3;Вино;7;6638
     *
     * Частина до "~":
     * 1026
     *
     * Після "~":
     * Тернопіль, Перля, 3
     *
     * Далі:
     * Вино
     * 7
     * 6638
     */
    private StoreData parseLine(String line) {

        // Розділяємо магазин та решту
        int tildeIndex = line.indexOf('~');

        if (tildeIndex == -1) {
            throw new IllegalArgumentException(
                    "Missing '~' separator"
            );
        }

        String storeNumberText =
                line.substring(0, tildeIndex).trim();

        String rest =
                line.substring(tildeIndex + 1).trim();

        // Розбираємо решту через ;
        String[] columns = rest.split(";");

        if (columns.length != 4) {
            throw new IllegalArgumentException(
                    "Expected 4 columns after '~', but got "
                            + columns.length
            );
        }

        String storeName = columns[0].trim();

        String category = columns[1].trim();

        String formatText = columns[2].trim();

        String salesText = columns[3].trim();

        Integer storeNumber =
                Integer.parseInt(storeNumberText);

        Integer format =
                Integer.parseInt(formatText);

        Long sales =
                Long.parseLong(salesText);

        /*
         * Нормалізуємо категорію.
         *
         * Вино
         * вино
         * ВИНО
         *
         * перетворяться в:
         *
         * вино
         */

        String categoryNormalized =
                category
                        .trim()
                        .toLowerCase(Locale.ROOT);

        return new StoreData(
                storeNumber,
                storeName,
                category,
                categoryNormalized,
                format,
                sales
        );
    }


    /**
     * Створюємо ключ для агрегації.
     *
     * Наприклад:
     *
     * 1026|вино|7
     */
    private String createKey(StoreData data) {

        return data.getStoreNumber()
                + "|"
                + data.getCategoryNormalized()
                + "|"
                + data.getFormat();
    }
}
