package jp.co.alpha.kgmwmr.common.util;

import java.util.concurrent.atomic.AtomicInteger;

public class ThreadIdentifier {

	/**
	 * スレッドごとの識別用スレッドID
	 */
	private static final AtomicInteger nextId = new AtomicInteger(0);

	/**
	 * スレッドごとの識別用スレッドIDの生成
	 */
	private static final ThreadLocal<Integer> threadId = new ThreadLocal<Integer>() {
		@Override
		protected Integer initialValue() {
			return nextId.getAndIncrement();
		}
	};

	/**
	 * スレッドIDの取得
	 * 
	 * @return スレッドID
	 */
	public static int getThreadId() {
		return threadId.get();
	}

	/**
	 * スレッド破棄
	 */
	public static void remove() {
		threadId.remove();
	}

}
