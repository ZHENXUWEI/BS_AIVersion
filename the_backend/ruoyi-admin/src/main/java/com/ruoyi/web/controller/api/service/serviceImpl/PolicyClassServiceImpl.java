package com.ruoyi.web.controller.api.service.serviceImpl;

import java.text.Collator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import com.ruoyi.web.controller.policy.service.IPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.web.controller.api.domain.PolicyClass;
import com.ruoyi.web.controller.api.domain.PolicyClassAPI;
import com.ruoyi.web.controller.api.domain.RegionEnum;
import com.ruoyi.web.controller.api.domain.dto.PolicyAPIDto;
import com.ruoyi.web.controller.api.domain.dto.PolicyDtoResult;
import com.ruoyi.web.controller.api.mapper.PolicyClassMapper;
import com.ruoyi.web.controller.api.service.PolicyClassService;
import com.ruoyi.web.controller.policy.domain.PolicyCategory;
import com.ruoyi.web.controller.policy.domain.PolicyCategoryData;
import com.ruoyi.web.controller.policy.domain.PolicyPropertiesData;
import com.ruoyi.web.controller.policy.service.IPolicyCategoryDataService;
import com.ruoyi.web.controller.policy.service.IPolicyCategoryService;
import com.ruoyi.web.controller.policy.service.IPolicyPropertiesDataService;

@Service
public class PolicyClassServiceImpl implements PolicyClassService {

    @Autowired
    private PolicyClassMapper policyClassMapper;
    @Autowired
    private IPolicyCategoryService policyCategoryService;
    @Autowired
    private IPolicyCategoryDataService policyCategoryDateService;
    @Autowired
    private IPolicyPropertiesDataService policyPropertiesDataService;

    @Autowired
    private IPolicyService policyService;

    private static boolean isCity(PolicyDtoResult policyDtoResult) {
        return !policyDtoResult.getChildren().isEmpty() && policyDtoResult.getSelectedDistrict().equals("");
    }

    @Override
    public List<PolicyClass> getAll() {

        return policyClassMapper.getAll();
    }

    @Override
    public List<PolicyAPIDto> findPolicyClassById(Integer id) {
        List<PolicyClassAPI> policyClassAPIS = policyClassMapper.findPolicyClassById(id);
        if (policyClassAPIS.isEmpty()) {
            return new ArrayList<>();
        }
//        System.out.println(policyClassAPIS);
        List<PolicyAPIDto> policyAPIDtos = new ArrayList<>();
        PolicyAPIDto policyAPIDto = new PolicyAPIDto();
        int oldIndex = policyClassAPIS.get(0).getId();
        BeanUtils.copyProperties(policyClassAPIS.get(0), policyAPIDto);
        for (int i = 0; i < policyClassAPIS.size(); i++) {
            int index = policyClassAPIS.get(i).getId();
            if (index != oldIndex) {
//                System.out.println(oldIndex);
                policyAPIDto.setPolicyName(policyAPIDto.getName());
                policyAPIDto.setPolicyConditions(policyAPIDto.getPolicyCondition());
                policyAPIDto.setPolicyContent(policyAPIDto.getContent());
                policyAPIDto.setCreatedAt(policyAPIDto.getCreatedAt());
                if (policyAPIDto.getDeadTime() == null) {
                    policyAPIDto.setDeadTime("2099-12-30");
                }
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");// 根据你的日期格式创建DateTimeFormatter对象
                LocalDate date = LocalDate.parse(policyAPIDto.getDeadTime(), formatter);
                Date dateString = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
                policyAPIDto.setExpireDate(dateString);
                policyAPIDtos.add(policyAPIDto);
                policyAPIDto = new PolicyAPIDto();
                BeanUtils.copyProperties(policyClassAPIS.get(i), policyAPIDto);
                oldIndex = index;
            }
            if (policyClassAPIS.get(i).getCategoryName() == null) {
                continue;
            }
            switch (policyClassAPIS.get(i).getCategoryName()) {
                case "leaderDepartment":
                    policyAPIDto.setLeaderDepartment(policyClassAPIS.get(i).getCategoryContent());
                    break;
                case "applicableArea":
                    policyAPIDto.setApplicableArea(policyClassAPIS.get(i).getCategoryContent());
                    break;
                case "policyClass":
                    policyAPIDto.setPolicyClass(policyClassAPIS.get(i).getCategoryContent());
                    break;
                case "policyType":
                    policyAPIDto.setPolicyType(policyClassAPIS.get(i).getCategoryContent());
                    break;
                case "declarationStatus":
                    policyAPIDto.setDeclarationStatus(policyClassAPIS.get(i).getCategoryContent());
                    break;
                case "releaseTime":
                    policyAPIDto.setReleaseTime(policyClassAPIS.get(i).getCategoryContent());
                    break;
            }
        }
        policyAPIDto.setPolicyName(policyAPIDto.getName() != null ? policyAPIDto.getName() : null);
        policyAPIDto.setPolicyConditions(policyAPIDto.getFormula() != null ? policyAPIDto.getFormula() : null);
        policyAPIDto.setPolicyFileUrl(policyAPIDto.getFileUrl() != null ? policyAPIDto.getFileUrl() : null);
        policyAPIDto.setPolicyContent(policyAPIDto.getContent() != null ? policyAPIDto.getContent() : null);
        if (policyAPIDto.getShengyuTime().startsWith("已过期了")) {
            policyAPIDtos.sort(Comparator.comparing(PolicyAPIDto::getPublishDate).reversed());
            return policyAPIDtos;
        }
        policyAPIDtos.add(policyAPIDto);
        policyAPIDtos.sort(Comparator.comparing(PolicyAPIDto::getPublishDate).reversed());
        return policyAPIDtos;
    }

