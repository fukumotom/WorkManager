package jp.kigami.ojt.servlet;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.kigami.ojt.common.exception.SystemException;
import jp.kigami.ojt.common.util.ConvertToModelUtils;
import jp.kigami.ojt.common.util.DateUtils;
import jp.kigami.ojt.logic.WorkLogic;
import jp.kigami.ojt.model.Work;

@WebServlet("/WorkEdit")
public class WorkEditServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory
			.getLogger(WorkEditServlet.class);

	@Override
	public void doPost(HttpServletRequest request,
			HttpServletResponse response) {

		String userName = request.getUserPrincipal().getName();
		String id = request.getParameter("id");
		Work inputWork = new Work();
		inputWork.setUserName(userName);
		inputWork.setId(ConvertToModelUtils.convertInt(id));

		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String contents = (String) request.getParameter("contents");
		String note = (String) request.getParameter("note");
		logger.info("入力値：開始時間[{}] 終了時間[{}] 作業内容[{}] 備考[{}]", startTime, endTime,
				contents, note);

		inputWork.setStartTime(DateUtils.getFomatTime(startTime));
		inputWork.setEndTime(DateUtils.getFomatTime(endTime));
		inputWork.setContents(contents);
		inputWork.setNote(note);

		// 作業終了時間が入力された場合、作業時間の再計算
		if (!endTime.isEmpty()) {
			WorkHelper helper = new WorkHelper();
			helper.calcWorkTime(inputWork);
		}

		WorkLogic logic = new WorkLogic();

		logic.updateWork(inputWork);

		// 作業リストへ戻る
		try {
			response.sendRedirect("/WorkManager/WorkList");
		} catch (IOException e) {
			throw new SystemException("リダイレクト失敗", e);
		}
	}
}