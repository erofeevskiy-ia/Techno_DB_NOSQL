import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by ihb on 31.05.17.
 */
public class dbFiller {



    private static File file;

    private static String SCRIPT_PATH = "src/main/resources/script";


    private static String USER_NAMES_PATH = "src/main/resources/user/names";
    private static String VIDEO_NAMES_PATH = "src/main/resources/video/names";




    private static int USER_COUNT = 10;
    private static int VIDEO_COUNT = 10;




    private static Random rand = new Random(System.currentTimeMillis());




    public static void main(String[] args) throws IOException {



        initFile();


        clearDB();
        createTables();

        generateUsers();
        generateVideos();
        generateViews();
        //generateFriends();

/*        generateCountries();
        generateQuestionnaire();
        generatePreferences();
        generateUserPreferences();
        generatePosts();
        generateVariants();
        generateVotes();
        generateComment();*/




        /*FileUtils.write(new File(POST_DESCRIPTIONS_PATH+1),
                FileUtils.readFileToString(new File(POST_DESCRIPTIONS_PATH), Charset.defaultCharset()).replaceAll("[\\[\\]\"{}()']",""),
                Charset.defaultCharset(), false);*/
    }



    private static void initFile() throws IOException {
        file = FileUtils.getFile(SCRIPT_PATH);
        try {
            FileUtils.forceDelete(file);
        } catch (Exception ignored){

        }
        FileUtils.touch(file);
    }




    private static void clearDB() throws IOException {
        FileUtils.write(file,"DROP DATABASE socialGraph;\n" +
                        "CREATE DATABASE socialGraph;\n" +
                        "USE socialGraph;\n"
                , Charset.defaultCharset(), true);
    }

    private static void createTables() throws IOException {
        String user = "CREATE TABLE IF NOT EXISTS\n" +
                "    `user` (\n" +
                "        `id` INT NOT NULL AUTO_INCREMENT,\n" +
                "        `name` VARCHAR(50) NOT NULL UNIQUE,\n" +
                "        PRIMARY KEY(`id`)\n" +
                "    );\n";

        String video = "CREATE TABLE IF NOT EXISTS\n" +
                "    `video` (\n" +
                "        `id` INT NOT NULL AUTO_INCREMENT,\n" +
                "        `name` VARCHAR(100) NOT NULL,\n" +
                "        PRIMARY KEY(`id`)\n" +
                "    );\n";

        String view = "CREATE TABLE IF NOT EXISTS\n" +
                "    `view` (\n" +
                "        `id` INT NOT NULL AUTO_INCREMENT,\n" +
                "        `user_id` INT NOT NULL,\n" +
                "        `video_id` INT NOT NULL,\n" +
                "        PRIMARY KEY(`id`),\n" +
                "        FOREIGN KEY (user_id) REFERENCES user(id),\n" +
                "        FOREIGN KEY (video_id) REFERENCES video(id)\n" +
                "    );\n";

        String friend = "CREATE TABLE IF NOT EXISTS\n" +
                "    `friend` (\n" +
                "        `id` INT NOT NULL AUTO_INCREMENT,\n" +
                "        `user_id_1` INT NOT NULL,\n" +
                "        `user_id_2` INT NOT NULL,\n" +
                "        PRIMARY KEY(`id`),\n" +
                "        FOREIGN KEY (user_id_1) REFERENCES user(id),\n" +
                "        FOREIGN KEY (user_id_2) REFERENCES user(id)\n" +
                "    );\n";

        String like = "CREATE TABLE IF NOT EXISTS\n" +
                "    `like` (\n" +
                "        `id` INT NOT NULL AUTO_INCREMENT,\n" +
                "        `video_id_1` INT NOT NULL,\n" +
                "        `video_id_2` INT NOT NULL,\n" +
                "        PRIMARY KEY(`id`),\n" +
                "        FOREIGN KEY (video_id_1) REFERENCES video(id),\n" +
                "        FOREIGN KEY (video_id_2) REFERENCES video(id)\n" +
                "    );\n";




        FileUtils.write(file, user + video + view + friend + like
                , Charset.defaultCharset(), true);

    }

    private static void generateUsers() throws IOException {

        List<String> userName = new ArrayList<>(FileUtils.readLines(new File(USER_NAMES_PATH), Charset.defaultCharset()));

        StringBuilder query = new StringBuilder("INSERT INTO user (id, name)\n"
                + "VALUES ");


        for(int i=0; i<USER_COUNT; i++){
            query.append("('").append(i+1).append("','").append(userName.get(i)).append("')");

            if(i!=USER_COUNT-1){
                query.append(",\n");
            }

            FileUtils.write(file, query.toString(), Charset.defaultCharset(), true);
            query = new StringBuilder();
        }

        query.append(";");

        FileUtils.write(file, query.toString(), Charset.defaultCharset(), true);
    }


    private static void generateVideos() throws IOException {

        List<String> videoName = new ArrayList<>(FileUtils.readLines(new File(VIDEO_NAMES_PATH), Charset.defaultCharset()));

        StringBuilder query = new StringBuilder("INSERT INTO video (id, name)\n"
                + "VALUES ");


        for(int i=0; i<VIDEO_COUNT; i++){
            query.append("('").append(i+1).append("','").append(videoName.get(i)).append("')");

            if(i!=VIDEO_COUNT-1){
                query.append(",\n");
            }

            FileUtils.write(file, query.toString(), Charset.defaultCharset(), true);
            query = new StringBuilder();
        }

        query.append(";");

        FileUtils.write(file, query.toString(), Charset.defaultCharset(), true);
    }

    private static void generateViews() throws IOException {

        StringBuilder query = new StringBuilder("INSERT INTO view (id, user_id, video_id)\n"
                + "VALUES ");


        for(int i=0; i<USER_COUNT; i++){
            for(int j=0; j<5; j++){
                query.append("('").append(5*i + j+1).append("','").append(i+1).append("','")
                        .append(Math.abs(rand.nextInt()) % VIDEO_COUNT + 1).append("')");

                if(i!=USER_COUNT-1 || j!=4){
                    query.append(",\n");
                }

            }

            FileUtils.write(file, query.toString(), Charset.defaultCharset(), true);
            query = new StringBuilder();
        }

        query.append(";");

        FileUtils.write(file, query.toString(), Charset.defaultCharset(), true);
    }





    /*private static void generateFriends() throws IOException {

        StringBuilder query = new StringBuilder("INSERT INTO friend (id, user_id_1, user_id_2)\n"
                + "VALUES ");


        for(int i=0; i<USER_COUNT; i++){
            for(int j=0; j<5; j++){
                query.append("('").append(i+1).append("','").append(i).append("','")
                        .append(Math.abs(rand.nextInt()) % VIDEO_COUNT).append("')");

                if(i!=USER_COUNT-1 && j!=4){
                    query.append(",\n");
                }

            }

            FileUtils.write(file, query.toString(), Charset.defaultCharset(), true);
            query = new StringBuilder();
        }

        query.append(";");

        FileUtils.write(file, query.toString(), Charset.defaultCharset(), true);
    }*/


}
