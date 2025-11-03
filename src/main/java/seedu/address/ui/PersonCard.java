package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import seedu.address.commons.util.StringUtil;
import seedu.address.model.person.Person;

/**
 * A UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label level;
    @FXML
    private VBox classGroupAssignmentContainer;

    /**
     * Creates a {@code PersonCard} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(StringUtil.toTitleCase(
                StringUtil.correctCapitalization(person.getName().fullName)));
        phone.setText(person.getPhone().value);
        level.setText(person.getLevel().toString());

        populateClassGroupAssignments();
    }

    /**
     * Populates the UI container with class groups and their corresponding assignments.
     *
     * Each row displays a class group label followed by its assignments.
     * Assignments that are marked appear grayed out and struck through.
     * The method clears any existing content before rebuilding the display.
     */
    private void populateClassGroupAssignments() {
        classGroupAssignmentContainer.getChildren().clear();

        person.getClassGroups().stream()
                .sorted(Comparator.comparing(cg -> cg.classGroupName))
                .forEach(classGroup -> {
                    // Create a row: [ClassGroup] - [Assignments]
                    HBox row = new HBox(5);
                    row.getStyleClass().add("classgroup-row");

                    Label classGroupLabel = new Label(StringUtil.toTitleCase(classGroup.classGroupName));
                    classGroupLabel.getStyleClass().add("classgroup-label");

                    FlowPane assignmentPane = new FlowPane(3, 3);
                    assignmentPane.getStyleClass().add("assignment-flow");

                    HBox.setHgrow(assignmentPane, Priority.ALWAYS);
                    assignmentPane.setMaxWidth(Double.MAX_VALUE);

                    // Filter only assignments that belong to this class group
                    person.getAssignments().stream()
                            .filter(a -> a.getClassGroupName().equals(classGroup.classGroupName))
                            .sorted(Comparator.comparing(a -> a.assignmentName))
                            .forEach(a -> {
                                Label assignmentLabel = new Label();
                                Text text = new Text(StringUtil.toTitleCase(a.getAssignmentName()));
                                if (a.isMarked()) {
                                    text.setStrikethrough(true);
                                    text.setFill(Color.GRAY);
                                } else {
                                    text.setFill(Color.WHITE);
                                }
                                assignmentLabel.setGraphic(text);
                                assignmentLabel.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                                assignmentPane.getChildren().add(assignmentLabel);
                            });

                    row.getChildren().addAll(classGroupLabel, assignmentPane);
                    classGroupAssignmentContainer.getChildren().add(row);
                });
    }
}
