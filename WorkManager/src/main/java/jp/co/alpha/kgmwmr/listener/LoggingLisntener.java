/**
 * 
 */
package jp.co.alpha.kgmwmr.listener;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * リクエスト開始終了前後のログ出力用リスナー
 * 
 * @author kigami
 *
 */
@WebListener
public class LoggingLisntener implements ServletRequestListener {

	/**
	 * ロガー
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(LoggingLisntener.class);

	/**
	 * ログ出力しないURIパターン
	 */
	private static final String URI_PATTERN = "/WorkManager/resources";

	/**
	 * リクエスト終了時
	 * 
	 * @see javax.servlet.ServletRequestListener#requestDestroyed(javax.servlet.
	 *      ServletRequestEvent)
	 */
	@Override
	public void requestDestroyed(ServletRequestEvent event) {
		HttpServletRequest request = (HttpServletRequest) event
				.getServletRequest();
		String url = request.getRequestURI();
		if (!url.contains(URI_PATTERN)) {
			// リソース取得時はログ出力しない
			logger.info("===== リクエスト終了 URL:{} =====", url);
		}
	}

	/**
	 * リクエスト開始時
	 * 
	 * @see javax.servlet.ServletRequestListener#requestInitialized(javax.servlet.
	 *      ServletRequestEvent)
	 */
	@Override
	public void requestInitialized(ServletRequestEvent event) {
		HttpServletRequest request = (HttpServletRequest) event
				.getServletRequest();
		String url = request.getRequestURI();
		if (!url.contains(URI_PATTERN)) {
			// リソース取得時はログ出力しない
			logger.info("===== リクエスト開始 URL:{} =====", url);
		}
	}
}
