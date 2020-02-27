package com.ybj.utils.constant;

/**
 * 业务相关常量
 * @author caicai.gao
 *
 */
public class BusniessConstants {
    
    /** 一年中月份数 */
    public static final int MOUTH_OF_YEAR = 12;

    /** 分页总记录数*/
    public static final String TOTAL_COUNT = "total";

    
    /**
     * 生命周期状态枚举类
     * @author caicai.gao
     *
     */
    public static enum LifeCycleStateEnum{
        
        // 生命周期状态 （名称，值）
        INWORK("工作中", "InWork"), UNDER_REVIEW("审签中", "UnderReview"),
        RELEASED("已发布", "Released"), ABOLISHED("已废止", "Abolished");
        
        private LifeCycleStateEnum(String name, String value) {
            this.name = name;
            this.value = value;
        }

        private final String name;

        private final String value;

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }
        
    }

    /**
     * 冻结状态枚举类
     * @author caicai.gao
     *
     */
    public static enum StatusEnum{

        // 冻结状态状态 （名称，值）
        NORMAL("正常", 1), FREEZE("冻结", 0);

        private StatusEnum(String name, int value) {
            this.name = name;
            this.value = value;
        }

        private final String name;

        private final int value;

        public String getName() {
            return name;
        }

        public int getValue() {
            return value;
        }

    }
    


}
