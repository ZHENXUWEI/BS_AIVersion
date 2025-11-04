package com.ruoyi.web.controller.api.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.web.controller.api.common.Result;
import com.ruoyi.web.controller.api.domain.CompanySubmissionStatus;
import com.ruoyi.web.controller.api.domain.EntNatureDate;
import com.ruoyi.web.controller.api.domain.MatchN;
import com.ruoyi.web.controller.api.domain.PolicyClass;
import com.ruoyi.web.controller.api.domain.PolicyLibrary;
import com.ruoyi.web.controller.api.domain.PolicyMatch;
import com.ruoyi.web.controller.api.domain.RegionEnum;
import com.ruoyi.web.controller.api.domain.UserVisitN;
import com.ruoyi.web.controller.api.domain.dto.PolicyAPIDto;
import com.ruoyi.web.controller.api.domain.dto.PolicyDtoResult;
import com.ruoyi.web.controller.api.service.CompanySubmissionStatusService;
import com.ruoyi.web.controller.api.service.EntNatureDataService;
import com.ruoyi.web.controller.api.service.GovernMatchService;
import com.ruoyi.web.controller.api.service.PolicyClassService;
import com.ruoyi.web.controller.api.service.PolicyLibraryService;
import com.ruoyi.web.controller.api.service.PolicyMatchService;
import com.ruoyi.web.controller.company.service.ICompanyIndicatorRecordService;
import com.ruoyi.web.controller.company.service.ICompanyService;
import com.ruoyi.web.controller.policy.domain.Policy;
import com.ruoyi.web.controller.policy.domain.PolicyCategory;
import com.ruoyi.web.controller.policy.domain.PolicyCategoryData;
import com.ruoyi.web.controller.policy.service.IPolicyCategoryDataService;
import com.ruoyi.web.controller.policy.service.IPolicyCategoryService;
import com.ruoyi.web.controller.policy.service.IPolicyService;
import com.ruoyi.web.controller.submission.domain.CompanyPolicySubmission;
import com.ruoyi.web.controller.submission.service.ICompanyPolicySubmissionService;

/**
 * @author Lemon-zhu119
 * @date 2025-04-22
 */
@RestController
@RequestMapping("/api/policy")
public class PolicyDtoController {

    /**
     *
     */
    @Autowired
    private PolicyLibraryService policyLibraryService;
    /**
     *
     */
    @Autowired
    private PolicyClassService policyClassService;
    /**
     *
     */
    @Autowired
    private EntNatureDataService entNatureDataService;
    /**
     *
     */
    @Autowired
    private PolicyMatchService policyMatchService;
    /**
     *
     */
    @Autowired
    private CompanySubmissionStatusService companySubmissionStatusService;

    /**
     *
     */
    @Autowired
    private IPolicyService policyService;

    /**
     *
     */
    @Autowired
    private ICompanyPolicySubmissionService companyPolicySubmissionService;
    /**
     *
     */
    @Autowired
    private GovernMatchService governMatchService;
    @Autowired
    private ICompanyService companyService;
    /**
     *
     */
    @Autowired
    private IPolicyCategoryService policyCategoryService;
    /**
     *
     */
    @Autowired
    private IPolicyCategoryDataService policyCategoryDataService;
    @Autowired
    private ICompanyIndicatorRecordService companyIndicatorRecordService;

