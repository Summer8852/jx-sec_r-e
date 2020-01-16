package com.fantacg.common.constant;

/**
 * @author DUPENGFEI
 * MQ key值
 */

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname MQConstant MQ key值
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
public class MQConstant {

    private MQConstant() {
        throw new IllegalStateException("MQConstant class");
    }

    /******************************* 清空购物车 **********************************/
    /**
     * 任务队列名称
     * cart.deleteQueue
     */
    public static final String CART_QUEUE_NAME = "jx.cart.cartQueue";

    /**
     * 任务交换机名称
     */
    public static final String CART_EXCHANGE_NAME = "jx.cart.cartExchange";

    /**
     * 任务交换机与任务队列绑定值
     */
    public static final String CART_ROUTE_KEY = "jx.cart.deleteKey";


    /**
     * 死信队列名称
     */
    public static final String CART_DEAD_QUEUE_NAME = "jx.cart.dead.cartQueue";

    /**
     * 死信交换器名称
     */
    public static final String CART_DEAD_EXCHANGE_NAME = "jx.cart.dead.cartExchange";


    /******************************订单自动取消********************************/
    /**
     * 任务队列名称
     */
    public static final String ORDER_QUEUE_NAME = "jx.order.orderQueue";

    /**
     * 任务交换机名称
     */
    public static final String ORDER_EXCHANGE_NAME = "jx.order.orderExchange";

    /**
     * 任务交换机与任务队列绑定值
     */
    public static final String ORDER_ROUTE_KEY = "jx.order.routeKey";

    /**
     * 死信队列名称
     */
    public static final String ORDER_DEAD_QUEUE_NAME = "jx.order.dead.orderQueue";

    /**
     * 死信交换器名称
     */
    public static final String ORDER_DEAD_EXCHANGE_NAME = "jx.order.dead.orderExchange";

/******************************删减播放次数********************************/

    /**
     * 任务队列名称
     */
    public static final String VIDEO_QUEUE_NAME = "jx.video.videoQueue";

    /**
     * 任务交换机名称
     */
    public static final String VIDEO_EXCHANGE_NAME = "jx.video.videoExchange";

    /**
     * 任务交换机与任务队列绑定值
     */
    public static final String VIDEO_ROUTE_KEY = "jx.video.routeKey";

    /**
     * 死信队列名称
     */
    public static final String VIDEO_DEAD_QUEUE_NAME = "jx.video.dead.videoQueue";

    /**
     * 死信交换器名称
     */
    public static final String VIDEO_DEAD_EXCHANGE_NAME = "jx.video.dead.videoExchange";


    /******************************上传图片********************************/

    /**
     * 任务队列名称
     */
    public static final String UPLOAD_QUEUE_NAME = "jx.upload.uploadQueue";

    /**
     * 任务交换机名称
     */
    public static final String UPLOAD_EXCHANGE_NAME = "jx.upload.uploadExchange";

    /**
     * 任务交换机与任务队列绑定值
     */
    public static final String UPLOAD_ROUTE_KEY = "jx.upload.routeKey";

    /**
     * 死信队列名称
     */
    public static final String UPLOAD_DEAD_QUEUE_NAME = "jx.upload.dead.uploadQueue";

    /**
     * 死信交换器名称
     */
    public static final String UPLOAD_DEAD_EXCHANGE_NAME = "jx.upload.dead.uploadExchange";


    /******************************添加实人信息********************************/

    /**
     * 任务队列名称
     */
    public static final String WORKER_QUEUE_NAME = "jx.worker.workerQueue";

    /**
     * 任务交换机名称
     */
    public static final String WORKER_EXCHANGE_NAME = "jx.worker.workerExchange";

    /**
     * 任务交换机与任务队列绑定值
     */
    public static final String WORKER_ROUTE_KEY = "jx.worker.routeKey";

    /**
     * 死信队列名称
     */
    public static final String WORKER_DEAD_QUEUE_NAME = "jx.worker.dead.workerQueue";

    /**
     * 死信交换器名称
     */
    public static final String WORKER_DEAD_EXCHANGE_NAME = "jx.worker.dead.workerExchange";


    /******************************添加班组人员信息********************************/

    /**
     * 任务队列名称
     */
    public static final String PROJECTWORKER_QUEUE_NAME = "jx.projectWorker.projectWorkerQueue";

    /**
     * 任务交换机名称
     */
    public static final String PROJECTWORKER_EXCHANGE_NAME = "jx.projectWorker.projectWorkerExchange";

    /**
     * 任务交换机与任务队列绑定值
     */
    public static final String PROJECTWORKER_ROUTE_KEY = "jx.projectWorker.routeKey";

    /**
     * 死信队列名称
     */
    public static final String PROJECTWORKER_DEAD_QUEUE_NAME = "jx.projectWorker.dead.projectWorkerQueue";

    /**
     * 死信交换器名称
     */
    public static final String PROJECTWORKER_DEAD_EXCHANGE_NAME = "jx.projectWorker.dead.projectWorkerExchange";


}
