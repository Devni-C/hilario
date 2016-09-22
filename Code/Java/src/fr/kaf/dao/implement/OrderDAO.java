package fr.kaf.dao.implement;

import java.sql.*;
import java.util.ArrayList;

import fr.kaf.bean.Order;
import fr.kaf.dao.DAO;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;

public class OrderDAO extends DAO<Order> {

	public OrderDAO(Connection conn) {
		super(conn);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean create(Order order) throws SQLException {
		PreparedStatement createQuery = this.connect.prepareStatement("INSERT INTO commande(date,prix_negociee,identifiant_vendeur) VALUES(?,?,?);");
		createQuery.setDate(1, (Date) order.getDate());
		createQuery.setInt(2, order.getRealPrice());
		createQuery.setInt(3, order.getSeller().getId());
		return createQuery.execute();
	}

	@Override
	public boolean delete(Order order) throws SQLException {
		PreparedStatement deleteQuery = this.connect.prepareStatement("DELETE FROM commande WHERE identifiant= ?;");
		deleteQuery.setInt(1, order.getId());
		return deleteQuery.execute();
	} /** Mettre un trigger pour set 0 ON Update
	 * @throws SQLException **/

	@Override
	public boolean update(Order order) throws SQLException {
		PreparedStatement updateQuery = this.connect.prepareStatement("UPDATE commande SET date = ?,prix_negociee = ? ,identifiant_vendeur=? WHERE identifiant = ?;");
		updateQuery.setDate(1, (Date) order.getDate());
		updateQuery.setInt(2, order.getRealPrice());
		updateQuery.setInt(3, order.getSeller().getId());
		updateQuery.setInt(4, order.getId());
		return updateQuery.execute();
	}
	
	public SimpleObjectProperty<Order> find(Order ord) throws SQLException{
		PreparedStatement retrieveQuery = this.connect.prepareStatement("SELECT * FROM commande WHERE identifiant = ?;");
		retrieveQuery.setInt(1, ord.getId());
		ResultSet result = retrieveQuery.executeQuery();
		if(result.first())
			return new SimpleObjectProperty<Order>(new Order(ord.getId(),result.getDate(2),result.getInt(3),result.getInt(4)));
		return null;
	}

	public SimpleListProperty<Order> findAll() throws SQLException{
		PreparedStatement retrieveQuery = this.connect.prepareStatement("SELECT * FROM commande;");
		ResultSet result = retrieveQuery.executeQuery();
		ArrayList<Order> orders = new ArrayList<>();
		while(result.next())
			orders.add(new Order(result.getInt(1),result.getDate(2),result.getInt(3),result.getInt(4)));
		
		return new SimpleListProperty<Order>(FXCollections.observableArrayList(orders));
	}
}
