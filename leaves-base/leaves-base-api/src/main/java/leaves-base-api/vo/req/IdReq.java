package io.github.ryanddu.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * id传参请求基类
 *
 * @author: ryan
 * @date: 2023/3/27 9:49
 **/
@Data
@Schema(description = "id")
public class IdReq<T> extends BaseRequest {

	private static final long serialVersionUID = 5316719924266480258L;

	/**
	 * id
	 */
	@NotNull(message = "id不能为空")
	@Schema(description = "id", required = true)
	protected T id;

}
