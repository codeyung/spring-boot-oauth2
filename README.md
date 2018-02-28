> ## spring-boot-oauth2
>SpringBoot  Apache Oltu  RESTful  Redis Mybatis  Implement OAuth2 authorization
>
>获取access_token
>
>access_token是开放平台的全局唯一接口调用凭据，调用各接口时都需使用access_token。access_token的有效期目前为1个小时，需定时刷新。
>
>开放平台的API调用所需的access_token的使用及生成方式说明：
>
>1、建议统一获取和刷新access_token，否则容易造成冲突，导致access_token覆盖而影响业务；
>
>2、目前access_token的有效期通过返回的expire_in来传达，目前是3600秒之内的值。服务器可根据返回的refresh_token刷新当前 access_token有效期。
>
>或者当refresh_token失效时重新申请 access_token。refresh_token失效时间默认为30天；
>
>3、access_token的有效时间可能会在未来有调整。
>
>请注意，此处access_token是1小时刷新一次，开发者需要自行进行access_token的缓存，避免access_token的获取次数达到每日的限定额度。
>
>开放接口的调用，如获取用户基本信息、获取文章等，都是需要获取用户身份认证的。目前开放平台用户身份鉴权主要采用的是OAuth2.0。
>
> ## OAuth2.0概述
>OAuth是一个关于授权（authorization）的开放网络标准，在全世界得到广泛应用，目前的版本是2.0版。
>
>客户端必须得到用户的授权（authorization grant），才能获得令牌（access token）。OAuth 2.0定义了四种授权方式。
>
>授权码模式（authorization code）
>简化模式（implicit）
>密码模式（resource owner password credentials）
>客户端模式（client credentials）
>
>本平台支持三种模式
>
>授权码模式（暂无）
>密码模式（暂无）
>客户端模式
>
>
>相关资料
>http://www.ruanyifeng.com/blog/2014/05/oauth_2_0.html
>https://cwiki.apache.org/confluence/display/OLTU/OAuth+2.0+Authorization+Server
>
>开放平台可以使用clinet_id和client_secret调用本接口来获取access_token。clinet_id和client_secret由开发平台分发 请妥善保管。服务器 IP 白名单后期添加。
>
>
> ## client_credential 客户端模式 申请 access_token
> POST /oauth2/token
>
|参数名称|描述|类型|是否必填|
|-|-|-|-|
| scope|申请的权限范围,可选项 默认为basic|String|Y|
| client_secret|第三方用户唯一凭证密钥，即app_secret|String|N|
| client_id|第三方用户唯一凭证，即app_id|String|N|
| grant_type|获取access_token填写client_credential|String|N|
>
> ### 返回结果
>
```
{
    "access_token": "948ad78157cf1b96a147b83b589c5d9a",
    "refresh_token": "91671e25b0bf077108fce003de322f56",
    "success": true,
    "token_type": "bearer",
    "expires_in": 3050
}
```
>
> ### 参数说明
>
|名称|描述|
|-|-|
| access_token|获取到的令牌|
| refresh_token|更新令牌|
| expires_in|access_token有效时间 单位 秒|
| token_type|默认返回|
>
>
> ## 更新 access_token
> POST /oauth2/token
>
>
|名称|描述|类型|可为空|
|-|-|-|-|
| grant_type|获取access_token填写refresh_token|String|N|
| scope|申请的权限范围，可选项 默认为basic|String|Y|
| refresh_token|更新令牌|String|N|
>
>
> ### 返回结果
>
```
{
    "access_token": "948ad78157cf1b96a147b83b589c5d9a",
    "refresh_token": "91671e25b0bf077108fce003de322f56",
    "success": true,
    "token_type": "bearer",
    "expires_in": 3050
}
```
>
> ### 参数说明
>
|名称|描述|
|-|-|
| access_token|获取到的凭证|
| refresh_token|刷新凭证|
| expires_in|access_token有效时间 单位 秒|
| token_type|默认返回|
>
>
> ## Scope说明
> 
> scope表示申请的权限范围
>
|名称|描述|
|-|-|
| basic|基本接口默认|
| develop|高级接口|
| root|超级接口|
>
> 默认用户只有basic 权限，如果要访问更高级别接口需要另外申请！
> 错误码说明
> ### 返回格式：
```
{
    "success": false,
    "error": {
        "msg": "INVALID_CLIENT_ID",
        "code": 5,
        "message": "客户端验证失败，如错误的client_id/client_secret。"
    }
}
```
>
> ### 参数说明
>
|名称|描述|
|-|-|
| msg|失败信息|
| code|错误码|
| message|错误详细描述|
>
> ## Code-错误码说明
>
|名称|描述|
|-|-|
| 1|错误|
| 2|服务器错误|
| 3|用户未找到|
| 4|密码错误|
| 5|客户端验证失败，如错误的client_id/client_secret。|
| 6|access_token无效或已过期。|
| 7|缺少授权成功后的回调地址。|
| 8|错误的授权码|
| 9|Content-Type 错误|
| 10|错误的用户名/密码|
| 11|授权类型错误|
| 12|access_token创建失败|
| 13|授权码创建失败|
| 14|权限不足，范围错误|
| 15|refresh_token失效|
| 16|refresh_token创建失败|
