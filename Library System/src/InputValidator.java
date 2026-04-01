import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidator {
    static public boolean validateBookName(String name){
        // this regex make sure that the movie name starts and ends with a latter or a number
        // can't start or end with an empty space, even tho we already used .trim()... the more the merrier
        // example of start with number: 12 years a slave
        // this regex also only work for English movie names
        // maximum number of char is 32 and minimum is 2 (first and last)
        String regex = "^[A-Za-z0-9][A-Za-z0-9\\s:,'\\-!?.()]{0,30}[A-Za-z0-9)]$";
        // Compile the regex into a pattern
        Pattern pattern = Pattern.compile(regex);
        // Match against the pattern
        Matcher matcher = pattern.matcher(name);
        // validation, return the match result
        return matcher.matches();
    }

    static public boolean validateAuthorName(String name){
        // this regex make sure that the Director name starts and ends with a latter
        // example of a name with ' is my boy Conan O'Brien
        // this regex also only work for English names
        // maximum number of char is 32 and minimum is 2 (first and last)
        String regex = "^[A-Za-z][A-Za-z '-.]{0,30}[A-Za-z]$";
        // Compile the regex into a pattern
        Pattern pattern = Pattern.compile(regex);
        // Match against the pattern
        Matcher matcher = pattern.matcher(name);
        // validation, return the match result
        return matcher.matches();
    }

    static public boolean validateMemberName(String name){
        // this regex make sure that the Director name starts and ends with a latter
        // example of a name with ' is my boy Conan O'Brien
        // this regex also only work for English names
        // maximum number of char is 32 and minimum is 2 (first and last)
        String regex = "^[A-Za-z][A-Za-z '-.]{0,30}[A-Za-z]$";
        // Compile the regex into a pattern
        Pattern pattern = Pattern.compile(regex);
        // Match against the pattern
        Matcher matcher = pattern.matcher(name);
        // validation, return the match result
        return matcher.matches();
    }


    static public boolean validateBookDescription(String name){
        // this regex starts with a latter and ends with a latter,number or ".".
        // this regex also only work for English names
        // maximum number of char is 256 and minimum is 2 (first and last)
        String regex = "^.{1,256}$";
        // Compile the regex into a pattern
        Pattern pattern = Pattern.compile(regex);
        // Match against the pattern
        Matcher matcher = pattern.matcher(name);
        // validation, return the match result
        return matcher.matches();
    }


}
