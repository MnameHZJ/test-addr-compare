package org.hzj.addr;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author huangzhuojie
 * @date 2024/4/17
 */
public class GeneratorDict {

    public static void main(String[] args) {
        File addrJson = new File(GeneratorDict.class.getClassLoader().getResource("addr.json").getPath());
        String json = FileUtil.readString(addrJson, StandardCharsets.UTF_8);

        Set<String> addrList = new HashSet<>();

        JSONObject jsonObject = new JSONObject(json);

        JSONArray provinces = jsonObject.getJSONArray("provinces");
        Iterator<Object> iterator = provinces.stream().iterator();
        while (iterator.hasNext()) {
            String provinceName = (String) iterator.next();
            addrList.add(provinceName);
            JSONObject provinceObj = (JSONObject) jsonObject.get(provinceName);
            addAlias(addrList, provinceObj);

            JSONArray citiesArr = provinceObj.getJSONArray("cities");
            Iterator<Object> citiesIterator = citiesArr.stream().iterator();
            while (citiesIterator.hasNext()) {
                String cityName = (String) citiesIterator.next();
                addrList.add(cityName);
                JSONObject cityObj = (JSONObject) provinceObj.get(cityName);
                addAlias(addrList, cityObj);

                JSONArray districtArr = cityObj.getJSONArray("districts");
                if(districtArr == null) {
                    continue;
                }
                Iterator<Object> districtIterator = districtArr.stream().iterator();
                while (districtIterator.hasNext()) {
                    String districtName = (String) districtIterator.next();
                    addrList.add(districtName);
                    JSONObject districtObj = (JSONObject) cityObj.get(districtName);
                    addAlias(addrList, districtObj);
                }
            }
        }

        StringBuilder builder = new StringBuilder();
        addrList.forEach((a) -> {
            builder.append(a).append("\t").append("3").append("\t").append("n").append("\n");
        });

        FileUtil.writeUtf8String(builder.toString(), new File("F:/jieba.dict"));

    }

    private static void addAlias(Set<String> addrList, JSONObject provinceObj) {
        JSONArray alias = provinceObj.getJSONArray("alias");
        Iterator<Object> aliasIterator = alias.stream().iterator();
        addAlias(addrList, aliasIterator);
    }

    private static void addAlias(Set<String> addrList, Iterator<Object> aliasIterator) {
        while (aliasIterator.hasNext()) {
            Object aliasStr = aliasIterator.next();
            addrList.add(aliasStr.toString());
        }
    }

}