    /**
     * @param policySelect
     * @return
     */
    @PostMapping("/policyinfo")
    public Result<PolicyRes> getUserInfo(
            @RequestBody PolicySelect policySelect
            //            @RequestParam List<PolicyDtoResult> categories, @RequestParam String query1,
            //                                         @RequestParam(required = false) String query2,
            //                                         @RequestParam List<String> timePeriod
            //                                         @RequestParam Integer pagenum,
            //                                         @RequestParam Integer pagesize
            //            @RequestParam(required = false) List<PolicyDtoResult> categories,
            //            @RequestParam(required = false) String query2,
            //            @RequestParam(required = false) String query1,
            //            @RequestParam(required = false) List<String> timePeriod
    ) {
        if (policySelect.getCategories() == null) {
            policySelect.setCategories(new ArrayList<>());
        }
        if (policySelect.getTimePeriod() == null) {
            policySelect.setTimePeriod(new ArrayList<>());
        }
        List<Date> dateList = new ArrayList<>();
        List<PolicyAPIDto> policyAPIDtoList = policyClassService.selectPolicyByClass(policyClassService.findPolicyClassById(1), policySelect.getCategories());
        policyAPIDtoList = policyClassService.sortPolicyByQuery(policyAPIDtoList, policySelect.getQuery1());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // 根据需要调整日期格式
        if (!(policySelect.getQuery2() == null || policySelect.getQuery2().equals(""))) {
            policyAPIDtoList = policyClassService.selectPolicyByName(policyAPIDtoList, policySelect.getQuery2());
        }
        for (String dateStr : policySelect.getTimePeriod()) {
            try {
                Date date = dateFormat.parse(dateStr);
                dateList.add(date);
            } catch (ParseException e) {
                e.printStackTrace();
                // 处理解析错误，例如返回错误信息或抛出异常
            }
        }
        if (!(dateList == null || dateList.size() == 0)) {
            policyAPIDtoList = policyClassService.selectPolicyByTime(policyAPIDtoList, dateList);
        }
        PolicyRes policyRes = new PolicyRes();
        policyRes.setTotal(policyAPIDtoList.size());
        policyRes.setRow(policyAPIDtoList);

        if("浏览量".equals(policySelect.getQuery1())){
            List<PolicyAPIDto> sortedRows = policyRes.getRow().stream()
                    .sorted(Comparator.comparing(
                            PolicyAPIDto::getHotLevel,
                            Comparator.nullsLast(Comparator.reverseOrder())
                    ))
                    .collect(Collectors.toList());

            policyRes.setRow(sortedRows);
        }
//        else if("昨日新增".equals(policySelect.getQuery1())){
//            System.out.println("进入昨日新增---------------------------------");
//            System.out.println(policyRes.getRow());
//            List<PolicyAPIDto> policyAPIDtos = policyService.filterPolicyAPIDtosByYesterdayDate(policyRes.getRow());
//            System.out.println("筛选完毕----------------------------------");
//            System.out.println(policyAPIDtos);
//            policyRes.setRow(policyAPIDtos);
//            policyRes.setTotal(policyAPIDtos.size());
//        }
        return Result.success(policyRes);
    }

    /**
     * @param policyId
     * @return
     */
    @GetMapping("/getpolicydetail/{id}")
    public Result<PolicyRes> getPolicyById(@PathVariable("id") Integer policyId) {
        List<PolicyAPIDto> policyAPIDtoList = policyClassService.findPolicyClassById(1);
        List<PolicyAPIDto> policyAPIDtos = new ArrayList<>();
        for (PolicyAPIDto policyAPIDto : policyAPIDtoList) {
            if (policyAPIDto.getId().equals(policyId)) {
                policyAPIDtos.add(policyAPIDto);
            }
        }
        PolicyRes policyRes = new PolicyRes(policyAPIDtos.size(), policyAPIDtos);
        return Result.success(policyRes);
    }

    /**
     * @param policyId
     * @return 自行封装一个返回类对请求进行统一的值返回
     */
    @GetMapping("/vistiedNumber/{id}")
    public Result addHotLevel(@PathVariable("id") Integer policyId) {
        Policy policy = policyService.selectPolicyById(policyId);
        policy.setHotLevel(policy.getHotLevel() + 1);
        int update = policyService.updatePolicy(policy);
        if (update == 1) {
            return Result.success("添加浏览量成功");
        } else {
            return Result.error("添加浏览量失败");
        }
    }

    /**
     * @return
     */
    @GetMapping("/policystatus")
    public Result<PolicyLibrary> getPolicyLibrary() {
        PolicyLibrary policyLibrary = policyLibraryService.getAll();
        return Result.success(policyLibrary);
    }

    /**
     * @return
     */
    @GetMapping("/matchN")
    public Result<MatchN> getMatchN() {
        MatchN matchN = policyLibraryService.getMatchN();
        matchN.setMatchN(matchN.getMatchN() + 1);
        int update = policyLibraryService.updateMatchN(matchN);
        return Result.success(matchN);
    }

    /**
     * @return
     */
    @GetMapping("/userVisitN")
    public Result<UserVisitN> getUserVisitN() {
        UserVisitN userVisitN = policyLibraryService.getUserVisitN();
        userVisitN.setUserVisitN(userVisitN.getUserVisitN() + 1);
        int update = policyLibraryService.updateUserVisitN(userVisitN);
        return Result.success(userVisitN);
    }

