package LoginWithAdminEmployeeCrud;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
@WebServlet("/")
public class AdminEmployeeControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	AdminEmployeeDao adminEmployeeDao;
	public void init() {
		adminEmployeeDao = new AdminEmployeeDao();
	}
	public AdminEmployeeControllerServlet() {
		super();
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getServletPath();
		try {
			if (action.equals("/checkType")) {
				checkEmpType(request, response);
			} else if (action.equals("/register")) {
				addNewEmpForm(request, response);
			} else if (action.equals("/insert")) {
				addNewEmp(request, response);
			} else if (action.equals("/list")) {
				ListOfAllEmps(request, response);
			} else if (action.equals("/edit")) {
				editEmpForm(request, response);
			} else if (action.equals("/update")) {
				update(request, response);
			} else if (action.equals("/delete")) {
				delete(request, response);
			} else {
				loginForm(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void loginForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/views/employeeLoginForm.jsp").forward(request, response);
	}
	private void checkEmpType(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		String empLoginEmail = request.getParameter("empLoginEmail");
		String empLoginPassword = request.getParameter("empLoginPassword");
		Employee showingEmpData = adminEmployeeDao.checkEmployee(empLoginEmail, empLoginPassword);
		
		if (showingEmpData.getId() == 0) {
			out.println("<h1>Enter correct Data or Ask Admin to Register</h1>");
//			response.sendRedirect("employeeLoginForm");
			request.getRequestDispatcher("/WEB-INF/views/employeeLoginForm.jsp").forward(request, response);
		}
		if (showingEmpData.getEmployeeType().equals("Admin")) {
			session.setAttribute("id", showingEmpData.getId());
			session.setAttribute("email", showingEmpData.getEmail());
			session.setAttribute("password", showingEmpData.getPassword());
			session.setAttribute("employeeType", showingEmpData.getEmployeeType());
			response.sendRedirect("ListOfAllEmployee");
		} else {
			session.setAttribute("id", showingEmpData.getId());
			session.setAttribute("email", showingEmpData.getEmail());
			session.setAttribute("password", showingEmpData.getPassword());
			session.setAttribute("employeeType", showingEmpData.getEmployeeType());
			response.sendRedirect("ShowGeneralEmployee");
		}
	}
	private void addNewEmpForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/views/accountLoginRegister.jsp").forward(request, response);
	}
	private void addNewEmp(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String name = request.getParameter("empName");
		String email = request.getParameter("empEmail");
		String password = request.getParameter("empPassword");
		String employeeType = request.getParameter("empType");
		Employee employee = new Employee();
		employee.setName(name);
		employee.setEmail(email);
		employee.setPassword(password);
		employee.setEmployeeType(employeeType);
		try {
			int status = adminEmployeeDao.save(employee);
			if (status > 0) {
				out.println("<h1>Employee Registerd Successfully!!</h1>");
				request.getRequestDispatcher("/WEB-INF/views/accountLoginRegister.jsp").include(request, response);
			} else {
				out.println("<h3>The Employee is not saved to List</h3>");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void ListOfAllEmps(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendRedirect("ListOfAllEmployee");
	}
	private void editEmpForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		Employee getAnEmployee = adminEmployeeDao.getOneEmployee(id);
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/views/employeeEditForm.jsp");
		request.setAttribute("employeeEdit", getAnEmployee);
		requestDispatcher.forward(request, response);
	}
	private void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		int id = Integer.parseInt(request.getParameter("empId"));
		String newName = request.getParameter("newEmpName");
		String newEmail = request.getParameter("newEmpEmail");
		String newPassword = request.getParameter("newEmpPassword");
		String newEmployeeType = request.getParameter("newEmpType");
		Employee newEmpDetail = new Employee();
		newEmpDetail.setId(id);
		newEmpDetail.setName(newName);
		newEmpDetail.setEmail(newEmail);
		newEmpDetail.setPassword(newPassword);
		newEmpDetail.setEmployeeType(newEmployeeType);
		int status = adminEmployeeDao.update(newEmpDetail);
		if (status > 0) {
			out.println("<h1>Employee Updated Successfully!!</h1>");
			response.sendRedirect("ListOfAllEmployee");
		} else {
			out.println("<h3>The Employee is not updated</h3>");
		}
	}
	private void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		int id = Integer.parseInt(request.getParameter("id"));
		try {
			int status = adminEmployeeDao.delete(id);
			if (status > 0) {
				out.println("<h1>Employee Deleted Successfully!!</h1>");
				response.sendRedirect("ListOfAllEmployee");
			} else {
				out.println("<h3>The Employee is not in List</h3>");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.sendRedirect("ListOfAllEmployee");
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
