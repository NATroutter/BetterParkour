package fi.natroutter.betterparkour.files;

import fi.natroutter.betterparkour.BetterParkour;
import fi.natroutter.natlibs.config.ILang;
import fi.natroutter.natlibs.config.Language;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter @AllArgsConstructor
public enum Lang implements ILang {

    Prefix("Prefix"),
    NoPerm("NoPerm"),
    TooManyArguments("TooManyArguments"),
    InvalidArgs("InvalidArgs"),
    WrongCommandUsage("WrongCommandUsage"),
    OnlyIngame("OnlyIngame"),
    WandGive("WandGive"),
    CourseInEdit("CourseInEdit"),
    CourseExist("CourseExist"),
    CourseDoesntExits("CourseDoesntExits"),
    CourseCreated("CourseCreated"),
    CourseEditSelected("CourseEditSelected"),
    CourseNotSelected("CourseNotSelected"),
    InvalidBlockTarget("InvalidBlockTarget"),
    PressurePlateNeeded("PressurePlateNeeded"),
    StartSet("StartSet"),
    EndSet("EndSet"),
    CheckpointAdded("CheckpointAdded"),
    FirstCornerSet("FirstCornerSet"),
    SecondCornerSet("SecondCornerSet"),
    DifficultySet("DifficultySet"),
    TopListSet("TopListSet"),
    SpawnSet("SpawnSet"),
    CourseEditFinished("CourseEditFinished"),
    CourseNotGood("CourseNotGood"),
    CourseCreatingAborted("CourseCreatingAborted"),
    CourseRemoved("CourseRemoved"),
    StatisticsReloaded("StatisticsReloaded"),
    CantWhileInCourse("CantWhileInCourse"),
    LeaveMessage("LeaveMessage"),
    LeaveInfoMessage("LeaveInfoMessage"),
    NotInCourse("NotInCourse"),
    ActionBar("ActionBar"),
    CourseRenamed("CourseRenamed"),
    InvalidPlayer("InvalidPlayer"),
    StatisticsRemoved("StatisticsRemoved"),
    InvalidCourse("InvalidCourse"),
    ConfigsReloaded("ConfigsReloaded"),

    CourseFinished("CourseFinished"),
    Wand_Name("Wand.Name"),
    Wand_Lore("Wand.Lore"),
    ValidateCourse_top("ValidateCourse.Top"),
    ValidateCourse_Entry("ValidateCourse.Entry"),
    ValidateCourse_Bottom("ValidateCourse.Bottom"),
    ValidateCourse_Err_Name("ValidateCourse.Err_Name"),
    ValidateCourse_Err_FirstCorner("ValidateCourse.Err_FirstCorner"),
    ValidateCourse_Err_SecCorner("ValidateCourse.Err_SecCorner"),
    ValidateCourse_Err_Ending("ValidateCourse.Err_Ending"),
    ValidateCourse_Err_Staring("ValidateCourse.Err_Staring"),
    ValidateCourse_Err_Difficulty("ValidateCourse.Err_Difficulty"),
    ValidateCourse_Err_Spawn("ValidateCourse.Err_Spawn"),
    ValidateCourse_Good_Name("ValidateCourse.Good_Name"),
    ValidateCourse_Good_FirstCorner("ValidateCourse.Good_FirstCorner"),
    ValidateCourse_Good_SecCorner("ValidateCourse.Good_SecCorner"),
    ValidateCourse_Good_Ending("ValidateCourse.Good_Ending"),
    ValidateCourse_Good_Staring("ValidateCourse.Good_Staring"),
    ValidateCourse_Good_Difficulty("ValidateCourse.Good_Difficulty"),
    ValidateCourse_Good_Spawn("ValidateCourse.Good_Spawn"),
    CourseList_Top("CourseList.Top"),
    CourseList_Entry("CourseList.Entry"),
    CourseList_NoCourses("CourseList.NoCourses"),
    CourseList_Bottom("CourseList.Bottom"),
    TopList_Top("TopList.Top"),
    TopList_Entry("TopList.Entry"),
    TopList_NoEntries("TopList.NoEntries"),
    TopList_Bottom("TopList.Bottom"),
    HelpMessage("HelpMessage");

    String path;

    @Override
    public Language lang() {
        return Language.getFromKey(Config.Language.asString());
    }

    @Override
    public ILang prefix() {
        return Prefix;
    }

    @Override
    public JavaPlugin getPlugin() {
        return BetterParkour.getInstance();
    }
}
