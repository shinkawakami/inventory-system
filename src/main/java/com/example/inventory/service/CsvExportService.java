package com.example.inventory.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class CsvExportService {

    public byte[] export(String[] headers, List<String[]> rows) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            // UTF-8 BOM
            out.write(0xEF);
            out.write(0xBB);
            out.write(0xBF);

            try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8))) {
                writer.println(toCsvLine(headers));

                for (String[] row : rows) {
                    writer.println(toCsvLine(row));
                }
                writer.flush();
            }

            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("CSV出力に失敗しました。", e);
        }
    }

    private String toCsvLine(String[] values) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(escape(values[i]));
        }

        return sb.toString();
    }

    private String escape(String value) {
        if (value == null) {
            return "\"\"";
        }

        String escaped = value.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }
}