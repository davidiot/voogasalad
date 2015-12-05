package view.actor;

import authoring.controller.AuthoringController;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;

/**
 * @author David
 * 
 *         This class is designed to be used with the ActorBrowser
 * 
 */
public class ActorCell extends AbstractListCell<String> {

	private AuthoringController controller;
	private boolean deselect;
	private String actor;
	private Image image;

	public ActorCell(AuthoringController controller) {
		this.controller = controller;
	}

	private ImageView makeImage(String item) {
		image = new Image(getClass().getClassLoader()
				.getResourceAsStream(controller.getAuthoringActorConstructor().getDefaultPropertyValue(item, "image")));
		ImageView output = new ImageView(image);
		this.actor = item;
		output.setFitHeight(25);
		output.setPreserveRatio(true);
		output.setSmooth(true);
		output.setCache(true);
		return output;
	}

	public boolean deselect() {
		boolean output = deselect;
		deselect = false;
		return output;
	}

	public void markForDeselection() {
		this.deselect = true;
	}

	public void drag(MouseEvent e) {
		this.deselect = false;
		Dragboard db = this.startDragAndDrop(TransferMode.ANY);
		ClipboardContent content = new ClipboardContent();
		content.putString(this.actor);
		db.setContent(content);
		db.setDragView(image, image.getWidth() / 2, image.getHeight() / 2);
	}

	@Override
	protected void makeCell(String item) {
		HBox box = new HBox(5);
		box.setAlignment(Pos.CENTER_LEFT);
		box.getChildren().add(makeImage(item));
		Label label = new Label(item);
		label.setFont(textFont);
		box.getChildren().add(label);
		setGraphic(box);
	}

	public void dragDone(DragEvent e) {
		e.consume();
	}
}
