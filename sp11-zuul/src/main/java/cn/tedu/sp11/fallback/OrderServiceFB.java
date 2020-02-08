package cn.tedu.sp11.fallback;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import cn.tedu.sp01.util.JsonResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderServiceFB implements FallbackProvider {

	@Override
	public String getRoute() {
		return "order-service";
	}

	@Override
	public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
		return response();
	}

	private ClientHttpResponse response() {
		return new ClientHttpResponse() {
			
			@Override
			public HttpHeaders getHeaders() {
				HttpHeaders heads = new HttpHeaders();
				heads.setContentType(MediaType.APPLICATION_JSON);
				return heads;
			}
			
			@Override
			public InputStream getBody() throws IOException {
				log.info("fallback  body");
				String s = JsonResult.err().msg("订单后台服务错误").toString();
				return new ByteArrayInputStream(s.getBytes("UTF-8"));
			}
			
			@Override
			public String getStatusText() throws IOException {
				return HttpStatus.OK.getReasonPhrase();
			}
			
			@Override
			public HttpStatus getStatusCode() throws IOException {
				return HttpStatus.OK;
			}
			
			@Override
			public int getRawStatusCode() throws IOException {
				return HttpStatus.OK.value();
			}
			
			@Override
			public void close() {
			}
		};
	}

}
