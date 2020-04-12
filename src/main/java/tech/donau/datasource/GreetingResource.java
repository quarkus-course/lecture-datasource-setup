package tech.donau.datasource;

import io.agroal.api.AgroalDataSource;
import io.quarkus.agroal.DataSource;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Path("/hello")
public class GreetingResource {

    @Inject
    @DataSource("hello")
    AgroalDataSource dataSource;

    @Inject
    @DataSource("users")
    AgroalDataSource usersDataSource;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() throws SQLException {
        final CallableStatement call = dataSource.getConnection().prepareCall("select * from greeting");
        call.execute();
        final ResultSet resultSet = call.getResultSet();
        resultSet.next();
        resultSet.next();
        final String hello = resultSet.getString(2);
        final CallableStatement call2 = usersDataSource.getConnection().prepareCall("select * from user");
        call2.execute();
        final ResultSet userSet = call2.getResultSet();
        userSet.next();
        return hello + " " + userSet.getString(2);
    }
}