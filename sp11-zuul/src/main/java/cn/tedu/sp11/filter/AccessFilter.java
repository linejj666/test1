package cn.tedu.sp11.filter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import cn.tedu.sp01.util.JsonResult;

@Component
public class AccessFilter extends ZuulFilter {

	@Override
	public boolean shouldFilter() {
		//对指定的serviceid过滤,如果要过滤所有服务,直接返回true
		RequestContext ctx = RequestContext.getCurrentContext();//上下文对象
		String serviceId = (String) ctx.get(FilterConstants.SERVICE_ID_KEY);
		if(serviceId.equals("item-service")) {
			return true;
		}
		return false;
	}

	@Override
	public Object run() throws ZuulException {
		//过滤代码
		RequestContext ctx = RequestContext.getCurrentContext();//上下文对象
		HttpServletRequest request = ctx.getRequest();
		String token = request.getParameter("token");
		if(token == null || token.length() == 0) {
			//阻止请求到后台服务
			ctx.setSendZuulResponse(false);
			//向客户端响应
			ctx.setResponseStatusCode(200);
			ctx.setResponseBody(JsonResult.err("Can not access item-service").code(JsonResult.NOT_LOGIN).toString());
		}
		//zuul过滤器返回的数据设计为以后拓展使用
		//目前该返回值没有被使用
		return null;
	}

	@Override
	public String filterType() {
		return FilterConstants.PRE_TYPE;
	}

	@Override
	public int filterOrder() {
		//过滤顺序  要大于5,才能得到serviceid(在5才保存,5之后都可以)
		return FilterConstants.PRE_DECORATION_FILTER_ORDER+1;//直接写数字也可以
	}

}
