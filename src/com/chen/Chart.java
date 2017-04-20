package com.chen;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * Created by chen on 2017/4/18.
 */
public class Chart {
    final static String dicPath = "dic.txt";
    final static String rulePath = "rule.txt";
    static List<String> word = new ArrayList<String>();
    static List<String> element = new ArrayList<String>();//词性序列
    static List<Rule> rules = new ArrayList<Rule>(); //存放规则集
    static List<Arc> chart = new ArrayList<Arc>();//活动边
    static List<Arc> activearcs = new ArrayList<Arc>();//非活动边
    static Stack<Arc> agenda = new Stack<Arc>();//待处理边
    /*

     */
    public static void SearchDic() {
        HashMap<String,String> dic = new HashMap<String,String>();
        FileReader reader = null ;
        BufferedReader bufferedReader = null;
        try {
            reader = new FileReader(dicPath);
            bufferedReader = new BufferedReader(reader);
            String tmp;
            while ((tmp = bufferedReader.readLine())!=null){
//                System.out.println(tmp);
                String[] d = tmp.split(" ");
                dic.put(d[0],d[1]);
            }
            for(String w : word){
                element.add(dic.get(w.toLowerCase()));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                bufferedReader.close();
                reader.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /*

     */
    public static void Load(){
        FileReader reader = null;
        BufferedReader bufferedReader = null;
        try{
            reader = new FileReader(rulePath);
            bufferedReader = new BufferedReader(reader);
            String tmp;
            while ((tmp = bufferedReader.readLine())!=null){
                String[] r = tmp.split(":");
                Rule rule = new Rule(r[0],r[1]);
                rules.add(rule);
            }
       }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                bufferedReader.close();
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /*

     */
    public static String Parser() {
        int size = element.size();
        int i = 0;
        while(! agenda.isEmpty()|| i <size){
            if(agenda.isEmpty()){
                //agenda为空，把句子中的下一个成分加进来
                Arc arc = new Arc();
                arc.head = element.get(i);
                arc.done = element.get(i);
                arc.rest = "";
                arc.start = i + 1;
                arc.end = i + 2;
                agenda.push(arc);
                i ++;
            }

            Arc arc = agenda.pop();
            for(Rule rule :rules){
               if(rule.latter.equals(arc.head)||rule.latter == arc.head){
                    Arc tmpArc = new Arc();
                    tmpArc.head = rule.format;
                    tmpArc.done = rule.latter;
                    tmpArc.rest = "";
                    tmpArc.start = arc.start;
                    tmpArc.end = arc.end;
                    agenda.push(tmpArc);
               }else if(rule.latter.contains(arc.head)){
                   Arc tmpArc = new Arc();
                   int index = rule.latter.indexOf(" ");
                   tmpArc.head = rule.format;
                   tmpArc.done= rule.latter.substring(0,index);
                   tmpArc.rest = rule.latter.substring(index +1 );
                   tmpArc.start = arc.start;
                   tmpArc.end = arc.end;
                   activearcs.add(tmpArc);
               }
            }

            chart.add(arc);

            for(Arc arcTmp : activearcs){
                if(arcTmp.end == arc.start &&  arcTmp.rest.equals(arc.head)){
                    Arc tmpArc = new Arc();
                    tmpArc.head = arcTmp.head;
                    tmpArc.done = arcTmp.done + arc.head;
                    tmpArc.rest = "";
                    tmpArc.start = arcTmp.start;
                    tmpArc.end = arc.end;
                    agenda.push(tmpArc);
                }else if(arcTmp.end == arc.start &&  arcTmp.rest.contains(arc.head+ " ")){
                    Arc tmpArc = new Arc();
                    tmpArc.head = arcTmp.head;
                    System.out.println(arcTmp.done);
                    int index = arcTmp.done.indexOf(" ");
                    tmpArc.done=arcTmp.done.substring(0,index);
                    tmpArc.rest = arcTmp.done.substring(index +1 );
                    tmpArc.start = arcTmp.start;
                    tmpArc.end = arc.end;
                    activearcs.add(tmpArc);
                }
            }

        }
        String head = "";
        String done = "";
        String result = "";
        for(int j=chart.size()-1;j>0;j--){
            Arc tmpArc = chart.get(j);
            if(j == chart.size()-1){
                head = tmpArc.head;
                done = tmpArc.done;
            }
            if(tmpArc.head.equals(tmpArc.done)||(tmpArc.head.equals(head) && tmpArc.done.equals(done)&& j != chart.size()-1))
                continue;
            result = result+ ";"+tmpArc.head+"->"+tmpArc.done+" "+tmpArc.rest+" ";
        }

    return result.substring(1);

    }
    /*
        main
    */
    public static void main(String[] args){
        String title = "The man buy a car";
        //处理输入句子
        String[] strigs = title.split(" ");
        for(String s : strigs){
            word.add(s);
        }
        //字典集查找
        SearchDic();
        //加载规则集
        Load();
        //chart算法
        String result = Parser();
        System.out.println(word);
        System.out.println(element);
        System.out.println(result);

    }


}
class Arc{
    String head;
    String done;
    String rest;
    int start;
    int end;

    @Override
    public String toString() {
        return "Arc{" +
                "head='" + head + '\'' +
                ", done='" + done + '\'' +
                ", rest='" + rest + '\'' +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
class Rule{
    public String format;
    public String latter;
    public Rule(String format,String latter){
        this.format = format;
        this.latter = latter;
    }
}