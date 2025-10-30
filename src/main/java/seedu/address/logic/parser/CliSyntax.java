package seedu.address.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    /* Prefix definitions */
    public static final Prefix PREFIX_NAME = new Prefix("n/");
    public static final Prefix PREFIX_PHONE = new Prefix("p/");
    public static final Prefix PREFIX_LEVEL = new Prefix("l/");
    public static final Prefix PREFIX_ASSIGNMENT = new Prefix("a/");
    public static final Prefix PREFIX_CLASSGROUP = new Prefix("c/");

    /* UPDATE WHEN ADDING A NEW PREFIX */
    public static final Prefix[] ALL_PREFIXES = new Prefix[] {
        PREFIX_NAME,
        PREFIX_PHONE,
        PREFIX_LEVEL,
        PREFIX_ASSIGNMENT,
        PREFIX_CLASSGROUP
    };


}
