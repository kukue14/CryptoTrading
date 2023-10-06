package Formats;

public class FormatLite {
//    Underline
    public static void underline(char style, int length) {
    for (int i = 0; i < length; i++)
        System.out.print(style);
    System.out.println();
    }

//    Title
    public static void title(String title) {
        System.out.println("<----------" + title + "---------->");
    }

//    AlignedTitle
    public static void alignedTitle(String title, char style, int length) {
        int half = title.length()/2;
        if (length == 0) length = 30;
        System.out.print("<");
        for (int i = length-half; i>0; i--) {
            System.out.print(style);
        }
        System.out.print(title);
        for (int i = length-half; i>0; i--) {
            System.out.print(style);
        }
        System.out.println(">");
    }
}

