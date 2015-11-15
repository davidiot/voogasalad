package view.element;

import javafx.geometry.Pos;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;
import view.actor.ActorView;
import view.screen.AbstractScreenInterface;

public class ActorBrowser extends AbstractDockElement {

	TreeView<ActorView> rightlist;
	TreeView<ActorView> leftlist;

	public ActorBrowser(GridPane pane, GridPane home, String title, AbstractScreenInterface screen) {
		super(pane, home, title, screen);
		makePane();
	}

	@Override
	protected void makePane() {
		rightlist = new TreeView<ActorView>();
		leftlist = new TreeView<ActorView>();
		GridPane labelPane = makeLabelPane();
		pane.add(labelPane, 0, 0);
		GridPane.setColumnSpan(labelPane, 2);
		pane.add(leftlist, 0, 1);
		pane.add(rightlist, 1, 1);
		pane.setAlignment(Pos.TOP_CENTER);
		leftlist.prefHeightProperty().bind(screen.getScene().heightProperty());
		rightlist.prefHeightProperty().bind(screen.getScene().heightProperty());
		leftlist.setFocusTraversable(false);
		rightlist.setFocusTraversable(false);
	}

}
