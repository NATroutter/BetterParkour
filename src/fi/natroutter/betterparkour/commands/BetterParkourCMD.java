package fi.natroutter.betterparkour.commands;

import fi.natroutter.betterparkour.BetterParkour;
import fi.natroutter.betterparkour.files.Config;
import fi.natroutter.betterparkour.files.Lang;
import fi.natroutter.betterparkour.handlers.CourseBuilder;
import fi.natroutter.betterparkour.handlers.Database.Database;
import fi.natroutter.betterparkour.handlers.ParkourHandler;
import fi.natroutter.natlibs.handlers.database.YamlDatabase;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class BetterParkourCMD extends Command {

    private CourseBuilder course;
    private YamlDatabase yaml;
    private ParkourHandler parkourHandler;
    private Database database = BetterParkour.getDatabase();

    public BetterParkourCMD() {
        super("BetterParkour");
        this.setAliases(Collections.singletonList("bp"));
        this.course = BetterParkour.getCourseBuilder();
        this.yaml = BetterParkour.getYaml();
        this.parkourHandler = BetterParkour.getParkourHandler();
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage(Lang.OnlyIngame.prefixed());
            return false;
        }

        if (args.length == 0) {
            p.sendMessage(Lang.WrongCommandUsage.prefixed());
        } else if (args.length == 1) {

            if (args[0].equalsIgnoreCase("help")) {
                if (!BetterParkour.hasPerm(p, "betterparkour.help")) {
                    return false;
                }
                p.sendMessage(Lang.HelpMessage.asSingleComponent());

            } else if (args[0].equalsIgnoreCase("reload")) {
                if (!BetterParkour.hasPerm(p, "betterparkour.reload")) {return false;}
                Config.Language.reloadFile();
                Lang.Prefix.reloadFile();
                p.sendMessage(Lang.ConfigsReloaded.asComponent());

            } else if (args[0].equalsIgnoreCase("leave")) {
                if (!BetterParkour.hasPerm(p, "betterparkour.leave")) {return false;}
                if (parkourHandler.inCourse(p)) {
                    p.sendMessage(Lang.LeaveMessage.prefixed());
                    parkourHandler.leave(p);
                } else {
                    p.sendMessage(Lang.NotInCourse.prefixed());
                }


            } else if (args[0].equalsIgnoreCase("courses")) {
                if (!BetterParkour.hasPerm(p, "betterparkour.courses")) {return false;}
                course.printCourses(p);

            } else {
                p.sendMessage(Lang.InvalidArgs.prefixed());
            }

        } else if (args.length == 2) {

            if (args[0].equalsIgnoreCase("setup")) {
                if (!BetterParkour.hasPerm(p, "betterparkour.setup")) {
                    return false;
                }

                if (args[1].equalsIgnoreCase("wand")) {
                    if (!BetterParkour.hasPerm(p, "betterparkour.setup.wand")) {return false;}
                    course.giveWand(p);

                } else if (args[1].equalsIgnoreCase("start")) {
                    if (!BetterParkour.hasPerm(p, "betterparkour.setup.start")) {return false;}
                    course.setStart(p);

                } else if (args[1].equalsIgnoreCase("toplist")) {
                    if (!BetterParkour.hasPerm(p, "betterparkour.setup.toplist")) {return false;}
                    course.setTopList(p, p.getLocation());

                } else if (args[1].equalsIgnoreCase("end")) {
                    if (!BetterParkour.hasPerm(p, "betterparkour.setup.end")) {return false;}
                    course.setEnd(p);

                } else if (args[1].equalsIgnoreCase("addpoint")) {
                    if (!BetterParkour.hasPerm(p, "betterparkour.setup.addpoint")) {return false;}
                    course.addCheckpoint(p);

                } else if (args[1].equalsIgnoreCase("abort")) {
                    if (!BetterParkour.hasPerm(p, "betterparkour.setup.abort")) {return false;}
                    course.abort(p);

                } else if (args[1].equalsIgnoreCase("info")) {
                    if (!BetterParkour.hasPerm(p, "betterparkour.setup.info")) {return false;}
                    course.printValidation(p);

                } else if (args[1].equalsIgnoreCase("done")) {
                    if (!BetterParkour.hasPerm(p, "betterparkour.setup.done")) {return false;}
                    course.save(p);

                } else if (args[1].equalsIgnoreCase("spawn")) {
                    if (!BetterParkour.hasPerm(p, "betterparkour.setup.spawn")) {return false;}
                    course.setSpawn(p, p.getLocation());

                } else {
                    p.sendMessage(Lang.InvalidArgs.prefixed());
                }

            } else if (args[0].equalsIgnoreCase("stats")) {
                if (!BetterParkour.hasPerm(p, "betterparkour.stats")) {return false;}

                if (args[1].equalsIgnoreCase("reload")) {
                    if (!BetterParkour.hasPerm(p, "betterparkour.stats.reload")) {
                        return false;
                    }
                    BetterParkour.getTopHologramHandler().loadHolograms();
                    p.sendMessage(Lang.StatisticsReloaded.prefixed());

                } else {
                    p.sendMessage(Lang.InvalidArgs.prefixed());
                }

            } else {
                p.sendMessage(Lang.InvalidArgs.prefixed());
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("setup")) {
                if (!BetterParkour.hasPerm(p, "betterparkour.setup")) {
                    return false;
                }

                if (args[1].equalsIgnoreCase("difficulty")) {
                    if (!BetterParkour.hasPerm(p, "betterparkour.setup.difficulty")) {
                        return false;
                    }
                    String name = args[2].replaceAll("_", " ");
                    course.setDiff(p, name);

                } else if (args[1].equalsIgnoreCase("rename")) {
                    if (!BetterParkour.hasPerm(p, "betterparkour.setup.rename")) {
                        return false;
                    }
                    String name = args[2].replaceAll("_", " ");
                    course.setName(p, name);

                } else {
                    p.sendMessage(Lang.InvalidArgs.prefixed());
                }
            } else if (args[0].equalsIgnoreCase("course")) {
                if (!BetterParkour.hasPerm(p, "betterparkour.course")) {
                    return false;
                }

                if (args[1].equalsIgnoreCase("remove")) {
                    if (!BetterParkour.hasPerm(p, "betterparkour.course.remove")) {
                        return false;
                    }
                    String name = args[2].replaceAll("_", " ");
                    course.remove(p, name);

                } else if (args[1].equalsIgnoreCase("create")) {
                    if (!BetterParkour.hasPerm(p, "betterparkour.course.create")) {
                        return false;
                    }
                    String name = args[2].replaceAll("_", " ");
                    course.create(p, name);

                } else if (args[1].equalsIgnoreCase("edit")) {
                    if (!BetterParkour.hasPerm(p, "betterparkour.course.edit")) {
                        return false;
                    }
                    String name = args[2].replaceAll("_", " ");
                    course.edit(p, name);

                } else {
                    p.sendMessage(Lang.InvalidArgs.prefixed());
                }

            } else {
                p.sendMessage(Lang.InvalidArgs.prefixed());
            }
        } else if (args.length == 4) {

            if (args[0].equalsIgnoreCase("stats")) {
                if (!BetterParkour.hasPerm(p, "betterparkour.stats")) {return false;}

                if (args[1].equalsIgnoreCase("remove")) {
                    if (!BetterParkour.hasPerm(p, "betterparkour.stats.remove")) {return false;}

                    OfflinePlayer ofp = Bukkit.getOfflinePlayerIfCached(args[2]);
                    if (ofp == null) {
                        p.sendMessage(Lang.InvalidPlayer.prefixed());
                        return false;
                    }

                    Set<String> keys = yaml.getKeys("courses");
                    if (keys == null) {
                        p.sendMessage(Lang.CourseList_NoCourses.prefixed());
                        return false;
                    }
                    String courseName = args[3].replaceAll("_", " ");
                    UUID selected = null;
                    for (String key : keys) {
                        if (yaml.getString("courses", key + ".name").equalsIgnoreCase(courseName)) {
                            selected = UUID.fromString(key);
                        }
                    }
                    if (selected == null) {
                        p.sendMessage(Lang.InvalidCourse.prefixed());
                        return false;
                    }

                    database.deleteStats(ofp.getUniqueId(), selected);
                    BetterParkour.getTopHologramHandler().loadHolograms();
                    p.sendMessage(Lang.StatisticsRemoved.prefixed());
                }

            }

        } else {
            p.sendMessage(Lang.TooManyArguments.prefixed());
        }
        return false;
    }

    private List<String> commandArgs(CommandSender sender) {
        return new ArrayList<>(){{
            if (sender.hasPermission("betterparkour.help")) {add("help");}
            if (sender.hasPermission("betterparkour.courses")) {add("courses");}
            if (sender.hasPermission("betterparkour.setup")) {add("setup");}
            if (sender.hasPermission("betterparkour.course")) {add("course");}
            if (sender.hasPermission("betterparkour.stats")) {add("stats");}
            if (sender.hasPermission("betterparkour.leave")) {add("leave");}
            if (sender.hasPermission("betterparkour.reload")) {add("reload");}

        }};
    }

    private List<String> playerNames() {
        List<String> list = new ArrayList<>();
        for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
            list.add(p.getName());
        }
        return list;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {

        if (args.length == 1) {
            List<String> shorted = new ArrayList<>();
            StringUtil.copyPartialMatches(args[0], commandArgs(sender), shorted);
            Collections.sort(shorted);
            return shorted;

        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("setup")) {
                if (!sender.hasPermission("betterparkour.setup")) {return null;}

                List<String> shorted = new ArrayList<>();
                StringUtil.copyPartialMatches(args[1], new ArrayList<>(){{
                    if (sender.hasPermission("betterparkour.setup.wand")) {add("wand");}
                    if (sender.hasPermission("betterparkour.setup.toplist")) {add("toplist");}
                    if (sender.hasPermission("betterparkour.setup.start")) {add("start");}
                    if (sender.hasPermission("betterparkour.setup.end")) {add("end");}
                    if (sender.hasPermission("betterparkour.setup.addpoint")) {add("addpoint");}
                    if (sender.hasPermission("betterparkour.setup.abort")) {add("abort");}
                    if (sender.hasPermission("betterparkour.setup.difficulty")) {add("difficulty");}
                    if (sender.hasPermission("betterparkour.setup.info")) {add("info");}
                    if (sender.hasPermission("betterparkour.setup.done")) {add("done");}
                    if (sender.hasPermission("betterparkour.setup.spawn")) {add("spawn");}
                    if (sender.hasPermission("betterparkour.setup.rename")) {add("rename");}
                }}, shorted);
                Collections.sort(shorted);
                return shorted;

            } else if (args[0].equalsIgnoreCase("course")) {
                if (!sender.hasPermission("betterparkour.course")) {return null;}

                List<String> shorted = new ArrayList<>();
                StringUtil.copyPartialMatches(args[1], new ArrayList<>(){{
                    if (sender.hasPermission("betterparkour.course.create")) {add("create");}
                    if (sender.hasPermission("betterparkour.course.remove")) {add("remove");}
                    if (sender.hasPermission("betterparkour.course.edit")) {add("edit");}
                }}, shorted);
                Collections.sort(shorted);
                return shorted;

            } else if (args[0].equalsIgnoreCase("stats")) {
                if (!sender.hasPermission("betterparkour.stats")) {return null;}

                List<String> shorted = new ArrayList<>();
                StringUtil.copyPartialMatches(args[1], new ArrayList<>(){{
                    if (sender.hasPermission("betterparkour.stats.reload")) {add("reload");}
                    if (sender.hasPermission("betterparkour.stats.remove")) {add("remove");}
                }}, shorted);
                Collections.sort(shorted);
                return shorted;

            } else {
                return null;
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("setup")) {
                if (!sender.hasPermission("betterparkour.setup")) {return null;}

                if (args[1].equalsIgnoreCase("difficulty")) {
                    if (!sender.hasPermission("betterparkour.setup.difficulty")) {return null;}
                    return Collections.singletonList("<text (use _ as space)>");

                } else if (args[1].equalsIgnoreCase("rename")) {
                    if (!sender.hasPermission("betterparkour.setup.rename")) {return null;}
                    return Collections.singletonList("<text (use _ as space)>");
                } else {
                    return null;
                }
            } else if (args[0].equalsIgnoreCase("stats")) {
                if (args[1].equalsIgnoreCase("remove")) {
                    if (!sender.hasPermission("betterparkour.stats.remove")) {return null;}

                    List<String> shorted = new ArrayList<>();
                    StringUtil.copyPartialMatches(args[2], playerNames(), shorted);
                    Collections.sort(shorted);
                    return shorted;

                }
            } else if (args[0].equalsIgnoreCase("course")) {
                if (!sender.hasPermission("betterparkour.course")) {return null;}

                if (args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("edit")) {
                    if (!sender.hasPermission("betterparkour.course.remove") || !sender.hasPermission("betterparkour.course.edit")) {return null;}

                    Set<String> keys = yaml.getKeys("courses");
                    if (keys != null) {
                        List<String> items = new ArrayList<>();
                        for (String key : keys) {
                            items.add(yaml.getString("courses." + key, "name").replaceAll(" ", "_"));
                        }

                        List<String> shorted = new ArrayList<>();
                        StringUtil.copyPartialMatches(args[2], items, shorted);
                        Collections.sort(shorted);
                        return shorted;
                    }
                    return Collections.singletonList("<name (use _ as space)>");

                } else if (args[1].equalsIgnoreCase("create")){
                    if (!sender.hasPermission("betterparkour.course.create")) {return null;}
                    return Collections.singletonList("<name (use _ as space)>");
                } else {
                    return null;
                }
            }
        } else if (args.length == 4) {
            if (args[0].equalsIgnoreCase("stats")) {
                if (args[1].equalsIgnoreCase("remove")) {
                    if (!sender.hasPermission("betterparkour.stats.remove")) {return null;}

                    Set<String> keys = yaml.getKeys("courses");
                    if (keys != null) {
                        List<String> items = new ArrayList<>();
                        for (String key : keys) {
                            items.add(yaml.getString("courses." + key, "name").replaceAll(" ", "_"));
                        }

                        List<String> shorted = new ArrayList<>();
                        StringUtil.copyPartialMatches(args[3], items, shorted);
                        Collections.sort(shorted);
                        return shorted;
                    }
                    return Collections.singletonList("<name (use _ as space)>");

                }
            }
        }
        return null;
    }

}
