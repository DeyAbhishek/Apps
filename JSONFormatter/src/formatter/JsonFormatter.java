package formatter;

/**
 * Created by abhishek on 8/29/17.
 */
public class JsonFormatter implements Formatter {

    @Override
    public String format(String input) {
        input = input.replaceAll("\\s", "");
        return format(input, 1);
    }

    private String format(String input, int count) {
        StringBuilder sb = new StringBuilder();
        sb.append("" + input.charAt(0) );

        for (int i = 1; i < input.length() - 1;) {

            sb.append('\n');
            int colonIndex = i + input.substring(i).indexOf(':');
            insertTab(sb, count);
            sb.append(input.substring(i, colonIndex + 1));

            int commaIndex = 
                input.substring(colonIndex + 1).contains(",") 
                ? input.substring(colonIndex).indexOf(',') 
                : Integer.MAX_VALUE;

            if (input.charAt(colonIndex + 1) == '[') {
                int closingBracketIndex = colonIndex 
                    + 1 + getClosingBracketIndex(input.substring(colonIndex + 1));
                
                sb.append(processJsonArray(input.substring(colonIndex + 1, closingBracketIndex + 1), count + 1));
                i = closingBracketIndex + 1;
                if (input.charAt(closingBracketIndex + 1) == ',') {
                    sb.append(',');
                    i++;
                }

            } else if (input.charAt(colonIndex + 1) == '{') {
                int endIndex = colonIndex + getEndIndex(input.substring(colonIndex + 1)) + 1;
                sb.append(format(input.substring(colonIndex + 1, endIndex + 1), count + 1));
                i = endIndex + 1;

            } else if (commaIndex == Integer.MAX_VALUE) {  // no other commas, print the rest of the part
                sb.append(input.substring(colonIndex + 1, input.length() - 1));
                break;

            } else {
                // the line ends with a comma
                // print the rest of the line and update i , i.e, go to next line
                sb.append(input.substring(colonIndex + 1, colonIndex + commaIndex + 1));
                i = colonIndex +  commaIndex + 1;
            }

        } // end of for loop

        sb.append('\n');
        insertTab(sb, count - 1);
        sb.append(input.charAt(input.length() - 1));
        return sb.toString();
    }

    private void insertTab(StringBuilder sb, int count) {
        for (int i = 0; i < count; i++) {
            sb.append("\t");
        }
    }

    private int getClosingBracketIndex(String input) {
        int i = 0;
        while (i < input.length() && input.charAt(i) != ']') {
            i++;
        }
        return i;
    }

    private String processJsonArray(String input, int count) {
        if (input.charAt(0) == '{') {
            return format(input, count);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        String[] arr = input.substring(1, input.length() - 1).split(",");
        for (String str : arr) {
            sb.append('\n');
            insertTab(sb, count);
            sb.append(str + ',');
        }
        sb.setLength(sb.length() - 1); // omitting the last comma
        sb.append('\n');
        insertTab(sb, count - 1);
        sb.append(']');
        return sb.toString();
    }

    private int getEndIndex(String input) {
        int i = 0;
        int count = 0;
        while (i < input.length()) {
            if (input.charAt(i) == '{') {
                count++;
            } else if (input.charAt(i) == '}') {
                count--;
            }
            if (count == 0) {
                return i;
            }
            i++;
        }
        return Integer.MAX_VALUE;
    }

}
