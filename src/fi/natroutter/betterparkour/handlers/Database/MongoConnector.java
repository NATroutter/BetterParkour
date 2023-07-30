package fi.natroutter.betterparkour.handlers.Database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import fi.natroutter.natlibs.objects.MongoConfig;
import lombok.Getter;
import org.bson.UuidRepresentation;
import org.bson.codecs.UuidCodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MongoConnector {

    private JavaPlugin instance;
    private Logger logger;

    @Getter private boolean validConfig;

    @Getter private boolean connected;

    private List<String> collections = new ArrayList<>();

    private MongoClient mongoClient;
    private MongoConfig config;

    public boolean disconnect() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
            log("MongoDB disconnected!");
            return true;
        }
        return false;
    }

    public MongoConnector(JavaPlugin instance, MongoConfig config) {
        this.instance = instance;
        this.logger = instance.getLogger();
        this.config = config;

        validConfig = validateConfig();

        if (validConfig) {
            try {

                this.mongoClient = MongoClients.create(
                        MongoClientSettings.builder()
                                .uuidRepresentation(UuidRepresentation.STANDARD)
                                .applyConnectionString(new ConnectionString(config.getURI()))
                                .codecRegistry(getCodecRegistry())
                                .build()
                );

                if (!mongoClient.listDatabaseNames().into(new ArrayList<>()).contains(config.getDatabase())) {
                    log("MongoDB Error : database " + config.getDatabase() + " doesn't exists!");
                    connected = false;
                    return;
                }
                MongoDatabase mdb = mongoClient.getDatabase(config.getDatabase());

                List<String> databaseNames = mdb.listCollectionNames().into(new ArrayList<>());
                for (String col : collections) {
                    if (!databaseNames.contains(col)) {
                        mdb.createCollection(col);
                    }
                }
                connected = mongoClient != null;
            } catch (Exception ignored) {}
        }
    }

    public void registerCollection(String name) {
        collections.add(name);
    }

    public CodecRegistry getCodecRegistry() {
        return CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(new UuidCodecProvider(UuidRepresentation.STANDARD)),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
        );
    }

    protected MongoDatabase getDatabase() {
        if (mongoClient == null) {
            log("Error : MongoDB is not connected!");
            return null;
        }
        return mongoClient.getDatabase(config.getDatabase());
    }

    private boolean validateConfig() {
        if (config.getDatabase().isBlank()) {
            log("Invalid MongoDB configuration : missing/invalid database!");
            return false;
        }
        if (config.getUsername().isBlank()) {
            log("Invalid MongoDB configuration : missing/invalid username!");
            return false;
        }
        if (config.getPassword().isBlank()) {
            log("Invalid MongoDB configuration : missing/invalid password!");
            return false;
        }
        if (config.getDatabase().isBlank()) {
            log("Invalid MongoDB configuration : missing/invalid host!");
            return false;
        }
        if (String.valueOf(config.getPort()).isBlank()) {
            log("Invalid MongoDB configuration : missing/invalid port!");
            return false;
        }
        return true;
    }

    private void log(String message) {
        Bukkit.getConsoleSender().sendMessage("§4["+instance.getName()+"][MongoConnector] §c" + message);
    }

}
