package com.cafevery.dto.type;

import lombok.Getter;

import java.util.EnumMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public enum EDay {

    MONDAY("월", 1),
    TUESDAY("화", 2),
    WEDNESDAY("수", 3),
    THURSDAY("목", 4),
    FRIDAY("금", 5),
    SATURDAY("토", 6),
    SUNDAY("일", 7);

    private final String name;
    private final int number;

    EDay(String name, int number) {
        this.name = name;
        this.number = number;
    }

    public static EDay fromString(String dayStr) {
        return switch (dayStr) {
            case "월" -> MONDAY;
            case "화" -> TUESDAY;
            case "수" -> WEDNESDAY;
            case "목" -> THURSDAY;
            case "금" -> FRIDAY;
            case "토" -> SATURDAY;
            case "일" -> SUNDAY;
            default -> null;
        };
    }

    private static final Pattern SCHEDULE_PATTERN = Pattern.compile(
            "(월|화|수|목|금|토|일)~?(월|화|수|목|금|토|일)?\\s(\\d{1,2}:\\d{2})\\s~\\s(\\d{1,2}:\\d{2})",
            Pattern.UNICODE_CHARACTER_CLASS
    );

    private static final Pattern DAILY_PATTERN = Pattern.compile(
            "매일\\s(\\d{1,2}:\\d{2})\\s~\\s(\\d{1,2}:\\d{2})",
            Pattern.UNICODE_CHARACTER_CLASS
    );

    public static Map<EDay, String> parseSchedule(String scheduleStr) {
        Map<EDay, String> scheduleMap = new EnumMap<>(EDay.class);
        for (EDay day : EDay.values()) {
            scheduleMap.put(day, null);
        }
        if (scheduleStr == null || scheduleStr.isBlank()) {
            return scheduleMap;
        }

        Matcher dailyMatcher = DAILY_PATTERN.matcher(scheduleStr);
        if (dailyMatcher.find()) {
            String startTime = dailyMatcher.group(1);
            String endTime = dailyMatcher.group(2);
            for (EDay day : EDay.values()) {
                scheduleMap.put(day, startTime + " ~ " + endTime);
            }
            return scheduleMap;
        }

        // 각 라인 별로 처리
        String[] lines = scheduleStr.split("\\n");
        for (String line : lines) {
            if (line.contains("라스트오더")) {
                continue;
            }
            if (line.contains(",")) {
                // 쉼표로 분리된 요일 처리
                String[] parts = line.split("\\s", 2);
                if (parts.length == 2) {
                    String[] days = parts[0].split(",");
                    String time = parts[1];
                    for (String day : days) {
                        EDay eDay = fromString(day.trim());
                        if (eDay != null) {
                            scheduleMap.put(eDay, time);
                        }
                    }
                }
            } else {
                // 기존 SCHEDULE_PATTERN 처리
                Matcher matcher = SCHEDULE_PATTERN.matcher(line);
                while (matcher.find()) {
                    EDay startDay = fromString(matcher.group(1));
                    EDay endDay = matcher.group(2) == null ? startDay : fromString(matcher.group(2));
                    String startTime = matcher.group(3);
                    String endTime = matcher.group(4);

                    if (startDay != null) {
                        if (startDay == endDay || endDay == null) {
                            scheduleMap.put(startDay, startTime + " ~ " + endTime);
                        } else {
                            boolean rangeStarted = false;
                            for (EDay day : EDay.values()) {
                                if (day == startDay || rangeStarted) {
                                    rangeStarted = true;
                                    scheduleMap.put(day, startTime + " ~ " + endTime);
                                }
                                if (day == endDay) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return scheduleMap;
    }

}
