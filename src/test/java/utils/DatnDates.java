package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/** Ngày sinh biên 18 / 81 tuổi theo ngày chạy test (khớp nghiệp vụ Excel). */
public final class DatnDates {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private DatnDates() {}

    public static String minusYearsAsDdMmYyyy(int years) {
        return LocalDate.now().minusYears(years).format(FMT);
    }
}
