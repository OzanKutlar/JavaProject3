package comp.CMPE343.Database;

import javax.sql.rowset.CachedRowSet;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.UUID;

public class DatabaseRequest {

    UUID requestID;
    String request;
    CachedRowSet resultSet;


    public DatabaseRequest(String request){
        requestID = UUID.randomUUID();
        this.request = request;
        resultSet = null;
    }


}
