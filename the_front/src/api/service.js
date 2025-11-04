import request from "@/utils/request";

// 1、获取企业信息回显数据
export function enterpriseDataAPI(obj) {
  return request({
    url: `/api/policy/enterprisedata`,
    method: "GET",
    params: obj,
  });
}

// 2.获取企业所属行业数据

export function entNatureDataAPI() {
  return request({
    url: `/api/policy/entNatureData`,
    method: "GET",
  });
}

//   3.提交企业信息数据

export function postEnterpriseDataAPI(obj) {
  return request({
    url: `/api/policy/enterprisedata`,
    method: "POST",
    data: obj,
  });
}

//   4.获取企业年报数据
export function enterpriseReportAPI(obj) {
  return request({
    url: `/api/policy/enterprisereport`,
    method: "GET",
    params: obj,
  });
}

//   5.提交企业年报数据
export function postEnterpriseReportAPI(obj) {
  return request({
    url: `/api/policy/putenterprisereport`,
    method: "POST",
    params: obj,
  });
}

//   6.获取政企匹配数据（根据企业所在地区）
export function goventMatchAPI(obj) {
  return request({
    url: `/api/policy/goventmatchdata`,
    method: "GET",
    params: obj,
  });
}

//  7.获取政策详情数据
export function policyDataAPI(policyid) {
  return request({
    url: `/api/policy/policydata/${policyid}`,
    method: "GET",
  });
}
//   8.获取政策模型数据
export function policyModelAPI(obj) {
  return request({
    url: `/api/policy/policymodel`,
    method: "GET",
    params: obj,
  });
}

//   9.获取模型材料数量
export function caiLiaoNumberAPI(obj) {
  return request({
    url: `/api/policy/cailiaonumber`,
    method: "GET",
    params: obj,
  });
}

//   10.获取材料详情数据
export function caiLiaoAPI(obj) {
  return request({
    url: `/api/policy/cailiao`,
    method: "GET",
    params: obj,
  });
}

//   10.获取模型提交材料的信息,一对一提交材料的信息获取
export function caiLiaoOtoOAPI(obj) {
  return request({
    url: `/api/policy/getmodelmaterialinfo`,
    method: "GET",
    params: obj,
  });
}

//   11.获取上传材料清单数据
export function caiLiaoQingDanAPI(obj) {
  return request({
    url: `/api/policy/cailiaoqingdan`,
    method: "GET",
    params: obj,
  });
}

//   12.上传材料接口
export function caiLiaoUpLoadAPI(obj) {
  return request({
    url: `/api/policy/materialupload`,
    method: "POST",
    data: obj,
  });
}

//   13.删除材料接口
export function deleteCaiLiaoAPI(materialId) {
  return request({
    url: `/api/policy/deletematerial/${materialId}`,
    method: "DELETE",
  });
}

//   14.查看审核信息接口
export function sheHeInfoAPI(obj) {
  return request({
    url: `/api/policy/sheheinfo`,
    method: "GET",
    params: obj,
  });
}

//   15.查看申报进度表

export function shenBaoProgressAPI(obj) {
  return request({
    url: `/api/policy/shenbaoprogress`,
    method: "GET",
    params: obj,
  });
}

export function policyModelUnqualified(obj) {
  return request({
    url: "/api/policy/policyModelUnqualified",
    method: "get",
    params: obj,
  });
}
