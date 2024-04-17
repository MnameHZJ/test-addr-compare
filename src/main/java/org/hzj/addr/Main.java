package org.hzj.addr;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.WordDictionary;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author huangzhuojie
 * @date 2024/4/17
 */
public class Main {

    public static void main(String[] args) {
        splitWord("广东省广州番禺区四海城3栋", "广东广州市番禺四海城3栋");
        splitWord("宁夏回族自治区银川市兴庆3栋", "宁夏银川市兴庆区3栋");
    }

    private static void splitWord(String str1, String str2) {
        Path path = Paths.get(new File(Main.class.getClassLoader().getResource("dicts/jieba.dict").getPath()).getAbsolutePath());

        //加载自定义的词典进词库
        WordDictionary.getInstance().loadUserDict(path) ;
        //重新分词
        JiebaSegmenter segmenter = new JiebaSegmenter();
        List<String> words = segmenter.sentenceProcess(str1);
        List<String> words2 = segmenter.sentenceProcess(str2);
        if (words.size() != words2.size()) {
            System.out.println(false);
            return;
        }

        int size = words.size();

        boolean result = true;
        for (int i = 0; i < size; i++) {
            result = words2.get(i).contains(words.get(i)) || words.get(i).contains(words2.get(i));
            if (!result) {
                break;
            }
        }

        System.out.println(result);
    }
}
