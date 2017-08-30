package main;

import SampleInputs.SampleJsonStrings;
import formatter.Formatter;
import formatter.JsonFormatter;

/**
 * Created by abhishek on 8/29/17.
 */
public class Main {

    public static void main(String[] args) {


        Formatter formatter = new JsonFormatter();
        System.out.println(formatter.format(SampleJsonStrings.SAMPLE5));
    }
}
