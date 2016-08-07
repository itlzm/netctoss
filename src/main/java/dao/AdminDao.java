package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.cj.api.mysqla.io.NativeProtocol.IntegerDataType;

import entity.Admin;
import util.DBUtil;

public class AdminDao {
	public Admin findByCode(String code){
		try {
			Connection conn = DBUtil.getConnection();
			String sql = "select * from admin_info_lzm where admin_code=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, code);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				return createAdmin(rs);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnection();
		}
		return null;
	}
	public void updateById(Admin a){
		try {
			Integer adminId = a.getAdminId();
			String name = a.getName();
			String telephone = a.getTelephone();
			String email = a.getEmail();
			Connection conn = DBUtil.getConnection();
			String sql = "update admin_info_lzm set name=?,telephone=?,email=? where admin_id=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, name);
			ps.setString(2, telephone);
			ps.setString(3, email);
			ps.setInt(4, adminId);
			int n = ps.executeUpdate();
			if(n>0){
				System.out.println("user修改成功");
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.closeConnection();
		}
	}
	public void updatePwdByAdminCode(String adminCode,String password) {
		try {
			Connection conn = DBUtil.getConnection();
			String sql = "update admin_info_lzm set password=? where admin_code=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, password);
			ps.setString(2, adminCode);
			int n = ps.executeUpdate();
			if(n>0){
				System.out.println("pwd修改成功");
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.closeConnection();
		}
		
	}

	private Admin createAdmin(ResultSet rs) throws SQLException {
		Admin admin = new Admin();
		admin.setAdminId(rs.getInt("admin_id"));
		admin.setAdminCode(rs.getString("admin_code"));
		admin.setPassword(rs.getString("password"));
		admin.setName(rs.getString("name"));
		admin.setTelephone(rs.getString("telephone"));
		admin.setEmail(rs.getString("email"));
		admin.setEnrolldate(rs.getTimestamp("enrolldate"));
		return admin;
	}
//	public static void main(String[] args) {
//	AdminDao dao = new AdminDao();
//	Admin admin = dao.findByCode("caocao");
//	System.out.println(admin.getAdminCode()+","+admin.getEmail()+","+admin.getName()+","+admin.getEnrolldate());
//}
}
