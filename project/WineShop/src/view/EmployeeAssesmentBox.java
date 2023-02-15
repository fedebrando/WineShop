package view;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import model.User;

/**
 * Class EmployeeAssesmentBox IS-A Group which represent graphically employees' assesments
 * @author Federico Brandini, Federico Putamorsi
 * @version 14.02.2023
 */
public class EmployeeAssesmentBox extends Group
{
	private final int MIN_MARK = 0, MAX_MARK = 5;
	
	private HBox hbRoot;
	private Label employeeInfo;
	private ChoiceBox<Integer> mark;
	
	/**
	 * Instantiates the assesment for an employee
	 * @param employee The employee to be evaluated
	 * @param cbList The list in which adding employee's mark choice box
	 */
	public EmployeeAssesmentBox(User employee, ArrayList<ChoiceBox<Integer>> cbList)
	{
		hbRoot = new HBox();
		hbRoot.setPadding(new Insets(10, 10, 10, 10));
		employeeInfo = new Label(employee.getSurname() + " " + employee.getName() + " [" + employee.getUsername() + "]");
		employeeInfo.setPadding(new Insets(0, 10, 0, 0));
		mark = new ChoiceBox<Integer>();
		for (int m = MIN_MARK; m <= MAX_MARK; m++)
			mark.getItems().add(m);
		mark.setValue(3);
		mark.setId(employeeInfo.getText());
		hbRoot.getChildren().addAll(employeeInfo, mark);
		super.getChildren().add(hbRoot);
		cbList.add(mark);
	}
}
