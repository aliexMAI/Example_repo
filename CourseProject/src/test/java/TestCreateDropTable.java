import ru.edu.dao.CityRepository;
import ru.edu.service.CityInfo;
import ru.edu.service.CityService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class TestCreateDropTable {
    public static void main(String[] args) throws SQLException {

        String url = "jdbc:sqlite:db/simple_database.db";
        Connection connect = DriverManager.getConnection(url);

        CityRepository repository = new CityRepository();
        repository.setConnection(connect);
        //repository.createTable();
        //repository.dropTable();

//        List<CityInfo> list = repository.getAll();
//        for(CityInfo info: list) {
//            System.out.println(info);
//        }

//        CityService service = new CityService();
//        service.setRepository(repository);
//        List<CityInfo> infoList = service.getAll();
//        System.out.println(infoList.size());
//        for(CityInfo info: infoList) {
//            System.out.println(info);
//        }
    }
}
