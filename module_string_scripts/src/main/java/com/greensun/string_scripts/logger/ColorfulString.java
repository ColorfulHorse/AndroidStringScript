package com.greensun.string_scripts.logger;

import java.util.HashMap;
import java.util.Map;

public class ColorfulString {

    private void test(){
        Map<String, String> map = new HashMap<>();
        map.put("黑色", "30");
        map.put("红色", "31");
        map.put("绿色", "32");
        map.put("黄色", "33");
        map.put("蓝色", "34");
        map.put("紫红色", "35");
        map.put("青蓝色", "36");
        map.put("白色", "37");

        for (String key : map.keySet()) {
            System.out.println("\033[1;" + map.get(key) + "m" + map.get(key) +"="+ key + "\033[0m");
        }

    }

    public String renderYellow(String str){
        return "\033[1;" + "33" + "m" + str + "\033[0m";
    }

    public String renderRed(String str){
        return "\033[1;" + "31" + "m" + str + "\033[0m";
    }

    public String renderUltramarine(String str){
        return "\033[1;" + "36" + "m" + str + "\033[0m";
    }

}
