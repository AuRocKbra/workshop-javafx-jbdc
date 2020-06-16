package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DBException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao{
	
private Connection conexao = null;
	
	public DepartmentDaoJDBC(Connection com) {
		this.conexao = com;
	}

	@Override
	public void insert(Department obj) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("INSERT INTO department (Name) "
					+ "VALUES(?);",Statement.RETURN_GENERATED_KEYS);
			st.setString(1, obj.getNome());
			int linhasAtualizadas = st.executeUpdate();
			if(linhasAtualizadas>0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					obj.setId(rs.getInt(1));
				}
				DB.closeResultSet(rs);
			}
		}catch(SQLException e) {
			throw new DBException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Department obj) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("UPDATE department SET Name = ? "
					+ "WHERE Id=?;");
			st.setString(1, obj.getNome());
			st.setInt(2, obj.getId());
			st.executeUpdate();
		}catch(SQLException e) {
			throw new DBException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conexao.prepareStatement("DELETE FROM department WHERE Id=?;");
			st.setInt(1, id);
			st.executeUpdate();
		}catch(SQLException e) {
			throw new DBException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Department findById(Integer id) {
		PreparedStatement st=null;
		ResultSet rs = null;
		try {
			st = conexao.prepareStatement("SELECT * FROM department "
					+ "WHERE Id = ?;");
			st.setInt(1, id);
			rs= st.executeQuery();
			if(rs.next()) {
				Department dep = instanciaDapartamento(rs);
				return dep;
			}
			else {
				throw new DBException("Departamento não encontrado!");
			}
		}catch(SQLException e) {
			throw new DBException(e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	@Override
	public List<Department> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st=conexao.prepareStatement("SELECT * FROM department;");
			rs=st.executeQuery();
			List<Department> lista = new ArrayList<>();
			while(rs.next()) {
				Department dep = instanciaDapartamento(rs);
				lista.add(dep);
			}
			return lista;
		}catch(SQLException e) {
			throw new DBException(e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	private Department instanciaDapartamento(ResultSet rs) throws SQLException {
		Department dp = new Department();
		dp.setId(rs.getInt("Id"));
		dp.setNome(rs.getString("Name"));
		return dp;
	}
}
