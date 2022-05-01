package net.natroutter.betterparkour.files;

import net.natroutter.natlibs.handlers.LangHandler.TranslationTemplate;
import net.natroutter.natlibs.handlers.LangHandler.language.key.TranslationKey;

public enum Translations implements TranslationTemplate {

    Prefix,
    NoPerm,
    TooManyArguments,
    InvalidArgs,
    WrongCommandUsage,
    OnlyIngame,
    WandGive,
    CourseInEdit,
    CourseExist,
    CourseDoesntExits,
    CourseCreated,
    CourseEditSelected,
    CourseNotSelected,
    InvalidBlockTarget,
    PressurePlateNeeded,
    StartSet,
    EndSet,
    CheckpointAdded,
    FirstCornerSet,
    SecondCornerSet,
    DifficultySet,
    TopListSet,
    SpawnSet,
    CourseEditFinished,
    CourseNotGood,
    CourseCreatingAborted,
    CourseRemoved,
    StatisticsSaved,
    CantWhileInCourse,
    LeaveMessage,
    LeaveInfoMessage,
    NotInCourse,
    ActionBar,
    CourseRenamed,
    InvalidPlayer,
    StatisticsRemoved,
    InvalidCourse,
    CourseFinished,
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
    HelpMessage;

    private String path = null;
    Translations() {}
    Translations(String path) {
        this.path = path;
    }

    @Override
    public TranslationKey getKey() {
        if (path == null) {
            return TranslationKey.of(this.name());
        }
        return TranslationKey.of(this.path);
    }
}