    /**
     * 获取所有政策分类信息
     *
     * @return
     */
    @GetMapping("/policyclassinfo")
    public Result<List<PolicyDtoResult>> getAll() {
        return Result.success(policyClassService.getAllPolicyClassInfoV2());
    }

    /**
     * @param companyId
     * @return
     */
    @GetMapping("/policyhobby")
    public Result<List<PolicyDtoResult>> getPolicyHobby(@RequestParam Integer companyId) {
        List<PolicyDtoResult> policyDtoResults = new ArrayList<>();
        List<PolicyClass> policyClassList = new ArrayList<>();
        List<PolicyClass> allHobby = policyClassService.getAllHobby(companyId);
        if (allHobby.get(0).getContent() == null || allHobby.get(0).getContent().equals("")) {
            return Result.success(policyDtoResults);
        }
        extracted(companyId, policyDtoResults, policyClassList, allHobby);
        return Result.success(policyDtoResults);
    }

    /**
     * @param companyId
     * @param policyDtoResults
     * @param policyClassList
     * @param allHobby
     */
    private void extracted(Integer companyId, List<PolicyDtoResult> policyDtoResults, List<PolicyClass> policyClassList, List<PolicyClass> allHobby) {

        if (allHobby.isEmpty() || allHobby.get(0).getContent() == null) {
            return;
        }
        //分割hobby
        String[] hobbiesId = allHobby.get(0).getContent().split(";");

        //拿到所有的pcd数据
        List<PolicyClass> policyClasses = policyClassService.getAllCategoryData();
        Integer loopId = -1;
        for (String hobbyId : hobbiesId) {
            String[] special = hobbyId.split(":");
            if (special.length > 1) {
                loopId++;
                PolicyClass specialPolicyClass = new PolicyClass(loopId, special[0], special[1]);
                policyClassList.add(specialPolicyClass);
                continue;
            }
            for (PolicyClass policyClass : policyClasses) {
                if (policyClass.getId() == Integer.parseInt(hobbyId)) {
                    policyClassList.add(policyClass);
                }
            }
        }

        // 使用service层方法处理政策分类数据
        List<PolicyDtoResult> results = policyClassService.processPolicyCategories(companyId, policyClassList, allHobby.get(0).getUpdateTime());
        policyDtoResults.addAll(results);
    }

    /**
     * @param hobbyRes
     * @return 自行封装一个返回类对请求进行统一的值返回
     *///
    @PostMapping("/policyhobby")
    @ResponseBody
    public Result updatePolicyHobby(@RequestBody HobbyRes hobbyRes) {
        List<String> hobbies = new ArrayList<>();
        List<FormData> FormDataList = hobbyRes.getFormData();
        if (FormDataList.isEmpty()) {
            return Result.error("喜好输入错误");
        }
        Integer companyId = hobbyRes.getCompanyId();
        outLoop:
        for (FormData formData : FormDataList) {
            if (formData.getChildren() == null || formData.getChildren().isEmpty()) {
                continue;
            }
            for (String content : formData.getChildren()) {
                if (content.equals("全部") || content.equals("其他")) {
                    hobbies.add(formData.getName() + ":" + content);
                    continue outLoop;
                }
            }
            if (formData.getName().equals("适用区域")) {
                formData.setChildren(HobbyApplicableArea(formData));
            }
            List<PolicyClass> policyClasses = policyClassService.getAllCategoryData();
            for (String content : formData.getChildren()) {
                for (PolicyClass policyClass : policyClasses) {
                    if (policyClass.getContent().equals(content) && policyClass.getName().equals(formData.getName())) {
                        hobbies.add(String.valueOf(policyClass.getId()));
                    }
                }
            }
        }
        String hobbiesStr = "";
        if (!hobbies.isEmpty()) {
            hobbiesStr = String.join(";", hobbies.stream().map(Object::toString).collect(Collectors.toList()));
        }
        List<PolicyClass> allHobbyDb = policyClassService.getAllHobby(companyId);
        if (allHobbyDb.get(0).getContent() == null) {
            allHobbyDb.get(0).setContent("");
        }
        if (allHobbyDb.get(0).getContent().equals(hobbiesStr)) {
            return Result.success("与之前喜好相同");
        } else {
            PolicyClass allHobby = new PolicyClass(companyId, null, hobbiesStr, new Date());
            policyClassService.updateHobby(allHobby);
            return Result.success("更新成功");
        }
    }

