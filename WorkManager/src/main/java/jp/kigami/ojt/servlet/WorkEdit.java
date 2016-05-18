package jp.kigami.ojt.servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.kigami.ojt.common.exception.BusinessException;
import jp.kigami.ojt.common.util.ConvertToModelUtils;
import jp.kigami.ojt.common.util.DateUtils;
import jp.kigami.ojt.logic.WorkLogic;
import jp.kigami.ojt.model.Work;

@WebServlet("/WorkEdit")
public class WorkEdit extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory
			.getLogger(WorkEdit.class);

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

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

		WorkLogic logic = new WorkLogic();

		logic.updateWork(inputWork);
		// TODO 作業リストへ戻る
		doGet(request, response);
	}
}