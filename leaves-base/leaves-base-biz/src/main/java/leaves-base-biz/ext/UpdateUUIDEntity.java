package io.github.ryanddu.ext;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 带创建和修改字段UUID算法entity基类
 *
 * @author: ryan
 * @date: 2023/3/27 20:57
 **/
@Data
public class UpdateUUIDEntity extends CreateUUIDEntity {

	public UpdateUUIDEntity() {
	}

	public UpdateUUIDEntity(String id) {
		super(id);
	}

	/**
	 * 更新时间
	 */
	@TableField(value = "gmt_update", fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime gmtUpdate;

	/**
	 * 修改人id
	 */
	@TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
	private String updateBy;

	public static final String GMT_UPDATE = "gmt_update";

	public static final String UPDATE_BY = "update_by";

}
