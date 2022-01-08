package net.natroutter.betterparkour.commands;

import net.natroutter.betterparkour.Handler;
import net.natroutter.betterparkour.files.Lang;
import net.natroutter.betterparkour.handlers.CourseBuilder;
import net.natroutter.betterparkour.handlers.ParkourHandler;
import net.natroutter.betterparkour.objs.Statistic;
import net.natroutter.natlibs.handlers.Database.YamlDatabase;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class BetterParkourCMD extends Command {

    private Handler handler;
    private Lang lang;
    private CourseBuilder course;
    private YamlDatabase yaml;
    private ParkourHandler parkourHandler;

    public BetterParkourCMD(Handler handler) {
        super("BetterParkour");
        this.setAliases(Collections.singletonList("bp"));
        this.handler = handler;
        this.lang = handler.getLang();
        this.course = handler.getCourseBuilder();
        this.yaml = handler.getYaml();
        this.parkourHandler = handler.getParkourHandler();
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage(lang.Prefix + lang.OnlyIngame);
            return false;
        }

        if (args.length == 0) {
            p.sendMessage(lang.Prefix + lang.WrongCommandUsage);
        } else if (args.length == 1) {

            if (args[0].equalsIgnoreCase("help")) {
                if (!handler.hasPerm(p, "help")) {return false;}
                lang.helpMessage.forEach(p::sendMessage);

            } else if (args[0].equalsIgnoreCase("leave")) {
                if (!handler.hasPerm(p, "leave")) {return false;}
                if (parkourHandler.inCourse(p)) {
                    p.sendMessage(lang.Prefix + lang.LeaveMessage);
                    parkourHandler.leave(p);
                } else {
                    p.sendMessage(lang.Prefix + lang.NotInCourse);
                }


            } else if (args[0].equalsIgnoreCase("courses")) {
                if (!handler.hasPerm(p, "courses")) {return false;}
                course.printCourses(p);

            } else {
                p.sendMessage(lang.Prefix + lang.InvalidArgs);
            }

        } else if (args.length == 2) {

            if (args[0].equalsIgnoreCase("setup")) {
                if (!handler.hasPerm(p, "setup")) {
                    return false;
                }

                if (args[1].equalsIgnoreCase("wand")) {
                    if (!handler.hasPerm(p, "setup.wand")) {return false;}
                    course.giveWand(p);

                } else if (args[1].equalsIgnoreCase("start")) {
                    if (!handler.hasPerm(p, "setup.start")) {return false;}
                    course.setStart(p);

                } else if (args[1].equalsIgnoreCase("toplist")) {
                    if (!handler.hasPerm(p, "setup.toplist")) {return false;}
                    course.setTopList(p, p.getLocation());

                } else if (args[1].equalsIgnoreCase("end")) {
                    if (!handler.hasPerm(p, "setup.end")) {return false;}
                    course.setEnd(p);

                } else if (args[1].equalsIgnoreCase("addpoint")) {
                    if (!handler.hasPerm(p, "setup.addpoint")) {return false;}
                    course.addCheckpoint(p);

                } else if (args[1].equalsIgnoreCase("abort")) {
                    if (!handler.hasPerm(p, "setup.abort")) {return false;}
                    course.abort(p);

                } else if (args[1].equalsIgnoreCase("info")) {
                    if (!handler.hasPerm(p, "setup.info")) {return false;}
                    course.printValidation(p);

                } else if (args[1].equalsIgnoreCase("done")) {
                    if (!handler.hasPerm(p, "setup.done")) {return false;}
                    course.save(p);

                } else if (args[1].equalsIgnoreCase("spawn")) {
                    if (!handler.hasPerm(p, "setup.spawn")) {return false;}
                    course.setSpawn(p, p.getLocation());

                } else {
                    p.sendMessage(lang.Prefix + lang.InvalidArgs);
                }

            } else if (args[0].equalsIgnoreCase("stats")) {
                if (!handler.hasPerm(p, "stats")) {return false;}

                if (args[1].equalsIgnoreCase("save")) {
                    if (!handler.hasPerm(p, "stats.save")) {return false;}

                    handler.getStatisticHandler().save(false);
                    handler.getTopHologramHandler().loadHolograms();
                    p.sendMessage(lang.Prefix + lang.StatisticsSaved);

                } else {
                    p.sendMessage(lang.Prefix + lang.InvalidArgs);
                }

            } else {
                p.sendMessage(lang.Prefix + lang.InvalidArgs);
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("setup")) {
                if (!handler.hasPerm(p, "setup")) {return false;}

                if (args[1].equalsIgnoreCase("difficulty")) {
                    if (!handler.hasPerm(p, "setup.difficulty")) {return false;}
                    String name = args[2].replaceAll("_", " ");
                    course.setDiff(p, name);

                } else if (args[1].equalsIgnoreCase("rename")) {
                    if (!handler.hasPerm(p, "setup.rename")) {return false;}
                    String name = args[2].replaceAll("_", " ");
                    course.setName(p, name);

                } else {
                    p.sendMessage(lang.Prefix + lang.InvalidArgs);
                }
            } else if (args[0].equalsIgnoreCase("course")) {
                if (!handler.hasPerm(p, "course")) {return false;}

                if (args[1].equalsIgnoreCase("remove")) {
                    if (!handler.hasPerm(p, "course.remove")) {return false;}
                    String name = args[2].replaceAll("_", " ");
                    course.remove(p, name);

                } else if (args[1].equalsIgnoreCase("create")) {
                    if (!handler.hasPerm(p, "course.create")) {return false;}
                    String name = args[2].replaceAll("_", " ");
                    course.create(p, name);

                } else if (args[1].equalsIgnoreCase("edit")) {
                    if (!handler.hasPerm(p, "course.edit")) {return false;}
                    String name = args[2].replaceAll("_", " ");
                    course.edit(p, name);

                } else {
                    p.sendMessage(lang.Prefix + lang.InvalidArgs);
                }

            } else {
                p.sendMessage(lang.Prefix + lang.InvalidArgs);
            }
        } else {
            p.sendMessage(lang.Prefix + lang.TooManyArguments);
        }
        return false;
    }

    private List<String> commandArgs(CommandSender sender) {
        return new ArrayList<>(){{
            if (handler.hasPerm(sender, "help")) {add("help");}
            if (handler.hasPerm(sender, "courses")) {add("courses");}
            if (handler.hasPerm(sender, "setup")) {add("setup");}
            if (handler.hasPerm(sender, "course")) {add("course");}
            if (handler.hasPerm(sender, "stats")) {add("stats");}
            if (handler.hasPerm(sender, "leave")) {add("leave");}

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
                if (!handler.hasPerm(sender, "setup")) {return null;}

                List<String> shorted = new ArrayList<>();
                StringUtil.copyPartialMatches(args[1], new ArrayList<>(){{
                    if (handler.hasPerm(sender, "setup.wand")) {add("wand");}
                    if (handler.hasPerm(sender, "setup.toplist")) {add("toplist");}
                    if (handler.hasPerm(sender, "setup.start")) {add("start");}
                    if (handler.hasPerm(sender, "setup.end")) {add("end");}
                    if (handler.hasPerm(sender, "setup.addpoint")) {add("addpoint");}
                    if (handler.hasPerm(sender, "setup.abort")) {add("abort");}
                    if (handler.hasPerm(sender, "setup.difficulty")) {add("difficulty");}
                    if (handler.hasPerm(sender, "setup.info")) {add("info");}
                    if (handler.hasPerm(sender, "setup.done")) {add("done");}
                    if (handler.hasPerm(sender, "setup.spawn")) {add("spawn");}
                    if (handler.hasPerm(sender, "setup.rename")) {add("rename");}
                }}, shorted);
                Collections.sort(shorted);
                return shorted;

            } else if (args[0].equalsIgnoreCase("course")) {
                if (!handler.hasPerm(sender, "course")) {return null;}

                List<String> shorted = new ArrayList<>();
                StringUtil.copyPartialMatches(args[1], new ArrayList<>(){{
                    if (handler.hasPerm(sender, "course.create")) {add("create");}
                    if (handler.hasPerm(sender, "course.remove")) {add("remove");}
                    if (handler.hasPerm(sender, "course.edit")) {add("edit");}
                }}, shorted);
                Collections.sort(shorted);
                return shorted;

            } else if (args[0].equalsIgnoreCase("stats")) {
                if (!handler.hasPerm(sender, "stats")) {return null;}

                List<String> shorted = new ArrayList<>();
                StringUtil.copyPartialMatches(args[1], new ArrayList<>(){{
                    if (handler.hasPerm(sender, "stats.save")) {add("save");}
                }}, shorted);
                Collections.sort(shorted);
                return shorted;

            } else {
                return null;
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("setup")) {
                if (!handler.hasPerm(sender, "setup")) {return null;}

                if (args[1].equalsIgnoreCase("difficulty")) {
                    if (!handler.hasPerm(sender, "setup.difficulty")) {return null;}
                    return Collections.singletonList("<text (use _ as space)>");

                } else if (args[1].equalsIgnoreCase("rename")) {
                    if (!handler.hasPerm(sender, "setup.rename")) {return null;}
                    return Collections.singletonList("<text (use _ as space)>");
                } else {
                    return null;
                }
            } else if (args[0].equalsIgnoreCase("course")) {
                if (!handler.hasPerm(sender, "course")) {return null;}

                if (args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("edit")) {
                    if (!handler.hasPerm(sender, "course.remove") || !handler.hasPerm(sender, "course.edit")) {return null;}

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
                    if (!handler.hasPerm(sender, "course.create")) {return null;}
                    return Collections.singletonList("<name (use _ as space)>");
                } else {
                    return null;
                }
            }
        }
        return null;
    }

}
