import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.server.Server;

import java.time.Instant;
import java.util.Comparator;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("This bot requires exactly two arguments, the id of the guild and the token of your Discord bot.");
            System.exit(1);
        }

        DiscordApi api = new DiscordApiBuilder().setToken(args[1]).login().exceptionally(e -> {
            System.err.println("Make sure the given token is correct.");
            System.exit(1);
            return null;
        }).join();

        Server server = api.getServerById(args[0]).orElseGet(() -> {
            System.err.println("Make sure the given id ('" + args[0] + "') is correct.");
            System.exit(1);
            return null;
        });

        server.getMembers().stream()
                .sorted(Comparator.comparing(u -> u.getJoinedAtTimestamp(server).orElse(Instant.EPOCH)))
                .forEach(user ->
                        System.out.println(user.getId() + " (" + user.getDiscriminatedName() + ") joined at "
                                + user.getJoinedAtTimestamp(server).orElse(Instant.EPOCH).toString()));
        System.exit(0);
    }
}
