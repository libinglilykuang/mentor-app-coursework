package uk.ac.nott.cs.comp2013.mentorapp.view;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * Event raised whenever a {@link ViewManager} should change the displayed view.
 */
public class ViewChangeEvent extends Event {

  private String view;

  public static final EventType<ViewChangeEvent> VIEW_CHANGE = new EventType<>(Event.ANY, "VIEW_CHANGE");

  public ViewChangeEvent(String view) {
    super(VIEW_CHANGE);
    this.view = view;
  }

  /**
   * Get the key of the view to display.
   *
   * @return view key
   */
  public String getView() {
    return view;
  }
}