    private List<String> HobbyApplicableArea(FormData formData) {
        List<PolicyDtoResult> subChildren = formData.getSubChildren();
        List<String> categoryList = formData.getChildren();
        List<String> countriesList = new ArrayList<>();
        if (subChildren != null && subChildren.size() > 0) {
            categoryList.removeIf(category -> subChildren.stream()
                    .anyMatch(child -> child.getName().equals(category)));
        }

        //得到适用区域在policyClass中的id
        List<PolicyCategory> PolicyCategoryId = policyCategoryService.selectPolicyCategoryList(new PolicyCategory(null, "适用区域", null));
        for (String category : categoryList) {
            List<String> countryList = new ArrayList<>();
            if (!PolicyCategoryId.isEmpty()) {
                if (category.equals("浙江省")) {
                    countryList.add("浙江省");
                    insertCategoryByArea(PolicyCategoryId, countryList);
                } else if (category.equals("国家级")) {
                    countryList.add("国家级");
                    insertCategoryByArea(PolicyCategoryId, countryList);
                } else {
                    List<PolicyCategoryData> city = policyCategoryDataService.findByContent(category);
                    if (city.size() == 1) {
                        List<PolicyCategoryData> countyList = policyCategoryDataService.findByBelong(city.get(0).getId());
                        List<String> counties = countyList.stream()
                                .map(PolicyCategoryData::getContent)
                                .collect(Collectors.toList());
                        insertCategoryByArea(PolicyCategoryId, counties);
                        countryList.addAll(counties);
                    }
                }
            }
            if (countryList.size() > 0) {
                countriesList.addAll(countryList);
            }

        }

        List<String> countryList = new ArrayList<>();
        if (subChildren != null && subChildren.size() > 0) {
            for (PolicyDtoResult subs : subChildren) {
                List<String> children = subs.getChildren();
                insertCategoryByArea(PolicyCategoryId, children);
                countryList.addAll(children);
            }

        }
        if (countryList.size() > 0) {
            countriesList.addAll(countryList);
        }
        return countriesList;
    }

    private void insertCategoryByArea(List<PolicyCategory> PolicyCategoryId, List<String> countryList) {
        for (String country : countryList) {
            List<PolicyCategoryData> categoryDataList = policyCategoryDataService.selectPolicyCategoryDataList(new PolicyCategoryData(null, PolicyCategoryId.get(0).getId(), country, 1));
            if (categoryDataList.isEmpty()) {
                policyCategoryDataService.insertPolicyCategoryData(new PolicyCategoryData(null, PolicyCategoryId.get(0).getId(), country, 1));
            }
        }
    }

    /**
     * @param companyId
     * @return
     *///获取根据喜好推送的policy
    @GetMapping("/policyPush")
    public Result<PolicyRes> getPolicyPush(@RequestParam Integer companyId) {
        List<PolicyDtoResult> policyDtoResults = new ArrayList<>();
        List<PolicyClass> policyClassList = new ArrayList<>();
        List<PolicyClass> allHobby = policyClassService.getAllHobby(companyId);
        extracted(companyId, policyDtoResults, policyClassList, allHobby);
        //根据获取的policyDtoResults获取policy
        List<PolicyAPIDto> policyAPIDtoList = new ArrayList<>();
        policyAPIDtoList.addAll(policyClassService.selectPolicyByClass(policyClassService.findPolicyClassById(1), policyDtoResults));
        PolicyRes policyRes = new PolicyRes();
        policyRes.setTotal(policyAPIDtoList.size());
        policyRes.setRow(policyAPIDtoList);
        return Result.success(policyRes);
    }

    /**
     * @return
     *///测试用接口
    @GetMapping("/inin")
    public Result<List<PolicyAPIDto>> get() {
        return Result.success(policyClassService.findPolicyClassById(1));
    }

    /**
     * @return
     */
    @GetMapping("/entNatureData")
    public Result<List<EntNatureDate>> getAllEntNatureData() {
        return Result.success(entNatureDataService.getAll());
    }

    /**
     * @return
     */
    @GetMapping("/policyMatchList")
    public Result<List<PolicyMatch>> getPolicyMatch() {
        List<PolicyMatch> policyMatchList = policyMatchService.getAll();
        return Result.success(policyMatchList);
    }

