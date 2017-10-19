/** 
 * 包名: package com.qt.sales.common; <br/> 
 * 添加时间: 2016年10月31日 下午7:54:34 <br/> 
 */
package com.compass.utils;

/**
 * 类名: CommonEnums <br/>
 * 作用：TODO(Enum类)<br/>
 * 创建者: zhangyinghui. <br/>
 * 添加时间: 2016年10月31日 下午7:54:34 <br/>
 * 版本： JDK 1.6 SalesPlatform 1.0
 */
public class CommonEnums {

    /**
     * <pre>
     * 【类型】: TerminalChangeType <br/> 
     * 【作用】: 批次类型. <br/>  
     * 【时间】：2016年10月31日 下午7:56:39 <br/> 
     * 【作者】：yinghui zhang <br/>
     * </pre>
     */
    public enum TerminalChangeType {
        
        /**
         * 下发
         */
        send("1"),
        
        /**
         *回拨
         */
        goback("2");

        /**
         * 值
         */
        private String val;

        /**
         * 
         * 【方法名】 : (审核状态). <br/>
         * 【作者】: yinghui zhang .<br/>
         * 【时间】： 2016年11月3日 下午7:17:27 .<br/>
         * 【参数】：@val key值 .<br/>
         * 
         * @param val
         *            .<br/>
         *            <p>
         *            修改记录.<br/>
         *            修改人: yinghui zhang 修改描述： .<br/>
         *            <p/>
         */
        private TerminalChangeType(String val) {
            this.val = val;
        }

        /**
         * 【方法名】    : (获取方法). <br/> 
         * 【作者】: yinghui zhang .<br/>
         * 【时间】： 2016年11月3日 下午7:18:08 .<br/>
         * 【参数】： .<br/>
         * @return .<br/>
         * <p>
         * 修改记录.<br/>
         * 修改人:  yinghui zhang 修改描述： .<br/>
         * <p/>
         */
        public String getVal() {
            return val;
        }
    }
    
    /**
     * <pre>
     * 【类型】: TerminalChangeType <br/> 
     * 【作用】: 批次状态. <br/>  
     * 【时间】：2016年10月31日 下午7:56:39 <br/> 
     * 【作者】：yinghui zhang <br/>
     * </pre>
     */
    public enum BatchState {
        
        /**
         * 0--申请   1--对方认可  2--对方驳回      3---处理完毕  4--处理异常
         */
        apply("0"),approve("1"),deny("2"),workFinish("3"),workError("4");

        /**
         * 值
         */
        private String val;

        private BatchState(String val) {
            this.val = val;
        }
        public String getVal() {
            return val;
        }
    }
    
    /**
     * <pre>
     * 【类型】: AgencyStatus <br/> 
     * 【作用】: 机构状态. <br/>  
     * 【时间】：2017年3月30日 下午2:30:45 <br/> 
     * 【作者】：yinghui zhang <br/> 
     * </pre>
     */
    public enum AgencyStatus {
        /**
         * 1:机构待审核  2:机构审核通过但未实名认证  3:机构审核通过且实名认证通过
         */
        waitAudit("1"), auditPass("2"), realNamePass("3");

        /**
         * 值
         */
        private String val;

        private AgencyStatus(String val) {
            this.val = val;
        }
        public String getVal() {
            return val;
        }
    }
    
    
    
}
