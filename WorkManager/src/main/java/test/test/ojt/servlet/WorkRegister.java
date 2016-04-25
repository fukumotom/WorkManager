package test.test.ojt.servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.test.ojt.logic.WorkRegisterLogic;
import test.test.ojt.model.Work;

@WebServlet("/WorkRegister")
public class WorkRegister extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory
			.getLogger(WorkRegister.class);

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String userName = request.getUserPrincipal().getName();
		Work inputWork = new Work();
		inputWork.setUserName(userName);
		inputWork.setWorkDate(LocalDate.now());
		logger.info("ユーザ名:{}", userName);

		WorkRegisterLogic logic = new WorkRegisterLogic();

		List<Work> workList = logic.findWorking(inputWork);
		Work work;
		String state;
		if (workList.size() == 0) {
			logger.info("仕掛処理なし");
			work = null;
			state = null;
		} else if (workList.size() == 1) {
			logger.info("仕掛処理あり");
			work = workList.get(0);
			state = "working";
		} else {
			logger.warn("仕掛処理が複数あります。");
			request.setAttribute("errMsg", "仕掛処理が複数あります。");
			work = workList.get(0);
			state = "working";
		}
		if (state != null) {
			request.setAttribute("state", state);
		}
		request.setAttribute("working", work);
		RequestDispatcher dispatcher = request
				.getRequestDispatcher("/WEB-INF/jsp/work/workRegistForm.jsp");

		dispatcher.forward(request, response);
	}
}
