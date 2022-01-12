
<div align="center">
<h1 style="margin: 0px;font-weight: 700;font-family:-apple-system,BlinkMacSystemFont,Segoe UI,Helvetica,Arial,sans-serif,Apple Color Emoji,Segoe UI Emoji">BetterParkour</h1>

![GitHub](https://img.shields.io/github/license/natroutter/betterparkour?style=for-the-badge)
![Custom badge](https://img.shields.io/endpoint?color=%2303fc4e&style=for-the-badge&url=https%3A%2F%2Fhub.nat.gs%2Fjavadoc%2Fversion.php%3Fproject%3Dbetterparkour)

![Jenkins](https://img.shields.io/jenkins/build?jobUrl=https%3A%2F%2Fhub.nat.gs%2Fjenkins%2Fjob%2FBetterParkour%2F&style=for-the-badge)
![Sonatype Nexus (Releases)](https://img.shields.io/nexus/r/net.natroutter/betterparkour?server=https%3A%2F%2Fhub.nat.gs%2Fnexus%2F&style=for-the-badge)

Simple miencraft parkour plugin
 
</div>

## Documentation
Documentation: [here](https://hub.nat.gs/javadoc/latest.php?project=betterparkour)  
Old version documentation can be found in [ProjectHub](https://hub.nat.gs/index.php?project=BetterParkour) for limited time

## Api
###Maven Repository:
````xml
<repository>
    <id>NAT-Software</id>
    <url>https://hub.nat.gs/nexus/repository/NAT-Software/</url>
</repository>
````

###Maven Dependency:
````xml
<dependency>
    <groupId>net.natroutter</groupId>
    <artifactId>betterparkour</artifactId>
    <version>{VERSION}</version>
    <scope>provided</scope>
</dependency>
````

##Getting started with api

````java
import net.natroutter.betterparkour.BetterParkour;
import net.natroutter.betterparkour.ParkourAPI;
import org.bukkit.Bukkit;

@Override
public class ExamplePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Plugin bp = Bukkit.getPluginManager().getPlugin("BetterParkour");
        if (bp != null && bp.isEnabled()) {
            ParkourAPI api = BetterParkour.getAPI();
            //now you can use api how ever you like!
        }
    }

}
````