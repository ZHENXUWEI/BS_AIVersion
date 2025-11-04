package com.ruoyi.web.controller.api.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum RegionEnum {
    HANGZHOU("杭州市", Arrays.asList(
        "上城区", "下城区", "江干区", "拱墅区", "西湖区", "滨江区", 
        "萧山区", "余杭区", "富阳区", "临安区", "桐庐县", "淳安县", "建德市"
    )),
    NINGBO("宁波市", Arrays.asList(
        "海曙区", "江北区", "北仑区", "镇海区", "鄞州区", "奉化区", 
        "象山县", "宁海县", "余姚市", "慈溪市"
    )),
    WENZHOU("温州市", Arrays.asList(
        "鹿城区", "龙湾区", "瓯海区", "洞头区", "永嘉县", "平阳县", 
        "苍南县", "文成县", "泰顺县", "瑞安市", "乐清市"
    )),
    JIAXING("嘉兴市", Arrays.asList(
        "南湖区", "秀洲区", "嘉善县", "海盐县", "海宁市", "平湖市", "桐乡市"
    )),
    HUZHOU("湖州市", Arrays.asList(
        "吴兴区", "南浔区", "德清县", "长兴县", "安吉县"
    )),
    SHAOXING("绍兴市", Arrays.asList(
        "越城区", "柯桥区", "上虞区", "新昌县", "诸暨市", "嵊州市"
    )),
    JINHUA("金华市", Arrays.asList(
        "婺城区", "金东区", "武义县", "浦江县", "磐安县", 
        "兰溪市", "义乌市", "东阳市", "永康市"
    )),
    QUZHOU("衢州市", Arrays.asList(
        "柯城区", "衢江区", "常山县", "开化县", "龙游县", "江山市"
    )),
    ZHOUSHAN("舟山市", Arrays.asList(
        "定海区", "普陀区", "岱山县", "嵊泗县"
    )),
    TAIZHOU("台州市", Arrays.asList(
        "椒江区", "黄岩区", "路桥区", "三门县", "天台县", 
        "仙居县", "温岭市", "临海市", "玉环市"
    )),
    LISHUI("丽水市", Arrays.asList(
        "莲都区", "青田县", "缙云县", "遂昌县", "松阳县", 
        "云和县", "庆元县", "景宁畲族自治县", "龙泉市"
    ));

    private final String cityName;
    private final List<String> counties;

    RegionEnum(String cityName, List<String> counties) {
        this.cityName = cityName;
        this.counties = counties;
    }

    public String getCityName() {
        return cityName;
    }

    public List<String> getCounties() {
        return counties;
    }

    // 根据城市名称获取区县列表
    public static List<String> getCountiesByCity(String cityName) {
        for (RegionEnum region : values()) {
            if (region.getCityName().equals(cityName)) {
//                return region.getCounties();
                List<String> countiesWithCity = new ArrayList<>();
                countiesWithCity.add(cityName); // 添加城市名
                countiesWithCity.addAll(region.getCounties()); // 添加区县列表
                return countiesWithCity;
            }
        }
        return null;
    }

    // 获取所有城市名称
    public static List<String> getAllCities() {
        return Arrays.stream(values())
                .map(RegionEnum::getCityName)
                .collect(Collectors.toList());
    }

    // 获取浙江省所有区县
    public static List<String> getAllCounties() {
        return Arrays.stream(values())
                .flatMap(region -> region.getCounties().stream())
                .collect(Collectors.toList());
    }
}
