import ru.edu.dao.CityRepository;
import ru.edu.service.CityInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class CityToDatabase {

    public static Scanner in = new Scanner(System.in);
    public static void main(String[] args) throws SQLException {

        String url = "jdbc:sqlite:db/simple_database.db";
        Connection connect = DriverManager.getConnection(url);

        CityRepository repository = new CityRepository();
        repository.setConnection(connect);

        while(true) {

            System.out.println("\n<для выхода введите в названии города слово 'выйти'>");
            CityInfo city = new CityInfo();

            System.out.print("\n#Введите название города > ");
            String name = in.nextLine();
            if(name.equalsIgnoreCase("выйти")) {
                System.exit(0);
            }
            city.setName(name);

            System.out.print("#Введите широту в радианах > ");
            String latitude = in.nextLine().trim();
            city.setLatitude(Double.parseDouble(latitude));

            System.out.print("#Введите долготу в радианах > ");
            String longitude = in.nextLine().trim();
            city.setLongitude(Double.parseDouble(longitude));

            System.out.print("#Введите численность населения > ");
            String num = in.nextLine().trim();
            num = num.replaceAll(" ", "");
            city.setPopulation(Integer.parseInt(num));


            System.out.print("#Введите описание города > ");
            if(in.hasNextLine()) {
                String descr = in.nextLine();
                descr += in.nextLine();
                descr += in.nextLine();
                city.setDescription(descr);
            }

            System.out.print("#Введите описание климата > ");
            if(in.hasNextLine()) {
                String clmt = in.nextLine();
                clmt  += in.nextLine();
                clmt  += in.nextLine();
                clmt  += in.nextLine();
                clmt  += in.nextLine();
                city.setClimate(clmt);
            }

            city.createId();

            repository.create(city);
        }
    }
}