    /**
     * @param id
     * @return 自行封装一个返回类对请求进行统一的值返回
     */
    @GetMapping("/goventmatchdata")
    public Result GovernMatch(@RequestParam(required = false) Integer id) {
        List<PolicyAPIDto> list = governMatchService.GovernMatchData(id);
        return Result.success(list);
    }

    /**
     * @param policyId
     * @return
     */
    @GetMapping("/policydata/{policyId}")
    public Result<PolicyAPIDto> getPolicyDataById(@PathVariable("policyId") Integer policyId) {
        return Result.success(policyClassService.selectPolicyById(policyId).get(0));
    }

    /**
     * @param id   companyId
     * @param year 匹配年份
     * @return 匹配信息
     */
    @GetMapping("/policymodel")
    public Result<List<CompanySubmissionStatus>> modelData(@RequestParam("id") Integer id, @RequestParam(required = false) Integer year) {
        if (year == null) {
            year = LocalDate.now().getYear();
        }
        List<Policy> policies = policyService.selectCustomModelList(new Policy(1));
        Integer finalYear = year;
        policies = policies.stream()
                .filter(policy -> policy.getName().startsWith(String.valueOf(finalYear)))
                .collect(Collectors.toList());
        List<CompanySubmissionStatus> result = new ArrayList<>();
        for (Policy policy : policies) {
            CompanySubmissionStatus css = new CompanySubmissionStatus();
            List<CompanyPolicySubmission> companyPolicySubmissions = companyPolicySubmissionService.selectCompanyPolicySubmissionList(new CompanyPolicySubmission(id, policy.getId()));
            List<Date> history = new ArrayList<>();
            for (CompanyPolicySubmission companyPolicySubmission : companyPolicySubmissions) {
                history.add(companyPolicySubmission.getDate());
            }
            css.setHistory(history);
            css.setModelId(policy.getId());
            css.setModelName(policy.getName());
            List<Map<String, String>> indicatorName = policyService.c2cNoMatchedIndicatorV2(id, policy, year);
            css.setIndicatorName(indicatorName);
            css.setTotal(indicatorName.size());
            result.add(css);
        }
        return Result.success(result);
    }

    /**
     * @return List<PolicyAPIDto>
     * 获取未过期的政策
     */
    @GetMapping("/policyNotExpired")
    public Result<List<PolicyAPIDto>> getPolicyNotExpired() {
        List<PolicyAPIDto> policyAPIDtoList = policyClassService.findPolicyClassById(1);
        List<PolicyAPIDto> policyAPIDtoList2 = new ArrayList<>();
        for (PolicyAPIDto policyAPIDto : policyAPIDtoList) {
            if (policyAPIDto.getShengyuTime() != null) {
                policyAPIDtoList2.add(policyAPIDto);
            }
        }
        return Result.success(policyAPIDtoList2);
    }

    /**
     * @author Lemon-zhu119
     * @date 2025-04-22
     */
    public static class HobbyRes {

        /**
         *
         */
        private Integer companyId;
        /**
         *
         */
        private List<FormData> formData;

        /**
         *
         */
        public HobbyRes() {
        }

        /**
         * @param companyId
         * @param formData
         * @param updateTime
         */
        public HobbyRes(Integer companyId, List<FormData> formData, Date updateTime) {
            this.companyId = companyId;
            this.formData = formData;
        }

        /**
         * @return
         */
        @Override
        public String toString() {
            return "HobbyRes{"
                    + "companyId=" + companyId
                    + ", formData=" + formData
                    + '}';
        }

        /**
         * @return
         */
        public Integer getCompanyId() {
            return companyId;
        }

        /**
         * @param companyId
         */
        public void setCompanyId(Integer companyId) {
            this.companyId = companyId;
        }

        /**
         * @return
         */
        public List<FormData> getFormData() {
            return formData;
        }

        /**
         * @param formData
         */
        public void setFormData(List<FormData> formData) {
            this.formData = formData;
        }
    }

    /**
     * @author Lemon-zhu119
     * @date 2025-04-22
     */
    private static class PolicyRes {

        /**
         *
         */
        private Integer total;
        /**
         *
         */
        private List<PolicyAPIDto> row;

        /**
         * @param total
         * @param row
         */
        public PolicyRes(Integer total, List<PolicyAPIDto> row) {
            this.total = total;
            this.row = row;
        }

        /**
         *
         */
        public PolicyRes() {
        }

        /**
         * @return
         */
        public Integer getTotal() {
            return total;
        }

