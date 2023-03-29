package com.github.ryanddu.ext;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 雪花算法entity基类
 *
 * @author: ryan
 * @date: 2023/3/27 20:50
 **/
@Data
public class SuperSnowFlakeEntity implements SuperEntity {

	public SuperSnowFlakeEntity() {
	}

	public SuperSnowFlakeEntity(Long id) {
		this.id = id;
	}

	/**
	 * 主键
	 */
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;

	@Override
	public void setId(Long id) {
		this.id = id;
	}

}