    @Override
    public List<PolicyAPIDto> selectPolicyByClass(List<PolicyAPIDto> policyAPIDtoList, List<PolicyDtoResult> policyDtoResultList) {
        outLoop:
        for (PolicyDtoResult policyDtoResult : policyDtoResultList) {
            if (policyDtoResult.getName().equals("适用区域")) {
                policyAPIDtoList = selectPolicyByArea(policyAPIDtoList, policyDtoResult);
                continue;
            }
            Set<Integer> idSet = new HashSet<>();
            for (String subPolicyType : policyDtoResult.getChildren()) {
                if (subPolicyType.equals("全部")) {
                    for (PolicyAPIDto policyAPIDto : policyAPIDtoList) {
                        idSet.add(policyAPIDto.getId());
                    }
                    break;
                } else if (subPolicyType.equals("其他")) {
                    List<PolicyCategory> policyCategories = policyCategoryService.selectPolicyCategoryListByName(new PolicyCategory(null, policyDtoResult.getName(), null));
                    List<PolicyCategoryData> policyCategoryDataList = policyCategoryDateService.selectPolicyCategoryDataList(new PolicyCategoryData(null, policyCategories.get(0).getId(), null, null));
                    Set<Integer> idSetRemove = new HashSet<>();
                    for (PolicyCategoryData policyCategoryData : policyCategoryDataList) {
                        if (policyCategoryData.getContent().equals("全部")) {
                            continue;
                        }
                        List<PolicyPropertiesData> policyPropertiesDataList = policyPropertiesDataService.selectPolicyPropertiesDataByCategoryDataId(policyCategoryData.getId());
                        if (policyCategoryData.getContent().equals("其他")) {
                            for (PolicyPropertiesData policyPropertiesData : policyPropertiesDataList) {
                                idSet.add(policyPropertiesData.getPolicyId());
                            }
                        } else {
                            for (PolicyPropertiesData policyPropertiesData : policyPropertiesDataList) {
                                idSetRemove.add(policyPropertiesData.getPolicyId());
                            }
                        }
                    }
                    for (PolicyAPIDto policyAPIDto : policyAPIDtoList) {
                        if (!idSetRemove.contains(policyAPIDto.getId())) {
                            idSet.add(policyAPIDto.getId());
                        }
                    }
                } else {
                    List<PolicyCategoryData> policyCategoryDataList1 = policyCategoryDateService.selectPolicyCategoryDataList(new PolicyCategoryData(null, null, subPolicyType, null));
                    if (!policyCategoryDataList1.isEmpty()) {
                        List<PolicyPropertiesData> policyPropertiesDataList = policyPropertiesDataService.selectPolicyPropertiesDataByCategoryDataId(policyCategoryDataList1.get(0).getId());
                        for (PolicyPropertiesData policyPropertiesData : policyPropertiesDataList) {
                            idSet.add(policyPropertiesData.getPolicyId());
                        }
                    }
                }
            }
            List<PolicyAPIDto> LoopPolicyApiDtoList = new ArrayList<>();
            for (PolicyAPIDto policyAPIDto : policyAPIDtoList) {
                if (idSet.contains(policyAPIDto.getId())) {
                    LoopPolicyApiDtoList.add(policyAPIDto);
                }
            }
            policyAPIDtoList.clear();
            policyAPIDtoList.addAll(LoopPolicyApiDtoList);
        }
        return policyAPIDtoList;
    }