        /**
         * @param total
         */
        public void setTotal(Integer total) {
            this.total = total;
        }

        /**
         * @return
         */
        public List<PolicyAPIDto> getRow() {
            return row;
        }

        /**
         * @param row
         */
        public void setRow(List<PolicyAPIDto> row) {
            this.row = row;
        }

        /**
         * @return
         */
        @Override
        public String toString() {
            return "PolicyRes{"
                    + "total=" + total
                    + ", row=" + row
                    + '}';
        }

    }

//    @GetMapping("/policymodel")
//    public Result<List<CompanySubmissionStatus>>
//    getSubmissionStatusByCompanyId(@RequestParam("id") Integer id) {
//        return Result.success(companySubmissionStatusService.selectSubmissionStatusByCompanyId(id));
//    }

    /**
     * @author Lemon-zhu119
     * @date 2025-04-22
     */
    private static class PolicySelect {

        /**
         *
         */
        private List<PolicyDtoResult> categories;
        /**
         *
         */
        private String query1;
        /**
         *
         */
        private String query2;
        /**
         *
         */
        private List<String> timePeriod;
        private String subCity;

        public String getSubCity() {
            return subCity;
        }

        public void setSubCity(String subCity) {
            this.subCity = subCity;
        }

        /**
         * @param categories
         * @param query1
         * @param query2
         * @param timePeriod
         */
        public PolicySelect(List<PolicyDtoResult> categories, String query1, String query2, List<String> timePeriod) {
            this.categories = categories;
            this.query1 = query1;
            this.query2 = query2;
            this.timePeriod = timePeriod;
        }

        /**
         *
         */
        public PolicySelect() {
        }

        /**
         * @return
         */
        @Override
        public String toString() {
            return "PolicySelect{"
                    + "categories=" + categories
                    + ", query1='" + query1 + '\''
                    + ", query2='" + query2 + '\''
                    + ", timePeriod=" + timePeriod
                    + '}';
        }

        /**
         * @return
         */
        public List<PolicyDtoResult> getCategories() {
            return categories;
        }

        /**
         * @param categories
         */
        public void setCategories(List<PolicyDtoResult> categories) {
            this.categories = categories;
        }

        /**
         * @return
         */
        public String getQuery1() {
            return query1;
        }

        /**
         * @param query1
         */
        public void setQuery1(String query1) {
            this.query1 = query1;
        }

        /**
         * @return
         */
        public String getQuery2() {
            return query2;
        }

        /**
         * @param query2
         */
        public void setQuery2(String query2) {
            this.query2 = query2;
        }

        /**
         * @return
         */
        public List<String> getTimePeriod() {
            return timePeriod;
        }

        /**
         * @param timePeriod
         */
        public void setTimePeriod(List<String> timePeriod) {
            this.timePeriod = timePeriod;
        }
    }

    /**
     * @author Lemon-zhu119
     * @date 2025-04-22
     */
    public static class FormData {

        /**
         *
         */
        private Integer id;
        /**
         *
         */
        private String name;
        /**
         *
         */
        private List<String> children;

        private List<PolicyDtoResult> subChildren;

        /**
         *
         */
        public FormData() {
        }

        /**
         * @param id
         * @param name
         * @param children
         */
        public FormData(Integer id, String name, List<String> children) {
            this.id = id;
            this.name = name;
            this.children = children;
        }

        public List<PolicyDtoResult> getSubChildren() {
            return subChildren;
        }

        public void setSubChildren(List<PolicyDtoResult> subChildren) {
            this.subChildren = subChildren;
        }

        public FormData(Integer id, String name, List<String> children, List<PolicyDtoResult> subChildren) {
            this.id = id;
            this.name = name;
            this.children = children;
            this.subChildren = subChildren;
        }

        /**
         * @return
         */
        @Override
        public String toString() {
            return "FormData{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", children=" + children +
                    ", subChildren=" + subChildren +
                    '}';
        }

        /**
         * @return
         */
        public List<String> getChildren() {
            return children;
        }

        /**
         * @param children
         */
        public void setChildren(List<String> children) {
            this.children = children;
        }

        /**
         * @return
         */
        public Integer getId() {
            return id;
        }

        /**
         * @param id
         */
        public void setId(Integer id) {
            this.id = id;
        }

        /**
         * @return
         */
        public String getName() {
            return name;
        }

        /**
         * @param name
         */
        public void setName(String name) {
            this.name = name;
        }
    }

}
