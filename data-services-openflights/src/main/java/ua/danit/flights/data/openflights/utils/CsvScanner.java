package ua.danit.flights.data.openflights.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String scanner for CSV files.
 *
 * @author Andrey Minov
 */
class CsvScanner {

  private static Pattern DOUBLE_PATTERN = Pattern.compile("((\\-)?\\d+(\\.\\d+)?)|(\\\\N)");
  private static Pattern INTEGER_PATTERN = Pattern.compile("((\\-)?\\d+)|(\\\\N)");
  private static Pattern STRING_PATTERN = Pattern.compile("(\")|(\\\\N)");
  private static Pattern NULL_PATTERN = Pattern.compile("\\\\N");
  private static Pattern SEPARATOR_PATTERN = Pattern.compile(",");
  private String data;
  private int pos;

  CsvScanner(String data) {
    this.data = data;
    this.pos = 0;
  }

  long nextLong() {
    Matcher matcher = INTEGER_PATTERN.matcher(data);
    if (!matcher.find(pos)) {
      throw new IllegalArgumentException("Incorrect value provided!");
    }
    Matcher nullMatcher = NULL_PATTERN.matcher(data);
    if (nullMatcher.find(pos) && nullMatcher.start() == matcher.start()) {
      pos = matcher.end();
      return 0;
    }
    pos = matcher.end();
    return Long.parseLong(data.substring(matcher.start(), matcher.end()));
  }

  String nextString() {
    Matcher matcher = STRING_PATTERN.matcher(data);
    if (!matcher.find(pos)) {
      throw new IllegalArgumentException("Incorrect value provided!");
    }

    Matcher nullMatcher = NULL_PATTERN.matcher(data);
    if (nullMatcher.find(pos) && nullMatcher.start() == matcher.start()) {
      pos = matcher.end();
      return null;
    }
    StringBuilder out = new StringBuilder();
    int stringStart = matcher.end();
    int prev = matcher.start() + 1;
    pos = stringStart;
    while (matcher.find(pos) && data.charAt(matcher.start() - 1) == '\\') {
      out.append(data.substring(prev, matcher.start() - 1)).append("\"");
      prev = matcher.start() + 1;
      pos = matcher.end();
    }
    if (matcher.end() == stringStart) {
      throw new IllegalArgumentException("Incorrect value provided!");
    }
    out.append(data.substring(pos, matcher.start()));
    pos = matcher.end();
    return out.toString();
  }

  double nextDouble() {
    Matcher matcher = DOUBLE_PATTERN.matcher(data);
    if (!matcher.find(pos)) {
      throw new IllegalArgumentException("Incorrect value provided!");
    }
    Matcher nullMatcher = NULL_PATTERN.matcher(data);
    if (nullMatcher.find(pos) && nullMatcher.start() == matcher.start()) {
      pos = matcher.end();
      return 0;
    }
    pos = matcher.end();
    return Double.parseDouble(data.substring(matcher.start(), matcher.end()));
  }

  boolean hasNext() {
    return data.length() > pos;
  }

  void skip() {
    Matcher matcher = SEPARATOR_PATTERN.matcher(data);
    if (!matcher.find(pos)) {
      throw new IllegalArgumentException("Incorrect value provided!");
    }
    pos = matcher.end();
  }
}