    private List<PolicyAPIDto> selectPolicyByArea(List<PolicyAPIDto> policyAPIDtoList, PolicyDtoResult policyDtoResult) {
        List<PolicyAPIDto> policyAPIDtoList1 = new ArrayList<>();
        if (policyDtoResult.getChildren().isEmpty() || policyDtoResult.getChildren().size() == 0) {
            return policyAPIDtoList;
        }
        for (String area : policyDtoResult.getChildren()) {
            if (area.equals("全部")) {
                return policyAPIDtoList;
            }
            if (area.equals("国家级") || area.equals("浙江省")) {
                selectPolicyByAreaSpecial(policyAPIDtoList, policyAPIDtoList1, area);
                continue;
            }
            if (area.equals("其他")) {
                selectPolicyByAreaOther(policyAPIDtoList, policyAPIDtoList1);
                continue;
            }
            //处理一级的逻辑
            selectPolicyByCity(policyAPIDtoList, policyAPIDtoList1, area, policyDtoResult);
        }
        return policyAPIDtoList1;
    }

    private void selectPolicyByCity(List<PolicyAPIDto> policyAPIDtoList, List<PolicyAPIDto> policyAPIDtoList1, String area, PolicyDtoResult policyDtoResult) {
        //处理获取全部市
        if (policyDtoResult.getChildren().isEmpty() || policyDtoResult.getSelectedDistrict() == null || policyDtoResult.getSelectedDistrict().equals("") || policyDtoResult.getSelectedDistrict().equals("全部")) {
            List<String> counties = RegionEnum.getCountiesByCity(area);
            for (String county : counties) {
                selectPolicyByAreaSpecial(policyAPIDtoList, policyAPIDtoList1, county);
            }
        } else {
            selectPolicyByAreaSpecial(policyAPIDtoList, policyAPIDtoList1, policyDtoResult.getSelectedDistrict());
        }
    }

    private void selectPolicyByAreaOther(List<PolicyAPIDto> policyAPIDtoList, List<PolicyAPIDto> policyAPIDtoList1) {
        List<PolicyCategory> policyCategories = policyCategoryService.selectPolicyCategoryListByName(new PolicyCategory(null, "适用区域", null));
        if (!policyCategories.isEmpty()) {
            Integer policyCategoryId = policyCategories.get(0).getId();
            List<PolicyCategoryData> policyCategoryDataList = policyCategoryDateService.selectPolicyCategoryDataListByCategoryIdOther(policyCategoryId);
            getAllAblePolicy(policyAPIDtoList, policyAPIDtoList1, policyCategoryDataList);
        }
    }

    //获取全部满足的policyId并且将结果加入policyAPIDtoList1
    private void getAllAblePolicy(List<PolicyAPIDto> policyAPIDtoList, List<PolicyAPIDto> policyAPIDtoList1, List<PolicyCategoryData> policyCategoryDataList) {
        List<Integer> policyIds = new ArrayList<>();
        if (!policyCategoryDataList.isEmpty()) {
            for (PolicyCategoryData policyCategoryData : policyCategoryDataList) {
                List<PolicyPropertiesData> policyPropertiesDataList = policyPropertiesDataService.selectPolicyPropertiesDataByCategoryDataId(policyCategoryData.getId());
                // 将新的policyId添加到现有列表中
                policyIds.addAll(policyPropertiesDataList.stream()
                        .map(PolicyPropertiesData::getPolicyId)
                        .collect(Collectors.toList()));
            }
            // 最后统一去重
            policyIds = policyIds.stream()
                    .distinct()
                    .collect(Collectors.toList());

            for (PolicyAPIDto policyAPIDto : policyAPIDtoList) {
                if (policyIds.contains(policyAPIDto.getId())) {
                    policyAPIDtoList1.add(policyAPIDto);
                }
            }
        }
    }

