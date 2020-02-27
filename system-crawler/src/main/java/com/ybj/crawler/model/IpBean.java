package com.ybj.crawler.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author ybj
 * @since 2020-02-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("TBL_IP_BEAN")
public class IpBean implements Serializable {

    private static final long serialVersionUID=1L;

    private String ipAddress;

    private Integer ipPort;

    private String serverAddress;

    private String anonyType;

    private String protocolType;


}
