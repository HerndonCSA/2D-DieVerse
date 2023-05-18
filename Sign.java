import java.util.Arrays;

public class Sign {
    String all_lines = "";
    int num_lines = 1;

    public Sign(String text, int max_width) {
        String[] chars = text.split("");
        int count = 0;

        for(int i=0; i < chars.length; i++) {
            if(count == max_width) {
                num_lines += 1;
                all_lines += ";";
                count = 0;
            } else {
                all_lines += ";";
                count ++;
            }
        }
    }

    public String getLines() {
        return  all_lines;
    }

    public int numberOfLines() {
        return num_lines;
    }
}
