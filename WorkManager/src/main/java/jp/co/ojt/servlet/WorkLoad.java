package jp.co.ojt.servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.ojt.common.exception.BusinessException;
import jp.co.ojt.logic.WorkListLogic;
import jp.co.ojt.model.Work;

public class WorkLoad extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(WorkLoad.class);

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String id = request.getParameter("radio");

		Work inputWork = new Work();
		inputWork.setId(Integer.valueOf(id));
		inputWork.setUserName(request.getUserPrincipal().getName());
		logger.info("★★ラジオボタン情報：{}", id);

		WorkListLogic logic = new WorkListLogic();

		if (request.getAttribute("add") != null) {

			// 選択された行の開始時間を取得
			Date startTime = null;
			try {
				startTime = logic.getStartTime(inputWork);
			} catch (BusinessException e) {
				logger.error("開始時間を取得できませんでした。", e);
			}
			logger.info("★★開始時間：{}", startTime);

			// 開始、終了時間は追加行の開始時間で設定
			inputWork.setStartTime(startTime);
			inputWork.setEndTime(startTime);
			// 挿入
			logic.insertWork(inputWork);

		} else if (request.getAttribute("delete") != null) {

		}

		response.sendRedirect("/WorkManager/WorkList");
	}

}