    //获取指定area的满足的policyId
    private void selectPolicyByAreaSpecial(List<PolicyAPIDto> policyAPIDtoList, List<PolicyAPIDto> policyAPIDtoList1, String area) {
        System.out.println("传进来的area为："+area);
        List<PolicyCategory> policyCategories = policyCategoryService.selectPolicyCategoryListByName(new PolicyCategory(null, "适用区域", null));
        List<PolicyCategoryData> policyCategoryDataList = new ArrayList<>();
        if (!policyCategories.isEmpty()) {
            policyCategoryDataList = policyCategoryDateService.selectPolicyCategoryDataListInClassInfo(new PolicyCategoryData(null, policyCategories.get(0).getId(), area, null));
        }
        getAllAblePolicy(policyAPIDtoList, policyAPIDtoList1, policyCategoryDataList);
    }

    @Override
    public List<PolicyAPIDto> selectPolicyByTime(List<PolicyAPIDto> policyAPIDtoList, List<Date> timePeriod) {
        List<PolicyAPIDto> policyAPIDtos = policyAPIDtoList;
        List<PolicyAPIDto> apiDtoList = new ArrayList<>();
        for (PolicyAPIDto policyAPIDto : policyAPIDtos) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // 根据实际格式调整
            Date publishDateObj = policyAPIDto.getPublishDate();
            for (int i = 0; i < timePeriod.size() - 1; i++) {
                if (!publishDateObj.before(timePeriod.get(i)) && !publishDateObj.after(timePeriod.get(i + 1))) {
                    apiDtoList.add(policyAPIDto);
                }
            }
        }
        return apiDtoList;
    }

    @Override
    public List<PolicyAPIDto> selectPolicyById(Integer id) {
        List<PolicyAPIDto> policyAPIDtos = findPolicyClassById(1);
        List<PolicyAPIDto> apiDtoList = new ArrayList<>();
        for (PolicyAPIDto policyAPIDto : policyAPIDtos) {
            if (policyAPIDto.getId().equals(id)) {
                apiDtoList.add(policyAPIDto);
            }
        }
        return apiDtoList;
    }

    @Override
    public List<PolicyAPIDto> selectPolicyByName(List<PolicyAPIDto> policyAPIDtoList, String policyName) {
        List<PolicyAPIDto> policyAPIDtos = policyAPIDtoList;
        List<PolicyAPIDto> apiDtoList = new ArrayList<>();
        for (PolicyAPIDto policyAPIDto : policyAPIDtos) {
            if (policyAPIDto.getName() != null && policyAPIDto.getName().contains(policyName)) {
                apiDtoList.add(policyAPIDto);
            }
        }
        return apiDtoList;
    }

    @Override
    public List<PolicyAPIDto> sortPolicyByQuery(List<PolicyAPIDto> policyAPIDtoList, String query) {
        List<PolicyAPIDto> policyAPIDtos = policyAPIDtoList;
        List<PolicyAPIDto> apiDtoList = new ArrayList<>();
        PolicyAPIDto policyAPIDto;
        if (query.equals("热度推荐")) {
            for (int i = 0; i < policyAPIDtos.size(); i++) {
                Integer max = i;
                Integer MaxRecLevelObj = policyAPIDtos.get(i).getRecLevel();
                for (int j = i + 1; j < policyAPIDtos.size(); j++) {
                    Integer nowRecLevelObj = policyAPIDtos.get(j).getRecLevel();
                    if (nowRecLevelObj > MaxRecLevelObj) {
                        max = j;
                        MaxRecLevelObj = nowRecLevelObj;
                    }
                }
                if (max != i) {
                    policyAPIDto = policyAPIDtos.get(i);
                    policyAPIDtos.set(i, policyAPIDtos.get(max));
                    policyAPIDtos.set(max, policyAPIDto);
                    apiDtoList.add(policyAPIDtos.get(i));
                } else {
                    apiDtoList.add(policyAPIDtos.get(max));
                }
            }
        } else if (query.equals("最新政策")) {
            for (int i = 0; i < policyAPIDtos.size(); i++) {
                Integer max = i;
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // 根据实际格式调整
                Date MaxPublishDateObj = policyAPIDtos.get(i).getPublishDate();
                for (int j = i + 1; j < policyAPIDtos.size(); j++) {
                    Date nowPublishDateObj = policyAPIDtos.get(j).getPublishDate();
                    if (nowPublishDateObj.after(MaxPublishDateObj)) {
                        max = j;
                        MaxPublishDateObj = nowPublishDateObj;
                    }
                }
                if (max != i) {
                    policyAPIDto = policyAPIDtos.get(i);
                    policyAPIDtos.set(i, policyAPIDtos.get(max));
                    policyAPIDtos.set(max, policyAPIDto);
                    apiDtoList.add(policyAPIDtos.get(i));
                }
            }
            if (apiDtoList.isEmpty()) {
                return policyAPIDtos;
            }
        } else if (query.equals("浏览量")) {
            for (int i = 0; i < policyAPIDtos.size(); i++) {
                Integer max = i;
                Integer MaxHotLevelObj = policyAPIDtos.get(i).getHotLevel();
                for (int j = i + 1; j < policyAPIDtos.size(); j++) {
                    Integer nowHotLevelObj = policyAPIDtos.get(j).getHotLevel();
                    if (nowHotLevelObj > MaxHotLevelObj) {
                        max = j;
                        MaxHotLevelObj = nowHotLevelObj;
                    }
                }
                if (max != i) {
                    policyAPIDto = policyAPIDtos.get(i);
                    policyAPIDtos.set(i, policyAPIDtos.get(max));
                    policyAPIDtos.set(max, policyAPIDto);
                    apiDtoList.add(policyAPIDtos.get(i));
                } else {
                    apiDtoList.add(policyAPIDtos.get(max));
                }
            }
        } else if (query.equals("可申报政策")) {
            for (PolicyAPIDto policyAPIDto1 : policyAPIDtos) {
                if (policyAPIDto1.getShengyuTime().startsWith("已过期了") || !"申报类".equals(policyAPIDto1.getPolicyType())) {
                    continue;
                }
                apiDtoList.add(policyAPIDto1);
            }
        }else if(query.equals("昨日新增")){
            List<PolicyAPIDto> policyAPIDtos1 = policyService.filterPolicyAPIDtosByYesterdayDate(policyAPIDtoList);
            System.out.println("昨日新增------------------------"+policyAPIDtos1);
            apiDtoList.addAll(policyAPIDtos1);
            System.out.println("apiDtoList---------------------------------"+apiDtoList);
        }

        return apiDtoList;
    }

    @Override
    public List<PolicyAPIDto> selectPolicyByIsNew(Integer isNew) throws ParseException {
        List<PolicyAPIDto> policyAPIDtos = findPolicyClassById(1);
//        System.out.println(policyAPIDtos);
        List<PolicyAPIDto> apiDtoList = new ArrayList<>();
        PolicyAPIDto policyAPIDto = new PolicyAPIDto();
        if (isNew == 1) {
            for (int i = 0; i < 5; i++) {
                Integer max = i;
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // 根据实际格式调整
                Date MaxPublishDateObj = policyAPIDtos.get(i).getPublishDate();
                for (int j = i + 1; j < policyAPIDtos.size(); j++) {
                    Date nowPublishDateObj = policyAPIDtos.get(j).getPublishDate();
                    if (nowPublishDateObj.after(MaxPublishDateObj)) {
                        max = j;
                        MaxPublishDateObj = nowPublishDateObj;
                    }
                }
                if (max != i) {
                    policyAPIDto = policyAPIDtos.get(i);
                    policyAPIDtos.set(i, policyAPIDtos.get(max));
                    policyAPIDtos.set(max, policyAPIDto);
                    apiDtoList.add(policyAPIDtos.get(i));
                } else {
                    apiDtoList.add(policyAPIDtos.get(i));
                }
            }
            return apiDtoList;
        } else {
            for (int i = 0; i < policyAPIDtos.size(); i++) {
                Integer max = i;
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // 根据实际格式调整
                Date MaxPublishDateObj = policyAPIDtos.get(i).getPublishDate();
                for (int j = i + 1; j < policyAPIDtos.size(); j++) {
                    Date nowPublishDateObj = policyAPIDtos.get(j).getPublishDate();
                    if (nowPublishDateObj.after(MaxPublishDateObj)) {
                        max = j;
                        MaxPublishDateObj = nowPublishDateObj;
                    }
                }
                if (max != i) {
                    policyAPIDto = policyAPIDtos.get(i);
                    policyAPIDtos.set(i, policyAPIDtos.get(max));
                    policyAPIDtos.set(max, policyAPIDto);
                    apiDtoList.add(policyAPIDtos.get(i));
                }
            }
            if (apiDtoList.isEmpty()) {
                return policyAPIDtos;
            }
            return apiDtoList;
        }
    }

    @Override
    public List<PolicyAPIDto> selectPolicyByHotLevel() {
        List<PolicyAPIDto> policyAPIDtos = findPolicyClassById(1);
        List<PolicyAPIDto> apiDtoList = new ArrayList<>();
        PolicyAPIDto policyAPIDto = new PolicyAPIDto();
        for (int i = 0; i < policyAPIDtos.size(); i++) {
            Integer max = i;
            Integer MaxHotLevelObj = policyAPIDtos.get(i).getHotLevel();
            for (int j = i + 1; j < policyAPIDtos.size(); j++) {
                Integer nowHotLevelObj = policyAPIDtos.get(j).getHotLevel();
                if (nowHotLevelObj > MaxHotLevelObj) {
                    max = j;
                    MaxHotLevelObj = nowHotLevelObj;
                }
            }
            if (max != i) {
                policyAPIDto = policyAPIDtos.get(i);
                policyAPIDtos.set(i, policyAPIDtos.get(max));
                policyAPIDtos.set(max, policyAPIDto);
                apiDtoList.add(policyAPIDtos.get(i));
            } else {
                apiDtoList.add(policyAPIDtos.get(max));
            }
        }
        return apiDtoList;
    }

    @Override
    public List<PolicyClass> getAllHobby(Integer companyId) {
        return policyClassMapper.getAllHobby(companyId);
    }

    @Override
    public List<PolicyClass> getAllCategoryData() {
        return policyClassMapper.getAllCategoryData();
    }

    @Override
    public Integer updateHobby(PolicyClass allHobby) {
        return policyClassMapper.updateHobby(allHobby);
    }

    @Override
    public List<PolicyDtoResult> getAllPolicyClassInfo() {
        List<PolicyDtoResult> policyDtoResults = new ArrayList<>();
        List<PolicyClass> policyClass = getAll();

        for (int i = 0; i < policyClass.size(); i++) {
            List<String> list = new ArrayList<>(Arrays.asList(policyClass.get(i).getContent().split(",")));
            Collator collator = Collator.getInstance(Locale.CHINA);
            Collections.sort(list, (o1, o2) -> collator.compare(o1, o2));

            // 特殊处理适用区域
            if (policyClass.get(i).getName().equals("适用区域")) {
                // 创建主列表
                List<String> mainList = new ArrayList<>();
                mainList.add("全部");
                mainList.add("国家级");
                mainList.add("浙江省");
                mainList.addAll(RegionEnum.getAllCities());
                mainList.add("其他");

                // 创建主PolicyDtoResult
                PolicyDtoResult mainResult = new PolicyDtoResult(i, "适用区域", mainList);

                // 为每个地级市创建子PolicyDtoResult
                List<PolicyDtoResult> cityResults = new ArrayList<>();
                for (String city : RegionEnum.getAllCities()) {
                    List<String> counties = RegionEnum.getCountiesByCity(city);
                    cityResults.add(new PolicyDtoResult(0, city, counties));
                }

                mainResult.setSubChildren(cityResults);
                policyDtoResults.add(mainResult);
                continue;
            }

            // 处理"全部"和"其他"
            if (policyClass.get(i).getContent().contains("全部") && policyClass.get(i).getContent().contains("其他")) {
                list.remove("全部");
                list.remove("其他");
                list.add(0, "全部");
                list.add("其他");
            } else if (policyClass.get(i).getContent().contains("其他")) {
                list.remove("其他");
                list.add(0, "全部");
                list.add("其他");
            } else if (policyClass.get(i).getContent().contains("全部")) {
                list.remove("全部");
                list.add(0, "全部");
                list.add("其他");
            } else {
                list.add(0, "全部");
                list.add("其他");
            }

            policyDtoResults.add(new PolicyDtoResult(i, policyClass.get(i).getName(), list));
        }
        return policyDtoResults;
    }

    @Override
    public List<PolicyDtoResult> getAllPolicyClassInfoV2() {
        List<PolicyDtoResult> policyDtoResults = new ArrayList<>();
        List<PolicyClass> policyClass = getAll();

        for (int i = 0; i < policyClass.size(); i++) {
            List<String> list = new ArrayList<>(Arrays.asList(policyClass.get(i).getContent().split(",")));
            Collator collator = Collator.getInstance(Locale.CHINA);
            Collections.sort(list, (o1, o2) -> collator.compare(o1, o2));

            // 特殊处理适用区域
            if (policyClass.get(i).getName().equals("适用区域")) {
                // 创建主列表
                List<String> mainList = new ArrayList<>();
                mainList.add("全部");
                mainList.add("国家级");
                List<PolicyCategoryData> provincePCDs = policyCategoryDateService.findByType("3");
                List<String> provinceList = provincePCDs.stream()
                        .map(PolicyCategoryData::getContent)
                        .collect(Collectors.toList());
                mainList.addAll(provinceList);
                List<PolicyCategoryData> cityPCDs = policyCategoryDateService.findByType("2");
                List<String> cityList = cityPCDs.stream()
                        .map(PolicyCategoryData::getContent)
                        .collect(Collectors.toList());
                mainList.addAll(cityList);
                mainList.add("其他");

                // 创建主PolicyDtoResult
                PolicyDtoResult mainResult = new PolicyDtoResult(i, "适用区域", mainList);

                // 为每个地级市创建子PolicyDtoResult
                List<PolicyDtoResult> cityResults = new ArrayList<>();
                for (PolicyCategoryData city : cityPCDs) {
                    List<String> counties = new ArrayList<>();
                    counties.add(city.getContent());
                    List<String> cities = policyCategoryDateService.findByBelong(city.getId()).stream()
                            .map(PolicyCategoryData::getContent)
                            .collect(Collectors.toList());
                    ;
                    counties.addAll(cities);
                    cityResults.add(new PolicyDtoResult(0, city.getContent(), counties));
                }

                mainResult.setSubChildren(cityResults);
                policyDtoResults.add(mainResult);
                continue;
            }

            // 处理"全部"和"其他"
            if (policyClass.get(i).getContent().contains("全部") && policyClass.get(i).getContent().contains("其他")) {
                list.remove("全部");
                list.remove("其他");
                list.add(0, "全部");
                list.add("其他");
            } else if (policyClass.get(i).getContent().contains("其他")) {
                list.remove("其他");
                list.add(0, "全部");
                list.add("其他");
            } else if (policyClass.get(i).getContent().contains("全部")) {
                list.remove("全部");
                list.add(0, "全部");
                list.add("其他");
            } else {
                list.add(0, "全部");
                list.add("其他");
            }

            policyDtoResults.add(new PolicyDtoResult(i, policyClass.get(i).getName(), list));
        }
        return policyDtoResults;
    }


    @Override
    public List<PolicyDtoResult> processPolicyCategories(Integer companyId, List<PolicyClass> policyClassList, Date updateTime) {
        List<PolicyDtoResult> policyDtoResults = new ArrayList<>();
        Set<String> processedNames = new HashSet<>();

        // 先创建所有的policyDtoResults
        for (PolicyClass policyClass : policyClassList) {
            if (!processedNames.contains(policyClass.getName())) {
                List<String> content = new ArrayList<>();
                policyDtoResults.add(new PolicyDtoResult(companyId, policyClass.getName(), content, updateTime));
                processedNames.add(policyClass.getName());
            }
        }

        // 处理每个policyDtoResult的children
        for (PolicyClass policyClass : policyClassList) {
            for (PolicyDtoResult policyDtoResult : policyDtoResults) {
                if (policyClass.getName().equals(policyDtoResult.getName())) {
                    List<String> content = policyDtoResult.getChildren();

                    if (policyClass.getContent().equals("全部") || policyClass.getContent().equals("其他")) {
                        content.clear();
                        content.add(policyClass.getContent());
                        break;
                    }

                    // 只对适用区域进行特殊处理
                    if ("适用区域".equals(policyClass.getName())) {
                        handleApplicableArea(policyClassList, policyDtoResult);
                    } else {
                        content.add(policyClass.getContent());
                    }
                    break;
                }
            }
        }

        return policyDtoResults;
    }

    private void handleApplicableArea(List<PolicyClass> policyClassList, PolicyDtoResult policyDtoResult) {
        List<String> content = policyDtoResult.getChildren();

        // 收集所有适用区域的区县
        Set<String> allCounties = policyClassList.stream()
                .filter(pc -> "适用区域".equals(pc.getName())
                        && !"全部".equals(pc.getContent())
                        && !"其他".equals(pc.getContent()))
                .map(PolicyClass::getContent)
                .collect(Collectors.toSet());

        if (allCounties.isEmpty()) {
            return;
        }

        // 检查是否包含所有城市的区县
        Set<String> matchedCities = new HashSet<>();

//        List<PolicyCategoryData> provincePCDs = policyCategoryDateService.findByType("3");
//        List<String> provinceList = provincePCDs.stream()
//                .map(PolicyCategoryData::getContent)
//                .collect(Collectors.toList());
//        provinceList.retainAll(allCounties);
//        matchedCities.addAll(provinceList);
//
        List<PolicyCategoryData> cityPCDs = policyCategoryDateService.findByType("2");
//        List<String> cityList = cityPCDs.stream()
//                .map(PolicyCategoryData::getContent)
//                .collect(Collectors.toList());
//        cityList.retainAll(allCounties);
//        matchedCities.addAll(cityList);

        List<PolicyDtoResult> cityResults = new ArrayList<>();
//        for (RegionEnum region : RegionEnum.values()) {
//            if (allCounties.containsAll(region.getCounties())) {
//                matchedCities.add(region.getCityName());
//                List<String> counties = region.getCounties();
//                cityResults.add(new PolicyDtoResult(0, region.getCityName(), counties));
//                allCounties.removeAll(region.getCounties());
//            }
//        }
        for (PolicyCategoryData city : cityPCDs) {
            List<PolicyCategoryData> counties = policyCategoryDateService.findByBelong(city.getId());
            List<String> area = counties.stream()
                    .map(PolicyCategoryData::getContent)
                    .collect(Collectors.toList());
            if (allCounties.containsAll(area)) {
                matchedCities.add(city.getContent());
//                cityResults.add(new PolicyDtoResult(0, city.getContent(), area));
                area.forEach(allCounties::remove);
            } else if (!allCounties.isEmpty()) {
                Set<String> both = area.stream()
                        .filter(allCounties::contains)
                        .collect(Collectors.toSet());
                if (!both.isEmpty()) {
                    matchedCities.add(city.getContent());
                    cityResults.add(new PolicyDtoResult(0, city.getContent(), new ArrayList<>(both)));
                    both.forEach(allCounties::remove);
                }
            }
        }
        policyDtoResult.setSubChildren(cityResults);

        // 确定最终显示内容
        content.clear();
        if (!matchedCities.isEmpty() && allCounties.contains("浙江省") && allCounties.contains("国家级")) {
            content.add("国家级");
            content.add("浙江省");
            content.addAll(matchedCities);
        } else if (!matchedCities.isEmpty() && allCounties.contains("浙江省")) {
            content.add("浙江省");
            content.addAll(matchedCities);
        } else if (!matchedCities.isEmpty() && allCounties.contains("国家级")) {
            content.add("国家级");
            content.addAll(matchedCities);
        } else if (!matchedCities.isEmpty()) {
            content.addAll(matchedCities);
        } else if (!allCounties.isEmpty()) {
            content.add(allCounties.iterator().next());
        }

        //subChildren
//        List<PolicyDtoResult> cityResults = new ArrayList<>();
//        for (String city : cityList) {
//            List<String> counties = new ArrayList<>();
//            counties.add(city);
//            List<PolicyCategoryData> mayCities = policyCategoryDateService.findByContent(city);
//            if(mayCities.size() == 1){
//                PolicyCategoryData pcd = mayCities.get(0);
//                List<String> cities = policyCategoryDateService.findByBelong(pcd.getId()).stream()
//                        .map(PolicyCategoryData::getContent)
//                        .collect(Collectors.toList());
//                counties.addAll(cities);
//                cityResults.add(new PolicyDtoResult(0, pcd.getContent(), counties));
//            }else if(mayCities.size()>1){
//                throw new RuntimeException("数据库有多个同名地区");
//            }else{
//                throw new RuntimeException("数据接收错误");
//            }
//        }
//        policyDtoResult.setSubChildren(cityResults);
    }
}
