package edu.asu.plp.tool.prototype;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.apache.commons.io.FilenameUtils;

import moore.fx.components.Components;
import edu.asu.plp.tool.prototype.model.PLPProject;
import edu.asu.plp.tool.prototype.model.PLPSourceFile;
import edu.asu.plp.tool.prototype.util.Dialogues;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class ProjectCreationPanel extends BorderPane
{
	private TextField projectNameField;
	private TextField mainSourceFileNameField;
	private TextField projectLocationField;
	private ComboBox<String> projectTypeDropdown;
	
	public ProjectCreationPanel()
	{
		this.setPadding(new Insets(20));
		GridPane grid = new GridPane();
		HBox buttons = new HBox(10);
		grid.setHgap(10);
		grid.setVgap(30);
		grid.setPadding(new Insets(10, 10, 10, 10));
		
		Label projectNameLabel = new Label();
		projectNameLabel.setText("Project Name: ");
		projectNameLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
		
		projectNameField = new TextField();
		projectNameField.setText("Project Name");
		projectNameField.requestFocus();
		projectNameField.setPrefWidth(200);
		
		Label mainSourceFileNameLabel = new Label();
		mainSourceFileNameLabel.setText("File Name: ");
		mainSourceFileNameLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
		
		mainSourceFileNameField = new TextField();
		mainSourceFileNameField.setText("Main.asm");
		mainSourceFileNameField.setPrefWidth(200);
		
		Label projectLocationLabel = new Label();
		projectLocationLabel.setText("Location: ");
		projectLocationLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
		
		projectLocationField = new TextField();
		projectLocationField.setPrefWidth(200);
		
		Button browseLocationButton = new Button();
		browseLocationButton.setText("Browse");
		browseLocationButton.setOnAction(this::onBrowseLocation);
		
		Label projectTypeLabel = new Label();
		projectTypeLabel.setText("Targetted ISA: ");
		projectTypeLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
		
		String PLP6 = "PLP6";
		String legacy = "PLP5(Legacy)";
		String mips = "MIPS";
		
		projectTypeDropdown = new ComboBox<>();
		projectTypeDropdown.getItems().addAll(PLP6, legacy, mips);
		projectTypeDropdown.setValue(PLP6);
		
		Button createProject = new Button("Create Project");
		createProject.setOnAction(this::onCreateProjectClicked);
		
		createProject.setDefaultButton(true);
		Button cancelCreate = new Button("Cancel");
		cancelCreate.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e)
			{
				Stage stage = (Stage) cancelCreate.getScene().getWindow();
				stage.close();
			}
		});
		
		grid.add(projectNameLabel, 0, 0);
		grid.add(projectNameField, 1, 0);
		grid.add(mainSourceFileNameLabel, 0, 1);
		grid.add(mainSourceFileNameField, 1, 1);
		grid.add(projectLocationLabel, 0, 2);
		grid.add(projectLocationField, 1, 2);
		grid.add(browseLocationButton, 2, 2);
		grid.add(projectTypeLabel, 0, 3);
		grid.add(projectTypeDropdown, 1, 3);
		
		this.setCenter(grid);
		
		buttons.getChildren().addAll(createProject, cancelCreate);
		buttons.setAlignment(Pos.BASELINE_RIGHT);
		this.setBottom(buttons);
	}
	
	private void onBrowseLocation(ActionEvent event)
	{
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Choose Project Location");
		File file = directoryChooser.showDialog(null);
		if (file != null)
		{
			Path directoryPath = file.toPath();
			String newProjectName = projectNameField.getText();
			Path targetProjectPath = directoryPath.resolve(newProjectName);
			String targetLocation = targetProjectPath.toString();
			projectLocationField.setText(targetLocation);
		}
	}
	
	private void onCreateProjectClicked(ActionEvent event)
	{
		ProjectCreationDetails details = extractDetailsFromGUI();
		boolean isValid = validateDefaultProjectDetails(details);
		if (isValid)
		{
			details.getProjectDirectory().mkdirs();
			String projectType = details.getProjectType();
			
			if (projectType.equals(legacy))
			{
				if (!fileName.contains(".plp"))
				{
					fileName = fileName.concat(".plp");
				}
				
				PLPProject legacyProject = new PLPProject(projectName);
				legacyProject.setPath(projLocationField.getText());
				PLPSourceFile legacySourceFile = new PLPSourceFile(legacyProject,
						fileName);
				try
				{
					legacyProject.saveLegacy();
				}
				catch (IOException ioException)
				{
					// TODO report exception to user
					ioException.printStackTrace();
				}
				projects.add(legacyProject);
				openFile(legacySourceFile);
			}
			else if (projectType.equals(PLP6))
			{
				if (!fileName.contains(".asm"))
				{
					fileName = fileName.concat(".asm");
				}
				
				PLPProject project = new PLPProject(projectName);
				project.setPath(projLocationField.getText());
				PLPSourceFile sourceFile = new PLPSourceFile(project, fileName);
				project.add(sourceFile);
				try
				{
					project.save();
				}
				catch (IOException ioException)
				{
					// TODO report exception to user
					ioException.printStackTrace();
				}
				projects.add(project);
				openFile(sourceFile);
			}
			
			Stage stage = (Stage) createProject.getScene().getWindow();
			stage.close();
		}
	}
	
	private ProjectCreationDetails extractDetailsFromGUI()
	{
		String name = projectNameField.getText();
		String mainSource = mainSourceFileNameField.getText();
		String location = projectLocationField.getText();
		String type = projectTypeDropdown.getValue();
		
		return new ProjectCreationDetails(name, mainSource, location, type);
	}
	
	private boolean validateDefaultProjectDetails(ProjectCreationDetails details)
	{
		String projectName = details.getProjectName();
		String fileName = details.getMainSourceFileName();
		String projectLocation = details.getProjectLocation();
		File projectDirectory = details.getProjectDirectory();
		
		if (projectName == null || projectName.trim().isEmpty())
		{
			Dialogues.showInfoDialogue("You entered an invalid Project Name");
		}
		else if (fileName == null || fileName.trim().isEmpty())
		{
			Dialogues.showInfoDialogue("You entered an invalid File Name");
		}
		else if (projectLocation == null || projectLocation.trim().isEmpty())
		{
			Dialogues.showInfoDialogue("You entered an invalid Project Location");
		}
		else if (projectDirectory.exists())
		{
			Dialogues.showInfoDialogue("This Project Already Exists");
		}
		else
		{
			return true;
		}
		
		return false;
	}
}
