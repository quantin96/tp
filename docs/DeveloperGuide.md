---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# Mentorstack Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

This project is based on the AddressBook-Level3 project created by the [SE-EDU initiative](https://se-education.org).

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/AY2425S2-CS2103-W11-1/tp/blob/master/src/main/java/seedu/mentorstack/Main.java) and [`MainApp`](https://github.com/AY2425S2-CS2103-W11-1/tp/blob/master/src/main/java/seedu/mentorstack/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2425S2-CS2103-W11-1/tp/blob/master/src/main/java/seedu/mentorstack/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2425S2-CS2103-W11-1/tp/blob/master/src/main/java/seedu/mentorstack/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2425S2-CS2103-W11-1/tp/blob/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2425S2-CS2103-W11-1/tp/blob/master/src/main/java/seedu/mentorstack/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `MentorstackParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `MentorstackParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `MentorstackParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `MentorstackParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/AY2425S2-CS2103-W11-1/tp/blob/master/src/main/java/seedu/mentorstack/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the mentorstack data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/AY2425S2-CS2103-W11-1/tp/blob/master/src/main/java/seedu/mentorstack/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both mentorstack data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `MentorstackStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.mentorstack.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Undo feature

#### Implementation

The key operation to store the states of the data is in the `Model` interface as `Model#remember()`.

Given below is an example usage scenario and how the undo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `Mentorstack` will be initialized with the initial mentorstack state, and the empty list of state `history` is initialized.

<puml src="diagrams/UndoState0.puml" alt="UndoState0" />

Step 2. The user executes `delete 5` command to delete the 5th person in the mentorstack. The `delete` command calls `Model#remember()`, causing the original state before the `delete 5` command executes to be saved in the `history`.

<puml src="diagrams/UndoState1.puml" alt="UndoState1" />

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#remember()`, causing another mentorstack state to be saved into the `history`.

<puml src="diagrams/UndoState2.puml" alt="UndoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#remember()`, so the mentorstack state will not be saved into the `history`.

</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undo()`, which, after verified by `Model#canUndo()`, will make `history` to pop once to give last state, and restores the mentorstack to that state.

<puml src="diagrams/UndoState3.puml" alt="UndoState3" />


<box type="info" seamless>

**Note:** If the `history` has no previous mentorstack states to restore. The `undo` command uses `Model#canUndo()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

Step 5. The user then decides to execute the command `list`. Commands that do not modify the mentorstack, such as `list`, will usually not call `Model#remember()`. Thus, the `history` remains unchanged.

<puml src="diagrams/UndoState4.puml" alt="UndoState4" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo executes:**

* **Alternative 1 (current choice):** Saves the entire mentorstack.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### Stats feature
#### Implementation
The `StatsCommand` provides statistical insights into the persons stored in MentorStack. It can either display overall statistics or filter by a specified subject.
#### Execution Flow
* If a subject is specified, filter the list of persons based on that subject.
* Count the number of persons, males, and females in the filtered list.
* Format the results into a string and return as a CommandResult.

<puml src="diagrams/StatsCommandClassDiagram.puml" alt="StatsCommandClassDiagram" />

The following sequence diagram shows how a stats operation goes through the `Logic` component:

<puml src="diagrams/StatsSequenceDiagram.puml" alt="StatsSequenceDiagram" />

### Student archiving

The `ArchiveCommand` archives students and transfers their data to an archive list stored on Mentorstack.
It is similar to the active list but student entries cannot be edited.

Given below is a sequence diagram for a sample `ArchiveCommand`

<puml src="diagrams/ArchiveSequenceDiagram.puml" alt="ArchiveSequenceDiagram" />

A similar sequence diagram can be drawn for the `UnarchiveCommand`, which moves the student back to the active list.

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

Computer science tutors who need an efficient system to organize student information and track student progress

**Value proposition**:

Mentorstack helps CS tutors efficiently manage and track student contacts, attendance, participation, progress, and streamlines communication. It simplifies student management across different levels and courses while catering to tech-savvy users who may prefer a command-line interface.

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​  | I want to …​                                        | So that I can…​                                           |
|----------|---------|-----------------------------------------------------|-----------------------------------------------------------|
| `* * *`  | tutor   | add a student’s details (name, email, course, year) | keep track of their information                           |
| `* * *`  | tutor   | remove a student’s details                          | remove a student whose details no longer need to track    |
| `* * *`  | tutor   | edit a student’s details                            | update their information                                  |
| `* * *`  | tutor   | search for a student by name or ID                  | quickly find their details                                |
| `* * *`  | tutor   | view all students’ information                      | get in touch with the student whenever I want             |
| `* *`    | tutor   | undo an unintended operation                        | quickly correct any mistakes                              |
| `* *`    | tutor   | View the gender distribution of my students         | adjust my teaching style                                  |
| `* *`    | tutor   | archive a student                                   | focus on current student while not deleting past students |
| `* *`    | tutor   | unarchive a student                                 | add mistakenly archived students back                     |
| `* *`    | tutor   | clear all student's details                         | reset the Mentorstack to an empty                         |
| `* *`    | tutor   | list all student's details                          | see all active students in Mentorstack                    |
| `* *`    | tutor   | mark students                                       | quickly identify students                                 |
| `* *`    | tutor   | unmark students                                     | unmark mistakenly marked students                         |
| `* *`    | tutor   | mark a student's subject as completed               | check if a student has completed a course                 |
| `* *`    | tutor   | mark a student's subject as uncompleted             | unmark mistakenly marked subjects                         |
| `* *`    | tutor   | show all archived students                          | check which students are archived                         |
| `* *`    | tutor   | view help window or hinter command                  | quickly get help to using Mentorstack                     |


### Use cases

(For all use cases below, the **System** is `Mentorstack` and the **Actor** is the `tutor`, unless specified otherwise)

**Use case: UC01 - Add student details**

**MSS**
1. Tutor requests to add a new student.
2. System displays the required input format to add student information.
3. Tutor enters student's information.
4. System creates a new student profile.

    Use case ends.

**Extensions**

* 4a. Required information is missing.
    * 4a1. System shows an error message.
    * 4a2. System prompts the tutor to enter the missing information.
    Use case resumes at step 3.

* 4b. Information input format is invalid.
    * 4b1. System shows an error message.
    * 4b2. System prompts the tutor to enter information.
    Use case resumes at step 3.

*  4c. A student’s information already exists.
    * 4c1. System shows an error message.
    * 4c2. System prompts the tutor to add a new student or update the existing student.
    Use case resumes at step 3.

**Use case: UC02 - Delete a student**

**MSS**

1.  Tutor requests to list students.
2.  System shows a list of students.
3.  Tutor requests to delete a specific student in the list.
4.  System deletes the person.

    Use case ends.

**Use case: UC03 - Edit student details**

**MSS**

1.  User inputs the student ID and the updated information of the corresponding student.
2.  Mentorstack shows a message of successful updating.

    Use case ends.

**Extensions**

* 2a. The given student ID is invalid.

    * 2a1. Mentorstack shows an error message.

      Use case ends.

**Use case: UC04 - Find students by name**

**MSS**

1.  User inputs a string.
2.  Mentorstack shows a list of students that contains the string in the name.

    Use case ends.

**Extensions**

* 2a. There is no matching student.

    * 2a1. Mentorstack shows an error message.

      Use case ends.


**Use case: UC05 - View all students’ information by filter**

**MSS**

1.  Tutor enters the view_students command with an optional filter type and value.
2.  Mentorstack retrieves a list of students matching the filter criteria.
3.  Mentorstack displays a table of students retrieved.

    Use case ends.

**Extensions**

* 1a. The filter or value is invalid.

    * 1a1. Mentorstack shows an error message.

  Use case ends.

* 2a. The list is empty.

    * 2a1. Mentorstack shows a message indicating no student satisfies the input requirements.

  Use case ends.

**Use case: UC06 - View student distribution by stats**

**MSS**

1.  Tutor enters the stats command with an optional subject name.
2.  Mentorstack calculates numbers by the filter criteria.
3.  Mentorstack displays the number of distributions retrieved.

    Use case ends.

**Extensions**

* 1a. The filter or value is invalid.

    * 1a1. Mentorstack shows an error message.

  Use case ends.

* 1b. No subject input.
    * 1b1. Take the filter criteria as the whole data set.
  Back to step 2

* 2a. The list is empty.

    * 2a1. Mentorstack shows a message indicating no student satisfies the input requirements.

  Use case ends.

**Use case: UC07 - Archive students**

**MSS**
1. Tutor requests to archive a list of students.
2. Mentorstack archives the corresponding students.

  Use case ends.

**Extensions**

* 2a. Students are not in the list.
    * 2a1. Mentorstack shows an error message.

  Use case ends.

* 2b. A student is already archived.
    * 2b1. Mentorstack shows an error message.

  Use case ends.


**Use case: UC08 - Unarchive students**

Preconditions: User is accessing the archive list and there are archived students.
**MSS**
1. Tutor requests to unarchive a list of students.
2. Mentorstack unarchives the corresponding students.

  Use case ends.

**Extensions**

* 2a. Students are not in the list.
    * 2a1. Mentorstack shows an error message.

  Use case ends.

* 2b. A student is already unarchived.
    * 2b1. Mentorstack shows an error message.

  Use case ends.

**Use case: UC09 - Show all archived students**

**MSS**
1. Tutor requests to show all archived students.
2. Mentorstack shows a list of archived students.

  Use case ends.

**Use case: UC10 - Mark student**

**MSS**
1. Tutor requests to indicate a student or list of students as marked.
2. Mentorstack marks the students if they have not been marked.

  Use case ends.

**Extensions**

* 2a. Student is not in the list.
    * 2a1. Mentorstack shows an error message.

  Use case ends.

* 2b. Student is archived.
    * 2b1. Mentorstack shows an error message.

  Use case ends.

**Use case: UC11 - Unmark student**

**MSS**
1. Tutor requests to indicate a student or list of students as unmarked.
2. Mentorstack unmarks the students if they have not been unmarked.

  Use case ends.

**Extensions**

* 2a. Student is not in the list.
    * 2a1. Mentorstack shows an error message.

  Use case ends.

* 2b. Student is archived.
    * 2b1. Mentorstack shows an error message.

  Use case ends.

**Use case: UC12 - Finish subject**

**MSS**
1. Tutor requests to indicate a student's subject as completed.
2. Mentorstack marks the subject as completed.

  Use case ends.

**Extensions**

* 2a. Student is not in the list.
    * 2a1. Mentorstack shows an error message.

  Use case ends.

* 2b. Student is archived.
    * 2b1. Mentorstack shows an error message.

  Use case ends.

* 2c. Student is not enrolled in the subject
    * 2c1. Mentorstack shows an error message.

  Use case ends.

**Use case: UC13 - Unfinish subject**

**MSS**
1. Tutor requests to indicate a student's subject as not completed.
2. Mentorstack marks the subject as not completed.

Use case ends.

**Extensions**

* 2a. Student is not in the list.
    * 2a1. Mentorstack shows an error message.

  Use case ends.

* 2b. Student is archived.
    * 2b1. Mentorstack shows an error message.

  Use case ends.

* 2c. Student is not enrolled in the subject.
    * 2c1. Mentorstack shows an error message.

  Use case ends.


**Use case: UC14 - Undo an unintended operation**

**Precondition**

1. The user has performed at least one action that modifies the application state (e.g., adding, editing, or deleting student data).

**MSS**

1.  Tutor enters the undo command.
2.  Mentorstack reverts the last operation that modified the data storage.
3.  Mentorstack displays a success message.
4.  Mentorstack displays a description for the operation that is undone.

    Use case ends.

**Extensions**

* 1a. There is no operation to undo - undo is not valid.

    * 1a1. Mentorstack shows a message indicating no previous operation exists.

    Use case ends.

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2.  Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4.  The system will respond to any command within 3 seconds.

*{More to be added}*

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Tutor**: CS educator using Mentorstack to manage students
* **Student Profile**: A record containing a student’s personal details (name, email, phone, subjects)


--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

   1. If this still fails, refer to the instructions in the Quick Start section in the User Guide for launch instructions.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

### Adding a person

1. Adding a person

    1. Test case: `add n/John Doe g/M p/98765432 e/johnd@example.com s/CS2103`<br>
       Expected: Student is added to the end of the list with the above details. Details of the added student shown in the status message.

    1. Test case: `add n/John Doe`<br>
       Expected: No person is added. Error details shown in the status message. Can try with other missing fields.

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First student is deleted from the list. Details of the deleted student shown in the status message.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

### Editing a person

1. Editing a person while all persons are being shown

    1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

    1. Test case: `edit 1 s/CS2103`<br>
       Expected: First student is edited with the above details. Details of the edited student shown in the status message.

    1. Test case: `edit 1`<br>
       Expected: No person is edited. Error details shown in the status message. At least one field must be edited.

    1. Other incorrect edit commands to try: `edit`, `edit x`, `...` (where x is larger than the list size)<br>
       Expected: Similar to previous.

### Finding a person

1. Finds persons with matching name in Mentorstack.

    1. Prerequisites: Student with name `alex` in Mentorstack.

    1. Test case: `find alex`<br>
       Expected: Students with name containing `alex` is listed.

### Filtering a person by view

1. Finds persons with matching values in Mentorstack.

    1. Test case: `view f/s v/CS`<br>
       Expected: Students taking `CS` subjects are listed.

    1. Test case: `view`<br>
       Expected: All students listed.

    1. Test case: `view f/s v/CS f/n v/alex`<br>
       Expected: Students taking `CS` subjects and name containing `alex` are listed.

   1. Test case: `view f/n`<br>
       Expected: Filter/value not specified. No error. All students are listed.

### Archiving a person

1. Archives students in Mentorstack.

    1. Prerequisites: Students in the active list in Mentorstack.

    1. Test case: `archive 1`<br>
       Expected: First student is moved to the archive list. Verify with `showarchive`. Verify that student can no longer be edited.

### Unarchiving a person

1. Unarchives students in Mentorstack.

    1. Prerequisites: Students in the archive list in Mentorstack. Access this list first using `showarchive`

    1. Test case: `unarchive 1`<br>
       Expected: First student in the archive list is moved back to the active list. Verify with `list`. Verify that student can now be edited.

### Marking a person

1. Marks students in Mentorstack.

    1. Prerequisites: List all students using the `list` command. Multiple persons in the list.

    1. Test case: `mark 1`<br>
       Expected: First student is marked. Verify that `mark 1` again keeps first student as marked.

    1. Test case: `mark 0`<br>
       Expected: No student is marked. Error details shown in the status message.

    1. Other incorrect mark commands to try: `mark`, `mark x`, `...` (where x is larger than the list size)<br>
       Expected: Similar to previous.

### Unmarking a person

1. Unmarks students in Mentorstack.

    1. Prerequisites: List all students using the `list` command. Multiple persons in the list.

    1. Test case: `unmark 1`<br>
       Expected: First student is unmarked. Verify that `unmark 1` again keeps first student as unmarked.

    1. Test case: `unmark 0`<br>
       Expected: No student is unmarked. Error details shown in the status message.

    1. Other incorrect unmark commands to try: `unmark`, `unmark x`, `...` (where x is larger than the list size)<br>
       Expected: Similar to previous.

### Mark a student's subject as finished

1. Marks a student's subject as finished in Mentorstack.

    1. Prerequisites: First student in the list must be enrolled in `CS2103` and not enrolled in `CS2104`.

    1. Test case: `finish 1 s/CS2103`<br>
       Expected: Subject marked as finished. Verify that running the command again simply keeps the subject as finished.

    1. Test case: `finish 1 s/CS2104`<br>
       Expected: Error details shown in the status message.

### Mark a student's subject as unfinished

1. Marks a student's subject as unfinished in Mentorstack.

    1. Prerequisites: First student in the list must have finished `CS2103` and not enrolled in `CS2104`.

    1. Test case: `unfinish 1 s/CS2103`<br>
       Expected: Subject marked as unfinished. Verify that running the command again simply keeps the subject as unfinished.

    1. Test case: `unfinish 1 s/CS2104`<br>
       Expected: Error details shown in the status message.

### Undo

1. Undoes the previous state-changing command in Mentorstack successfully.

    1. Prerequisites: Valid commands have been run that have changed the state of Mentorstack in the current session.

       1. Test case: `undo`<br>
          Expected: Undoes the previous state-changing command. Mentorstack is restored to the previous state.

1. Fails to undo the commands in Mentorstack.

    1. Prerequisites: New Mentorstack session.

       1. Test case: `undo`<br>
          Expected: No operation undone. Error details shown in the status message.

## **Appendix: Planned Enhancements**

Team size: 5

1. **Change condition for duplicate entry**: Mentorstack currently considers entries with the same name as duplicates (case-sensitive). As it is possible for students to have the same name, this will be changed to checking for duplicate phone or email in future iterations.
2. **Allow other symbols for names**: Mentorstack currently only allows alphanumeric characters and spaces in names. This could be extended to include other symbols (e.g. `.`, `/`).
3. **Rework name parser**: Mentorstack currently includes all spaces between parts of a name. This could be reworked to remove redundant or accidental spacing.
4. **Rework `view` command**: The `view` command simply lists all students if invalid arguments are given. This could be reworked to always throw an error message whenever filter-value pairs are incorrectly specified to help users use the format.
5. **Make `stats` command work for archived students**: The `stats` command currently only displays statistics for students currently enrolled in subjects in the active list. This could be extended to work for the archive list and completed subjects.
6. **Make index error more specific**: Currently, Mentorstack does not flag index <= 0 as index error. This could be reworked to display an error message that the index is not within the range of the list of students.
7. **Improved error message for multiple indices**: For commands with multiple indices involved, Mentorstack can be reworked to be more specific in which index is invalid (if any).
8. **Allow other symbols for phones**: Mentorstack can allow other symbols (e.g. `+`, `-`) to account for country codes.
9. **Change subjects to be case-insensitive**: Subjects are currently case-sensitive in Mentorstack (i.e. `CS2103` is different from `cs2103`). This could be extended to consider these subjects as the same by making them case-insensitive.
10. **Create a search function that matches full words**: `find` and `view` currently matches partial words. It could be more effective if another search function was added that searches by full matching words.
