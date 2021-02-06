import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;

public class Main {

    public static void main(String[] args) {
        System.setProperty("hadoop.home.dir", "D:\\Programs\\Hadoop");
        SparkConf sparkConf = new SparkConf().setAppName("JavaWordCount").setMaster("local").set("spark.executor.memory","1g");
        JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);
        SparkSession sparkSession = new SparkSession(javaSparkContext.sc());

        Dataset<Emp> employeeDS = sparkSession.read().option("header", "true").csv("src/main/resources/employee.csv").toDF().as(Encoders.bean(Emp.class));
        Dataset<Bonus> bonusDS = sparkSession.read().option("header", "true").csv("src/main/resources/bonus.csv").toDF().as(Encoders.bean(Bonus.class));
        Dataset<City> cityDS = sparkSession.read().option("header", "true").csv("src/main/resources/city.csv").toDF().as(Encoders.bean(City.class));

        Dataset<Result> firstJoin = employeeDS.select("id", "name").
                join(bonusDS.select("id", "bonus"), bonusDS.col("id").
                        eqNullSafe(employeeDS.col("id")), "left_outer").
                drop(bonusDS.col("id")).map(x -> Result.builder().bonus(x.getAs("bonus")).id(x.getAs("id")).build(), Encoders.bean(Result.class));

        firstJoin.show();

        Dataset<Result> secondJoin = firstJoin.
                join(cityDS.select("id", "city"), firstJoin.col("id").
                        eqNullSafe(cityDS.col("id")), "left_outer").
                drop(cityDS.col("id")).map(x -> {
            return Result.builder().bonus(x.getAs("bonus")).id(x.getAs("id")).city(x.getAs("city")).build();
        }, Encoders.bean(Result.class));

        secondJoin.show();

        sparkSession.stop();
    }
}
