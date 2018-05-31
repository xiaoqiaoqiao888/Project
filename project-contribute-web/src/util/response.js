
/* 后台接口返回状态码 */
export const Success = 200 // 调用成功
export const Forbidden = 403 // 没有权限访问
export const Unauthorized = 401 // 未登录
export const ServerError = 500  // 服务器错误
export const BadRequest = 400  // 参数错误
// 判断调用是否成功
export function isSuccess (res) {
  return res.status == Success
}
/* 判断是否没有权限调用 */
export function isForbidden (res) {
  return res.status == Forbidden
}
/* 判断是否未登录 */
export function isUnauthorized (res) {
  return res.status == Unauthorized
}
/* 判断是否服务器发生异常 */
export function isError (res) {
  return res.status == ServerError
}
/* 判断是否参数存在问题异常 */
export function isBadRequest (res) {
  return res.status == BadRequest
}
/*
方法名：获取数据集
参数：res，http请求返回数据
object,页面的this对象，用于调用object.$Message.error打印错误信息，可不传
返回值：请求成功返回结果集
        请求失败，返回false
*/
export function data (res, object) {
  if (isSuccess(res)) {
    return res.data
  } else if (isUnauthorized(res)) {
    // to login
    let currHref = window.location.href
    currHref = currHref.split('#')[0]
    window.location.href = currHref + '#/Login'
  } else if (isError(res)) {
    if (object) {
      if (res.data.data) {
        object.$Message.error(res.data.data)
      } else {
        object.$Message.error('出错啦，稍后再试吧')
      }
    }
  } else if (isForbidden(res)) {
    if (object) {
      if (res.data.data) {
        object.$Message.error(res.data.data)
      } else {
        object.$Message.error('您没有权限访问该内容')
      }
    }
  } else if (isBadRequest(res)) {
    if (object) {
      if (res.data.data) {
        object.$Message.error(res.data.data)
      } else {
        object.$Message.error('参数错误，处理失败')
      }
    }
  }
  return false
}