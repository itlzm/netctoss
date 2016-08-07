package web;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import dao.AdminDao;
import dao.CostDao;
import entity.Admin;
import entity.Cost;
import util.ImageUtil;

public class MainServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String path = req.getServletPath();
		if("/findCost.do".equals(path)){
			//查询所有资费
			findCost(req,res);
		} else if("/toAddCost.do".equals(path)){
			//打开增加资费页面
			toAddCost(req, res);
		} else if("/addCost.do".equals(path)){
			//向数据库保存资费
			addCost(req, res);
		} else if("/toUpdateCost.do".equals(path)){
			toUpdateCost(req,res);
		} else if("/updateCost.do".equals(path)){
			updateCost(req,res);
		} else if("/detailCost.do".equals(path)){
			detailCost(req,res);
		} else if("/deleteCost.do".equals(path)){
			deleteCost(req,res);
		} else if("/toLogin.do".equals(path)){
			toLogin(req,res);
		} else if("/login.do".equals(path)){
			login(req,res);
		} else if("/toIndex.do".equals(path)){
			toIndex(req, res);
		} else if("/logout.do".equals(path)){
			logout(req,res);
		} else if("/createImg.do".equals(path)){
			createImg(req,res);
		} else if("/toUserInfo.do".equals(path)){
			toUserInfo(req,res);
		} else if("/userInfo.do".equals(path)){
			userInfo(req, res);
		} else if("/updateUserInfo.do".equals(path)){
			updateUserInfo(req,res);
		} else if("/toModifyUserPwd.do".equals(path)){
			toModifyUserPwd(req,res);
		} else if("/modifyUserPwd.do".equals(path)){
			modifyUserPwd(req,res);
		} else if("/checkCode.do".equals(path)){
			checkCode(req,res);
		}
		
		else {
			throw new RuntimeException("无效的访问路径。");
		}
	}
	//使用ajax异步验证验证码是否正确
	protected void checkCode(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html;charset=utf-8");
		PrintWriter out = res.getWriter();
		HttpSession session = req.getSession();
		String imgcode = (String)session.getAttribute("imgcode");
		String code = req.getParameter("codeNum");
		if(code==null || !code.equalsIgnoreCase(imgcode) || code.equals("")){
			out.println("验证码错误");
		}else{
			out.println("验证码正确");
		}
	}

	protected void findCost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		//查询所有的资费
		CostDao dao = new CostDao();
		List<Cost> list = dao.findAll();
		//转发到查询页面
		//当前：/netctoss/findCost.do
		//目标：/webapp/WEB-INF/cost/find.jsp
		//部署后：/netctoss/WEB-INF/cost/find.jsp
		req.setAttribute("costs", list);
		req.getRequestDispatcher("WEB-INF/cost/find.jsp").forward(req, res);
	}
	protected void toAddCost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		//转发到增加页面
		//当前：/netctoss/toAddCost.do
		//目标：/webapp/WEB-INF/cost/add.jsp
		//部署后：/netctoss/WEB-INF/cost/add.jsp
		req.getRequestDispatcher("WEB-INF/cost/add.jsp").forward(req, res);
	}
	protected void addCost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		String name = req.getParameter("name");
		String costType = req.getParameter("radFeeType");
		String descr = req.getParameter("descr");
		Cost c = new Cost();
		c.setName(name);
		c.setCostType(costType);
		c.setDescr(descr);
		String baseDuration = req.getParameter("baseDuration");
		if(baseDuration!=null && !baseDuration.equals("")){
			c.setBaseDuration(new Integer(baseDuration));
		}
		String baseCost = req.getParameter("baseCost");
		if(baseCost!=null && !baseCost.equals("")){
			c.setBaseCost(new Double(baseCost));
		}
		String unitCost = req.getParameter("unitCost");
		if(unitCost!=null && !unitCost.equals("")){
			c.setUnitCost(new Double(unitCost));
		}
		CostDao dao = new CostDao();
		if(dao.save(c)){
			res.sendRedirect("findCost.do");
		}
	}
	protected void toUpdateCost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		Integer costId = Integer.parseInt(req.getParameter("cost_id"));
		CostDao dao = new CostDao();
		Cost cost = dao.findById(costId);
		req.setAttribute("cost", cost);
		req.getRequestDispatcher("WEB-INF/cost/update.jsp").forward(req, res);
	}
	private void updateCost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		Integer costId = Integer.parseInt(req.getParameter("costId"));
		String name = req.getParameter("name");
		String costType = req.getParameter("costType");
		String baseDuration = req.getParameter("baseDuration");
		String baseCost = req.getParameter("baseCost");
		String unitCost = req.getParameter("unitCost");
		String descr = req.getParameter("descr");
		Cost c = new Cost();
		c.setName(name);
		c.setCostType(costType);
		c.setDescr(descr);
		c.setCostId(costId);
		if(baseDuration!=null && !baseDuration.equals("")){
			c.setBaseDuration(new Integer(baseDuration));
		}
		if(baseCost!=null && !baseCost.equals("")){
			c.setBaseCost(new Double(baseCost));
		}
		if(unitCost!=null && !unitCost.equals("")){
			c.setUnitCost(new Double(unitCost));
		}
		CostDao dao = new CostDao();
		dao.update(c);
		res.sendRedirect("findCost.do");
	}
	protected void detailCost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		Integer costId = Integer.parseInt(req.getParameter("cost_id"));
		CostDao dao = new CostDao();
		Cost cost = dao.findById(costId);
		req.setAttribute("cost", cost);
		req.getRequestDispatcher("WEB-INF/cost/detail.jsp").forward(req, res);
	}
	protected void deleteCost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String id = req.getParameter("cost_id");
		CostDao dao = new CostDao();
		dao.deleteById(new Integer(id));
		res.sendRedirect("findCost.do");
	}
	protected void toLogin(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.getRequestDispatcher("WEB-INF/main/login.jsp").forward(req, res);
	}
	protected void login(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String user = req.getParameter("user");
		String pwd = req.getParameter("pwd");
		String code = req.getParameter("code");
		AdminDao dao = new AdminDao();
		Admin admin = dao.findByCode(user);
		HttpSession session = req.getSession();
		String imgcode = (String)session.getAttribute("imgcode");
		if(code==null || !code.equalsIgnoreCase(imgcode) || code.equals("")){
			req.setAttribute("erro", "验证码错误");
			req.getRequestDispatcher("WEB-INF/main/login.jsp").forward(req,res);
			return;
		}
		if(admin==null){
			req.setAttribute("erro", "账号错误");
			req.getRequestDispatcher("WEB-INF/main/login.jsp").forward(req, res);
		}else if(!admin.getPassword().equals(pwd)) {
			req.setAttribute("erro", "密码错误");
			req.getRequestDispatcher("WEB-INF/main/login.jsp").forward(req, res);
		}else {
			
			session.setAttribute("user", user);
			res.sendRedirect("toIndex.do");
		}
	}
	protected void toIndex(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.getRequestDispatcher("WEB-INF/main/index.jsp").forward(req, res);
	}
	protected void logout(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.getSession().invalidate();
		res.sendRedirect("toLogin.do");
	}
	protected void createImg(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		Object[] objs = ImageUtil.createImage();
		HttpSession session = req.getSession();
		session.setAttribute("imgcode", objs[0]);
		BufferedImage image = (BufferedImage)objs[1];
		res.setContentType("image/png");
		OutputStream out = res.getOutputStream();
		ImageIO.write(image, "png", out);
		out.close();
	}
	protected void toUserInfo(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession();
		String user = (String)session.getAttribute("user");
		AdminDao dao = new AdminDao();
		Admin admin = dao.findByCode(user);
		req.setAttribute("admin", admin);
		req.getRequestDispatcher("userInfo.do").forward(req, res);
	}
	protected void userInfo(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.getRequestDispatcher("WEB-INF/user/info.jsp").forward(req,res);
	}
	protected void updateUserInfo(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String name = req.getParameter("name");
		String telephone = req.getParameter("telephone");
		String email = req.getParameter("email");
		String adminId = req.getParameter("adminId");
		Admin a = new Admin();
		if(name!=null && !name.equals("")){
			a.setName(name);
		}
		a.setTelephone(telephone);
		a.setEmail(email);
		a.setAdminId(new Integer(adminId));
		AdminDao dao = new AdminDao();
		dao.updateById(a);
		res.sendRedirect("toIndex.do");
	}
	protected void toModifyUserPwd(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.getRequestDispatcher("WEB-INF/user/modifyPwd.jsp").forward(req, res);
	}
	protected void modifyUserPwd(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession();
		String adminCode = (String)session.getAttribute("user");
		String oldPassword = req.getParameter("oldPassword");
		String newPassword = req.getParameter("newPassword");
		AdminDao dao = new AdminDao();
		String pwd = dao.findByCode(adminCode).getPassword();
		if(oldPassword.equals(pwd)){
			dao.updatePwdByAdminCode(adminCode,newPassword);
			res.sendRedirect("toLogin.do");
		}
	}
}
