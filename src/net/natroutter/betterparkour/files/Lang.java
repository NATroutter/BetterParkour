package net.natroutter.betterparkour.files;

import java.nio.channels.Pipe;
import java.util.Arrays;
import java.util.List;

public class Lang {

    private ValidateCourse validateCourse;
    private CourseList courseList;
    private wand wand;
    private TopList topList;

    public ValidateCourse getValidateCourse() {
        return validateCourse;
    }
    public CourseList getCourseList() {
        return courseList;
    }
    public Lang.wand getWand() {return wand;}
    public TopList getTopList() {return topList;}

    public Lang() {
        this.validateCourse = new ValidateCourse();
        this.courseList = new CourseList();
        this.wand = new wand();
        this.topList = new TopList();
    }

    public String Prefix = "§9§lBetterParkour §8§l» ";
    public String NoPerm = "§7You dont have permissions to do that!";
    public String TooManyArguments = "§7too many arguments, do §b/bp help §7for more information";
    public String InvalidArgs = "§7Invalid command args, do §b/bp help §7for more information";
    public String WrongCommandUsage = "§7Invalid command usage, do §b/bp help §7for more information";
    public String OnlyIngame = "§7This Command can only be executed ingame!";
    public String WandGive = "§7BetterParkour wand added to your inventory";
    public String CourseInEdit = "§7Some course is already being edited!";
    public String CourseExist = "§7This course is already created!";
    public String CourseDoesntExits = "§7This course doesn't exists";
    public String CourseCreated = "§7Course created, now you need to do setup";
    public String CourseEditSelected = "§7Editting course §9%course%";
    public String CourseNotSelected = "§7You need to select course first!";
    public String InvalidBlockTarget = "§7You need to look at the starting block";
    public String PressurePlateNeeded = "§7You need to look at pressure plate";
    public String StartSet = "§7Starting point set";
    public String EndSet = "§7Ending point set";
    public String CheckpointAdded = "§7Checkpoint added!";
    public String FirstCornerSet = "§7First corner set!";
    public String SecondCornerSet = "§7Second corner set!";
    public String DifficultySet = "§7Difficulty set!";
    public String TopListSet = "§7TopList set!";
    public String SpawnSet = "§7Spawn set!";
    public String CourseEditFinished = "§7Course Editing finished!";
    public String CourseNotGood = "§7Your course is missing some parts more info with §9/bp setup info";
    public String CourseCreatingAborted = "§7Course creating aborted!";
    public String CourseRemoved = "§7Course removed!";
    public String StatisticsSaved = "§7Statistics saved!";
    public String CantWhileInCourse = "§7You cant do that while you are in parkour course";
    public String LeaveMessage = "§7You have left the parkour!";
    public String LeaveInfoMessage = "§7If you want to leave parkour type §b/bp leave";
    public String NotInCourse = "§7You are not in parkour course!";
    public String ActionBar = "§b§l%name%§8(§7%diff%§8) §9§l- §b§l%time%§7ms";
    public String CourseRenamed = "§7Course renamed!";

    public List<String> CourseFinished = Arrays.asList(
            " ",
            "§8§l§m━━━━━━━━━━━━§8§l|§9§l BetterParkour §8§l|§m━━━━━━━━━━━━",
            " ",
            "§8§l» §7Parkour §b%courseName% §7finished!",
            "§8§l» §7Difficulty§8: §b%diff%",
            "§8§l» §7Time§8: §b%courseTime%§7ms",
            " ",
            "§8§l§m━━━━━━━━━━━━§8§l|§9§l BetterParkour §8§l|§m━━━━━━━━━━━━",
            " "
    );

    public class wand {
        public String name = "§9§lBetterParkour Wand";
        public List<String> lore = Arrays.asList(
                "§7You can use this to select course corners!"
        );
    }

