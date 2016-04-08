package jp.co.ojt.servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.ojt.logic.WorkListLogic;
import jp.co.ojt.model.Work;

@WebServlet("/WorkList")
public class WorkListServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// 作業リスト取得
		String userName = request.getUserPrincipal().getName();
		Work work = new Work();
		work.setUserName(userName);
		WorkListLogic logic = new WorkListLogic();
		ArrayList<Work> workList = logic.findAllWork(work);
		request.setAttribute("workList", workList);

		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/work/workList.jsp");
		dispatcher.forward(request, response);

	}

}
