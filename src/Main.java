import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;

public class Main {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().filename(".env").load();
        String token = dotenv.get("SITEPIC_TOKEN"); //SITEPIC'S TOKEN
        DefaultShardManagerBuilder.createDefault(token).addEventListeners(new Hub())
                .setActivity(Activity.playing("Message me for avatar!")).build();
    }
}
