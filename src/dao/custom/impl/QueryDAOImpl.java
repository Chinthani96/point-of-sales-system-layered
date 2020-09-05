package dao.custom.impl;

import dao.custom.QueryDAO;
import dao.CrudUtil;
import entity.CustomEntity;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class QueryDAOImpl implements QueryDAO {

    @Override
    public List<CustomEntity> getOrderDetails() throws Exception {
        List<CustomEntity> orders = new ArrayList<>();
        ResultSet resultSet = CrudUtil.execute("SELECT o.id,o.date,o.customerId,c.name, Sum(OD.qty *OD.unitPrice) as `Total`\n" +
                "from `Order` o INNER JOIN Customer C ON o.customerId = C.id INNER JOIN OrderDetail OD on o.id = OD.orderId GROUP BY o.id");
        while (resultSet.next()) {
            orders.add(new CustomEntity(resultSet.getString(1),resultSet.getDate(2),resultSet.getString(3),resultSet.getString(4),resultSet.getBigDecimal(5)));
        }
        return orders;
    }
}
