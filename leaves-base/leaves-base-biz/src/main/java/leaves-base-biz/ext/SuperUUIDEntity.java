package io.github.ryanddu.ext;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * uuid算法entity基类
 *
 * @author: ryan
 * @date: 2023/3/27 20:48
 **/
@Data
public class SuperUUIDEntity implements SuperEntity {

	public SuperUUIDEntity() {
	}

	public SuperUUIDEntity(String id) {
		this.id = id;
	}

	/**
	 * 主键
	 */
	@TableId(value = "id", type = IdType.ASSIGN_UUID)
	private String id;

	@Override
	public void setId(String id) {
		this.id = id;
	}

}
