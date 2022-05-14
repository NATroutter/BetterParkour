package net.natroutter.betterparkour.handlers;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;
import net.natroutter.betterparkour.Handler;
import net.natroutter.betterparkour.files.Config;
import net.natroutter.betterparkour.objs.Statistic;
import net.natroutter.natlibs.utilities.MojangAPI;
import org.apache.logging.log4j.LogManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {

    private JavaPlugin plugin;
    private HikariConfig hikConfig;
    private HikariDataSource hikData;
    private MojangAPI mojangAPI;
    private boolean valid = false;

    public boolean isValid() {
        return valid;
    }

    public Database(Handler handler) {
        this.plugin = handler.getInstance();
        this.mojangAPI = handler.getMojangAPI();
        Config.MySQL cfg = handler.getConfig().getMySQL();

        if (cfg.getHost().length() > 2 && cfg.getPort().toString().length() > 1 && cfg.getDatabase().length() > 2) {
            try {
                this.hikConfig = new HikariConfig();
                this.hikConfig.setJdbcUrl("jdbc:mysql://" + cfg.getHost() + ":" + cfg.getPort() + "/" + cfg.getDatabase());
                this.hikConfig.setUsername(cfg.getUser());
                this.hikConfig.setPassword(cfg.getPass());
                this.hikConfig.addDataSourceProperty("cachePrepStmts", "true");
                this.hikConfig.addDataSourceProperty("prepStmtCacheSize", "250");
                this.hikConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
                this.hikConfig.setConnectionTimeout(1000);
                this.hikConfig.setPoolName("BetterParkour_Pool");
                this.hikData = new HikariDataSource(this.hikConfig);
                this.valid = true;
            } catch (Exception ignore) {
                plugin.getLogger().warning("[Database] Database connection failed!");
                Bukkit.getPluginManager().disablePlugin(plugin);
                return;
            }
        } else {
            plugin.getLogger().warning("[Database] Database is not configured!");
            Bukkit.getPluginManager().disablePlugin(plugin);
            return;
        }
        createTable();
    }

    private void createTable() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, ()->{
            String sql = "CREATE TABLE IF NOT EXISTS stats(id varchar(255) not null, courseID varchar(36) not null, playerID varchar(36) not null, time long default 0 not null, constraint stats_pk primary key (id));";
            try (Connection con = hikData.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)){
                stmt.execute();
            } catch (Exception e) {
                plugin.getLogger().warning("[Database] Database table creation failed!");
                e.printStackTrace();
            }
        });
    }

    public void saveBatch(ConcurrentHashMap<String, Statistic> map, boolean async, Consumer<Boolean> consumer) {
        if (async) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, ()->{
                sqlSave(map, consumer);
            });
            return;
        }
        sqlSave(map, consumer);
    }

    private void sqlSave(ConcurrentHashMap<String, Statistic> map, Consumer<Boolean> consumer) {
        String sql = "INSERT INTO stats(id,courseID,playerID,time) VALUES (?,?,?,?) ON DUPLICATE KEY UPDATE time = IF(time < ?, time, ?);";
        try (Connection con = hikData.getConnection(); PreparedStatement stmt = con.prepareCall(sql)){
            for (Map.Entry<String, Statistic> entry : map.entrySet()) {
                Statistic stat = entry.getValue();

                stmt.setString(1, entry.getKey());
                stmt.setString(2, stat.getCourseID().toString());
                stmt.setString(3, stat.getPlayerID().toString());
                stmt.setLong(4, stat.getTime());
                stmt.setLong(5, stat.getTime());
                stmt.setLong(6, stat.getTime());
                stmt.addBatch();
            }
            stmt.clearParameters();
            stmt.executeBatch();
            consumer.accept(true);
        } catch (Exception e) {
            plugin.getLogger().warning("[Database] Failed to save batch!");
            e.printStackTrace();
        }
        consumer.accept(false);
    }

    public void deleteStats(UUID playerID, UUID courseID) {
        if (!valid) {return;}

        String sql = "DELETE FROM stats WHERE playerID = ? AND courseID = ?";

        Bukkit.getScheduler().runTaskAsynchronously(plugin, ()->{
            try (Connection con = hikData.getConnection(); PreparedStatement st = con.prepareStatement(sql)) {
                st.setString(1, playerID.toString());
                st.setString(2, courseID.toString());
                st.executeUpdate();
            } catch (Exception e) {
                plugin.getLogger().info("[Database] Failed to delete statistics!");
                e.printStackTrace();
            }
        });
    }

    public void getTop10(UUID courseID, Consumer<List<Statistic>> consumer) {
        if (!valid) {return;}

        String sql = "SELECT * FROM stats WHERE courseID = ? ORDER BY SEC_TO_TIME(time/1000) LIMIT 10";

        Bukkit.getScheduler().runTaskAsynchronously(plugin, ()->{
            List<Statistic> top = new ArrayList<>();
            try (Connection con = hikData.getConnection(); PreparedStatement st = con.prepareStatement(sql)) {
                st.setString(1, courseID.toString());
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    UUID cID = UUID.fromString(rs.getString("courseID"));
                    UUID playerID = UUID.fromString(rs.getString("playerID"));
                    long time = rs.getLong("time");

                    String name = mojangAPI.getName(playerID);
                    if (name == null) {continue;}

                    top.add(new Statistic(cID,playerID,name,time));
                }
                consumer.accept(top);
            } catch (Exception e) {
                plugin.getLogger().info("[Database] Failed to retrive top10 list from database");
                e.printStackTrace();
            }
        });
    }

    public void deleteCourse(UUID courseID) {
        if (!valid) {return;}

        String sql = "DELETE FROM stats WHERE courseID = ?";

        Bukkit.getScheduler().runTaskAsynchronously(plugin, ()->{
            try (Connection con = hikData.getConnection(); PreparedStatement st = con.prepareStatement(sql)) {
                st.setString(1, courseID.toString());
                st.executeUpdate();
            } catch (Exception e) {
                plugin.getLogger().info("[Database] Failed to retrive top10 list from database");
                e.printStackTrace();
            }
        });
    }

}
