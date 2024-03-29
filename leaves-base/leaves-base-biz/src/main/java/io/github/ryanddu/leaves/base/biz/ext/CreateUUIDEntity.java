package io.github.ryanddu.leaves.base.biz.ext;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 带创建字段UUID算法entity基类
 *
 * @author: ryan
 * @date: 2023/3/27 20:52
 **/
@Data
public class CreateUUIDEntity extends SuperUUIDEntity {

	public CreateUUIDEntity() {
	}

	public CreateUUIDEntity(String id) {
		super(id);
	}

	/**
	 * 创建时间
	 */
	@TableField(value = "gmt_create", fill = FieldFill.INSERT)
	private LocalDateTime gmtCreate;

	/**
	 * 创建人id
	 */
	@TableField(value = "create_by", fill = FieldFill.INSERT)
	private String createBy;

	public static final String GMT_CREATE = "gmt_create";

	public static final String CREATE_BY = "create_by";

}
