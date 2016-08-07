package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


import entity.Cost;
import util.DBUtil;

public class CostDao {
	public List<Cost> findAll() {
		try {
			Connection conn = DBUtil.getConnection();
			String sql = "select * from cost_lzm order by cost_id";
			Statement state = conn.createStatement();
			ResultSet rs = state.executeQuery(sql);
			List<Cost> list = new ArrayList<Cost>();
			while(rs.next()){
				Cost c = creatCost(rs);
				list.add(c);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("查询资费失败",e);
		} finally{
			DBUtil.closeConnection();
		}
	}
	
	public boolean save(Cost c) {
		String name = c.getName();
		String costType = c.getCostType();
		Integer baseDuration = c.getBaseDuration();
		Double baseCost = c.getBaseCost();
		Double unitCost = c.getUnitCost();
		String descr = c.getDescr();
		try {
			Connection conn = DBUtil.getConnection();
			String sql = "insert into cost_lzm(name,base_duration,base_cost,"
					+ "unit_cost,status,descr,creatime,startime,cost_type) "
					+ "values (?,?,?,?,'1',?,DEFAULT,DEFAULT,?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, name);
			ps.setObject(2, baseDuration);
			ps.setObject(3, baseCost);
			ps.setObject(4, unitCost);
			ps.setString(5, descr);
			ps.setString(6, costType);
			int n = ps.executeUpdate();
			if(n>0){
				System.out.println("保存成功");
				return true;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			DBUtil.closeConnection();
		}
		return false;
	}
	
	public Cost findById(int id) {
		try {
			Connection conn = DBUtil.getConnection();
			String sql = "select * from cost_lzm where cost_id=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				return creatCost(rs);
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
		return null;
	}

	private Cost creatCost(ResultSet rs) throws SQLException {
		Cost c = new Cost();
		c.setCostId(rs.getInt("cost_id"));
		c.setName(rs.getString("name"));
		c.setBaseDuration(rs.getInt("base_duration"));
		c.setBaseCost(rs.getDouble("base_cost"));
		c.setUnitCost(rs.getDouble("unit_cost"));
		c.setStatus(rs.getString("status"));
		c.setDescr(rs.getString("descr"));
		c.setCreatime(rs.getTimestamp("creatime"));
		String startime = rs.getString("startime");
		//在数据库连接url后面加上zeroDateTimeBehavior=convertToNull
		if(startime.equals("0000-00-00 00:00:00")){
			c.setStartime(null);
		}else{
			Timestamp time = Timestamp.valueOf(startime);
			c.setStartime(time);
		}
		c.setCostType(rs.getString("cost_type"));
		return c;
	}
	public void update(Cost c){
		Integer costId = c.getCostId();
		Double baseCost = c.getBaseCost();
		Integer baseDuration = c.getBaseDuration();
		Double unitCost = c.getUnitCost();
		String costType = c.getCostType();
		String descr = c.getDescr();
		String name = c.getName();
		try {
			Connection conn = DBUtil.getConnection();
			String sql = "update cost_lzm set name=?,cost_Type=?,base_Duration=?"
					+ ",base_Cost=?,unit_Cost=?,descr=? where cost_Id=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, name);
			ps.setString(2, costType);
			ps.setObject(3, baseDuration);
			ps.setObject(4, baseCost);
			ps.setObject(5, unitCost);
			ps.setString(6, descr);
			ps.setInt(7, costId);
			int n = ps.executeUpdate();
			if(n>0){
				System.out.println("修改成功");
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnection();
		}
	}
	
	public void deleteById(int id) {
		try {
			Connection conn = DBUtil.getConnection();
			String sql = "delete from cost_lzm where cost_id=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			int n = ps.executeUpdate();
			if(n>0){
				System.out.println("删除成功");
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
	
	public static void main(String[] args) {
		CostDao dao = new CostDao();
		List<Cost> list = dao.findAll();
		for(Cost c:list){
			System.out.println(c.getCostId()+","+c.getName()+","+c.getDescr());
		}
	}
}
