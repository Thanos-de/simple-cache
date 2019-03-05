simple-cache...基于spring-aop优雅灵活添加缓存，支持各种数据源灵活配置。

1、实现CacheInvoker接口的put、delete、get接口

2、在接口处添加注解

@Cacheable(expression = "1.id,1.name") 或 @CacheRemove(expression = "1.id,1.name", methodName = "getUser")

还有灵活的编程方式协助 SimpleCache.removeCache(...); SimpleCache.putCache(...);SimpleCache.cacheCondition(...)简单明了
   
3、自定义序列化、异步增加缓存。

4、追求简单、高效。

5、集思广益，不断优化。
   