    public class ValidateCourse {
        public List<String> top = Arrays.asList(
                " ",
                "§8§l§m━━━━━━━━━━━━§8§l|§9§l BetterParkour §8§l|§m━━━━━━━━━━━━",
                " ",
                "§8§l» §7ID§8: §b%id% §8|| §7Name§8: §b%name%",
                " "
        );
        public String entry = "§8§l» %part%";
        public List<String> bottom = Arrays.asList(
                " ",
                "§8§l§m━━━━━━━━━━━━§8§l|§9§l BetterParkour §8§l|§m━━━━━━━━━━━━",
                " "
        );
        public String err_Name = "§b✘ §7Invalid course name!";
        public String err_FirstCorner = "§b✘ §7Invalid first corner";
        public String err_SecCorner = "§b✘ §7Invalid second corner";
        public String err_Ending = "§b✘ §7Invalid Ending position";
        public String err_Staring = "§b✘ §7Invalid starting position";
        public String err_Difficulty = "§b✘ §7Invalid difficulty";
        public String err_Spawn = "§b✘ §7Invalid spawn";

        public String good_Name = "§b✔ §7Name set!";
        public String good_FirstCorner = "§b✔ §7First corner set!";
        public String good_SecCorner = "§b✔ §7Second corner set!";
        public String good_Ending = "§b✔ §7Ending set!";
        public String good_Staring = "§b✔ §7Start set!";
        public String good_Difficulty = "§b✔ §7Difficulty set!";
        public String good_Spawn = "§b✔ §7Spawn set!";
    }

    public class CourseList {
        public List<String> top = Arrays.asList(
                " ",
                "§8§l§m━━━━━━━━━━━━§8§l|§9§l BetterParkour §8§l|§m━━━━━━━━━━━━",
                " "
        );
        public String entry = "§8§l» §b%name% §8- §7Difficulty§8: §b%diff%";
        public String noCourses = "§8§l» §7There are no courses yet";
        public List<String> bottom = Arrays.asList(
                " ",
                "§8§l§m━━━━━━━━━━━━§8§l|§9§l BetterParkour §8§l|§m━━━━━━━━━━━━",
                " "
        );
    }

    public class TopList {
        public List<String> top = Arrays.asList(
                "§8§l§m━━━━━━━━━━━━§8§l|§9§l %courseName% §8(§7%diff%§8) §8§l|§m━━━━━━━━━━━━"
        );
        public String entry = "§7#%pos% §8- §b%name% §8- §b%time%§7ms";
        public String noentries = "§7No data available";
        public List<String> bottom = Arrays.asList(
                "§8§l§m━━━━━━━━━━━━§8§l|§9§l %courseName% §8(§7%diff%§8) §8§l|§m━━━━━━━━━━━━"
        );
    }

    public List<String> helpMessage = Arrays.asList(
            " ",
            "§8§l§m━━━━━━━━━━━━§8§l|§9§l BetterParkour §8§l|§m━━━━━━━━━━━━",
            "§8§l» §b/bp help §8:: §7Shows this help message",
            "§8§l» §b/bp courses §8:: §7Shows list of courses",
            "§8§l» §b/bp setup wand §8:: §7Gives setup wand",
            "§8§l» §b/bp setup start §8:: §7Set course start",
            "§8§l» §b/bp setup end §8:: §7SSet course ending",
            "§8§l» §b/bp setup addpoint §8:: §7Adds new checkpoint",
            "§8§l» §b/bp setup abort §8:: §7Cancels course creation",
            "§8§l» §b/bp setup spawn §8:: §7First spawnpoint",
            "§8§l» §b/bp setup toplist §8:: §7Set toplist location",
            "§8§l» §b/bp setup done §8:: §7SFinish setup",
            "§8§l» §b/bp setup difficulty <text> §8:: §7Set difficulty",
            "§8§l» §b/bp course remove <name> §8:: §7Remove course",
            "§8§l» §b/bp course create <name> §8:: §7Create new parkour course",
            "§8§l» §b/bp course edit <name> §8:: §7Shows this help message",
            "§8§l» §b/bp stats save §8:: §7Save current statistics",
            "§8§l§m━━━━━━━━━━━━§8§l|§9§l BetterParkour §8§l|§m━━━━━━━━━━━━",
            " "
    );

}
