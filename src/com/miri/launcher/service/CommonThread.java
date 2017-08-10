/* 
 * 文件名：CommonThread.java
 * 版权：
 * 描述： 
 * 修改人：
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.miri.launcher.service;

/**
 * 通用线程类，增加了设置停止位的方法
 */
public class CommonThread extends Thread {

    public CommonThread() {
        super();
    }

    public CommonThread(Runnable runnable, String threadName) {
        super(runnable, threadName);
    }

    public CommonThread(Runnable runnable) {
        super(runnable);
    }

    public CommonThread(String threadName) {
        super(threadName);
    }

    public CommonThread(ThreadGroup group, Runnable runnable, String threadName, long stackSize) {
        super(group, runnable, threadName, stackSize);
    }

    public CommonThread(ThreadGroup group, Runnable runnable, String threadName) {
        super(group, runnable, threadName);
    }

    public CommonThread(ThreadGroup group, Runnable runnable) {
        super(group, runnable);
    }

    public CommonThread(ThreadGroup group, String threadName) {
        super(group, threadName);
    }

    private boolean isSuspendExecute = false;

    public boolean isSuspendExecute() {
        return isSuspendExecute;
    }

    public void restoreSuspendExecute() {
        this.isSuspendExecute = false;
    }

    /**
     * 停止执行，用于停止线程循环和耗时的操作
     */
    public void suspendExecute() {
        this.isSuspendExecute = true;
    }

}
