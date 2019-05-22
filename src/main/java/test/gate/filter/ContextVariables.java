package test.gate.filter;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.HashSet;
import java.util.Set;

public class ContextVariables {
    private static Set<String> ignoreUrls = new HashSet<>();

    public static void removeIgnoreUrl(String url){
        ignoreUrls.remove(url);
    }
    public static void addIgnoreUrl(String url){
        ignoreUrls.add(url);
    }

    public static boolean containsIgnoreUrl(String url) {
        PathMatcher matcher = new AntPathMatcher();
        for(String pattern:ignoreUrls){
            if(matcher.match(pattern,url)){
                return true;
            }
        }
        return false;
    }
}
