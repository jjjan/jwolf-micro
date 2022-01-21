package common;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Description: TODO
 *
 * @author majun
 * @version 1.0
 * @date 2022-01-19 19:44
 */
public abstract class AbstractMultiThreadTask {

    public abstract void taskContent(ThreadPoolTaskExecutor taskExecutor) throws Exception;

    public void doTask() {
        int threadNum = Runtime.getRuntime().availableProcessors();
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(threadNum);
        taskExecutor.setMaxPoolSize(threadNum);
        taskExecutor.setThreadNamePrefix("test");
        taskExecutor.setQueueCapacity(10);
        taskExecutor.setRejectedExecutionHandler((runnable, threadPoolExecutor) -> System.out.println("threadpool拒绝任务"));
        // 等待运行中及已在等待队列的任务完成再关闭
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.initialize();
        try {
            taskContent(taskExecutor);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            taskExecutor.shutdown();
        }
        System.out.println("done>>>>");
    }
}
