package io.github.ryanddu.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * id列表传参基类
 *
 * @author: ryan
 * @date: 2023/3/27 9:49
 **/
@Data
@Schema(description = "id列表")
public class IdListReq<T> extends BaseRequest {

	private static final long serialVersionUID = -2682641171802291323L;

	/**
	 * id列表
	 */
	@NotEmpty(message = "id列表不能为空")
	@Schema(description = "id列表", required = true)
	protected List<T> idList;

}
